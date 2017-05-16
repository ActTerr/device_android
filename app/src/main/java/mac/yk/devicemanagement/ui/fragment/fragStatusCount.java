package mac.yk.devicemanagement.ui.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import mac.yk.devicemanagement.MyMemory;
import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.adapter.FormStatusAdapter;
import mac.yk.devicemanagement.net.ApiWrapper;
import mac.yk.devicemanagement.net.ServerAPI;
import mac.yk.devicemanagement.util.ConvertUtils;
import mac.yk.devicemanagement.util.L;
import rx.Subscriber;

/**
 * Created by mac-yk on 2017/5/8.
 */

public class fragStatusCount extends BaseFragment {
    ProgressDialog dialog;
    String year = "all";
    ArrayList<ArrayList<String>> data;
    int memory = 0;
    @BindView(R.id.list)
    ListView lv;
    FormStatusAdapter adapter;

    boolean isAdding=false;
    String TAG="fragStatusCount";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_count, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        dialog = new ProgressDialog(getContext());
        data = new ArrayList<>();
        adapter=new FormStatusAdapter(getContext(),data);
        lv.setAdapter(adapter);
        initData();
//        String[] arra = new String[]{"", "备用", "待用", "运行", "待修", "维修"};
//        list.addAll(ConvertUtils.array2List(arra));
        setListener();
        return view;
    }

    private void setListener() {

        lv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                int lastPosition=lv.getLastVisiblePosition();
                if(scrollState== AbsListView.OnScrollListener.SCROLL_STATE_IDLE&&lastPosition==adapter.getCount()-1&&memory!=20&&!isAdding){
                    initData();
                    L.e(TAG,"add");
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.all) {
            year = "all";
        } else {
            year = String.valueOf(item.getItemId()).substring(1);
        }
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_year, menu);
    }

    private void initData() {
        dialog.show();
        isAdding=true;
        ApiWrapper<ServerAPI> wrapper = new ApiWrapper<>();
        wrapper.targetClass(ServerAPI.class).getAPI().getStatusCount(MyMemory.getInstance().getUser().getUnit(), year, memory)
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
                        isAdding=false;
                        for (String[] s : strings) {
                                data.add(ConvertUtils.array2List(s));
                        }
                        adapter.notifyDataSetChanged();
                        memory+=5;
                    }
                });
    }
}
