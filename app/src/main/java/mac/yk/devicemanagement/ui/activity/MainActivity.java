package mac.yk.devicemanagement.ui.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.xys.libzxing.zxing.activity.CaptureActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mac.yk.customdialog.CustomDialog;
import mac.yk.devicemanagement.I;
import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.application.MyMemory;
import mac.yk.devicemanagement.bean.User;
import mac.yk.devicemanagement.db.dbUser;
import mac.yk.devicemanagement.net.ApiWrapper;
import mac.yk.devicemanagement.net.ServerAPI;
import mac.yk.devicemanagement.service.check.MonitorService;
import mac.yk.devicemanagement.ui.fragment.CountFragment;
import mac.yk.devicemanagement.ui.fragment.EndLineFragment;
import mac.yk.devicemanagement.ui.fragment.LineDetailFragment;
import mac.yk.devicemanagement.ui.fragment.MainFragment;
import mac.yk.devicemanagement.ui.fragment.NoticeFragment;
import mac.yk.devicemanagement.ui.fragment.ScrapCountFragment;
import mac.yk.devicemanagement.ui.fragment.ScrapFragment;
import mac.yk.devicemanagement.util.ActivityUtils;
import mac.yk.devicemanagement.util.ExceptionFilter;
import mac.yk.devicemanagement.util.L;
import mac.yk.devicemanagement.util.MFGT;
import mac.yk.devicemanagement.util.SpUtil;
import mac.yk.devicemanagement.util.ToastUtil;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class MainActivity extends BaseActivity {
    String id;
    CustomDialog progressDialog;

    AlertDialog.Builder builder;
    AlertDialog Adialog;
    @BindView(R.id.toolBar)
    Toolbar toolBar;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawLayout)
    DrawerLayout drawLayout;

    DialogHolder dialogHolder;
    Context context;
    @BindView(R.id.netView)
    TextView mTv;

    CountFragment countFragment;
    ScrapCountFragment scrapCountFragment;
    NoticeFragment noticeFragment;
    EndLineFragment endLineFragment;
    LineDetailFragment lineDetailFragment;

    ArrayList<Fragment> fragments;
    int showId;
    String TAG="main";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency);

        ButterKnife.bind(this);
        checkUser();
        EventBus.getDefault().register(this);
        checkNet();
        init();
        initActionbar();
        setNavView();
        showWarning();
        isFromNotification();
        startCheck();
        initFragment();
    }

    private void initFragment() {
        MainFragment mainFragment =new MainFragment();
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mainFragment, R.id.frame);
        if (showId!=0){
            ActivityUtils.changeFragment(getSupportFragmentManager(),mainFragment,fragments.get(showId));
        }
    }

    private void showWarning() {
        if (!SpUtil.getPrompt(this)) {
            View view = View.inflate(this, R.layout.dialog_warning, null);
            getWarning(view);

        }
    }

    private void checkNet() {
        boolean netConnect = this.isNetConnect();
        if (netConnect) {
            mTv.setVisibility(View.GONE);
        } else {
            mTv.setVisibility(View.VISIBLE);
        }
    }

    private void checkUser() {
        if (SpUtil.getLoginUser(this).equals("")){
            MFGT.gotoLoginActivity(this);
        }
    }

    private void startCheck() {
        L.e(TAG,"startService");
            Intent intent=new Intent(this, MonitorService.class);
            startService(intent);

//            Intent intent2=new Intent(this, GuardService.class);
//            startService(intent2);



    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getTitle(String title){
        if (title.contains("all年")){
            title="总"+title.substring(4);
        }
            toolBar.setTitle(title);
    }

    @Subscribe(threadMode =ThreadMode.BACKGROUND)
    public void refresh(boolean b){
        L.e(TAG,"get refresh message");
        noticeFragment.refresh();
    }




    private void isFromNotification() {
        if(getIntent().getBooleanExtra("fromNtf",false)){
                    L.e("main","切换frag");
                    FragmentManager manager=getSupportFragmentManager();
                    if(fragments.contains(countFragment)){
                        ActivityUtils.changeFragment(manager, noticeFragment,fragments.get(showId));
                        for(int i=0;i<fragments.size();i++){
                            if(fragments.get(i)== noticeFragment){
                                showId=i;
                            }
                        }
                    }else {
                        noticeFragment =new NoticeFragment();
                        showId=fragments.size();
                        fragments.add(fragments.size(), noticeFragment);
                        ActivityUtils.addFragmentToActivity(manager, noticeFragment, R.id.frame);
                    }
                }

    }

    private void initActionbar() {
        setSupportActionBar(toolBar);
        ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);
    }

