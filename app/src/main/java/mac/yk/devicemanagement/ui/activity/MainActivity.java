package mac.yk.devicemanagement.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
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

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mac.yk.devicemanagement.I;
import mac.yk.devicemanagement.MyApplication;
import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.bean.DeviceOld;
import mac.yk.devicemanagement.net.ApiWrapper;
import mac.yk.devicemanagement.net.ServerAPI;
import mac.yk.devicemanagement.ui.fragment.fragCount;
import mac.yk.devicemanagement.ui.fragment.fragDevice;
import mac.yk.devicemanagement.ui.fragment.fragMain;
import mac.yk.devicemanagement.ui.fragment.fragScrap;
import mac.yk.devicemanagement.util.ActivityUtils;
import mac.yk.devicemanagement.util.ExceptionFilter;
import mac.yk.devicemanagement.util.L;
import mac.yk.devicemanagement.util.MFGT;
import mac.yk.devicemanagement.util.SpUtil;
import mac.yk.devicemanagement.util.ToastUtil;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static mac.yk.devicemanagement.R.id.yujing;

public class MainActivity extends BaseActivity {
    String id;
    ProgressDialog progressDialog;

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

    fragDevice fragDevice;
    fragScrap fragScrap;
    fragCount fragCount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency);
        View view = View.inflate(this, R.layout.dialog_yujing, null);
        ButterKnife.bind(this);
        boolean netConnect = this.isNetConnect();
        if (netConnect) {
            mTv.setVisibility(View.GONE);
        } else {
            mTv.setVisibility(View.VISIBLE);
        }
        init();
        initActionbar();
        dialogHolder = new DialogHolder(view);
        Adialog = builder.setTitle("预警信息")
                .setView(view)
                .setPositiveButton("已读以上信息", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), new fragMain(), R.id.frame);
        setNavView();
        if (!SpUtil.getPrompt(this)) {
            getYujing();
        }
    }

    private void initActionbar() {
        setSupportActionBar(toolBar);
        ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);
    }

    private void init() {
        fragDevice = new fragDevice();
        fragScrap=new fragScrap();
        fragCount=new fragCount();
        context = this;
        builder = new AlertDialog.Builder(this);
        progressDialog = new ProgressDialog(this);
    }

    private void setNavView() {
        if (navView != null) {
            navView.inflateMenu(R.menu.menu_main);
            setUpNavView(navView);

            ImageView imageView = (ImageView) navView.getHeaderView(0).findViewById(R.id.avatar);
            TextView textView = (TextView) navView.getHeaderView(0).findViewById(R.id.nav_name);
            textView.setText(MyApplication.getInstance().getUser().getName());
            L.e("main", "name:" + MyApplication.getInstance().getUser().getName());
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
        return true;
    }

    /**
     * 设置navView的监听
     *
     * @param navView
     */
    private void setUpNavView(NavigationView navView) {
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case yujing:
                        getYujing();
                        break;
                    case R.id.tongji:

                        ActivityUtils.changeFragment(getSupportFragmentManager(), fragCount, R.id.frame);
                        break;
                    case R.id.bf_tongji:
                        ActivityUtils.changeFragment(getSupportFragmentManager(), fragScrap, R.id.frame);
                        break;
                }
                item.setChecked(true);
                drawLayout.closeDrawers();
                return true;
            }
        });
    }

    Observer<DeviceOld> obChaxun = new Observer<DeviceOld>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            progressDialog.dismiss();
            if (ExceptionFilter.filter(context, e)) {
                L.e("main", "gotoSave");
                MFGT.gotoSaveActivity(MainActivity.this, id);
            }
        }

        @Override
        public void onNext(DeviceOld deviceOld) {
            progressDialog.dismiss();
            L.e("main", deviceOld.toString());
            MFGT.gotoDetailActivity(MainActivity.this, deviceOld);
            MFGT.finish((Activity) context);
        }
    };

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        L.e("main", "resultCode:" + resultCode);
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                id = (bundle.getString("result"));
                L.e("main", id + "");
                ApiWrapper<ServerAPI> network = new ApiWrapper<>();
                subscription = network.targetClass(ServerAPI.class).
                        getAPI().chaxun(id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .compose(network.<DeviceOld>applySchedulers())
                        .subscribe(obChaxun);
            }

        }
    }

    Observer<String> observerYujing = new Observer<String>() {
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
            dialogHolder.yujing.setText(s);
            Adialog.show();
        }
    };


    private void getYujing() {
        progressDialog.show();
        ApiWrapper<ServerAPI> network = new ApiWrapper<>();
        subscription = network.targetClass(ServerAPI.class).
                getAPI().getyujing()
                .compose(network.<String>applySchedulers())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observerYujing);
    }

    class DialogHolder {
        @BindView(R.id.yujing)
        TextView yujing;
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
                long SecondTime = System.currentTimeMillis();
                if (SecondTime - FirstTime > 2000) {
                    ToastUtil.showToast(context, "再按一次退出应用");
                    FirstTime = SecondTime;
                    return true;
                } else {
                    System.exit(0);
                }
                break;
        }
        return super.onKeyUp(keyCode, event);
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


}


