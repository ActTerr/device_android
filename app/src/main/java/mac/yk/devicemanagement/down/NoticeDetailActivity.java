package mac.yk.devicemanagement.down;

import android.content.Context;
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

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.adapter.ViewPagerAdapter;
import mac.yk.devicemanagement.bean.Notice;
import mac.yk.devicemanagement.db.dbFile;
import mac.yk.devicemanagement.net.ApiWrapper;
import mac.yk.devicemanagement.net.ServerAPI;
import mac.yk.devicemanagement.net.downServer;
import mac.yk.devicemanagement.service.down.FileService;
import mac.yk.devicemanagement.ui.activity.BaseActivity;
import mac.yk.devicemanagement.ui.fragment.NoticeDetailFragment;
import mac.yk.devicemanagement.util.ExceptionFilter;
import mac.yk.devicemanagement.util.L;
import mac.yk.devicemanagement.util.ToastUtil;
import mac.yk.devicemanagement.util.schedulers.SchedulerProvider;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by mac-yk on 2017/5/9.
 */

public class NoticeDetailActivity extends BaseActivity {

    private static final int REQUEST_CHOOSER = 1234;

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    ViewPagerAdapter viewPagerAdapter;
    @BindView(R.id.checkll)
    LinearLayout checkLl;
    public Handler handler;
    public static int open = 1;
    public static int close = 0;
    @BindView(R.id.item_attachment)
    TextView itemAttachment;
    String TAG="NoticeDetailActivity";
    AttachmentFragment attachmentFragment;
    downPresenter presenter;
    long nid;
    Context context;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        ButterKnife.bind(this);
        context=this;
        init();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == open) {
                    checkLl.setVisibility(View.VISIBLE);
                } else {
                    checkLl.setVisibility(View.GONE);
                }
            }
        };

    }
    private void init() {
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        boolean isFromNtf=getIntent().getBooleanExtra("fromNtf",false);

        if (isFromNtf){
            final String title=getIntent().getStringExtra("title");
            getNotice(title);

        }else {
            notice = (Notice) getIntent().getSerializableExtra("notice");
            initNoticeFragment();
            initAttachmentFragment();
        }

        viewPager.setAdapter(viewPagerAdapter);

    }

    private void initAttachmentFragment(){
        if (notice != null) {
            attachmentFragment = new AttachmentFragment();
            nid=notice.getNid();
            viewPagerAdapter.addFragment(attachmentFragment, "附件");
            FileService fileService=new FileService();
            presenter=new downPresenter(attachmentFragment,new downServer(), dbFile.getInstance(this)
                    ,SchedulerProvider.getInstance(),nid,fileService);
            fileService.onStartCommand(null,0,0);

        }else {
            itemAttachment.setVisibility(View.GONE);
        }

    }

    private void initNoticeFragment(){
        boolean isEdit = getIntent().getBooleanExtra("isEdit", false);
        NoticeDetailFragment NoticeDetailFragment = new NoticeDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("notice", notice);
        bundle.putBoolean("isEdit", isEdit);
        NoticeDetailFragment.setArguments(bundle);
        viewPagerAdapter.addFragment(NoticeDetailFragment, "公告");
    }

    Notice notice;
    private void getNotice(final String title) {
            L.e(TAG,"req title:"+title);
                ApiWrapper<ServerAPI> wrapper=new ApiWrapper<>();
                wrapper.targetClass(ServerAPI.class).getAPI()
                        .getNotice(title)
                        .compose(wrapper.<Notice>applySchedulers())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<Notice>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                if (ExceptionFilter.filter(context,e)){
                                    ToastUtil.showToast(context,"服了，这是为啥？？？");
                                }
                            }

                            @Override
                            public void onNext(Notice not) {
                                notice=not;
                                L.e(TAG,notice.toString()+"成功");
                                initNoticeFragment();
                                initAttachmentFragment();
                                viewPagerAdapter.notifyDataSetChanged();
                                viewPager.setCurrentItem(1);
                            }
                        });


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
                if (resultCode == RESULT_OK) {
                    final Uri uri = data.getData();
                    // Get the File path from the Uri
                    String path = FileUtils.getPath(this, uri);
                    // Alternatively, use FileUtils.getFile(Context, Uri)
                    if (path != null && FileUtils.isLocal(path)) {
                        File file = new File(path);
                        if(requestCode!=REQUEST_CHOOSER){
                            presenter.deleteAttachment(null,true,file);
                        }else {
                            presenter.filterFile(file);
                        }
                    }
                }

        }




}
