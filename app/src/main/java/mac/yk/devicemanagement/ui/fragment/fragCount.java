package mac.yk.devicemanagement.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mac.yk.devicemanagement.MyMemory;
import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.adapter.VPAdapter;
import mac.yk.devicemanagement.bean.User;

/**
 * Created by mac-yk on 2017/5/8.
 */

public class fragCount extends BaseFragment{
    @BindView(R.id.vp)
    ViewPager vp;
    VPAdapter VPAdapter;

    User user;
    @BindView(R.id.select)
    LinearLayout selectL;

    fragTotalCount fragTotalCount;
    fragStatusCount fragStatusCount;
    fragServiceCount fragServiceCount;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_count_main, container, false);
        ButterKnife.bind(this, view);
        user = MyMemory.getInstance().getUser();
        setUpViewPager();
        return view;
    }

    private void setUpViewPager() {
        VPAdapter = new VPAdapter(getChildFragmentManager());
        if (user.getGrade() == 2) {
            VPAdapter.addFragment(new fragServiceCount(), "维修统计");
            selectL.setVisibility(View.GONE);
        } else {
            fragTotalCount=new fragTotalCount();
            fragStatusCount=new fragStatusCount();
            fragServiceCount=new fragServiceCount();
            VPAdapter.addFragment(fragTotalCount, "统计分析");
            VPAdapter.addFragment(fragStatusCount, "状态统计");
            VPAdapter.addFragment(fragServiceCount,"维修点统计");
        }
        vp.setAdapter(VPAdapter);
    }

    @OnClick({R.id.total_count, R.id.status_count,R.id.service_count})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.total_count:
                vp.setCurrentItem(0);

                break;
            case R.id.status_count:
                vp.setCurrentItem(1);
                break;
            case R.id.service_count:
                vp.setCurrentItem(2);
                break;
        }
    }



}
