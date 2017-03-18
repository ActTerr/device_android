package mac.yk.devicemanagement.ui.activity;

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
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mac.yk.devicemanagement.I;
import mac.yk.devicemanagement.MyApplication;
import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.bean.Device;
import mac.yk.devicemanagement.bean.Result;
import mac.yk.devicemanagement.model.IModel;
import mac.yk.devicemanagement.net.ServerAPI;
import mac.yk.devicemanagement.net.netWork;
import mac.yk.devicemanagement.ui.fragment.fragBaofei;
import mac.yk.devicemanagement.ui.fragment.fragDevice;
import mac.yk.devicemanagement.ui.fragment.fragMain;
import mac.yk.devicemanagement.util.ActivityUtils;
import mac.yk.devicemanagement.util.L;
import mac.yk.devicemanagement.util.MFGT;
import mac.yk.devicemanagement.util.OkHttpUtils;
import mac.yk.devicemanagement.util.SpUtil;
import mac.yk.devicemanagement.util.TestUtil;
import mac.yk.devicemanagement.util.ToastUtil;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static mac.yk.devicemanagement.R.id.yujing;

public class MainActivity extends BaseActivity{
    IModel model;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency);
        View view = View.inflate(this, R.layout.dialog_yujing, null);
        ButterKnife.bind(this);
        context=this;
        builder = new AlertDialog.Builder(this);
        model = TestUtil.getData();
        progressDialog = new ProgressDialog(this);
        setSupportActionBar(toolBar);
        ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);
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
        progressDialog = new ProgressDialog(this);
        if (navView != null) {
            navView.inflateMenu(R.menu.menu_main);
            setUpNavView(navView);

            ImageView imageView = (ImageView) navView.getHeaderView(0).findViewById(R.id.avatar);
            TextView textView = (TextView) navView.getHeaderView(0).findViewById(R.id.nav_name);
            textView.setText(MyApplication.getInstance().getUserName());
            L.e("main", "name:" + MyApplication.getInstance().getUserName());
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MFGT.gotoSetActivity(MainActivity.this);
                }
            });
        }
        if (!SpUtil.getPrompt(this)) {
            getYujing();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpNavView(NavigationView navView) {
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case yujing:
                        getYujing();
                        break;
                    case R.id.tongji:
                        ActivityUtils.changeFragment(getSupportFragmentManager(), new fragDevice(), R.id.frame);
                        break;
                    case R.id.bf_tongji:
                        ActivityUtils.changeFragment(getSupportFragmentManager(), new fragBaofei(), R.id.frame);
                        break;
                }
                item.setChecked(true);
                drawLayout.closeDrawers();
                return true;
            }
        });
    }


    @Override
    protected void onActivityResult(final int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        L.e("main", "resultCode:" + resultCode);
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                id = (bundle.getString("result"));
                L.e("main", id + "");
                model.chaxun(this, id, new OkHttpUtils.OnCompleteListener<Result>() {
                    @Override
                    public void onSuccess(Result result) {
                        if (result != null && result.getRetCode() == I.RESULT.SUCCESS) {
                            L.e("main", result.toString());
                            String s = result.getRetData().toString();
                            Gson gson = new Gson();
                            Device d = gson.fromJson(s, Device.class);
                            L.e("main", "gotoDetail");
                            MFGT.gotoDetailActivity(MainActivity.this, d);
                            finish();
                        } else {
                            L.e("main", "gotoSave");
                            MFGT.gotoSaveActivity(MainActivity.this, id);
                        }
                    }

                    @Override
                    public void onError(String error) {
                        ToastUtil.showNetWorkBad(context);
                    }
                });
            }

        }
    }
    Observer<String> observer=new Observer<String>(){
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            progressDialog.dismiss();
            ToastUtil.showNetWorkBad(context);
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
        netWork<ServerAPI> netWork=new netWork<>();
       subscription= netWork.targetClass(ServerAPI.class).getAPI().getyujing().map(new Func1<Result, String>() {
            @Override
            public String call(Result result) {
                return result.getRetData().toString();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

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
                SpUtil.savePrompt(MainActivity.this, true);
            }
        }
    }


}
