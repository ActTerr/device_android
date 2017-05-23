package mac.yk.devicemanagement.ui.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
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
    boolean isDesc=true;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
           if (msg.what==1){
               Download(ACTION_DOWNLOAD);

           }else {

               tvRefresh.setVisibility(View.GONE);
           }
        }
    };
    final static String TAG="fragRecord";
    final static int ACTION_ADD=1;
    final static int ACTION_DOWNLOAD=2;
   String id;
   Context context;
    int page=1;
    RecyclerView.Adapter adapter;

    boolean isMore=true;
    @BindView(R.id.tvRefresh)
    TextView tvRefresh;
    @BindView(R.id.rv)
            public
    RecyclerView rv;
    @BindView(R.id.goTop)
    Button goTop;
//    @BindView(R.id.srl)
//    SwipeRefreshLayout srl;
    ArrayList<Weixiu> wxList=new ArrayList<>();
    ArrayList<Xunjian> xunjianList=new ArrayList<>();
    boolean isWeixiu;
    xunjianAdapter xunjianAdapter;
    weixiuAdapter weixiuAdapter;
    LinearLayoutManager llm;
    ProgressDialog pd;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_currency, container, false);
        ButterKnife.bind(this, view);
        context= getContext();
        llm=new LinearLayoutManager(context);
        L.e("TAG","frag解析完布局"+page);
        setHasOptionsMenu(true);
        init();
        return view;
    }

    private void init() {
        pd=new ProgressDialog(context);
        id=getArguments().getString("id");
        if (id==null){
            Activity a= (Activity) context;
            MFGT.finish(a);
        }
        isWeixiu=getArguments().getBoolean("flag");

        L.e("TAG","是否维修:"+isWeixiu);


//        srl.setColorSchemeColors(
//                getResources().getColor(R.color.google_blue),
//                getResources().getColor(R.color.google_green),
//                getResources().getColor(R.color.google_red),
//                getResources().getColor(R.color.google_yellow)
//        );

        if (page==1){
            if (isWeixiu&&wxList.size()==0){
                Download(ACTION_DOWNLOAD);
                getActivity().setTitle("维修记录");
                adapter=new weixiuAdapter(context);
            }else if (!isWeixiu&&xunjianList.size()==0){
                Download(ACTION_DOWNLOAD);
                getActivity().setTitle("巡检记录");
                adapter=new xunjianAdapter(context);
                L.e(TAG,"巡检adapter被创建");
            }
        }
        rv.setAdapter(adapter);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);
        setListener();
    }
    MenuItem itemRecent;
    MenuItem itemReverse;



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        L.e(TAG,"menu size:"+menu.size());
                inflater.inflate(R.menu.menu_record,menu);
                itemRecent = menu.getItem(0);
                itemReverse = menu.getItem(1);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.recent){
            L.e("recent");
            isDesc=true;
            item.setIcon(R.drawable.recent_selected);
            itemReverse.setIcon(R.drawable.reverse);
        }else {
            L.e("reverse");
            itemRecent.setIcon(R.drawable.recent);
            item.setIcon(R.drawable.reverse_selected);
            isDesc=false;
        }
        setListSort();
        return true;
    }

    class MyWxComparator implements Comparator<Weixiu> {

        public MyWxComparator() {

        }
    
        @Override
        public int compare(Weixiu o1, Weixiu o2) {
            if (!isDesc){

                    return (int) (o2.getXjDate().getTime()-o1.getXjDate().getTime());
            }else {

                    return (int) (o1.getXjDate().getTime()-o2.getXjDate().getTime());
                }
            }
    }
    
    class MyXunjianComParator implements Comparator<Xunjian>{

        public MyXunjianComParator() {

        }

        @Override
        public int compare(Xunjian o1, Xunjian o2) {
            if (!isDesc){
                return (int) (o2.getXjDate().getTime()-o1.getXjDate().getTime());
            }else {
                return (int) (o1.getXjDate().getTime()-o2.getXjDate().getTime());
            }
        }
    }
    private void setListSort() {
        L.e(TAG,"execute sort");
        if (isWeixiu){
            ArrayList<Weixiu> list=new ArrayList<Weixiu>();
            ArrayList<Weixiu> list2=new ArrayList<>();
            for (Weixiu w:wxList){
                if (w.getXjDate()!=null){
                    list.add(w);
                }else {
                    L.e(TAG,"null:"+w.toString());
                    list2.add(w);
                }
            }
            ArrayList<Weixiu> change=new ArrayList<>();
            Collections.sort(list,new MyWxComparator());
            Iterator<Weixiu> it=list.iterator();
            while (it.hasNext()){
                Weixiu w=it.next();
                change.add(w);
            }
            if (isDesc){
                weixiuAdapter.changeData(change);
                weixiuAdapter.addwData(list2);
                weixiuAdapter.refresh();
            }else {
                weixiuAdapter.changeData(list2);
                weixiuAdapter.addwData(change);
                weixiuAdapter.refresh();
            }
            L.e(TAG,"itemcount:"+weixiuAdapter.getItemCount()+"listsize:"+list.size());
        }else {
            L.e(TAG,"xunjianSize:"+xunjianList.size());
            Collections.sort(xunjianList,new MyXunjianComParator());
            ArrayList<Xunjian> list=new ArrayList<>();
            Iterator<Xunjian> it=xunjianList.iterator();
            while ((it.hasNext())){
                list.add(it.next());
            }
            xunjianAdapter.changeData(list);
        }

    }

    private void setListener() {
//        setPullDownListener();
        setPullUpListener();
    }

    private void setPullUpListener() {
        rv.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastPosition=llm.findLastVisibleItemPosition();
                if (newState==RecyclerView.SCROLL_STATE_IDLE
                        &&lastPosition==getList().size()-1&&isMore&&getList().size()>=10){
                    page++;
                    Download(ACTION_ADD);

                    L.e("TAG","执行ADD");
                }else {
                    L.e("TAG","没有更多数据");
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int firstPosition=llm.findFirstVisibleItemPosition();
//                srl.setEnabled(firstPosition==0);
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

//    private void setPullDownListener() {
//        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                int firstPosition=llm.findFirstVisibleItemPosition();
//                if (firstPosition!=0){
//                    return;
//                }
//                srl.setRefreshing(true);
//                tvRefresh.setVisibility(View.VISIBLE);
//                tvRefresh.setText("刷新中");
//                page=1;
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            Thread.sleep(2000);
//                            handler.sendEmptyMessage(1);
//
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }).start();
//
//            }
//        });
//    }

    private void downloadxunjian(final int Action) {
        pd.show();
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
                        pd.dismiss();
                        if (ExceptionFilter.filter(context, e)) {
//                            srl.setRefreshing(false);
                            xunjianAdapter = (xunjianAdapter) adapter;
                            tvRefresh.setText("刷新失败");
                            isMore = false;

                            delayToInvisible();
                        }
                    }

                    @Override
                    public void onNext(Xunjian[] result) {
//                        srl.setRefreshing(false);
                        pd.dismiss();
                        L.e("adapter被赋值");
                        xunjianAdapter = (xunjianAdapter) adapter;
                        ArrayList<Xunjian> xunjianLists = ConvertUtils.array2List(result);
                        L.e("TAG", "down size:" + xunjianLists.size());

                        if (Action == ACTION_ADD) {
                            xunjianList.addAll(xunjianLists);
                        } else {
                                if (xunjianList != null) {
                                    xunjianList.clear();
                                }
                                xunjianList.addAll(xunjianLists);
                            isMore = true;
                        }
                        if (xunjianLists.size() < 10) {
                            isMore = false;
                        }
                        tvRefresh.setText("加载成功");
                        setListSort();
                        delayToInvisible();
                    }
                });
    }



    private void downloadweixiu(final int Action) {
        pd.show();
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
                        pd.dismiss();
                        if (ExceptionFilter.filter(context, e)) {
//                            srl.setRefreshing(false);
                            isMore=false;
                            weixiuAdapter= (weixiuAdapter) adapter;
                                weixiuAdapter.setLast(true);
                            tvRefresh.setText("刷新失败");
                            delayToInvisible();
                        }
                    }
                    @Override
                    public void onNext(Weixiu[] result) {
//                        srl.setRefreshing(false);
                        pd.dismiss();
                        weixiuAdapter= (weixiuAdapter) adapter;
                        ArrayList<Weixiu> weixius = ConvertUtils.array2List(result);
                        L.e("TAG", "down size:" + weixius.size());
                        if (Action == ACTION_ADD) {
                            wxList.addAll(weixius);

                        } else {
                                if (wxList != null) {
                                    wxList.clear();
                                }
                                wxList.addAll(weixius);
                            isMore = true;
                        }

                        if (weixius.size() < 10) {
                            isMore = false;
                            weixiuAdapter.setLast(true);
                        }
                        setListSort();
                        tvRefresh.setText("加载成功");
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

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @OnClick(R.id.goTop)
    public void onClick() {
        rv.scrollToPosition(0);
    }

}
