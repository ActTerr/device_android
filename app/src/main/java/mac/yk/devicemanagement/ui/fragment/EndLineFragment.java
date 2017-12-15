package mac.yk.devicemanagement.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.adapter.EndLineAdapter;
import mac.yk.devicemanagement.application.MyMemory;
import mac.yk.devicemanagement.bean.EndLine;
import mac.yk.devicemanagement.bean.EndLine2;
import mac.yk.devicemanagement.listener.ItemTouchHelperGestureListener;
import mac.yk.devicemanagement.listener.OnRecyclerItemClickListener;
import mac.yk.devicemanagement.net.ApiWrapper;
import mac.yk.devicemanagement.net.ServerAPI;
import mac.yk.devicemanagement.ui.activity.MainActivity;
import mac.yk.devicemanagement.util.ExceptionFilter;
import mac.yk.devicemanagement.util.L;
import mac.yk.devicemanagement.util.ToastUtil;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by mac-yk on 2017/7/18.
 */

public class EndLineFragment extends BaseFragment {
    @BindView(R.id.rv)
    RecyclerView rv;
    EndLineAdapter endLineAdapter;
    Context context;
    String TAG=getClass().getName();
    String type;
    HashMap<Integer,Integer> index;
    ArrayList<EndLine> lines;
    ArrayList<EndLine2> line2s;
    String unit;
    StringTokenizer st;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_list, container, false);
        ButterKnife.bind(this, view);
        context=getContext();
        lines=new ArrayList<>();
        line2s=new ArrayList<>();
        type=MyMemory.getInstance().getUser().getType();
        st=new StringTokenizer(type,",");
        L.e("type",type);
        unit=MyMemory.getInstance().getUnit();
        endLineAdapter=new EndLineAdapter(context,lines,line2s);
        rv.setAdapter(endLineAdapter);
        rv.setLayoutManager(new LinearLayoutManager(context));
        initForIndex();
        setHasOptionsMenu(true);
        return view;
    }

    int count2=0;
    private void initForIndex() {
        if(st.hasMoreTokens()){
            L.e("initforindex",(count2+=1)+"");
            String t=st.nextToken();
            switch (t){
                case "1":
                    initEndLine();
                    break;
                case "2":
                    initEndLine2();
                    break;
            }
        }else{
            setListener();
            endLineAdapter.notifyDataSetChanged();
            L.e("change","dataNotify");
        }


    }



    private void gotoDetail(int number,int type) {
        MainActivity activity= (MainActivity) context;
        L.e(TAG,number+"");
        activity.gotoLineDetail(number,type);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        L.e(TAG,"clear");
        String unit=MyMemory.getInstance().getUser().getUnit();
        L.e(TAG,"unit:"+unit);
        if(unit.equals("")){
            inflater.inflate(R.menu.menu_main,menu);
        }

    }



    int count=0;

    private void initEndLine() {
        ApiWrapper<ServerAPI> wrapper=new ApiWrapper<>();
        wrapper.targetClass(ServerAPI.class);
        wrapper.getAPI().getLineTotal(unit,1)
                .compose(wrapper.<ArrayList<EndLine>>applySchedulers())
                .subscribeOn(Schedulers.io())
                .timeout(10, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ArrayList<EndLine>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (ExceptionFilter.filter(context, e)) {
                            ToastUtil.showException(context);
                        }
                    }

                    @Override
                    public void onNext(ArrayList<EndLine> endLines) {
                        if (endLines.size() > 0) {
                            lines.clear();
                            lines.addAll(endLines);
//                            index.put(1,endLines.size());
                            count+=1;
                            L.e("size", lines.size() + "");
                            initForIndex();
                        }
                    }
                });
    }

    private void initEndLine2() {
        ApiWrapper<ServerAPI> wrapper=new ApiWrapper<>();
        wrapper.targetClass(ServerAPI.class);
        wrapper.getAPI().getLine2Total(unit, 2)
                .compose(wrapper.<ArrayList<EndLine2>>applySchedulers())
                .subscribeOn(Schedulers.io())
                .timeout(10, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ArrayList<EndLine2>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (ExceptionFilter.filter(context, e)) {
                            ToastUtil.showException(context);
                        }
                    }

                    @Override
                    public void onNext(ArrayList<EndLine2> endLines) {
                        L.e("size2", endLines.size() + "");
                        if (endLines.size() > 0) {
                            L.e("size2",  "douwo");
                            line2s.clear();
                            line2s.addAll(endLines);
//                            index.put(2,endLines.size());
                            count+=1;
                            L.e("size2", line2s.size() + "");
                            initForIndex();
                        }
                    }
                });
    }

    private void setListener() {
        ItemTouchHelperGestureListener listener=new ItemTouchHelperGestureListener(
                rv, EndLineAdapter.LineHolder.class) {

            @Override
            public void onItemClick(Object o) {
                EndLineAdapter.LineHolder holder= (EndLineAdapter.LineHolder) o;
                L.e(TAG,holder.id.getText().toString());
                gotoDetail(Integer.parseInt(holder.id.getText().toString()), (Integer) holder.itemView.getTag());
            }
        };
        rv.addOnItemTouchListener(new OnRecyclerItemClickListener(new GestureDetectorCompat(context,listener)) {
        });
    }


    public void refreshData() {
        initForIndex();
    }
}