//    CountFragment CountFragment;
//    ScrapCountFragment ScrapCountFragment;
//    NoticeFragment NoticeFragment;
    private void init() {
        if (fragments==null){
            fragments=new ArrayList<>();
        }else {
            for(Fragment fragment:fragments){
                if(fragment instanceof CountFragment){
                    countFragment= (mac.yk.devicemanagement.ui.fragment.CountFragment) fragment;
                }else if(fragment instanceof ScrapFragment){
                    scrapCountFragment= (mac.yk.devicemanagement.ui.fragment.ScrapCountFragment) fragment;
                }else {
                    noticeFragment= (mac.yk.devicemanagement.ui.fragment.NoticeFragment) fragment;
                }
                ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),fragment, R.id.frame);
            }
        }

        context = this;
        User user = dbUser.getInstance(context).select2(SpUtil.getLoginUser(context));
        MyMemory.getInstance().setUser(user);
        builder = new AlertDialog.Builder(this);
        progressDialog = CustomDialog.create(context,"加载中...",false,null);
    }

    private void setNavView() {
        if (navView != null) {
            navView.inflateMenu(R.menu.menu_main);
            setUpNavView(navView);

            ImageView imageView = (ImageView) navView.getHeaderView(0).findViewById(R.id.avatar);
            TextView textView = (TextView) navView.getHeaderView(0).findViewById(R.id.nav_name);

            textView.setText(MyMemory.getInstance().getUser().getName().trim());
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MFGT.gotoSetActivity(MainActivity.this);
                }
            });
        }
    }


    /**
     * 设置actionBar的menu
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.action_capture:
                scan(I.CONTROL.START);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void scan(int id) {
        startActivityForResult(new Intent(context, CaptureActivity.class), id);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_name, menu);
        try {
            Class c = Class.forName("android.view.Menu");
            Field field = c.getField("size");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 设置navView的监听,
     * 实现fragments的懒加载
     *
     * @param navView
     */
    private void setUpNavView(NavigationView navView) {
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentManager manager=getSupportFragmentManager();
                switch (item.getItemId()) {
                    case R.id.warning:
                        View view = View.inflate(context, R.layout.dialog_warning, null);
                        getWarning(view);
                        L.e(TAG,"执行warning");

                        break;
                    case R.id.record:
                            if(fragments.contains(countFragment)){
                                L.e("cao","showid1:"+showId);
                                ActivityUtils.changeFragment(manager, countFragment,fragments.get(showId));
                                for(int i=0;i<fragments.size();i++){
                                    if(fragments.get(i)== countFragment){
                                       showId=i;
                                        L.e("cao","showid2:"+showId);
                                    }
                                }
                            }else {
                                countFragment =new CountFragment();
                                showId=fragments.size();
                                fragments.add(fragments.size(), countFragment);
                                ActivityUtils.addFragmentToActivity(manager, countFragment, R.id.frame);
                            }
                            toolBar.setTitle("设备统计");

                        break;
                    case R.id.scrap_record:
                        if(fragments.contains(scrapCountFragment)){
                            L.e("cao","showid1:"+showId);
                            ActivityUtils.changeFragment(manager, scrapCountFragment,fragments.get(showId));
                            for(int i=0;i<fragments.size();i++){
                                if(fragments.get(i)== scrapCountFragment){
                                    showId=i;
                                    L.e("cao","showid2:"+showId);
                                }
                            }
                        }else {
                            scrapCountFragment =new ScrapCountFragment();
                            showId=fragments.size();
                            fragments.add(fragments.size(), scrapCountFragment);
                            ActivityUtils.addFragmentToActivity(manager, scrapCountFragment, R.id.frame);
                        }
                        toolBar.setTitle("报废统计");
                        break;
                    case R.id.notice:
                        if(fragments.contains(noticeFragment)){
                            ActivityUtils.changeFragment(manager, noticeFragment,fragments.get(showId));
                            for(int i=0;i<fragments.size();i++){
                                if(fragments.get(i)== noticeFragment){
                                    showId=i;
                                }
                            }
                        }else {
                            noticeFragment =new NoticeFragment();
                            showId=fragments.size();
                            fragments.add(fragments.size(), noticeFragment);
                            ActivityUtils.addFragmentToActivity(manager, noticeFragment, R.id.frame);
                        }
                        toolBar.setTitle("公告");
                        break;
                    case R.id.end_line:
                        if(fragments.contains(endLineFragment)){
                            L.e("cao","showid1:"+showId);
                            ActivityUtils.changeFragment(manager, endLineFragment,fragments.get(showId));
                            for(int i=0;i<fragments.size();i++){
                                if(fragments.get(i)== endLineFragment){
                                    showId=i;
                                    L.e("cao","showid2:"+showId);
                                }
                            }
                        }else {
                            endLineFragment =new EndLineFragment();
                            showId=fragments.size();
                            fragments.add(fragments.size(), endLineFragment);
                            ActivityUtils.addFragmentToActivity(manager, endLineFragment, R.id.frame);
                        }
                        toolBar.setTitle("尽头线状态");
                        break;
                }
                item.setChecked(true);
                drawLayout.closeDrawers();
                return true;
            }
        });
    }

    public void gotoLineDetail(int number){
        FragmentManager manager=getSupportFragmentManager();
        if(fragments.contains(lineDetailFragment)){
            L.e("cao","showid1:"+showId);
            ActivityUtils.changeFragment(manager, lineDetailFragment,fragments.get(showId));
            for(int i=0;i<fragments.size();i++){
                if(fragments.get(i)== lineDetailFragment){
                    showId=i;
                    L.e("cao","showid2:"+showId);
                }
            }
        }else {
            lineDetailFragment=new LineDetailFragment();
            showId=fragments.size();

            fragments.add(fragments.size(), lineDetailFragment);
            Bundle bundle=new Bundle();
            bundle.putInt("number",number);
            lineDetailFragment.setArguments(bundle);
            ActivityUtils.addFragmentToActivity(manager, lineDetailFragment, R.id.frame);
        }

        toolBar.setTitle("终点线状态");
    }


