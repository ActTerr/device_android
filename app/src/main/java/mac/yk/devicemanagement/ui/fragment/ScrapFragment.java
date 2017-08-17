package mac.yk.devicemanagement.ui.fragment;


import android.content.Context;
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
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mac.yk.customdialog.CustomDialog;
import mac.yk.devicemanagement.I;
import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.adapter.ScrapAdapter;
import mac.yk.devicemanagement.bean.Scrap;
import mac.yk.devicemanagement.net.ApiWrapper;
import mac.yk.devicemanagement.net.ServerAPI;
import mac.yk.devicemanagement.util.ConvertUtils;
import mac.yk.devicemanagement.util.ExceptionFilter;
import mac.yk.devicemanagement.util.L;
import mac.yk.devicemanagement.util.ToastUtil;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by mac-yk on 2017/3/13.
 */

public class ScrapFragment extends BaseFragment {
    Context context;
    @BindView(R.id.tv)
    TextView tv;
    @BindView(R.id.rv)
    RecyclerView rv;
    final static String TAG = "ScrapFragment";
    int page = 1;
    int selected = 0;
    ScrapAdapter scrapAdapter;
    GridLayoutManager gridLayoutManager;
    boolean isMore = true;
    CustomDialog pd;
    Integer[] tongji;
    ArrayList<Scrap> diantais = new ArrayList<>();
    ArrayList<Scrap> dianchis = new ArrayList<>();
    ArrayList<Scrap> jikongqis = new ArrayList<>();
    ArrayList<Scrap> qukongqis = new ArrayList<>();
    boolean show = true;
    @BindView(R.id.Prompt)
    TextView Prompt;
    ArrayList<Integer> pages = new ArrayList<>();
    ArrayList<Boolean> mores = new ArrayList<>();
    @BindView(R.id.btn_top)
    Button btnTop;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_scrap, container, false);
        ButterKnife.bind(this, view);
        context = getContext();
        scrapAdapter = new ScrapAdapter(context);
        gridLayoutManager = new GridLayoutManager(context, 1);
        pd = CustomDialog.create(getContext(),"加载中...",false,null);
        rv.setAdapter(scrapAdapter);
        rv.setLayoutManager(gridLayoutManager);
        btnTop.setVisibility(View.GONE);
        setListener();
        setHasOptionsMenu(true);
        initMemory();
        getActivity().setTitle("报废列表");
        return view;
    }

    private void initMemory() {
        for (int i = 0; i < 4; i++) {
            pages.add(1);
            mores.add(true);
        }

    }
//
//    private void showPrompt() {
//        Observable.just(selected).subscribe(new Action1<Integer>() {
//            @Override
//            public void call(Integer integer) {
//                L.e("main", "rx被调用");
//                if (integer != 0) {
//                    Prompt.setVisibility(View.GONE);
//                    downData();
//                }
//            }
//        });
//    }


    private void setTitle() {
        if (tongji != null) {
            if (selected == 0) {
                int count = 0;
                for (int i = 1; i < 5; i++) {
                    count += tongji[i];
                }
                tv.setText("设备总数：" + count);
            } else {
                tv.setText(ConvertUtils.getdevName(selected) + "个数:" + tongji[selected]);
            }
        }

    }

    private void setListener() {
        rv.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                int lastPosition = gridLayoutManager.findLastVisibleItemPosition();
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastPosition == scrapAdapter.getItemCount() - 1 && isMore) {
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
        subscription = wrapper.targetClass(ServerAPI.class).getAPI().downScrap(page, 10, selected)
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
                        isMore = false;
                    }

                    @Override
                    public void onNext(Scrap[] scraps) {
                        pd.dismiss();
                        ArrayList<Scrap> list = ConvertUtils.array2List(scraps);
                        L.e("main", "list" + list.size());
                        SetSelectedList(list);
                        if (scraps.length < 10) {
                            isMore = false;
                        }
                    }
                });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.findItem(R.id.dianchi).setVisible(true);
        menu.findItem(R.id.qukongqi).setVisible(true);
        menu.findItem(R.id.jikongqi).setVisible(true);
        menu.findItem(R.id.diantai).setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        pages.add(selected, page);
        mores.add(selected, isMore);
        switch (item.getItemId()) {
            case R.id.dianchi:
                selected = I.DNAME.BATTERY;
                break;
            case R.id.diantai:
                selected = I.DNAME.TRANSCEIVER;
                break;
            case R.id.qukongqi:
                selected = I.DNAME.ZONE_CONTROLLER;
                break;
            case R.id.jikongqi:
                selected = I.DNAME.MACHINE_CONTROLLER;
                break;
        }
        if (show) {
            if (selected != 0) {
                Prompt.setVisibility(View.GONE);
                show = false;
            }
        }
        if (item.getItemId() == R.id.action_capture) {
            return true;
        }else if(item.getItemId()==android.R.id.home){
            return true;
        }
        page = pages.get(selected);
        isMore = mores.get(selected);
        if (page == 1 && isMore) {
            scrapAdapter.clear();
            downData();
        } else {
            SetSelectedList(null);
        }
        setTitle();
        return true;
    }

    private void SetSelectedList(ArrayList<Scrap> list) {
        if (list == null) {
            scrapAdapter.changeData(getCurrentList());
        } else {
            getCurrentList().addAll(list);
            scrapAdapter.addData(list);
        }
    }

    private ArrayList<Scrap> getCurrentList() {
        switch (selected) {
            case I.DNAME.BATTERY:
                return dianchis;
            case I.DNAME.TRANSCEIVER:
                return diantais;
            case I.DNAME.MACHINE_CONTROLLER:
                return jikongqis;
            case I.DNAME.ZONE_CONTROLLER:
                return qukongqis;
        }
        return null;
    }

    /**
     * 减少for循环次数
     */
//    private void SetSelectedList(ArrayList<Scrap> list) {
//        ArrayList<Scrap> changelist;
//        ArrayList<Scrap> selectList=new ArrayList<>();
//        if (list==null){
//            changelist=devices;
//        }else {
//            changelist=list;
//        }
//            if (selected==0){
//                selectList.addAll(changelist);
//            }else {
//                for (Scrap d : changelist) {
//                    if (d.getdevName() == selected) {
//                        selectList.add(d);
//                    }
//                }
//            }
//        if (list==null) {
//            scrapAdapter.changeData(selectList);
//        } else {
//            scrapAdapter.addData(selectList);
//        }
//    }
    @OnClick(R.id.btn_top)
    public void onClick() {
        rv.scrollToPosition(0);

    }

}
