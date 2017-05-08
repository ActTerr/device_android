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

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import mac.yk.devicemanagement.MyApplication;
import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.net.ApiWrapper;
import mac.yk.devicemanagement.net.ServerAPI;
import mac.yk.devicemanagement.util.ConvertUtils;
import rx.Subscriber;

/**
 * Created by mac-yk on 2017/5/8.
 */

public class fragStatusCount extends BaseFragment {
    ProgressDialog dialog;
    String yaer="all";
    ArrayList<ArrayList<String>> data;
    ArrayList<String> list = new ArrayList<>();
    int size=0;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_count, container, false);
        ButterKnife.bind(this, view);
        dialog=new ProgressDialog(getContext());
        initView();
        setHasOptionsMenu(true);
        String[] arra = new String[]{"", "备用", "待用", "运行", "待修","维修"};
        list.addAll(ConvertUtils.array2List(arra));
        return view;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.all){
            yaer="all";
        }else {
            yaer=String.valueOf(item.getItemId()).substring(1);
        }
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_year,menu);
    }

    private void initView() {
        dialog.show();
        data = new ArrayList<>();
        ApiWrapper<ServerAPI> wrapper=new ApiWrapper<>();
        wrapper.targetClass(ServerAPI.class).getAPI().getStatusCount(MyApplication.getInstance().getUser().getUnit(),yaer,size)
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

                    }
                });
    }
}
