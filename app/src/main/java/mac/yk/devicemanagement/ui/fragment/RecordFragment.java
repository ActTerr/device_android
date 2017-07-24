package mac.yk.devicemanagement.ui.fragment;

import android.app.Activity;
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
import mac.yk.customdialog.CustomDialog;
import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.adapter.CheckAdapter;
import mac.yk.devicemanagement.adapter.ServiceAdapter;
import mac.yk.devicemanagement.bean.Check;
import mac.yk.devicemanagement.bean.Service;
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

public class RecordFragment extends BaseFragment {
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
    final static String TAG="RecordFragment";
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
    ArrayList<Service> wxList=new ArrayList<>();
    ArrayList<Check> checkList =new ArrayList<>();
    boolean isService;
    CheckAdapter CheckAdapter;
    ServiceAdapter ServiceAdapter;
    LinearLayoutManager llm;
    CustomDialog pd;
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
        pd=CustomDialog.create(getContext(),"加载中...",false,null);
        id=getArguments().getString("id");
        if (id==null){
            Activity a= (Activity) context;
            MFGT.finish(a);
        }
        isService=getArguments().getBoolean("flag");

        L.e("TAG","是否维修:"+isService);


//        srl.setColorSchemeColors(
//                getResources().getColor(R.color.google_blue),
//                getResources().getColor(R.color.google_green),
//                getResources().getColor(R.color.google_red),
//                getResources().getColor(R.color.google_yellow)
//        );

        if (page==1){
            if (isService&&wxList.size()==0){
                Download(ACTION_DOWNLOAD);
                getActivity().setTitle("维修记录");
                adapter=new ServiceAdapter(context);
            }else if (!isService&& checkList.size()==0){
                Download(ACTION_DOWNLOAD);
                getActivity().setTitle("巡检记录");
                adapter=new CheckAdapter(context);
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

    class ServiceComparator implements Comparator<Service> {

        public ServiceComparator() {

        }
    
        @Override
        public int compare(Service o1, Service o2) {
            if (!isDesc){

                    return (int) (o2.getRepairDate().getTime()-o1.getRepairDate().getTime());
            }else {

                    return (int) (o1.getRepairDate().getTime()-o2.getRepairDate().getTime());
                }
            }
    }
    
    class checkComparator implements Comparator<Check>{

        public checkComparator() {

        }

        @Override
        public int compare(Check o1, Check o2) {
            if (!isDesc){
                return (int) (o2.getDate().getTime()-o1.getDate().getTime());
            }else {
                return (int) (o1.getDate().getTime()-o2.getDate().getTime());
            }
        }
    }
    private void setListSort() {
        L.e(TAG,"execute sort");
        if (isService){
            ArrayList<Service> list=new ArrayList<Service>();
            ArrayList<Service> list2=new ArrayList<>();
            for (Service w:wxList){
                if (w.getRepairDate()!=null){
                    list.add(w);
                }else {
                    L.e(TAG,"null:"+w.toString());
                    list2.add(w);
                }
            }
            ArrayList<Service> change=new ArrayList<>();
            Collections.sort(list,new ServiceComparator());
            Iterator<Service> it=list.iterator();
            while (it.hasNext()){
                Service w=it.next();
                change.add(w);
            }
            if (isDesc){
                ServiceAdapter.changeData(change);
                ServiceAdapter.addwData(list2);
                ServiceAdapter.refresh();
            }else {
                ServiceAdapter.changeData(list2);
                ServiceAdapter.addwData(change);
                ServiceAdapter.refresh();
            }
            L.e(TAG,"itemcount:"+ ServiceAdapter.getItemCount()+"listsize:"+list.size());
        }else {
            L.e(TAG,"checkSize:"+ checkList.size());
            Collections.sort(checkList,new checkComparator());
            ArrayList<Check> list=new ArrayList<>();
            Iterator<Check> it= checkList.iterator();
            while ((it.hasNext())){
                list.add(it.next());
            }
            CheckAdapter.changeData(list);
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
        if (isService){
            downloadService(actionAdd);
        }else {
            downloadcheck(actionAdd);
        }
    }

    private List getList() {
        if (isService){
            return wxList;
        }else {
            return checkList;
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

    private void downloadcheck(final int Action) {
        pd.show();
        ApiWrapper<ServerAPI> wrapper = new ApiWrapper<>();
        subscription = wrapper.targetClass(ServerAPI.class).getAPI()
                .downloadCheck(id, page, 10)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(wrapper.<Check[]>applySchedulers())
                .subscribe(new Subscriber<Check[]>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        pd.dismiss();
                        if (ExceptionFilter.filter(context, e)) {
//                            srl.setRefreshing(false);
                            CheckAdapter = (CheckAdapter) adapter;
                            tvRefresh.setText("刷新失败");
                            isMore = false;

                            delayToInvisible();
                        }
                    }

                    @Override
                    public void onNext(Check[] result) {
//                        srl.setRefreshing(false);
                        pd.dismiss();
                        L.e("adapter被赋值");
                        CheckAdapter = (CheckAdapter) adapter;
                        ArrayList<Check> checkLists = ConvertUtils.array2List(result);
                        L.e("TAG", "down size:" + checkLists.size());

                        if (Action == ACTION_ADD) {
                            checkList.addAll(checkLists);
                        } else {
                                if (checkList != null) {
                                    checkList.clear();
                                }
                                checkList.addAll(checkLists);
                            isMore = true;
                        }
                        if (checkLists.size() < 10) {
                            isMore = false;
                        }
                        tvRefresh.setText("加载成功");
                        setListSort();
                        delayToInvisible();
                    }
                });
    }



    private void downloadService(final int Action) {
        pd.show();
        ApiWrapper<ServerAPI> wrapper = new ApiWrapper<>();
        subscription = wrapper.targetClass(ServerAPI.class).getAPI().downLoadService(id, page, 10)
                .compose(wrapper.<Service[]>applySchedulers())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Service[]>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        pd.dismiss();
                        if (ExceptionFilter.filter(context, e)) {
//                            srl.setRefreshing(false);
                            isMore=false;
                            ServiceAdapter = (ServiceAdapter) adapter;
                                ServiceAdapter.setLast(true);
                            tvRefresh.setText("刷新失败");
                            delayToInvisible();
                        }
                    }
                    @Override
                    public void onNext(Service[] result) {
//                        srl.setRefreshing(false);
                        pd.dismiss();
                        ServiceAdapter = (ServiceAdapter) adapter;
                        ArrayList<Service> services = ConvertUtils.array2List(result);
                        L.e("TAG", "down size:" + services.size());
                        if (Action == ACTION_ADD) {
                            wxList.addAll(services);

                        } else {
                                if (wxList != null) {
                                    wxList.clear();
                                }
                                wxList.addAll(services);
                            isMore = true;
                        }

                        if (services.size() < 10) {
                            isMore = false;
                            ServiceAdapter.setLast(true);
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
