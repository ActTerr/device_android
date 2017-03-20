package mac.yk.devicemanagement.ui.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xys.libzxing.zxing.activity.CaptureActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mac.yk.devicemanagement.I;
import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.adapter.DeviceAdapter;
import mac.yk.devicemanagement.bean.Device;
import mac.yk.devicemanagement.model.IModel;
import mac.yk.devicemanagement.net.ApiWrapper;
import mac.yk.devicemanagement.net.ServerAPI;
import mac.yk.devicemanagement.util.ConvertUtils;
import mac.yk.devicemanagement.util.ExceptionFilter;
import mac.yk.devicemanagement.util.L;
import mac.yk.devicemanagement.util.TestUtil;
import mac.yk.devicemanagement.util.ToastUtil;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by mac-yk on 2017/3/3.
 */

public class fragDevice extends BaseFragment {

    IModel model;
    Context context;
    @BindView(R.id.tv)
    TextView tv;
    @BindView(R.id.rv)
    RecyclerView rv;

    int page = 1;
    int selected = 0;
    ArrayList<Device> mDevices = new ArrayList<>();
    DeviceAdapter deviceAdapter;
    ArrayList<Device> currentDevices = new ArrayList<>();
    GridLayoutManager gridLayoutManager;
    boolean isMore;
    ProgressDialog pd;
    Integer[] tongji;

    public fragDevice() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_tongji, container, false);
        ButterKnife.bind(this, view);
        model = TestUtil.getData();
        context = getContext();
        deviceAdapter = new DeviceAdapter(context);
        gridLayoutManager = new GridLayoutManager(context, 1);
        pd = new ProgressDialog(context);
        rv.setAdapter(deviceAdapter);
        rv.setLayoutManager(gridLayoutManager);
        downData();
        gettongji();
        setListener();
        setHasOptionsMenu(true);
        return view;
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
            L.e("integer", integers.toString());
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
        if (selected == 0) {
            int count = 0;
            for (int i = 1; i < 5; i++) {
                count += tongji[i];
            }
            tv.setText("设备总数：" + count);
        } else {
            tv.setText(ConvertUtils.getDname(selected) + "个数:" + tongji[selected]);
        }
    }


    private void setListener() {
        rv.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                int lastPosition = gridLayoutManager.findLastVisibleItemPosition();
                L.e("main", lastPosition + " count:" + deviceAdapter.getItemCount() + "flag" + isMore);
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
        subscription = wrapper.targetClass(ServerAPI.class).getAPI().downDevice(page, 10)
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
                        L.e("main", "list" + list.size());
                        mDevices.addAll(list);
                        if (selected == 0) {
                            deviceAdapter.addData(list);
                        } else {
                            SetSelectedList(selected, false, list);
                        }
                        isMore = true;
                    }
                });

    }

    public void scan(int id) {
        getActivity().startActivityForResult(new Intent(getActivity(), CaptureActivity.class), id);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_name, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.dianchi:
                selected = I.DNAME.DIANCHI;
                break;
            case R.id.diantai:
                selected = I.DNAME.DIANTAI;
                break;
            case R.id.qukongqi:
                selected = I.DNAME.QUKONGQI;
                break;
            case R.id.jikongqi:
                selected = I.DNAME.JIKONGQI;
                break;
            case R.id.action_capture:
                scan(I.CONTROL.START);
                break;
        }
        setTitle();
        SetSelectedList(selected, true, null);
        return true;
    }

    /**
     * 减少for循环次数
     *
     * @param selected
     * @param ischange
     * @param list
     */
    private void SetSelectedList(int selected, boolean ischange, ArrayList<Device> list) {
        ArrayList<Device> slist = new ArrayList<>();
        if (ischange) {
            for (Device d : mDevices) {
                if (d.getDname() == selected) {
                    slist.add(d);
                }
            }
            deviceAdapter.changeData(slist);
        } else {
            for (Device d : list) {
                if (d.getDname() == selected) {
                    slist.add(d);
                }
            }
            deviceAdapter.addData(slist);
        }
    }


    @OnClick(R.id.btn_top)
    public void onClick() {
        rv.setScrollingTouchSlop(0);
    }
}
