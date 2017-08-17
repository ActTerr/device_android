package mac.yk.devicemanagement.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mac.yk.customdialog.CustomDialog;
import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.adapter.FormTotalAdapter;
import mac.yk.devicemanagement.application.MyMemory;
import mac.yk.devicemanagement.bean.User;
import mac.yk.devicemanagement.net.ApiWrapper;
import mac.yk.devicemanagement.net.ServerAPI;
import mac.yk.devicemanagement.util.ConvertUtils;
import mac.yk.devicemanagement.util.ExceptionFilter;
import mac.yk.devicemanagement.util.L;
import mac.yk.devicemanagement.util.ToastUtil;
import rx.Subscriber;

/**
 * Created by mac-yk on 2017/4/30.
 */

public class TotalCountFragment extends BaseFragment {
    @BindView(R.id.list)
    ListView lv;
    ArrayList<ArrayList<String>> data;

    int transceiver;
    int fixed;
    int move;
    int zoneController;

    String year = "all";
    CustomDialog dialog;
    @BindView(R.id.add_from)
    Button addFrom;

    String TAG="totalCount";
    User user;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_count, container, false);
        ButterKnife.bind(this, view);
        L.e("cao", "onCreate");
        dialog= CustomDialog.create(getContext(),"加载中...",false,null);
        setHasOptionsMenu(true);
        user=MyMemory.getInstance().getUser();

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            return true;
        }
        if (item.getItemId() == R.id.all) {
            year = "all";
        } else if(item.getItemId()==android.R.id.home){
            return true;
        }else {
            year = String.valueOf(item.getTitle());
        }
        L.e(TAG,"select"+year);
        if (data!=null){
            data.clear();
        }
        EventBus.getDefault().post(year+"年设备数量统计");
        addFrom.setVisibility(View.GONE);
        
        initView();
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_year,menu);
    }



    private void initView() {
        EventBus.getDefault().post(year+"年设备数量统计");
        dialog.show();
        data = new ArrayList<>();
        final ArrayList<String> list = new ArrayList<>();
        String[] arra = new String[]{"顺号", "单位", "手持电台", "固定机控器", "移动机控器", "区控器"};
        list.addAll(ConvertUtils.array2List(arra));
        data.add(list);
        ApiWrapper<ServerAPI> wrapper = new ApiWrapper<>();
        wrapper.targetClass(ServerAPI.class).getAPI().getTotalCount(user.getUnit(), year, "default")
                .compose(wrapper.<ArrayList<String[]>>applySchedulers())
                .timeout(30, TimeUnit.SECONDS)
                .subscribe(new Subscriber<ArrayList<String[]>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        dialog.dismiss();
                        if (ExceptionFilter.filter(getContext(),e)){
                            ToastUtil.showException(getContext());
                        }
                    }

                    @Override
                    public void onNext(ArrayList<String[]> strings) {
                        dialog.dismiss();
                        for (int i = 0; i < strings.size(); i++) {
                            ArrayList<String> list = new ArrayList<String>();
                            list.add(i + 1 + "");
                            list.add(ConvertUtils.getUnitName(i + 1));
                            list.addAll(ConvertUtils.array2List(strings.get(i)));
                             transceiver+= Integer.parseInt(strings.get(i)[0]);
                            fixed += Integer.parseInt(strings.get(i)[1]);
                            move += Integer.parseInt(strings.get(i)[2]);
                            zoneController += Integer.parseInt(strings.get(i)[3]);
                            L.e(i + "", list.toString());
                            data.add(list);
                        }

                        if(user.getGrade()==0){
                            ArrayList<String> list1 = new ArrayList<String>();
                            list1.add("");
                            list1.add("合计");
                            list1.add(String.valueOf(transceiver));
                            list1.add(String.valueOf(fixed));
                            list1.add(String.valueOf(move));
                            list1.add(String.valueOf(zoneController));
                            data.add(list1);
                        }
                        lv.setAdapter(new FormTotalAdapter(getContext(), data));

                    }
                });

    }



    @OnClick(R.id.add_from)
    public void onClick() {
        addFrom.setVisibility(View.GONE);
        initView();
    }
}
