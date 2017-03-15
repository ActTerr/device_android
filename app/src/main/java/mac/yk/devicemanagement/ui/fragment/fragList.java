package mac.yk.devicemanagement.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.bean.Weixiu;
import mac.yk.devicemanagement.bean.Xunjian;
import mac.yk.devicemanagement.adapter.weixiuAdapter;
import mac.yk.devicemanagement.adapter.xunjianAdapter;
import mac.yk.devicemanagement.model.IModel;
import mac.yk.devicemanagement.util.ConvertUtils;
import mac.yk.devicemanagement.util.L;
import mac.yk.devicemanagement.util.OkHttpUtils;
import mac.yk.devicemanagement.util.TestUtil;
import mac.yk.devicemanagement.widget.MyItemDecoration;

/**
 * Created by mac-yk on 2017/3/2.
 */

public class fragList extends Fragment {


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
    IModel model;
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
    ArrayList<Xunjian> xiujunList;
    boolean isWeixiu;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_currency, container, false);
        ButterKnife.bind(this, view);
        L.e("main","frag解析完布局");
        init();
        return view;
    }

    private void init() {
        context= getContext();
        model= TestUtil.getData();
        wxList=new ArrayList<>();
        xiujunList=new ArrayList<>();

        id=getArguments().getString("id");
        if (id==null){
            Activity a= (Activity) context;
            a.finish();
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
        rv.addItemDecoration(new MyItemDecoration(12));
        rv.setHasFixedSize(true);
        Download(ACTION_DOWNLOAD);
        setListener();
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
                        &&lastPosition==getList().size()&&isMore){
                    page++;
                    Download(ACTION_ADD);
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
            return xiujunList;
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
        model.downloadXunjian(context, id, page, new OkHttpUtils.OnCompleteListener<Xunjian[]>() {
            @Override
            public void onSuccess(Xunjian[] result) {
                srl.setRefreshing(false);
                L.e("main","xunjianl:"+result.length);
                xunjianAdapter adapter1= (xunjianAdapter) adapter;
                if (result!=null){
                    ArrayList<Xunjian> xunjianList= ConvertUtils.array2List(result);
                    if (Action==ACTION_ADD){

                        adapter1.addxData(xunjianList);
                    }else {
                        adapter1.initxData(xunjianList);
                    }
                    tvRefresh.setText("刷新成功");

                }else {
                    tvRefresh.setText("刷新失败");
                    isMore=false;
                }
                delayToInvisible();
            }

            @Override
            public void onError(String error) {
                srl.setRefreshing(false);
                tvRefresh.setText("刷新失败");
                delayToInvisible();
            }
        });
    }

    private void downloadweixiu(final int Action) {
        model.downloadWeixiu(context, id, page, new OkHttpUtils.OnCompleteListener<Weixiu[]>() {
            @Override
            public void onSuccess(Weixiu[] result) {
               weixiuAdapter adapter1= (weixiuAdapter) adapter;
                srl.setRefreshing(false);
                L.e("main","weixiul:"+result.length);
                if (result!=null&&result.length>0){
                  ArrayList<Weixiu> weixius= ConvertUtils.array2List(result);
                    if (Action==ACTION_ADD){
                        adapter1.addwData(weixius);
                    }else {
                        adapter1.initwData(weixius);
                    }
                    tvRefresh.setText("刷新成功");

                }else {
                    tvRefresh.setText("刷新失败");
                }
                delayToInvisible();
            }

            @Override
            public void onError(String error) {
                srl.setRefreshing(false);
                tvRefresh.setText("刷新失败");
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
