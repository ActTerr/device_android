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

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import mac.yk.customdialog.CustomDialog;
import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.adapter.Line2DetailAdapter;
import mac.yk.devicemanagement.adapter.LineDetailAdapter;
import mac.yk.devicemanagement.application.MyMemory;
import mac.yk.devicemanagement.bean.EndLine;
import mac.yk.devicemanagement.bean.EndLine2;
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
    Line2DetailAdapter adapter2;
    ArrayList<EndLine> lines = new ArrayList<>();
    ArrayList<EndLine2> line2s = new ArrayList<>();

    @BindView(R.id.list)
    ListView lv;
    Context context;
    boolean isAdding = false;
    boolean isMore = true;
    int range = 10;
    int page;
    CustomDialog dialog;
    String TAG = getClass().getName();
    int type;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initView();
        View view = null;
        if (type == 1) {
            view = inflater.inflate(R.layout.frag_line_detail, container, false);
        } else if (type == 2) {
            view = inflater.inflate(R.layout.frag_line_detail2, container, false);
        }
        unbinder = ButterKnife.bind(this, view);
        if (type == 1) {
            adapter = new LineDetailAdapter(getContext(), lines);
            lv.setAdapter(adapter);
        } else {
            adapter2 = new Line2DetailAdapter(getContext(), line2s);
            lv.setAdapter(adapter2);
        }

        context = getContext();
        dialog = CustomDialog.create(context, "加载中...", false, null);
        L.e(TAG, "onCreate");

        initDataForType();
        setListener();
        return view;
    }

    private void initDataForType() {
        if (type == 1) {
            initData();
        } else if (type == 2) {
            initData2();
        }
    }

    private void initData2() {
        dialog.show();
        isAdding = true;

        ApiWrapper<ServerAPI> wrapper = new ApiWrapper<>();
        wrapper.targetClass(ServerAPI.class).getAPI().getLine2Detail(MyMemory.getInstance().getUnit(),
                number, range, page, 18, type)
                .compose(wrapper.<ArrayList<EndLine2>>applySchedulers())
                .timeout(10, TimeUnit.SECONDS)
                .subscribe(new Subscriber<ArrayList<EndLine2>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        dialog.dismiss();
                        isAdding = false;
                        if (ExceptionFilter.filter(context, e)) {
                            ToastUtil.showException(context);
                        }
                    }

                    @Override
                    public void onNext(ArrayList<EndLine2> endLines) {
                        dialog.dismiss();
                        isAdding = false;
                        if (endLines.size() < 18) {
                            isMore = false;
                        }
                        line2s.addAll(endLines);
                        adapter2.notifyDataSetChanged();
                        page++;
                    }
                });
    }

    private void setListener() {

        lv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                int lastPosition = lv.getLastVisiblePosition();
                L.e(TAG, lastPosition + "：" + scrollState + ":" + isMore + ":" + !isAdding);
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && isMore && !isAdding) {
                    if (adapter != null && lastPosition == adapter.getCount() - 1) {
                        L.e(TAG, "add");
                        initData();
                    } else if (adapter2 != null & lastPosition == adapter2.getCount() - 1) {
                        initData2();
                    }


                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }


    private void initView() {
        setHasOptionsMenu(true);
        number = getArguments().getInt("number");
        type = getArguments().getInt("type");
        EventBus.getDefault().post(number + "号尽头线数据");



    }

    private void initData() {
        dialog.show();
        isAdding = true;

        ApiWrapper<ServerAPI> wrapper = new ApiWrapper<>();
        wrapper.targetClass(ServerAPI.class).getAPI().getLineDetail(MyMemory.getInstance().getUnit(),
                number, range, page, 18, type)
                .compose(wrapper.<ArrayList<EndLine>>applySchedulers())
                .timeout(10, TimeUnit.SECONDS)
                .subscribe(new Subscriber<ArrayList<EndLine>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        dialog.dismiss();
                        isAdding = false;
                        if (ExceptionFilter.filter(context, e)) {
                            ToastUtil.showException(context);
                        }
                    }

                    @Override
                    public void onNext(ArrayList<EndLine> endLines) {
                        dialog.dismiss();
                        isAdding = false;
                        if (endLines.size() < 18) {
                            isMore = false;
                        }
                        lines.addAll(endLines);
                        adapter.notifyDataSetChanged();
                        page++;
                    }
                });
    }

//    private void sort(ArrayList<EndLine> endLines){
//        Collections.sort(endLines,new MyComparator());
//    }

//    class MyComparator implements Comparator<EndLine> {
//
//        public MyComparator() {
//
//        }
//
//
//        @Override
//        public int compare(EndLine o1, EndLine o2) {
//            return (int) (o2.getTime()-o1.getTime());
//        }
//    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_end_line, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.one_day:
                range = 1;
                break;
            case R.id.three_days:
                range = 3;
                break;
            case R.id.seven_days:
                range = 7;
                break;
            case R.id.ten_days:
                range = 10;
                break;
        }
        if (item.getItemId() != android.R.id.home && item.getItemId() != R.id.action_capture) {
            resetData();
        }

        return true;
    }

    private void resetData() {
        lines.clear();
        line2s.clear();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
        if (adapter2 != null) {
            adapter.notifyDataSetChanged();
        }
        isMore = true;
        page = 0;
        initDataForType();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        adapter = null;
        unbinder.unbind();
    }
}
