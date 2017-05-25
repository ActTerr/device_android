package mac.yk.devicemanagement.down;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ipaulpro.afilechooser.utils.FileUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mac.yk.devicemanagement.I;
import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.application.MyMemory;
import mac.yk.devicemanagement.bean.FileEntry;
import mac.yk.devicemanagement.db.dbFile;
import mac.yk.devicemanagement.service.FileService;
import mac.yk.devicemanagement.ui.fragment.BaseFragment;
import mac.yk.devicemanagement.util.ConvertUtils;
import mac.yk.devicemanagement.util.L;
import mac.yk.devicemanagement.util.OpenFileUtil;
import mac.yk.devicemanagement.util.ToastUtil;

/**
 * Created by mac-yk on 2017/5/9.
 */

public class AttachmentFragment extends BaseFragment implements downContract.View{
    public static int REQUEST_CHOOSER = 1234;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.add_history)
    TextView addHistory;
    @BindView(R.id.iv_add)
    ImageView ivAdd;

    AttachmentAdapter adapter;
    ProgressDialog pd;
    Context context;
    downContract.Presenter presenter;
    boolean isEdit;
    ArrayList<FileEntry> entries;
    String TAG="AttachmentFragment";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_notice, container, false);
        ButterKnife.bind(this, view);
        addHistory.setVisibility(View.GONE);
        context=getContext();
        pd=new ProgressDialog(context);
        adapter=new AttachmentAdapter(context);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(context));


        return view;
    }


    private void getAttachments() {
        presenter.getAttachments();
    }



    @OnClick({ R.id.iv_add})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.iv_add:
                selectFile();
                break;
        }
    }

    private void selectFile() {
        Intent getContentIntent = FileUtils.createGetContentIntent();
        Intent intent = Intent.createChooser(getContentIntent, "Select a file");
        Activity activity= (Activity) context;
        activity.startActivityForResult(intent,REQUEST_CHOOSER);
    }

    @Override
    public void showProgress(int i, AttachmentAdapter.AttachmentViewHolder holder) {
        holder.pb.setProgress(i);
    }

    @Override
    public void refreshView() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void setUnEditStatus(AttachmentAdapter.AttachmentViewHolder holder) {
        holder.ivEdit.setVisibility(View.VISIBLE);
        holder.ivDelete.setVisibility(View.VISIBLE);
        holder.ivCancel.setVisibility(View.GONE);
        holder.ivSave.setVisibility(View.GONE);
        holder.attachmentName.setFocusable(false);
        holder.attachmentName.setFocusableInTouchMode(false);
        isEdit = false;
    }

    @Override
    public void setEditStatus(AttachmentAdapter.AttachmentViewHolder holder) {
        holder.ivEdit.setVisibility(View.GONE);
        holder.ivDelete.setVisibility(View.GONE);
        holder.ivCancel.setVisibility(View.VISIBLE);
        holder.ivSave.setVisibility(View.VISIBLE);

        holder.attachmentName.setFocusable(true);
        holder.attachmentName.setFocusableInTouchMode(true);
        holder.attachmentName.requestFocus();
        isEdit = true;
    }

    @Override
    public void showProgressDialog() {
        pd.show();
    }

    @Override
    public void dismissProgressDialog() {
        pd.dismiss();
    }

    @Override
    public void showProgress(AttachmentAdapter.AttachmentViewHolder holder) {
        holder.pb.setVisibility(View.VISIBLE);
    }

    @Override
    public void toastException() {
        ToastUtil.showException(context);
    }

    @Override
    public void toastString(String s) {
        ToastUtil.showToast(context,s);
    }

    @Override
    public void choseFile(Intent intent) {
        startActivityForResult(intent,REQUEST_CHOOSER);
    }

    @Override
    public void setEntries(ArrayList<FileEntry> entries) {
        this.entries=entries;
    }

    @Override
    public void setPresenter(downContract.Presenter presenter) {
        this.presenter=presenter;
    }

    public class AttachmentAdapter extends RecyclerView.Adapter<AttachmentAdapter.AttachmentViewHolder> {


        Context context;
        String TAG = "AttachmentAdapter";
        boolean isEdit;
        boolean admin;

        //已选中要下载的
        ArrayList<String> names = new ArrayList<>();

        ArrayList<FileEntry> fileEntries = new ArrayList<>();
        AttachmentViewHolder memoryHolder;
        AttachmentViewHolder firstHolder;
        PopupWindow popupWindow;
        PopuHolder popuHolder;
        int downStatus;
        long Nid;
        boolean isStop;

        public AttachmentAdapter(Context context) {
            this.context = context;
            EventBus.getDefault().register(this);
            admin = MyMemory.getInstance().getUser().getGrade() == 0;
            L.e(TAG,"isadmin:"+admin);
        }




        private void sort() {
            Collections.sort(fileEntries, new Comparator<FileEntry>() {
                @Override
                public int compare(FileEntry o1, FileEntry o2) {
                    return (int) ((int) o2.getAid()-o1.getAid());
                }
            });
        }

        @Override
        public AttachmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_attachment, parent, false);
            AttachmentViewHolder holder = new AttachmentViewHolder(view);

            return holder;
        }

        @Override
        public void onBindViewHolder(final AttachmentViewHolder holder, int position) {

            if (position == 0) {
                firstHolder = holder;
            }
            final FileEntry entry = fileEntries.get(position);
            final int status = entry.getDownloadStatus();
            if (admin) {
                L.e("wocao","丫不是admin啊");
                holder.ivDelete.setVisibility(View.VISIBLE);
                holder.ivEdit.setVisibility(View.VISIBLE);
                holder.cbAt.setVisibility(View.GONE);

                holder.ivEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setEditStatus(holder);
                    }
                });
                holder.ivDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        postDelete(entry);
                    }
                });
                holder.ivCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setUnEditStatus(holder);
                        holder.attachmentName.setText(entry.getFileName());
                    }
                });
                holder.ivSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        postSave(entry, holder.attachmentName.getText().toString(), "0", holder);
                    }
                });

            }
            if (status == I.DOWNLOAD_STATUS.FINISH) {
                holder.cbAt.setVisibility(View.GONE);
            }

            if (isEdit) {
                setEditStatus(holder);
            } else {
                setUnEditStatus(holder);
            }

            holder.attachmentName.setText(entry.getFileName());
            holder.uploadTime.setText(ConvertUtils.Date2String(new Date(entry.getAid())));
            holder.ivDocument.setImageResource(OpenFileUtil.getPic(entry.getFileName()));
            L.e(TAG, "设置成功");

            holder.cbAt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    names.add(entry.getFileName());
                }
            });

            holder.ivDocument.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (admin) {
                        L.e("wocao","丫不是admin啊");
                        showSelectPopuWindow(holder.ivDocument, entry, holder);
                    } else {
                        if (status == I.DOWNLOAD_STATUS.FINISH) {
                            L.e(TAG, entry.getSaveDirPath());
                            openFile(entry.getSaveDirPath());
                        } else {
                            downLoadFile(entry,holder);
//                        memoryHolder=holder;
                        }
                    }
                }
            });
            holder.control.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isStop){
                        holder.control.setImageResource(R.mipmap.audio);
                        uploadFile(entry.getFileName());
                        isStop=false;
                    }else {
                        holder.control.setImageResource(R.mipmap.audio);
                        L.e(TAG,"post stop");
                        EventBus.getDefault().post(1L);
                        isStop=true;
                    }
                }
            });

        }

        private void showSelectPopuWindow(ImageView iv, FileEntry entry, AttachmentViewHolder holder) {
            int width = ConvertUtils.dp2px(context, 150);
            int height = ConvertUtils.dp2px(context, 100);
            View view = LayoutInflater.from(context).inflate(R.layout.popu_file, null);
            popupWindow = new PopupWindow(view, width, height);
//        View view = View.inflate(context, R.layout.item_popu_file, null);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setTouchable(true);
            popupWindow.setFocusable(true);
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            popuHolder = new PopuHolder(view, entry, holder);
            popupWindow.showAsDropDown(iv);
        }


        class PopuHolder {
            View view;
            FileEntry entry;
            AttachmentViewHolder holder;

            public PopuHolder(View view, FileEntry entry, AttachmentViewHolder holder) {
                this.view = view;
                this.entry = entry;
                this.holder = holder;
                ButterKnife.bind(this, view);
            }

            @OnClick({R.id.check_file, R.id.update_file})
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.check_file:
                        openFile(entry.getSaveDirPath());
                        popupWindow.dismiss();
                        popuHolder = null;
                        break;
                    case R.id.update_file:
                        popupWindow.dismiss();
                        postSave(entry, "", "1", holder);
                        break;

                }
            }
        }


        private void uploadFile(String name){
            Intent intent = new Intent();
            intent.setClass(context, FileService.class);
            intent.putExtra("type", FileService.UPLOAD);
            L.e(TAG,"name:"+name);
            FileEntry entry = dbFile.getInstance(context).getFileEntry(name);
            L.e(TAG,"upload:"+entry.toString());
            intent.putExtra("entry", entry);
            context.startService(intent);
        }
        /**
         * 文件选择之后传过来的file
         *
         * @param file
         */
        @Subscribe(threadMode = ThreadMode.MAIN)
        public void getEventFile(File file) {
            L.e(TAG, "getfile");
            Intent intent = new Intent();
            intent.setClass(context, FileService.class);
            intent.putExtra("type", FileService.UPLOAD);
            FileEntry entry;

            if (memoryHolder != null) {
                memoryHolder.ivDocument.setImageResource(OpenFileUtil.getPic(file.getPath()));
                memoryHolder.attachmentName.setText(file.getName());
                entry = (FileEntry) memoryHolder.itemView.getTag();
                entry.setFileName(file.getName());
                entry.setSaveDirPath(file.getAbsolutePath());
                entry.setCompletedSize(file.length());
                memoryHolder.pb.setVisibility(View.VISIBLE);
                memoryHolder.control.setVisibility(View.VISIBLE);
            } else {

                entry = new FileEntry();
                entry.setFileName(file.getName());
                entry.setSaveDirPath(file.getAbsolutePath());
                entry.setCompletedSize(0L);
                entry.setNid(Nid);
                entry.setAid(System.currentTimeMillis());
                entry.setDownloadStatus(I.DOWNLOAD_STATUS.NOT);
                entry.setToolSize(file.length());
                fileEntries.add(entry);
                sort();
                notifyDataSetChanged();

            }

            if(dbFile.getInstance(context).insertFileEntry(entry)){
                ToastUtil.showToast(context,"数据库插入成功");
            }
            intent.putExtra("entry", entry);
            context.startService(intent);
            L.e(TAG,"start Service");

        }


        /**
         * service传过来的进度
         *
         * @param i
         */
        @Subscribe(threadMode = ThreadMode.MAIN)
        public void getPrograss(Integer i) {
            if (memoryHolder != null) {
                memoryHolder.pb.setProgress(i);
            } else {
                if (firstHolder==null){
                    firstHolder.pb.setVisibility(View.VISIBLE);
                    firstHolder.control.setVisibility(View.VISIBLE);
                    firstHolder.pb.setProgress(i);
                }
            }
        }

        /**
         * 上传或者下载完成
         *
         * @param finish
         */
        @Subscribe(threadMode = ThreadMode.MAIN)
        public void isFinish(boolean finish) {
            if (finish) {
                memoryHolder.pb.setVisibility(View.GONE);
            }

        }

        @Subscribe(threadMode = ThreadMode.MAIN)
        public void isUploadFinish(FileEntry entry) {
            firstHolder.pb.setVisibility(View.GONE);
            firstHolder.control.setVisibility(View.GONE);
            if(dbFile.getInstance(context).deleteFileEntry(entry.getFileName())){
                if (dbFile.getInstance(context).insertFileEntry(entry)){
                    L.e(TAG,"更新成功！");
                }

            }

        }


        /**
         * 使用service实现下载
         */
        private void downLoadFile(FileEntry entry,AttachmentViewHolder holder) {
            memoryHolder=holder;
            Intent intent = new Intent(context, FileService.class);
            intent.putExtra("type", 1);
            intent.putExtra("entry", entry);
            context.startService(intent);
        }

        private void openFile(String path) {

            Intent intent = OpenFileUtil.openFile(path);
            context.startActivity(intent);
        }


        private void postSave(final FileEntry entry, String text, final String type, final AttachmentViewHolder holder) {
          presenter.updateAttachment(entry,text,type,holder);
        }




        private void postDelete(final FileEntry entry) {
            presenter.deleteAttachment(entry);
        }

        @Override
        public int getItemCount() {
            return fileEntries.size();
        }


       public class AttachmentViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.cb_at)
            CheckBox cbAt;
            @BindView(R.id.iv_document)
            ImageView ivDocument;
            @BindView(R.id.attachment_name)
            EditText attachmentName;
            @BindView(R.id.upload_time)
            TextView uploadTime;
            @BindView(R.id.iv_save)
            ImageView ivSave;
            @BindView(R.id.iv_cancel)
            ImageView ivCancel;
            @BindView(R.id.iv_edit)
            ImageView ivEdit;
            @BindView(R.id.iv_delete)
            ImageView ivDelete;
            @BindView(R.id.pb)
            ProgressBar pb;
            @BindView(R.id.control)
            ImageView control;
            public AttachmentViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }

}