//    private void changeFragment(BaseFragment fragment,T t){
//        FragmentManager manager=getSupportFragmentManager();
//        if(fragments.contains(fragment)){
//            ActivityUtils.changeFragment(manager, fragment,fragments.get(showId));
//            for(int i=0;i<fragments.size();i++){
//                if(fragments.get(i)== fragment){
//                    showId=i;
//                }
//            }
//        }else {
//            Fragment fragment1 =new c();
//            showId=fragments.size();
//            fragments.add(fragments.size(), ScrapCountFragment);
//            ActivityUtils.addFragmentToActivity(manager, ScrapCountFragment,R.id.frame);
//        }
//    }


    @Override
    protected void onActivityResult(final int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        L.e("main", "resultCode:" + resultCode);
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                id = (bundle.getString("result"));
                MFGT.gotoDetailActivity(context,false,false,id);
                finish();
            }

        }
    }

    Observer<String> observerWarning = new Observer<String>() {
        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
            progressDialog.dismiss();
            if (ExceptionFilter.filter(context, e)) {
                ToastUtil.showToast(context, "服务器没有响应");
            }
        }

        @Override
        public void onNext(String s) {
            progressDialog.dismiss();
            dialogHolder.warning.setText(s);
            Adialog.show();
        }
    };


    private void getWarning(View view) {
        dialogHolder = new DialogHolder(view);
        Adialog = builder.setTitle("预警信息")
                .setView(view)
                .setPositiveButton("已读以上信息", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        L.e(TAG,"执行warning");
        progressDialog.show();
        ApiWrapper<ServerAPI> network = new ApiWrapper<>();
        subscription = network.targetClass(ServerAPI.class).
                getAPI().getWarning()
                .timeout(10, TimeUnit.SECONDS)
                .compose(network.<String>applySchedulers())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observerWarning);
    }

    class DialogHolder {
        @BindView(R.id.warning)
        TextView warning;
        @BindView(R.id.no_prompt)
        CheckBox noPrompt;

        public DialogHolder(View view) {
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.no_prompt)
        public void onClick() {
            if (noPrompt.isChecked()) {
                SpUtil.savePrompt(context, true);
            }
        }

    }

    /**
     * 两次back键退出应用
     */

    private long FirstTime = 0;

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (fragments.get(showId).equals(lineDetailFragment)){
                    backToEndLine();
                    return true;
                }else{
                    long SecondTime = System.currentTimeMillis();
                    if (SecondTime - FirstTime > 2000) {
                        ToastUtil.showToast(context, "再按一次退出应用");
                        FirstTime = SecondTime;
                        return true;
                    } else {
                        System.exit(0);
                    }
                }
                break;
        }
        return super.onKeyUp(keyCode, event);
    }

    private void backToEndLine() {
        for(int i=0;i<fragments.size();i++) {
            if (fragments.get(i) == endLineFragment) {
                showId = i;
            }
        }
        ActivityUtils.changeFragment(getSupportFragmentManager(),endLineFragment,lineDetailFragment);
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (featureId == 108
//                Window.FEATURE_ACTION_BAR
                && menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }
//    public static boolean isServiceWorked(Context context, String serviceName) {
//        ActivityManager myManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//        ArrayList<ActivityManager.RunningServiceInfo> runningService = (ArrayList<ActivityManager.RunningServiceInfo>) myManager.getRunningServices(Integer.MAX_VALUE);
//        for (int i = 0; i < runningService.size(); i++) {
//            if (runningService.get(i).service.getClassName().toString().equals(serviceName)) {
//                return true;
//            }
//        }
//        return false;
//    }
//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putInt("showId",showId);
//        outState.putSerializable("fragments",fragments);
//    }
//
//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        showId=savedInstanceState.getInt("showId");
//       fragments= (ArrayList<Fragment>) savedInstanceState.getSerializable("fragments");
//
//    }
}


