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
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mac.yk.devicemanagement.I;
import mac.yk.devicemanagement.MyApplication;
import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.bean.Device;
import mac.yk.devicemanagement.bean.Result;
import mac.yk.devicemanagement.controller.fragment.fragBaofei;
import mac.yk.devicemanagement.controller.fragment.fragMain;
import mac.yk.devicemanagement.controller.fragment.fragTongji;
import mac.yk.devicemanagement.model.IModel;
import mac.yk.devicemanagement.util.ActivityUtils;
import mac.yk.devicemanagement.util.L;
import mac.yk.devicemanagement.util.MFGT;
import mac.yk.devicemanagement.util.OkHttpUtils;
import mac.yk.devicemanagement.util.SpUtil;
import mac.yk.devicemanagement.util.TestUtil;

import static mac.yk.devicemanagement.R.id.yujing;

public class MainActivity extends AppCompatActivity {
    IModel model;
    String id;
    ProgressDialog progressDialog;

    AlertDialog.Builder builder;
    @BindView(R.id.toolBar)
    Toolbar toolBar;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawLayout)
    DrawerLayout drawLayout;

    DialogHolder dialogHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency);
        View view = View.inflate(this, R.layout.dialog_yujing, null);
        ButterKnife.bind(this);
        builder = new AlertDialog.Builder(this);
        model = TestUtil.getData();
        progressDialog = new ProgressDialog(this);
        setSupportActionBar(toolBar);
        ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);
       dialogHolder=new DialogHolder(view);
        builder.setTitle("预警信息")
                .setView(view)
                .setPositiveButton("已读以上信息", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), new fragMain(), R.id.frame);
        progressDialog = new ProgressDialog(this);
        if (navView != null) {
            navView.inflateMenu(R.menu.menu_main);
            setUpNavView(navView);

            ImageView imageView = (ImageView) navView.getHeaderView(0).findViewById(R.id.avatar);
            TextView textView = (TextView) navView.getHeaderView(0).findViewById(R.id.nav_name);
            textView.setText(MyApplication.getInstance().getUserName());
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MFGT.gotoSetActivity(MainActivity.this);
                    finish();
                }
            });
        }
        if (!SpUtil.getPrompt(this)){
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
                        ActivityUtils.changeFragment(getSupportFragmentManager(), new fragTongji(), R.id.frame);
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
                progressDialog.show();
                model.chaxun(this, id, new OkHttpUtils.OnCompleteListener<Result>() {
                    @Override
                    public void onSuccess(Result result) {
                        progressDialog.dismiss();
                        if (result != null && result.getRetCode() == I.RESULT.SUCCESS) {
                            Device device = (Device) result.getRetData();
                            L.e("main", "gotoDetail");
                            MFGT.gotoDetailActivity(MainActivity.this, device);

                        } else {
                            L.e("main", "gotoSave");
                            MFGT.gotoSaveActivity(MainActivity.this, id);
                        }
                    }

                    @Override
                    public void onError(String error) {
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this, "请检查网络！", Toast.LENGTH_SHORT).show();
                    }
                });
            }

        }
    }

    private void getYujing() {
        progressDialog.show();
        model.getYujing(this, new OkHttpUtils.OnCompleteListener<Result>() {
            @Override
            public void onSuccess(Result result) {
                progressDialog.dismiss();
                if (result != null && result.getRetCode() == I.RESULT.SUCCESS) {
                    String yujingm = (String) result.getRetData();
                    dialogHolder.yujing.setText(yujingm);
                    builder.show();
                } else {
                    Toast.makeText(MainActivity.this, "获取预警信息失败！", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String error) {
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, "请检查网络！", Toast.LENGTH_SHORT).show();
            }
        });
    }
    class DialogHolder{
        @BindView(R.id.yujing)
        TextView yujing;
        @BindView(R.id.no_prompt)
        CheckBox noPrompt;

        public DialogHolder(View view) {
            ButterKnife.bind(this,view);
        }
        @OnClick(R.id.no_prompt)
        public void onClick() {
            if(noPrompt.isChecked()){
                SpUtil.savePrompt(MainActivity.this,true);
            }
        }
    }


}
