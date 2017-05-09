package mac.yk.devicemanagement.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.adapter.VPAdapter;
import mac.yk.devicemanagement.ui.fragment.fragAttachment;
import mac.yk.devicemanagement.ui.fragment.fragNotice;

/**
 * Created by mac-yk on 2017/5/9.
 */

public class NoticeDetailActivity extends BaseActivity {
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    VPAdapter vpAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        ButterKnife.bind(this);
        vpAdapter=new VPAdapter(getSupportFragmentManager());
        vpAdapter.addFragment(new fragNotice(),"公告");
        vpAdapter.addFragment(new fragAttachment(),"附件");
        viewPager.setAdapter(vpAdapter);


    }

    @OnClick({R.id.item_notice, R.id.item_attachment})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.item_notice:
                viewPager.setCurrentItem(0);
                break;
            case R.id.item_attachment:
                viewPager.setCurrentItem(1);
                break;
        }
    }
}
