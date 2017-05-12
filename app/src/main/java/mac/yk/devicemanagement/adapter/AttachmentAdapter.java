package mac.yk.devicemanagement.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mac.yk.devicemanagement.MyApplication;
import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.bean.Attachment;
import mac.yk.devicemanagement.bean.FileEntry;
import mac.yk.devicemanagement.db.dbFile;
import mac.yk.devicemanagement.net.ApiWrapper;
import mac.yk.devicemanagement.net.ServerAPI;
import mac.yk.devicemanagement.util.ConvertUtils;
import mac.yk.devicemanagement.util.ExceptionFilter;
import mac.yk.devicemanagement.util.OpenFileUtil;
import mac.yk.devicemanagement.util.ToastUtil;
import rx.Subscriber;

/**
 * Created by mac-yk on 2017/5/11.
 */

public class AttachmentAdapter extends RecyclerView.Adapter<AttachmentAdapter.AttachmentViewHolder> {

    ArrayList<Attachment> attachments;
    Context context;
    boolean isFinish;
    boolean isEdit;
    boolean admin;
    ArrayList<String> names = new ArrayList<>();
    ProgressDialog dialog;

    public AttachmentAdapter(ArrayList<Attachment> attachments, Context context, Boolean isFinish) {
        this.attachments = attachments;
        this.context = context;
        this.isFinish = isFinish;
        dialog = new ProgressDialog(context);
    }

    @Override
    public AttachmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_attachment, parent, false);
        AttachmentViewHolder holder = new AttachmentViewHolder(view);
        admin = MyApplication.getInstance().getUser().getGrade() == 0;
        return holder;
    }

    @Override
    public void onBindViewHolder(final AttachmentViewHolder holder, int position) {
        final Attachment attachment = attachments.get(position);
        if (admin) {
            holder.ivDelete.setVisibility(View.VISIBLE);
            holder.ivEdit.setVisibility(View.VISIBLE);
            holder.ivEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setEditStatus(holder);
                }
            });
            holder.ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    postDelete(attachment);
                }
            });
            holder.ivCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setUnEditStatus(holder);
                }
            });
            holder.ivSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    postSave(attachment.getAid(), holder.attachmentName.getText().toString(), holder);
                }
            });
        } else if (isFinish) {
            holder.cbAt.setVisibility(View.GONE);
        }

        holder.attachmentName.setText(attachment.getName());
        holder.uploadTime.setText(ConvertUtils.Date2String(attachment.getDate()));
        holder.ivDocument.setImageResource(OpenFileUtil.getPic(attachment.getName()));

        holder.cbAt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                names.add(attachment.getName());
            }
        });

        holder.ivDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (admin) {
                    showSelectPopuWindow(holder.ivDocument);
                } else {
                    if (isFinish) {
                        openFile(attachment.getAid());
                    } else {
                        downLoadFile(attachment.getAid());
                    }
                }
            }
        });
    }

    private void showSelectPopuWindow(ImageView iv) {
        PopupWindow popupWindow = new PopupWindow(context);
        View view = View.inflate(context, R.layout.item_popu_file, null);
//        PopuHolder popuHolder=new PopuHolder(view);
        popupWindow.setContentView(view);
        popupWindow.showAsDropDown(iv);
    }

//    class PopuHolder{
//       View view;
//
//        public PopuHolder(View view) {
//            this.view = view;
//        }
//    }
    /**
     * 使用代理模式实现下载
     *
     * @param aid
     */
    private void downLoadFile(long aid) {

    }

    private void openFile(long aid) {
        FileEntry fileEntry= dbFile.getInstance(context).getFile(aid);
        Intent intent= OpenFileUtil.openFile(fileEntry.getSaveDirPath());
        context.startActivity(intent);
    }


    private void postSave(long id, String text, final AttachmentViewHolder holder) {
        dialog.show();
        ApiWrapper<ServerAPI> wrapper = new ApiWrapper<>();
        wrapper.targetClass(ServerAPI.class).getAPI().updateAttachment(id, text)
                .compose(wrapper.<String>applySchedulers())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        dialog.dismiss();
                        if (ExceptionFilter.filter(context, e)) {
                            ToastUtil.showException(context);
                        }
                    }

                    @Override
                    public void onNext(String s) {
                        dialog.dismiss();
                        ToastUtil.showToast(context, "保存成功!");
                        setUnEditStatus(holder);
                    }
                });
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


    private void postDelete(final Attachment attachment) {
        dialog.show();
        ApiWrapper<ServerAPI> wrapper = new ApiWrapper<>();
        wrapper.targetClass(ServerAPI.class).getAPI().deleteAttachment(attachment.getAid())
                .compose(wrapper.<String>applySchedulers())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        dialog.dismiss();
                        if (ExceptionFilter.filter(context, e)) {
                            ToastUtil.showToast(context, "删除失败");
                        }
                    }

                    @Override
                    public void onNext(String s) {
                        dialog.dismiss();
                        ToastUtil.showToast(context, "删除成功！");
                        attachments.remove(attachment);
                        notifyDataSetChanged();
                    }
                });
    }

    private void setEditStatus(AttachmentViewHolder holder) {
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
    public int getItemCount() {
        return attachments.size();
    }

    @OnClick({R.id.check_file, R.id.update_file})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.check_file:
                break;
            case R.id.update_file:
                break;
        }
    }


    class AttachmentViewHolder extends RecyclerView.ViewHolder {
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

        public AttachmentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
