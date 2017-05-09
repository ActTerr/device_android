package mac.yk.devicemanagement.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.adapter.VPAdapter;

/**
 * Created by mac-yk on 2017/5/8.
 */

public class fragCount extends BaseFragment {
    @BindView(R.id.vp)
    ViewPager vp;
    VPAdapter VPAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_count_main, container, false);
        ButterKnife.bind(this, view);
        setUpViewPager();
        return view;
    }

    private void setUpViewPager() {
       VPAdapter = new VPAdapter(getChildFragmentManager());
        VPAdapter.addFragment(new fragTotalCount(), "统计分析");
        VPAdapter.addFragment(new fragStatusCount(), "状态统计");
        vp.setAdapter(VPAdapter);
    }

    @OnClick({R.id.total_count, R.id.status_count})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.total_count:
                vp.setCurrentItem(0);
                break;
            case R.id.status_count:
                vp.setCurrentItem(1);
                break;
        }
    }


}
