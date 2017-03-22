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
import mac.yk.devicemanagement.adapter.ScrapAdapter;
import mac.yk.devicemanagement.bean.Scrap;
import mac.yk.devicemanagement.model.IModel;
import mac.yk.devicemanagement.net.ApiWrapper;
import mac.yk.devicemanagement.net.ServerAPI;
import mac.yk.devicemanagement.util.ConvertUtils;
import mac.yk.devicemanagement.util.ExceptionFilter;
import mac.yk.devicemanagement.util.L;
import mac.yk.devicemanagement.util.TestUtil;
import mac.yk.devicemanagement.util.ToastUtil;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by mac-yk on 2017/3/13.
 */

public class fragBaofei extends BaseFragment {
    IModel model;
    Context context;
    @BindView(R.id.tv)
    TextView tv;
    @BindView(R.id.rv)
    RecyclerView rv;

    int page = 1;
    int selected = 0;
    ArrayList<Scrap> devices = new ArrayList<>();
    ScrapAdapter scrapAdapter;
    ArrayList<Scrap> currentDevices = new ArrayList<>();
    GridLayoutManager gridLayoutManager;
    boolean isMore;
    ProgressDialog pd;
    Integer[] tongji;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_tongji, container, false);
        ButterKnife.bind(this, view);
        model = TestUtil.getData();
        context = getContext();
        scrapAdapter = new ScrapAdapter(context);
        gridLayoutManager = new GridLayoutManager(context, 1);
        pd = new ProgressDialog(context);
        rv.setAdapter(scrapAdapter);
        rv.setLayoutManager(gridLayoutManager);
        downData();
        getTongji();
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


    private void getTongji() {
        ApiWrapper<ServerAPI> network = new ApiWrapper<>();
        subscription = network.targetClass(ServerAPI.class).getAPI().getTongji(I.BAOFEI.TABLENAME)
                .compose(network.<Integer[]>applySchedulers())
                .subscribeOn(Schedulers.io())
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
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastPosition == scrapAdapter.getItemCount() && isMore) {
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
        subscription = wrapper.targetClass(ServerAPI.class).getAPI().downScrap(page, 10)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(wrapper.<Scrap[]>applySchedulers())
                .subscribe(new Subscriber<Scrap[]>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        pd.dismiss();
                        if (ExceptionFilter.filter(context, e)) {
                            ToastUtil.showToast(context, "没有更多数据");
                        }
                    }

                    @Override
                    public void onNext(Scrap[] scraps) {
                        pd.dismiss();
                        ArrayList<Scrap> list = ConvertUtils.array2List(scraps);
                        L.e("main", "list" + list.size());
                        devices.addAll(list);
                        if (selected == 0) {
                            scrapAdapter.addData(list);
                        } else {
                            SetSelectedList(selected, false, list);

                        }
                        isMore = true;
                    }
                });
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
            case R.id.all:
                selected=I.DNAME.ALL;
                break;
        }
        setTitle();
        SetSelectedList(selected, true, null);
        return true;
    }

    public void scan(int id) {
        getActivity().startActivityForResult(new Intent(getActivity(), CaptureActivity.class), id);
    }

    /**
     * 减少for循环次数
     *
     * @param selected
     * @param ischange
     * @param list
     */
    private void SetSelectedList(int selected, boolean ischange, ArrayList<Scrap> list) {
        ArrayList<Scrap> slist = new ArrayList<>();
        if (ischange) {
            for (Scrap d : devices) {
                if (d.getDname() == selected) {
                    slist.add(d);
                }
            }
            scrapAdapter.changeData(slist);
        } else {
            for (Scrap d : list) {
                if (d.getDname() == selected) {
                    slist.add(d);
                }
            }
            scrapAdapter.addData(slist);
        }
    }

    @OnClick(R.id.btn_top)
    public void onClick() {
        rv.setScrollingTouchSlop(0);
    }
}
