package mac.yk.devicemanagement.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.adapter.FormAdapter;
import mac.yk.devicemanagement.util.ConvertUtils;

/**
 * Created by mac-yk on 2017/4/30.
 */

public class fragCount extends BaseFragment {
    @BindView(R.id.list)
    ListView lv;
    ArrayList<ArrayList<String>> data;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_count, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        data = new ArrayList<>();
        ArrayList<String> list = new ArrayList<>();
        String[] arra = new String[]{"顺号", "单位", "手持电台", "固定机控器", "移动机控器", "区控器"};
        list.addAll(ConvertUtils.array2List(arra));
        data.add(list);
        lv.setAdapter(new FormAdapter(getContext(), data));
    }

}
