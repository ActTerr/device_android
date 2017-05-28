package mac.yk.devicemanagement.ui.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.Serializable;

import butterknife.BindView;
import butterknife.ButterKnife;
import mac.yk.devicemanagement.R;

public class AttachmentViewHolder extends RecyclerView.ViewHolder implements Serializable {
            @BindView(R.id.cb_at)
           public CheckBox cbAt;
            @BindView(R.id.iv_document)
            public    ImageView ivDocument;
            @BindView(R.id.attachment_name)
            public  EditText attachmentName;
            @BindView(R.id.upload_time)
            public  TextView uploadTime;
            @BindView(R.id.iv_save)
            public  ImageView ivSave;
            @BindView(R.id.iv_cancel)
            public ImageView ivCancel;
            @BindView(R.id.iv_edit)
            public ImageView ivEdit;
            @BindView(R.id.iv_delete)
            public  ImageView ivDelete;
            @BindView(R.id.pb)
            public  ProgressBar pb;
            @BindView(R.id.iv_control)
            public ImageView ivControl;
            @BindView(R.id.tv_cancel)
            public TextView tvCancel;

            public AttachmentViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }