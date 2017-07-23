package mac.yk.devicemanagement.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    @BindView(R.id.add_from)
    Button addFrom;
    @BindView(R.id.list)
    ListView lv;
    Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frag_count, container, false);
        unbinder = ButterKnife.bind(this, view);
        context=getContext();
        initView();
        initForm();
        initData();
        return view;
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
                number, 0, 0, 10)
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
                        lines.addAll(endLines);
                        adapter.notifyDataSetChanged();
                    }
                });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
