package mac.yk.devicemanagement.controller.fragment;

import android.app.Activity;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.bean.Weixiu;
import mac.yk.devicemanagement.controller.adapter.MyAdapter;
import mac.yk.devicemanagement.model.IModel;
import mac.yk.devicemanagement.util.ConvertUtils;
import mac.yk.devicemanagement.util.OkHttpUtils;
import mac.yk.devicemanagement.widget.MyItemDecoration;

/**
 * Created by mac-yk on 2017/3/2.
 */

public class fragList extends Fragment {
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            tvRefresh.setVisibility(View.INVISIBLE);
        }
    };
    final static int ACTION_ADD=1;
    final static int ACTION_DOWNLOAD=2;
    IModel model;
    int id=0;
    Activity context;
    int page=1;
    MyAdapter adapter;
    LinearLayoutManager llm=new LinearLayoutManager(context);
    boolean isMore=true;
    @BindView(R.id.tvRefresh)
    TextView tvRefresh;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.goTop)
    Button goTop;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;
    ArrayList<Weixiu> wxList=new ArrayList<>();
    ArrayList<String> xiujunList=new ArrayList<>();
    boolean isWeixiu;

    public fragList() {
        context= (Activity) getContext();
        id=getArguments().getInt("id");
        if (id==0){
            context.finish();
        }
        isWeixiu=getArguments().getBoolean("flag");
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_currency, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        if (isWeixiu){
            adapter=new MyAdapter(context,wxList,null);

        }else {
            adapter=new MyAdapter(context,null,xiujunList);

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
                    Toast.makeText(context, "没有更多数据！", Toast.LENGTH_SHORT).show();
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
                tvRefresh.setVisibility(View.VISIBLE);
                tvRefresh.setText("刷新中");
                page=1;
               Download(ACTION_DOWNLOAD);
            }
        });
    }

    private void downloadxunjian(final int Action) {
        model.downloadXunjian(context, id, page, new OkHttpUtils.OnCompleteListener<String[]>() {
            @Override
            public void onSuccess(String[] result) {
                if (result!=null){
                    ArrayList<String> xunjianList= ConvertUtils.array2List(result);
                    if (Action==ACTION_ADD){
                        adapter.addxData(xunjianList);
                    }else {
                        adapter.initxData(xunjianList);
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
                tvRefresh.setText("刷新失败");
                delayToInvisible();
            }
        });
    }

    private void downloadweixiu(final int Action) {
        model.downloadWeixiu(context, id, page, new OkHttpUtils.OnCompleteListener<Weixiu[]>() {
            @Override
            public void onSuccess(Weixiu[] result) {
                if (result!=null){
                  ArrayList<Weixiu> weixius= ConvertUtils.array2List(result);
                    if (Action==ACTION_ADD){
                        adapter.addwData(weixius);
                    }else {
                        adapter.initwData(weixius);
                    }
                    tvRefresh.setText("刷新成功");

                }else {
                    tvRefresh.setText("刷新失败");
                }
                delayToInvisible();
            }

            @Override
            public void onError(String error) {
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
