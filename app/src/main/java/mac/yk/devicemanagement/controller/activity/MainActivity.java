package mac.yk.devicemanagement.controller.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import mac.yk.devicemanagement.I;
import mac.yk.devicemanagement.MyApplication;
import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.bean.Device;
import mac.yk.devicemanagement.bean.Result;
import mac.yk.devicemanagement.controller.fragment.fragMain;
import mac.yk.devicemanagement.controller.fragment.fragMain2;
import mac.yk.devicemanagement.model.IModel;
import mac.yk.devicemanagement.model.Model;
import mac.yk.devicemanagement.util.ActivityUtils;
import mac.yk.devicemanagement.util.MFGT;
import mac.yk.devicemanagement.util.OkHttpUtils;

public class MainActivity extends AppCompatActivity {
    IModel model;
    int id;
    ProgressDialog progressDialog;

    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    @BindView(R.id.toolBar)
    Toolbar toolBar;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawLayout)
    DrawerLayout drawLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency);
        ButterKnife.bind(this);
        model = Model.getInstance();
        progressDialog = new ProgressDialog(this);
        setSupportActionBar(toolBar);
        ActionBar ab=getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);
        builder.setTitle("预警信息")
                .setPositiveButton("已读以上信息", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), new fragMain(), R.id.frame);
        progressDialog = new ProgressDialog(this);
        if (navView != null) {
            setUpNavView(navView);
            ImageView imageView= (ImageView) navView.getHeaderView(0).findViewById(R.id.avatar);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MFGT.gotoSetActivity(MainActivity.this);
                    finish();
                }
            });
        }
        getYujing();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
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
                    case R.id.yujing:
                        getYujing();
                        break;
                    case R.id.back:
                        model.LogOut(MainActivity.this, MyApplication.getInstance().getUserName(), new OkHttpUtils.OnCompleteListener<Result>() {
                            @Override
                            public void onSuccess(Result result) {
                                if (result != null && result.getRetCode() == I.SUCCESS) {
                                    MFGT.gotoLoginActivity(MainActivity.this);
                                } else {
                                    Toast.makeText(MainActivity.this, "登出失败，请重试", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onError(String error) {
                                Toast.makeText(MainActivity.this, "请检查网络状态", Toast.LENGTH_SHORT).show();
                            }
                        });
                        break;
                    case R.id.tongji:
                        ActivityUtils.changeFragment(getSupportFragmentManager(), new fragMain2(), R.id.frag_main);
                        break;
                }
                item.setChecked(true);
                drawLayout.closeDrawers();
                return false;
            }
        });
    }


    @Override
    protected void onActivityResult(final int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                id = Integer.parseInt(bundle.getString("result"));
                if (requestCode == I.SCAN.SAVE) {
                    MFGT.gotoSaveActivity(this, id);
                } else {
                    progressDialog.show();
                    model.chaxun(this, id, new OkHttpUtils.OnCompleteListener<Result>() {
                        @Override
                        public void onSuccess(Result result) {
                            progressDialog.dismiss();
                            if (result != null && result.getRetCode() == I.SUCCESS) {
                                Device device = (Device) result.getRetData();
                                MFGT.gotoDetailActivity(MainActivity.this, device);
                            } else {
                                MFGT.gotoSaveActivity(MainActivity.this, id);
                            }
                        }

                        @Override
                        public void onError(String error) {
                            progressDialog.dismiss();
                            Toast.makeText(MainActivity.this, "请求失败！", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }
    }

    private void getYujing() {

        model.getYujing(this, new OkHttpUtils.OnCompleteListener<Result>() {
            @Override
            public void onSuccess(Result result) {
                progressDialog.dismiss();
                if (result != null && result.getRetCode() == I.SUCCESS) {
                    String yujing = (String) result.getRetData();
                    builder.setMessage(yujing).show();
                } else {
                    Toast.makeText(MainActivity.this, "获取预警信息失败！", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String error) {
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, "获取预警信息失败！", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
