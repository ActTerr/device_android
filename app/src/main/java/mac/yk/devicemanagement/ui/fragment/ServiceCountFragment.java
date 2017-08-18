package mac.yk.devicemanagement.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mac.yk.customdialog.CustomDialog;
import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.adapter.FormServiceAdapter;
import mac.yk.devicemanagement.application.MyMemory;
import mac.yk.devicemanagement.net.ApiWrapper;
import mac.yk.devicemanagement.net.ServerAPI;
import mac.yk.devicemanagement.util.ConvertUtils;
import mac.yk.devicemanagement.util.L;
import rx.Subscriber;

import static android.content.ContentValues.TAG;

/**
 * Created by mac-yk on 2017/5/17.
 */

public class ServiceCountFragment extends BaseFragment {
    @BindView(R.id.list)
    ListView lv;
    CustomDialog dialog;

    ArrayList<ArrayList<String>> data;
    @BindView(R.id.add_from)
    Button addFrom;

    FormServiceAdapter adapter;
    boolean isAdding = false;
    int memory;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_count, container, false);
        ButterKnife.bind(this, view);
        dialog= CustomDialog.create(getContext(),"加载中...",false,null);
        data = new ArrayList<>();
        adapter = new FormServiceAdapter(getContext(), data);
        lv.setAdapter(adapter);
        setListener();
        return view;
    }

    private void setListener() {

        lv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                int lastPosition = lv.getLastVisiblePosition();
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastPosition == adapter.getCount() - 1 &&memory != 20 && !isAdding) {
                    initData();
                    L.e(TAG, "add");
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }


    private void initData() {
        dialog.show();
        isAdding = true;
        ApiWrapper<ServerAPI> wrapper = new ApiWrapper<>();
        wrapper.targetClass(ServerAPI.class).getAPI().getServiceCount(MyMemory.getInstance().getUser().getUnit())
                .compose(wrapper.<ArrayList<String[]>>applySchedulers())
                .timeout(30, TimeUnit.SECONDS)
                .subscribe(new Subscriber<ArrayList<String[]>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ArrayList<String[]> strings) {
                        dialog.dismiss();
                        isAdding = false;
                        for (String[] s : strings) {
                            data.add(ConvertUtils.array2List(s));
                        }
                        adapter.notifyDataSetChanged();
                        memory+= 5;
                    }
                });
    }


    @OnClick(R.id.add_from)
    public void onClick() {
        addFrom.setVisibility(View.GONE);
        initData();
    }


}
