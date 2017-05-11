package mac.yk.devicemanagement.adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import mac.yk.devicemanagement.MyApplication;
import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.bean.Notice;
import mac.yk.devicemanagement.bean.User;
import mac.yk.devicemanagement.net.ApiWrapper;
import mac.yk.devicemanagement.net.ServerAPI;
import mac.yk.devicemanagement.util.ExceptionFilter;
import mac.yk.devicemanagement.util.MFGT;
import mac.yk.devicemanagement.util.ToastUtil;
import rx.Subscriber;

/**
 * Created by mac-yk on 2017/5/10.
 */

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.NoticeViewHolder> {
    Context context;
    String TAG = "NoticeAdapter";
    ArrayList<Notice> list;

    User user;
    ProgressDialog dialog;

    public NoticeAdapter(Context context, ArrayList<Notice> list) {
        this.context = context;
        this.list = list;
        dialog=new ProgressDialog(context);
    }

    @Override
    public NoticeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_notice, parent, false);
        NoticeViewHolder viewHolder = new NoticeViewHolder(view);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(NoticeViewHolder holder, int position) {
        final Notice notice = list.get(position);
        user = MyApplication.getInstance().getUser();
        if (user.getGrade()== 0) {
            holder.ivDelete.setVisibility(View.VISIBLE);
            holder.ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog dialog=new AlertDialog.Builder(context)
                            .setTitle("确定删除该条公告吗？")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    postDelete(String.valueOf(notice.getNid()));
                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).create();
                    dialog.show();
                }
            });
            holder.ivEdit.setVisibility(View.VISIBLE);
            holder.ivEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MFGT.gotoNoticeDetail(context,true,notice);
                }
            });
        }
        holder.noticePosition.setText(position + 1 + ".");
        holder.noticeTitle.setText(notice.getTitle());
        holder.noticeTime.setText(notice.getDate().toString());

    }


    @Override
    public int getItemCount() {
        return list.size();
    }



    private void postDelete(String Nid) {
        dialog.show();
        ApiWrapper<ServerAPI> wrapper=new ApiWrapper<>();
        wrapper.targetClass(ServerAPI.class).getAPI().deleteNotice(Nid)
                .compose(wrapper.<String>applySchedulers())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        dialog.dismiss();
                        if (ExceptionFilter.filter(context,e)){
                            ToastUtil.showException(context);
                        }
                    }

                    @Override
                    public void onNext(String s) {
                        dialog.dismiss();
                        ToastUtil.showToast(context,"删除成功!");
                    }
                });
    }

    class NoticeViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.notice_position)
        TextView noticePosition;
        @BindView(R.id.notice_title)
        TextView noticeTitle;
        @BindView(R.id.notice_time)
        TextView noticeTime;
        @BindView(R.id.iv_edit)
        ImageView ivEdit;
        @BindView(R.id.iv_delete)
        ImageView ivDelete;

        public NoticeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
