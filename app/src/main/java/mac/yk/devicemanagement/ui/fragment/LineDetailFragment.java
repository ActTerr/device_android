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
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
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

import static mac.yk.devicemanagement.ui.fragment.ScrapFragment.TAG;

/**
 * Created by mac-yk on 2017/7/19.
 */

public class LineDetailFragment extends BaseFragment {
    Unbinder unbinder;
    int number;
    LineDetailAdapter adapter;
    ArrayList<EndLine> lines = new ArrayList<>();
    @BindView(R.id.add_from)
    Button addFrom;
    @BindView(R.id.list)
    ListView lv;
    Context context;
    boolean isAdding = false;
    boolean isMore=true;
    int range=0;
    int page;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frag_count, container, false);
        unbinder = ButterKnife.bind(this, view);
        context=getContext();
        initView();
        initForm();
        initData();
        setListener();
        return view;
    }

    private void setListener() {

        lv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                int lastPosition = lv.getLastVisiblePosition();
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastPosition == adapter.getCount() - 1 && isMore && !isAdding) {
                    initData();
                    L.e(TAG, "add");
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    private void initForm() {
        lines.add(null);
    }

    private void initView() {
        addFrom.setVisibility(View.GONE);
        adapter = new LineDetailAdapter(getContext(), lines);
        lv.setAdapter(adapter);
    }

    private void initData() {
        number=getArguments().getInt("number");
        ApiWrapper<ServerAPI> wrapper = new ApiWrapper<>();
        wrapper.targetClass(ServerAPI.class).getAPI().getLineDetail(MyMemory.getInstance().getUser().getUnit(),
                number, range, page, 10)
                .compose(wrapper.<ArrayList<EndLine>>applySchedulers())
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
                        if (endLines.size()<10){
                            isMore=false;
                        }
                        lines.addAll(endLines);
                        adapter.notifyDataSetChanged();
                        page++;
                    }
                });
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
        lines.add(null);
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
