package mac.yk.devicemanagement.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.adapter.weixiuAdapter;
import mac.yk.devicemanagement.adapter.xunjianAdapter;
import mac.yk.devicemanagement.bean.Weixiu;
import mac.yk.devicemanagement.bean.Xunjian;
import mac.yk.devicemanagement.net.ApiWrapper;
import mac.yk.devicemanagement.net.ServerAPI;
import mac.yk.devicemanagement.util.ConvertUtils;
import mac.yk.devicemanagement.util.ExceptionFilter;
import mac.yk.devicemanagement.util.L;
import mac.yk.devicemanagement.util.MFGT;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by mac-yk on 2017/3/2.
 */

public class fragRecord extends BaseFragment {

    boolean isDesc;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
           if (msg.what==1){
               L.e("main","执行download");
               Download(ACTION_DOWNLOAD);

           }else {

               tvRefresh.setVisibility(View.GONE);
           }
        }
    };
    final static int ACTION_ADD=1;
    final static int ACTION_DOWNLOAD=2;
   String id;
   Context context;
    int page=1;
    RecyclerView.Adapter adapter;
    LinearLayoutManager llm;
    boolean isMore=true;
    @BindView(R.id.tvRefresh)
    TextView tvRefresh;
    @BindView(R.id.rv)
            public
    RecyclerView rv;
    @BindView(R.id.goTop)
    Button goTop;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;
    ArrayList<Weixiu> wxList;
    ArrayList<Xunjian> xunjianList;
    boolean isWeixiu;
    xunjianAdapter xunjianAdapter;
    weixiuAdapter weixiuAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_currency, container, false);
        ButterKnife.bind(this, view);
        L.e("main","frag解析完布局");
        init();
        setHasOptionsMenu(true);
        return view;
    }

    private void init() {
        context= getContext();
        wxList=new ArrayList<>();
        xunjianList=new ArrayList<>();
        id=getArguments().getString("id");
        if (id==null){
            Activity a= (Activity) context;
            MFGT.finish(a);
        }
        isWeixiu=getArguments().getBoolean("flag");
        llm=new LinearLayoutManager(context);
        L.e("main","是否维修:"+isWeixiu);
        if (isWeixiu){

            adapter=new weixiuAdapter(context);
        }else {
            adapter=new xunjianAdapter(context);
        }

        srl.setColorSchemeColors(
                getResources().getColor(R.color.google_blue),
                getResources().getColor(R.color.google_green),
                getResources().getColor(R.color.google_red),
                getResources().getColor(R.color.google_yellow)
        );
        rv.setAdapter(adapter);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);
        Download(ACTION_DOWNLOAD);
        setListener();
    }
    MenuItem itemRecent;
    MenuItem itemReverse;
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_record,menu);
        itemRecent = menu.getItem(0);
        itemReverse = menu.getItem(1);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.recent){
            isDesc=true;
            item.setIcon(R.drawable.recent_selected);
            itemReverse.setIcon(R.drawable.reverse);
          setListSort(true);
        }else {
            itemRecent.setIcon(R.drawable.recent);
            item.setIcon(R.drawable.reverse_selected);
            isDesc=false;
           setListSort(false);
        }
        return true;
    }

    class MyWxComparator implements Comparator<Weixiu> {
        boolean desc;

        public MyWxComparator(boolean desc) {
            this.desc = desc;
        }
    
        @Override
        public int compare(Weixiu o1, Weixiu o2) {
            if (desc){
                if (o1.getXjDate()==null||o2.getXjDate()==null){
                    return 1;
                }
                return (int) (o2.getXjDate().getTime()-o1.getXjDate().getTime());
            }
            if (o1.getWxDate()==null||o2.getXjDate()==null){
                return -1;
            }
            return (int) (o1.getXjDate().getTime()-o2.getXjDate().getTime());
        }
    }
    
    class MyXunjianComParator implements Comparator<Xunjian>{
        boolean desc;

        public MyXunjianComParator(boolean desc) {
            this.desc = desc;
        }

        @Override
        public int compare(Xunjian o1, Xunjian o2) {
            if (desc){
                return (int) (o2.getXjDate().getTime()-o1.getXjDate().getTime());
            }
            return (int) (o1.getXjDate().getTime()-o2.getXjDate().getTime());
        }
    }
    private void setListSort(boolean flag) {
        if (isWeixiu){
            Collections.sort(wxList,new MyWxComparator(flag));
            ArrayList<Weixiu> list=new ArrayList<Weixiu>();
            Iterator<Weixiu> it=wxList.iterator();
            while (it.hasNext()){
                list.add(it.next());
            }
            weixiuAdapter.initwData(list);
        }else {
            Collections.sort(xunjianList,new MyXunjianComParator(flag));
            ArrayList<Xunjian> list=new ArrayList<>();
            Iterator<Xunjian> it=xunjianList.iterator();
            while ((it.hasNext())){
                list.add(it.next());
            }
            xunjianAdapter.initxData(list);
        }

    }

    private void setListener() {
        setPullDownListener();
        setPullUpListener();
    }

    private void setPullUpListener() {
        rv.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastPosition=llm.findLastVisibleItemPosition();
                if (newState==RecyclerView.SCROLL_STATE_IDLE
                        &&lastPosition==getList().size()-1&&isMore&&getList().size()>10){
                    page++;
                    Download(ACTION_ADD);

                    L.e("main","执行ADD");
                }else {
                    L.e("main","没有更多数据");
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int firstPosition=llm.findFirstVisibleItemPosition();
                srl.setEnabled(firstPosition==0);
            }
        });

    }

    private void Download(int actionAdd) {
        if (isWeixiu){
            downloadweixiu(actionAdd);
        }else {
            downloadxunjian(actionAdd);
        }
    }

    private List getList() {
        if (isWeixiu){
            return wxList;
        }else {
            return xunjianList;
        }
    }

    private void setPullDownListener() {
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srl.setRefreshing(true);
                tvRefresh.setVisibility(View.VISIBLE);
                tvRefresh.setText("刷新中");
                page=1;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                            handler.sendEmptyMessage(1);

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

            }
        });
    }

    private void downloadxunjian(final int Action) {
        ApiWrapper<ServerAPI> wrapper = new ApiWrapper<>();
        subscription = wrapper.targetClass(ServerAPI.class).getAPI()
                .downloadXunJian(id, page, 10)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(wrapper.<Xunjian[]>applySchedulers())
                .subscribe(new Subscriber<Xunjian[]>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (ExceptionFilter.filter(context, e)) {
                            srl.setRefreshing(false);
                            tvRefresh.setText("刷新失败");
                            isMore = false;
                            delayToInvisible();
                        }
                    }

                    @Override
                    public void onNext(Xunjian[] result) {
                        srl.setRefreshing(false);
                        L.e("main", "xunjianl:" + result.length);
                        xunjianAdapter = (xunjianAdapter) adapter;
                        ArrayList<Xunjian> xunjianLists = ConvertUtils.array2List(result);
                        L.e("main", "down size:" + xunjianLists.size());
                        if (xunjianLists.size() < 10) {
                            isMore = false;
                            L.e("main", "ismore为false");
                        }
                        if (Action == ACTION_ADD) {

                            xunjianAdapter.addxData(xunjianLists);
                            xunjianList.addAll(xunjianLists);
                        } else {
                            xunjianAdapter.initxData(xunjianLists);
                            synchronized (xunjianList) {
                                if (xunjianList != null) {
                                    xunjianList.clear();
                                }
                                xunjianList.addAll(xunjianLists);
                            }
                            isMore = true;
                        }
                        setListSort(isDesc);
                        tvRefresh.setText("刷新成功");
                        delayToInvisible();
                    }
                });
    }



    private void downloadweixiu(final int Action) {
        ApiWrapper<ServerAPI> wrapper = new ApiWrapper<>();
        subscription = wrapper.targetClass(ServerAPI.class).getAPI().downLoadWeixiu(id, page, 10)
                .compose(wrapper.<Weixiu[]>applySchedulers())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Weixiu[]>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (ExceptionFilter.filter(context, e)) {
                            srl.setRefreshing(false);
                            tvRefresh.setText("刷新失败");
                            delayToInvisible();
                        }
                    }
                    @Override
                    public void onNext(Weixiu[] result) {
                        srl.setRefreshing(false);
                        L.e("main", "weixiul:" + result.length);
                        weixiuAdapter= (weixiuAdapter) adapter;
                        ArrayList<Weixiu> weixius = ConvertUtils.array2List(result);
                        L.e("main", "down size:" + weixius.size());
                        if (weixius.size() < 10) {
                            isMore = false;
                            L.e("main", "ismore为false");
                        }
                        if (Action == ACTION_ADD) {
                            weixiuAdapter.addwData(weixius);
                            wxList.addAll(weixius);
                        } else {
                            weixiuAdapter.initwData(weixius);
                            synchronized (wxList) {
                                if (wxList != null) {
                                    wxList.clear();
                                }
                                wxList.addAll(weixius);
                            }
                            isMore = true;
                        }
                        setListSort(isDesc);
                        tvRefresh.setText("刷新成功");
                        delayToInvisible();
                    }
                });
    }
    private void delayToInvisible() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    handler.sendEmptyMessage(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    @OnClick(R.id.goTop)
    public void onClick() {
        rv.scrollToPosition(0);
    }
}
