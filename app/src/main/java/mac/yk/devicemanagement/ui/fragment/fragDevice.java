package mac.yk.devicemanagement.ui.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mac.yk.devicemanagement.I;
import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.adapter.DeviceAdapter;
import mac.yk.devicemanagement.bean.Device;
import mac.yk.devicemanagement.net.ApiWrapper;
import mac.yk.devicemanagement.net.ServerAPI;
import mac.yk.devicemanagement.util.ConvertUtils;
import mac.yk.devicemanagement.util.ExceptionFilter;
import mac.yk.devicemanagement.util.L;
import mac.yk.devicemanagement.util.ToastUtil;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by mac-yk on 2017/3/3.
 */
public class fragDevice extends BaseFragment {

    Context context;
    @BindView(R.id.tv)
    TextView tv;
    @BindView(R.id.rv)
    RecyclerView rv;

    int page = 1;
    int nameSelected = 0;
    int statusSelected = 0;
    DeviceAdapter deviceAdapter;
    GridLayoutManager gridLayoutManager;
    boolean isMore = true;
    ProgressDialog pd;
    Integer[] tongji;
    PopupWindow namePop,statusPop;
    View vName, vStatus;
    View view;
    ArrayList<ArrayList<Device>> diantais = new ArrayList<>();
    ArrayList<ArrayList<Device>> dianchis = new ArrayList<>();
    ArrayList<ArrayList<Device>> jikongqis = new ArrayList<>();
    ArrayList<ArrayList<Device>> qukongqis = new ArrayList<>();
    ArrayList<ArrayList<Integer>> pages = new ArrayList<>();
    ArrayList<ArrayList<Boolean>> mores = new ArrayList<>();
    @BindView(R.id.Prompt)
    TextView Prompt;
    final static String TAG="fragDevice";
    public fragDevice() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_tongji, container, false);
        ButterKnife.bind(this, view);
        context = getContext();
        deviceAdapter = new DeviceAdapter(context);
        gridLayoutManager = new GridLayoutManager(context, 1);
        pd = new ProgressDialog(context);
        rv.setAdapter(deviceAdapter);
        rv.setLayoutManager(gridLayoutManager);
        gettongji();
        setListener();
        setHasOptionsMenu(true);
        initMemory();
        initList();
        new namePopuHolder();
        new statusPopuHolder();
        return view;
    }

    private void initList() {
        for (int i=0;i<5;i++){
            diantais.add(new ArrayList<Device>());
            dianchis.add(new ArrayList<Device>());
            jikongqis.add(new ArrayList<Device>());
            qukongqis.add(new ArrayList<Device>());
        }
    }

    private void initMemory() {
        for (int i = 0; i < 5; i++) {
            ArrayList<Integer> pages2= new ArrayList<>();
            pages.add(pages2);
            ArrayList<Boolean> mores2=new ArrayList<>();
            mores.add(mores2);
            for (int x = 0; x < 5; x++) {
                pages2.add(1);
                mores2.add(true);
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.getItem(0).setVisible(true);
        menu.getItem(1).setVisible(true);
        menu.getItem(3).setVisible(false);
    }

    Observer<Integer[]> obTongji = new Observer<Integer[]>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            if (ExceptionFilter.filter(context, e)) {
                ToastUtil.showToast(context, "查询失败");
            }
        }

        @Override
        public void onNext(Integer[] integers) {
            tongji = integers;
            setTitle();
        }
    };

    private void gettongji() {
        ApiWrapper<ServerAPI> wrapper = new ApiWrapper<>();
        subscription = wrapper.targetClass(ServerAPI.class).getAPI().
                getTongji(I.DEVICE.TABLENAME).subscribeOn(Schedulers.io())
                .compose(wrapper.<Integer[]>applySchedulers())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(obTongji);

    }

    private void setTitle() {
        if (nameSelected == 0) {
            int count = 0;
            for (int i = 1; i < 5; i++) {
                count += tongji[i];
            }
            tv.setText("设备总数：" + count);
        } else {
            tv.setText(ConvertUtils.getDname(nameSelected) + "个数:" + tongji[nameSelected]);
        }
    }


    private void setListener() {
        rv.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                int lastPosition = gridLayoutManager.findLastVisibleItemPosition();
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastPosition == deviceAdapter.getItemCount() - 1 && isMore) {
                    page++;
                    downData();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

            }
        });

    }

    private void downData() {
        pd.show();
        ApiWrapper<ServerAPI> wrapper = new ApiWrapper<>();
        subscription = wrapper.targetClass(ServerAPI.class).getAPI().downDevice(page, 10, nameSelected, statusSelected)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(wrapper.<Device[]>applySchedulers())
                .subscribe(new Observer<Device[]>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        pd.dismiss();
                        if (ExceptionFilter.filter(context, e)) {
                            isMore = false;
                            ToastUtil.showNoMore(context);
                        }
                    }

                    @Override
                    public void onNext(Device[] devices) {
                        pd.dismiss();
                        ArrayList<Device> list = ConvertUtils.array2List(devices);
                        L.e("TAG", "list" + list.size());
                        setDataChanged(list);
                        if (list.size() < 10) {
                            isMore = false;
                        }
                    }
                });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.select_name:
                showSelNamePopu();
                break;
            case R.id.select_status:
                showSelStatusPopu();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    class namePopuHolder {
        public namePopuHolder() {
            vName = LayoutInflater.from(context).inflate(R.layout.item_name_popu, null);
            ButterKnife.bind(this, vName);
        }

        @OnClick({R.id.diantai, R.id.jikongqi, R.id.qukongqi, R.id.dianchi})
        public void onClick(View view) {
            pages.get(nameSelected).add(statusSelected, page);
            mores.get(nameSelected).add(statusSelected, isMore);
            switch (view.getId()) {
                case R.id.dianchi:
                    nameSelected = I.DNAME.DIANCHI;
                    break;
                case R.id.diantai:
                    nameSelected = I.DNAME.DIANTAI;
                    break;
                case R.id.qukongqi:
                    nameSelected = I.DNAME.QUKONGQI;
                    break;
                case R.id.jikongqi:
                    nameSelected = I.DNAME.JIKONGQI;
                    break;
            }
            setTitle();
            if (nameSelected != 0) {
                Prompt.setVisibility(View.GONE);
            }
            statusSelected = 0;
            page = pages.get(nameSelected).get(statusSelected);
            isMore = mores.get(nameSelected).get(statusSelected);
            if (page==1&&isMore){
                deviceAdapter.clear();
                downData();
                L.e("TAG","downData");
            }else {
                L.e("TAG","dataChange");
                setDataChanged(null);
            }
            namePop.dismiss();
        }

    }

    private void showSelNamePopu() {

        int width = ConvertUtils.dp2px(context, 100);
        int height = ConvertUtils.dp2px(context, 170);
        namePop = new PopupWindow(vName, width, height);
        namePop.setOutsideTouchable(true);
        namePop.setTouchable(true);
        namePop.setFocusable(true);
        namePop.setBackgroundDrawable(new BitmapDrawable());
        namePop.showAtLocation(view, Gravity.TOP, ConvertUtils.dp2px(context, 50), ConvertUtils.dp2px(context, 81));
    }

    class statusPopuHolder {
        public statusPopuHolder() {
            vStatus = LayoutInflater.from(context).inflate(R.layout.item_status_popu, null);
            ButterKnife.bind(this, vStatus);
        }
        @OnClick({R.id.beiyong, R.id.daiyong, R.id.yunxing, R.id.weixiu, R.id.yonghou, R.id.all})
        public void onClick(View view) {
            pages.get(nameSelected).set(statusSelected, page);
            mores.get(nameSelected).set(statusSelected, isMore);
            switch (view.getId()) {
                case R.id.beiyong:
                    statusSelected= I.CONTROL.BEIYONG;
                    break;
                case R.id.daiyong:
                    statusSelected= I.CONTROL.DAIYONG;
                    break;
                case R.id.yunxing:
                    statusSelected= I.CONTROL.YUNXING;
                    break;
                case R.id.weixiu:
                    statusSelected= I.CONTROL.WEIXIU;
                    break;
                case R.id.yonghou:
                    statusSelected = I.CONTROL.YONGHOU;
                    break;
                case R.id.all:
                    statusSelected= 0;
                    break;
            }
            page = pages.get(nameSelected).get(statusSelected);
            isMore = mores.get(nameSelected).get(statusSelected);
            if (page == 1 && isMore) {
                deviceAdapter.clear();
                downData();
            }else {
                setDataChanged(null);
            }
            statusPop.dismiss();
        }

    }

    private void showSelStatusPopu() {
        new statusPopuHolder();
        int width = ConvertUtils.dp2px(context, 100);
        int height = ConvertUtils.dp2px(context, 170);
        statusPop = new PopupWindow(vStatus, width, height);
        statusPop.setOutsideTouchable(true);
        statusPop.setTouchable(true);
        statusPop.setFocusable(true);
        statusPop.setBackgroundDrawable(new BitmapDrawable());
        statusPop.showAtLocation(view, Gravity.TOP, 0, ConvertUtils.dp2px(context, 81));
    }

    private void setDataChanged(ArrayList<Device> list) {
        if (list == null) {
            deviceAdapter.changeData(getNameList().get(statusSelected));
        } else {
            getNameList().get(statusSelected).addAll(list);
            deviceAdapter.addData(list);
        }
    }

    private ArrayList<ArrayList<Device>> getNameList() {
        switch (nameSelected) {
            case I.DNAME.DIANCHI:
                return dianchis;
            case I.DNAME.JIKONGQI:
                return jikongqis;
            case I.DNAME.QUKONGQI:
                return qukongqis;
            case I.DNAME.DIANTAI:
                return diantais;
        }
        return null;

    }
//    /**
//     * 减少for循环次数
//     *
//     * @param ischange
//     * @param list
//     */
//    private void SetSelectedList(boolean ischange, ArrayList<Device> list) {
//        ArrayList<Device> slist = new ArrayList<>();
//        if (status == 0 && selected == 0) {
//            slist.addAll(list);
//        }else {
//            for (Device d : mDevices) {
//                if (selected == 0 && status == d.getStatus()) {
//                    slist.add(d);
//                } else if (status == 0 && selected == d.getDname()) {
//                    slist.add(d);
//                } else if (d.getStatus() == status && d.getDname() == selected) {
//                    slist.add(d);
//                }
//        }
//            if (ischange) {
//                deviceAdapter.changeData(slist);
//            } else {
//                deviceAdapter.addData(slist);
//            }
//        }
//    }

    @OnClick(R.id.btn_top)
    public void onClick() {
        rv.scrollToPosition(0);
    }
}
