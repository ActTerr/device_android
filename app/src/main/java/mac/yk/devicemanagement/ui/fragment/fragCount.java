package mac.yk.devicemanagement.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mac.yk.devicemanagement.R;

/**
 * Created by mac-yk on 2017/5/8.
 */

public class fragCount extends BaseFragment {
    @BindView(R.id.vp)
    ViewPager vp;
    Adapter adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_count_main, container, false);
        ButterKnife.bind(this, view);
        setUpViewPager();
        return view;
    }

    private void setUpViewPager() {
       adapter = new Adapter(getChildFragmentManager());
        adapter.addFragment(new fragTotalCount(), "统计分析");
        adapter.addFragment(new fragStatusCount(), "状态统计");
        vp.setAdapter(adapter);
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

    class Adapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }
}
