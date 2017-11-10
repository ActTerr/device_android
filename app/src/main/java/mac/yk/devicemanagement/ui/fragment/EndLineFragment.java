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
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.adapter.EndLineAdapter;
import mac.yk.devicemanagement.application.MyMemory;
import mac.yk.devicemanagement.bean.EndLine;
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
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_list, container, false);
        ButterKnife.bind(this, view);
        context=getContext();
//        initData(MyMemory.getInstance().getUser().getUnit());
        int unit=MyMemory.getInstance().getUnit();
        if(unit!=0){
            initData(unit);
        }

        setHasOptionsMenu(true);
        return view;
    }

    private void gotoDetail(int number) {
        MainActivity activity= (MainActivity) context;
        L.e(TAG,number+"");
        activity.gotoLineDetail(number);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_name, menu);
    }

    private void initData(int uId) {
        ApiWrapper<ServerAPI> wrapper=new ApiWrapper<>();
        wrapper.targetClass(ServerAPI.class).getAPI().getLineTotal(String.valueOf(uId))
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
                       if(ExceptionFilter.filter(context,e)){
                           ToastUtil.showException(context);
                        }
                    }

                    @Override
                    public void onNext(ArrayList<EndLine> endLines) {
                        if (endLines.size()>0){
                            endLineAdapter=new EndLineAdapter(context,endLines);
                            rv.setAdapter(endLineAdapter);
                            rv.setLayoutManager(new LinearLayoutManager(context));
                            setListener();
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
                gotoDetail(Integer.parseInt(holder.id.getText().toString()));
            }
        };
        rv.addOnItemTouchListener(new OnRecyclerItemClickListener(new GestureDetectorCompat(context,listener)) {
        });
    }


}
