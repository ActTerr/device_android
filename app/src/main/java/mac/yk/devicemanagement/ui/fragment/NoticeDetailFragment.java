package mac.yk.devicemanagement.ui.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.application.MyMemory;
import mac.yk.devicemanagement.bean.Notice;
import mac.yk.devicemanagement.down.NoticeDetailActivity;
import mac.yk.devicemanagement.gson.UtilGsonBuilder;
import mac.yk.devicemanagement.net.ApiWrapper;
import mac.yk.devicemanagement.net.ServerAPI;
import mac.yk.devicemanagement.util.ConvertUtils;
import mac.yk.devicemanagement.util.ExceptionFilter;
import mac.yk.devicemanagement.util.L;
import mac.yk.devicemanagement.util.MFGT;
import mac.yk.devicemanagement.util.ToastUtil;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by mac-yk on 2017/5/11.
 */

public class NoticeDetailFragment extends BaseFragment {

    @BindView(R.id.notice_title)
    EditText noticeTitle;
    @BindView(R.id.notice_common)
    EditText noticeCommon;
    @BindView(R.id.iv_delete)
    ImageView ivDelete;
    @BindView(R.id.iv_edit)
    ImageView ivEdit;

    String TAG = "NoticeDetailFragment";
    ProgressDialog dialog;
    Notice notice;
    boolean isEdit;
    Context context;
    @BindView(R.id.notice_time)
    TextView noticeTime;

    Handler handler;
    @BindView(R.id.btn_ll)
    LinearLayout btnLl;
    @BindView(R.id.division)
    View division;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_notice_detail, container, false);
        ButterKnife.bind(this, view);
        NoticeDetailActivity activity = (NoticeDetailActivity) getActivity();
        handler = activity.handler;
        context = getContext();
        dialog = new ProgressDialog(context);
        getBundle();
        initView();
        setViewStatus();

        return view;
    }

    private void initView() {
        if (notice == null) {
            setEditStatus();
        } else {
            setUNEditStatus();
            String title = notice.getTitle();
            noticeTitle.setText(title);
//            String common = ;
            noticeCommon.setText(notice.getCommon());
            noticeTime.setText(ConvertUtils.Date2String(notice.getDate()));
        }
    }

    private void setViewStatus() {
        if (MyMemory.getInstance().getUser().getGrade() == 0) {
            ivEdit.setVisibility(View.VISIBLE);
            ivDelete.setVisibility(View.VISIBLE);
        }

    }

    private void setEditStatus() {
        handler.sendEmptyMessage(NoticeDetailActivity.close);
        isEdit = true;
        btnLl.setVisibility(View.VISIBLE);
        noticeCommon.setFocusable(true);
        noticeCommon.setFocusableInTouchMode(true);
        noticeTitle.setFocusable(true);
        noticeTitle.setFocusableInTouchMode(true);
        noticeTitle.requestFocus();
        noticeCommon.requestFocus();
        division.setVisibility(View.GONE);
    }

    private void setUNEditStatus() {
        L.e(TAG, "execute unedit");
        handler.sendEmptyMessage(NoticeDetailActivity.open);
        isEdit = false;
        btnLl.setVisibility(View.GONE);
        noticeCommon.setFocusable(false);
        noticeCommon.setFocusableInTouchMode(false);
        noticeTitle.setFocusable(false);
        noticeTitle.setFocusableInTouchMode(false);
        division.setVisibility(View.VISIBLE);
    }

    private void getBundle() {
        Bundle bundle = getArguments();
        notice = (Notice) bundle.getSerializable("notice");
        isEdit = bundle.getBoolean("isEdit");
    }

    @OnClick({R.id.btn_save, R.id.iv_delete, R.id.iv_edit, R.id.btn_cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_save:
                postSaveNotice();
                break;
            case R.id.btn_cancel:
                noticeCommon.setText(notice.getCommon());
                noticeTitle.setText(notice.getTitle());
                setUNEditStatus();
                break;
            case R.id.iv_delete:
                showDeleteDialog();
                break;
            case R.id.iv_edit:
                if (isEdit) {
                    ToastUtil.showToast(context, "已是可编辑状态");
                } else {
                    setEditStatus();
                }
                break;
        }
    }

    private void showDeleteDialog() {
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("确定删除该条公告吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        postDelete();
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

    private void postDelete() {

        dialog.show();
        ApiWrapper<ServerAPI> wrapper = new ApiWrapper<>();
        wrapper.targetClass(ServerAPI.class).getAPI().deleteNotice(notice.getNid())
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
                        ToastUtil.showToast(context, "删除成功!");
                        MFGT.finish((Activity) context);
                    }
                });
    }

    private void postSaveNotice() {

        if (noticeTitle.getText() == null || noticeCommon.getText() == null) {
            ToastUtil.showToast(context, "标题或者内容不能为空！");
            return;
        }
        dialog.show();
        final Notice sn = new Notice(System.currentTimeMillis(), noticeTitle.getText().toString(),
                new Date(System.currentTimeMillis()), noticeCommon.getText().toString());
        Gson gson= UtilGsonBuilder.create();
        String json = gson.toJson(sn);
        ApiWrapper<ServerAPI> wrapper = new ApiWrapper<>();
        long nid=0;
        if (notice!=null){
            notice.getNid();
        }
        wrapper.targetClass(ServerAPI.class).getAPI().updateNotice(nid, json)
                .compose(wrapper.<String>applySchedulers())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        dialog.dismiss();
                        if (ExceptionFilter.filter(context, e)) {
                            ToastUtil.showToast(context, "保存失败");
                        }
                    }

                    @Override
                    public void onNext(String s) {
                        dialog.dismiss();
                        ToastUtil.showToast(context, "保存成功");
                        notice = sn;
                        setUNEditStatus();
                        NoticeDetailActivity activity= (NoticeDetailActivity) context;
                        activity.
                    }
                });
    }



}
