package mac.yk.devicemanagement.ui.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.adapter.NoticeAdapter;
import mac.yk.devicemanagement.application.MyMemory;
import mac.yk.devicemanagement.bean.Notice;
import mac.yk.devicemanagement.net.ApiWrapper;
import mac.yk.devicemanagement.net.ServerAPI;
import mac.yk.devicemanagement.util.ExceptionFilter;
import mac.yk.devicemanagement.util.MFGT;
import mac.yk.devicemanagement.util.ToastUtil;
import rx.Observer;

/**
 * 公告列表
 */

public class NoticeFragment extends BaseFragment {
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.iv_add)
    ImageView ivAdd;

    Context context;
    NoticeAdapter adapter;
    ArrayList<Notice> data;
    ProgressDialog pd;
    int memory;
    @BindView(R.id.add_history)
    TextView addHistory;
    @BindView(R.id.btn_down)
    Button btnDown;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_notice, container, false);
        ButterKnife.bind(this, view);
        context = getContext();
        showIv();
        data = new ArrayList<>();
        adapter = new NoticeAdapter(context, data);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(context));
        pd = new ProgressDialog(context);
        btnDown.setVisibility(View.GONE);
        initData();
        return view;
    }

    private void showIv() {
        if (MyMemory.getInstance().getUser().getGrade() == 0) {
            ivAdd.setVisibility(View.VISIBLE);
        } else {
            ivAdd.setVisibility(View.GONE);
        }
    }

    private void initData() {
        pd.show();
        ApiWrapper<ServerAPI> wrapper = new ApiWrapper<>();
        wrapper.targetClass(ServerAPI.class).getAPI().getNotice(memory)
                .compose(wrapper.<ArrayList<Notice>>applySchedulers())
                .subscribe(new Observer<ArrayList<Notice>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        pd.dismiss();
                        if (ExceptionFilter.filter(context, e)) {
                            ToastUtil.showException(context);
                        }
                    }

                    @Override
                    public void onNext(ArrayList<Notice> notices) {
                        pd.dismiss();
                        data.addAll(notices);
                        adapter.notifyDataSetChanged();
                        if (notices.size() < 6) {
                            addHistory.setVisibility(View.GONE);
                        }
                    }
                });
    }

    @OnClick({R.id.add_history, R.id.iv_add})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_history:
                memory += 6;
                initData();
                break;
            case R.id.iv_add:
                MFGT.gotoNoticeDetail(context, true, null);
                break;
        }
    }
}
