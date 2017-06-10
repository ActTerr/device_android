package mac.yk.devicemanagement.down;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.ipaulpro.afilechooser.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mac.yk.devicemanagement.I;
import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.application.MyMemory;
import mac.yk.devicemanagement.bean.FileEntry;
import mac.yk.devicemanagement.service.down.FileService;
import mac.yk.devicemanagement.ui.fragment.BaseFragment;
import mac.yk.devicemanagement.ui.holder.AttachmentViewHolder;
import mac.yk.devicemanagement.util.ConvertUtils;
import mac.yk.devicemanagement.util.L;
import mac.yk.devicemanagement.util.OpenFileUtil;
import mac.yk.devicemanagement.util.ToastUtil;

/**
 * Created by mac-yk on 2017/5/9.
 */

public class AttachmentFragment extends BaseFragment implements DownContract.View {
    public static int REQUEST_CHOOSER = 1234;



    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.iv_add)
    ImageView ivAdd;

    AttachmentAdapter adapter;
    ProgressDialog pd;
    Context context;
    DownContract.Presenter presenter;
    boolean isEdit;
    String TAG = "AttachmentFragment";
    ArrayList<FileEntry> fileEntries = new ArrayList<>();

    ArrayList<FileEntry> downloads = new ArrayList<>();
    @BindView(R.id.btn_down)
    Button btnDown;
    private boolean admin;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_notice, container, false);
        ButterKnife.bind(this, view);
        initView();

        getAttachments();

        return view;
    }


    private void initView() {
        context = getContext();
        pd = new ProgressDialog(context);
        adapter = new AttachmentAdapter(context);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(context));
        admin = MyMemory.getInstance().getUser().getGrade() == 0;
        if (!admin) {
            ivAdd.setVisibility(View.GONE);
        } else {
            btnDown.setVisibility(View.GONE);
        }
        //取消动画显示
        ((SimpleItemAnimator)rv.getItemAnimator()).setSupportsChangeAnimations(false);
    }


    private void getAttachments() {
        presenter.getAttachments();
    }


    @OnClick({R.id.iv_add, R.id.btn_down})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.iv_add:
                selectFile(false);
                break;
            case R.id.btn_down:
                for (FileEntry entry:downloads){
                    L.e(TAG,"选中："+entry.getFileName());
                }
                presenter.downloadFiles(downloads);

                downloads.clear();
                break;
        }
    }

    private void selectFile(boolean update) {
        Intent getContentIntent = FileUtils.createGetContentIntent();
        Intent intent = Intent.createChooser(getContentIntent, "Select a file");
        Activity activity = (Activity) context;
        int requestCode;
        if (!update){
            requestCode=REQUEST_CHOOSER;
        }else{
            requestCode=11111;
        }
        activity.startActivityForResult(intent, requestCode);
    }


    @Override
    public void refreshView() {
//        for (FileEntry entry : fileEntries) {
//            if (entry.getDownloadStatus() == I.DOWNLOAD_STATUS.DOWNLOADING) {
//                L.e(entry.getFileName(),  "downloading:" + entry.getCompletedSize());
//            }
//        }
        adapter.notifyDataSetChanged();
        L.e(TAG,"adapter update");
    }

    private void setUnEditStatus(AttachmentViewHolder holder) {
        holder.ivEdit.setVisibility(View.VISIBLE);
        holder.ivDelete.setVisibility(View.VISIBLE);
        holder.ivCancel.setVisibility(View.GONE);
        holder.ivSave.setVisibility(View.GONE);
        holder.attachmentName.setFocusable(false);
        holder.attachmentName.setFocusableInTouchMode(false);
        isEdit = false;
    }

    private void setEditStatus(AttachmentViewHolder holder) {
        holder.ivEdit.setVisibility(View.GONE);
        holder.ivDelete.setVisibility(View.GONE);
        holder.ivSave.setVisibility(View.VISIBLE);
        holder.ivCancel.setVisibility(View.VISIBLE);
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
    public void toastException() {
        ToastUtil.showException(context);
    }

    @Override
    public void toastString(String s) {
        ToastUtil.showToast(context, s);
    }

    @Override
    public void choseFile(Intent intent) {
        startActivityForResult(intent, REQUEST_CHOOSER);
    }

    @Override
    public void setEntries(ArrayList<FileEntry> entries) {
        this.fileEntries = entries;
    }

    @Override
    public void startService(FileEntry entry) {
        Intent intent = new Intent();
        intent.setClass(context, FileService.class);
        intent.putExtra("type", FileService.UPLOAD);
        intent.putExtra("entry", entry);
        context.startService(intent);
    }

    @Override
    public void showDialog(final FileEntry entry, final File file) {
            AlertDialog dialog=new AlertDialog.Builder(context)
                    .setTitle("该文件已上传,是否更新？")
                    .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).setNegativeButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            presenter.updateFile(entry,file);
                        }
                    }).create();
        dialog.show();
    }

    @Override
    public void updateItem(FileEntry entry) {
        L.e("caonima","update item1");
        int position =-1;
        for(int i=0;i<fileEntries.size();i++){
            if (fileEntries.get(i)==entry){
                position=i;
            }
        }
        if (position!=-1){
            adapter.notifyItemChanged(position);
            L.e("caonima","update item2");
        }
    }



    @Override
    public void setPresenter(DownContract.Presenter presenter) {
        this.presenter = presenter;
    }

    public class AttachmentAdapter extends RecyclerView.Adapter<AttachmentViewHolder> {


        Context context;
        String TAG = "AttachmentAdapter";
        boolean isEdit;

        //已选中要下载的


        PopupWindow popupWindow;
        PopupHolder popupHolder;
        boolean isStop;


        public AttachmentAdapter(Context context) {
            this.context = context;
//            EventBus.getDefault().register(this);

            L.e(TAG, "isadmin:" + admin);
        }


        @Override
        public AttachmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_attachment, parent, false);
            AttachmentViewHolder holder = new AttachmentViewHolder(view);
            return holder;
        }
        boolean isMark=false;
        @Override
        public void onBindViewHolder(final AttachmentViewHolder holder, int position) {

            final FileEntry entry = fileEntries.get(position);
            final int status = entry.getDownloadStatus();

            switch (status){
                case I.DOWNLOAD_STATUS.PAUSE:
                case I.DOWNLOAD_STATUS.PREPARE:
                    L.e("caonima","prepare");
                    holder.pb.setVisibility(View.VISIBLE);
                    holder.cbAt.setChecked(false);
                    holder.tvCancel.setVisibility(View.VISIBLE);
                    holder.ivControl.setVisibility(View.VISIBLE);
                    break;
                case I.DOWNLOAD_STATUS.DOWNLOADING:
                    L.e("caonima",entry.getFileName()+"传输中");

                    int process = (int) ((entry.getCompletedSize() * 100) / entry.getToolSize());
                    holder.pb.setProgress(process);
                    break;
                case I.DOWNLOAD_STATUS.COMPLETED:
                    holder.pb.setVisibility(View.GONE);
                    holder.cbAt.setVisibility(View.GONE);
                    holder.tvCancel.setVisibility(View.GONE);
                    holder.ivControl.setVisibility(View.GONE);
                    L.e("caonima",entry.getFileName()+ "完成");
                    break;
                case I.DOWNLOAD_STATUS.INIT:
                    holder.pb.setVisibility(View.GONE);
                    holder.cbAt.setVisibility(View.VISIBLE);
                    holder.ivControl.setVisibility(View.GONE);
                    holder.ivCancel.setVisibility(View.GONE);
                    L.e("caonima",entry.getFileName()+"闲置");
            }


            /**
             * 添加图标抖动效果和背景凸显
             */
            if (admin) {
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        isMark=true;
                        holder.itemView.setBackgroundResource(R.drawable.long_click_bg);
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
                                postSave(entry, holder.attachmentName.getText().toString());
                                holder.itemView.setBackgroundResource(R.color.gray2);
                            }
                        });
                        return false;
                    }
                });


                if (isEdit) {
                    setEditStatus(holder);
                } else {
                    setUnEditStatus(holder);
                }

            } else {
                holder.attachmentName.setFocusable(false);
                holder.attachmentName.setFocusableInTouchMode(false);
            }


            holder.attachmentName.setText(entry.getFileName());
            holder.uploadTime.setText(ConvertUtils.Date2String(new Date(entry.getAid())));
            holder.ivDocument.setImageResource(OpenFileUtil.getPic(entry.getFileName()));

            holder.cbAt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.cbAt.isChecked()){
                        downloads.add(entry);
                    }else {
                        downloads.remove(entry);
                    }
                }
            });

            holder.ivDocument.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (admin) {
                        showSelectPopupWindow(holder.ivDocument, entry, holder);
                    } else {
                        if (status == I.DOWNLOAD_STATUS.COMPLETED) {
                            L.e(TAG, entry.getSaveDirPath());
                            openFile(entry.getSaveDirPath());
                        } else {
                            L.e(TAG, "点击下载"+entry.getFileName());
                            downLoadFile(entry);
                        }
                    }
                }
            });
            holder.ivControl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isStop) {
                        holder.ivControl.setImageResource(R.mipmap.pause);
                        uploadFile(entry);
                        isStop = false;
                    } else {
                        holder.ivControl.setImageResource(R.mipmap.start);
                        //得给一个标记
                        presenter.stopUpload(entry.getFileName());
                        isStop = true;
                    }
                }
            });
            holder.tvCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showCancelDialog(entry);
                }
            });
        }



        private void showCancelDialog(final FileEntry entry) {
            AlertDialog dialog = new AlertDialog.Builder(context)
                    .setTitle("确定取消吗下载?")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (admin) {
                                presenter.cancelUpload(entry);
                            } else {
                                presenter.cancelDownload(entry);
                            }
                            dialog.dismiss();
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create();
            dialog.show();
        }

        private void showSelectPopupWindow(ImageView iv, FileEntry entry, AttachmentViewHolder holder) {
            int width = ConvertUtils.dp2px(context, 150);
            int height = ConvertUtils.dp2px(context, 100);
            View view = LayoutInflater.from(context).inflate(R.layout.popu_file, null);
            popupWindow = new PopupWindow(view, width, height);
//        View view = View.inflate(context, R.layout.item_popu_file, null);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setTouchable(true);
            popupWindow.setFocusable(true);
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            popupHolder = new PopupHolder(view, entry, holder);
            popupWindow.showAsDropDown(iv);
        }


        class PopupHolder {
            View view;
            FileEntry entry;
            AttachmentViewHolder holder;

            public PopupHolder(View view, FileEntry entry, AttachmentViewHolder holder) {
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
                        popupHolder = null;
                        break;
                    case R.id.update_file:
                        popupWindow.dismiss();
                        presenter.setMemory(entry);
                        selectFile(true);
                        break;

                }
            }
        }


        private void uploadFile(FileEntry entry) {
            presenter.uploadFile(entry);
        }


        /**
         * 使用service实现下载
         */
        private void downLoadFile(FileEntry entry) {
//            Intent intent = new Intent(context, FileService.class);
//            intent.putExtra("type", 1);
//            intent.putExtra("entry", entry);
//            context.startService(intent);
//            FileService.setPresenter(presenter);
            presenter.downloadFile(entry);
        }

        private void openFile(String path) {
            Intent intent = OpenFileUtil.openFile(path);
            context.startActivity(intent);
        }


        private void postSave(final FileEntry entry, String text) {
            presenter.updateAttachment(entry, text);
        }


        private void postDelete(final FileEntry entry) {
            presenter.deleteAttachment(entry,false,null);
        }

        @Override
        public int getItemCount() {
            return fileEntries.size();
        }

    }


}
