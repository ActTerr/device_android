package mac.yk.devicemanagement.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ipaulpro.afilechooser.utils.FileUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.adapter.VPAdapter;
import mac.yk.devicemanagement.bean.Notice;
import mac.yk.devicemanagement.ui.fragment.fragAttachment;
import mac.yk.devicemanagement.ui.fragment.fragNoticeDetail;
import mac.yk.devicemanagement.util.L;
import mac.yk.devicemanagement.util.OpenFileUtil;

/**
 * Created by mac-yk on 2017/5/9.
 */

public class NoticeDetailActivity extends BaseActivity {

    private static final int REQUEST_CHOOSER = 1234;

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    VPAdapter vpAdapter;
    @BindView(R.id.checkll)
    LinearLayout checkll;
    public Handler handler;
    public static int open = 1;
    public static int close = 0;
    @BindView(R.id.item_attachment)
    TextView itemAttachment;
    String TAG="NoticeDetailActivity";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        ButterKnife.bind(this);
        init();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == open) {
                    checkll.setVisibility(View.VISIBLE);
                } else {
                    checkll.setVisibility(View.GONE);
                }
            }
        };


    }

    private void init() {
        vpAdapter = new VPAdapter(getSupportFragmentManager());
        Notice notice = (Notice) getIntent().getSerializableExtra("notice");
        boolean isEdit = getIntent().getBooleanExtra("isEdit", false);

        fragNoticeDetail fragNoticeDetail = new fragNoticeDetail();
        Bundle bundle = new Bundle();
        bundle.putSerializable("notice", notice);
        bundle.putBoolean("isEdit", isEdit);
        fragNoticeDetail.setArguments(bundle);
        vpAdapter.addFragment(fragNoticeDetail, "公告");

        Bundle bundle2 = new Bundle();
        if (notice != null) {
            bundle2.putLong("Nid", notice.getNid());
            fragAttachment fragAttachment = new fragAttachment();
            fragAttachment.setArguments(bundle2);
            vpAdapter.addFragment(fragAttachment, "附件");
        }else {
            itemAttachment.setVisibility(View.GONE);
        }

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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        L.e(TAG,"activityresult");
        switch (requestCode) {
            case REQUEST_CHOOSER:
                if (resultCode == RESULT_OK) {

                    final Uri uri = data.getData();

                    // Get the File path from the Uri
                    String path = FileUtils.getPath(this, uri);

                    // Alternatively, use FileUtils.getFile(Context, Uri)
                    if (path != null && FileUtils.isLocal(path)) {
                        File file = new File(path);
                        File newFile=new File(OpenFileUtil.getPath(file.getName()));
                        if(file.renameTo(newFile)){
                            L.e(TAG,file.getAbsolutePath());
                        }
                        EventBus.getDefault().post(newFile);
                    }
                }
                break;
        }
    }
}
