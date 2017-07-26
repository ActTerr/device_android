package mac.yk.devicemanagement.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import mac.yk.customdialog.CustomDialog;
import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.adapter.LineDetailAdapter;
import mac.yk.devicemanagement.application.MyMemory;
import mac.yk.devicemanagement.bean.EndLine;
import mac.yk.devicemanagement.net.ApiWrapper;
import mac.yk.devicemanagement.net.ServerAPI;
import mac.yk.devicemanagement.util.ExceptionFilter;
import mac.yk.devicemanagement.util.L;
import mac.yk.devicemanagement.util.ToastUtil;
import rx.Subscriber;

/**
 * Created by mac-yk on 2017/7/19.
 */

public class LineDetailFragment extends BaseFragment {
    Unbinder unbinder;
    int number;
    LineDetailAdapter adapter;
    ArrayList<EndLine> lines = new ArrayList<>();

    @BindView(R.id.list)
    ListView lv;
    Context context;
    boolean isAdding = false;
    boolean isMore=true;
    int range=0;
    int page;
    CustomDialog dialog;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frag_line_detail, container, false);
        unbinder = ButterKnife.bind(this, view);
        context=getContext();
        dialog=CustomDialog.create(context,"加载中...",false,null);
        setHasOptionsMenu(true);
        initView();
        initData();
        setListener();
        return view;
    }

    private void setListener() {

        lv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                int lastPosition = lv.getLastVisiblePosition();
                L.e("cao",lastPosition+"："+scrollState+":"+isMore+":"+!isAdding);
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastPosition == adapter.getCount()-1  && isMore && !isAdding) {
                    L.e("cao", "add");
                    initData();

                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }



    private void initView() {
        adapter = new LineDetailAdapter(getContext(), lines);
        lv.setAdapter(adapter);
    }

    private void initData() {
        dialog.show();
        isAdding=true;
        number=getArguments().getInt("number");
        ApiWrapper<ServerAPI> wrapper = new ApiWrapper<>();
        wrapper.targetClass(ServerAPI.class).getAPI().getLineDetail(MyMemory.getInstance().getUser().getUnit(),
                number, range, page, 18)
                .compose(wrapper.<ArrayList<EndLine>>applySchedulers())
                .subscribe(new Subscriber<ArrayList<EndLine>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        dialog.dismiss();
                        isAdding=false;
                        if(ExceptionFilter.filter(context,e)){
                            ToastUtil.showException(context);
                        }
                    }

                    @Override
                    public void onNext(ArrayList<EndLine> endLines) {
                        dialog.dismiss();
                        isAdding=false;
                        if (endLines.size()<18){
                            isMore=false;
                        }
                        lines.addAll(endLines);
                        L.e("cao",endLines.size()+"size1");
                        sort(lines);
                        L.e("cao",lines.size()+"size2");
                        adapter.notifyDataSetChanged();
                        page++;
                    }
                });
    }

    private void sort(ArrayList<EndLine> endLines){
        Collections.sort(endLines,new MyComparator());
    }

    class MyComparator implements Comparator<EndLine> {

        public MyComparator() {

        }


        @Override
        public int compare(EndLine o1, EndLine o2) {
            return (int) (o2.getTime()-o1.getTime());
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_end_line,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.one_day:
                range=1;
                break;
            case R.id.twice_day:
                range=2;
                break;
            case R.id.three_day:
                range=3;
                break;
        }
        resetData();
        return true;
    }

    private void resetData() {
        lines.clear();
        adapter.notifyDataSetChanged();
        isMore=true;
        page=0;
        initData();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
