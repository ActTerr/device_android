package mac.yk.devicemanagement.controller.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
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
import mac.yk.devicemanagement.controller.fragment.fragDetail;
import mac.yk.devicemanagement.model.IModel;
import mac.yk.devicemanagement.util.ActivityUtils;
import mac.yk.devicemanagement.util.L;
import mac.yk.devicemanagement.util.MFGT;
import mac.yk.devicemanagement.util.OkHttpUtils;
import mac.yk.devicemanagement.util.TestUtil;


public class DetailActivity extends AppCompatActivity {
    ProgressDialog progressDialog;
    IModel model;
    Device device;
    Activity context;
    boolean isDianchi = false;
    fragDetail fragD;
    Dialog dialog;
    @BindView(R.id.toolBar)
    Toolbar toolBar;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawLayout)
    DrawerLayout drawLayout;

    xiujunHoler xiujunHoler;
    xunjianHolder xunjianHolder;
    /**
     * dialog
     */

    String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency);
        ButterKnife.bind(this);
        context = this;
        progressDialog = new ProgressDialog(context);
        dialog = new Dialog(context);
        model = TestUtil.getData();
        device = (Device) getIntent().getSerializableExtra("device");
        MyApplication.setDevice(device);
        L.e("main","detail:"+ device.toString());
        if (device == null) {
            finish();
        } else {
            id = device.getId();
            if (device.getName().equals("电池")) {
                isDianchi = true;
            }
        }
        setSupportActionBar(toolBar);
        ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);
        fragD = new fragDetail();
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), fragD, R.id.frame);
        if (navView != null) {
            navView.inflateMenu(R.menu.menu_detail);
            setUpNavView(navView);
            ImageView imageView = (ImageView) navView.getHeaderView(0).findViewById(R.id.avatar);
            TextView textView= (TextView) navView.getHeaderView(0).findViewById(R.id.nav_name);
            textView.setText(MyApplication.getInstance().getUserName());
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MFGT.gotoSetActivity(context);
                    finish();
                }
            });
        }
        if (isDianchi) {
            navView.getMenu().getItem(3).setTitle("用后");
            navView.getMenu().getItem(4).setTitle("充电");
        }
//        setArguments();
        Log.e("main","setArgument执行");
    }


//    private void setArguments() {
//        setArguments();
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("device", device);
//        fragD.setArguments(bundle);
//    }

    private void setUpNavView(NavigationView navView) {
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.daiyong:
                    case R.id.yunxing:
                    case R.id.baofei:
                    case R.id.weixiu:
                        postControl(item.getItemId());
                        break;
                    case R.id.xunjian:
                        postxunjian();
                        break;
                    case R.id.xiujun:
                        postXiujun();
                        break;
                    case R.id.record:
                        MFGT.gotoRecordActivity(context, id);
                        break;
                }
                item.setChecked(true);
                drawLayout.closeDrawers();
                return false;
            }
        });
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


    private void postControl(int Cid) {
        progressDialog.show();
        model.control(context, isDianchi, MyApplication.getInstance().getUserName(), String.valueOf(Cid), id, new OkHttpUtils.OnCompleteListener<Result>() {
            @Override
            public void onSuccess(Result result) {
                progressDialog.dismiss();
                if (result != null && result.getRetCode() == I.RESULT.SUCCESS) {
                    device = (Device) result.getRetData();
                    MyApplication.getDevice().setZhuangtai(device.getZhuangtai());
//                    setArguments();
                } else {
                    Toast.makeText(context, "请求失败！", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String error) {
                progressDialog.dismiss();
                Toast.makeText(context, "检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }




    public class xiujunHoler {
        @BindView(R.id.cb_no)
        CheckBox cbNo;
        @BindView(R.id.cb_yes)
        CheckBox cbYes;
        @BindView(R.id.remark)
        EditText remark;

        View v;

        public xiujunHoler() {
            v = View.inflate(context, R.layout.dialog_xiujun, null);
            ButterKnife.bind(v);
        }

        public View getV() {
            return v;
        }

        @OnClick({R.id.cb_no, R.id.cb_yes, R.id.btn_commit2})
        public void onClick(View view) {
            boolean translate=false;
            switch (view.getId()) {
                case R.id.cb_no:
                    cbNo.setChecked(true);
                    cbYes.setChecked(false);
                    translate=false;
                    break;
                case R.id.cb_yes:
                    cbYes.setChecked(true);
                    cbNo.setChecked(false);
                    translate=true;
                    break;
                case R.id.btn_commit2:
                    dialog.dismiss();
                    progressDialog.show();
                    model.xiujun(context, MyApplication.getInstance().getUserName(), isDianchi, id, translate,remark.getText().toString(), new OkHttpUtils.OnCompleteListener<Result>() {
                        @Override
                        public void onSuccess(Result result) {
                            progressDialog.dismiss();
                            if (result != null && result.getRetCode() == I.RESULT.SUCCESS) {
                                device = (Device) result.getRetData();
                                MyApplication.getDevice().setZhuangtai(device.getZhuangtai());
//                                setArguments();
                            } else {
                                Toast.makeText(context, "请求失败！", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(String error) {
                            progressDialog.dismiss();
                            Toast.makeText(context, "检查网络", Toast.LENGTH_SHORT).show();
                        }
                    });
                    break;
            }
        }
    }

    public class xunjianHolder {
        @BindView(R.id.cb_good)
        CheckBox cbGood;
        @BindView(R.id.cb_abnormal)
        CheckBox cbAbnormal;
        @BindView(R.id.remark)
        EditText remark;
        View v;

        public xunjianHolder() {
            v = View.inflate(context, R.layout.dialog_xunjian, null);
            ButterKnife.bind(v);
        }

        public View getV() {
            return v;
        }

        @OnClick({R.id.cb_good, R.id.cb_abnormal, R.id.btn_commit})
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.cb_good:
                    cbGood.setChecked(true);
                    cbAbnormal.setChecked(false);
                    break;
                case R.id.cb_abnormal:
                    cbAbnormal.setChecked(true);
                    cbGood.setChecked(false);
                    break;
                case R.id.btn_commit:
                    dialog.dismiss();
                    progressDialog.show();
                    String zhuangtai;
                    if (cbAbnormal.isChecked()) {
                        zhuangtai = "正常";
                    } else {
                        zhuangtai = "异常";
                    }
                    model.xunjian(context, MyApplication.getInstance().getUserName(), isDianchi, id, zhuangtai, remark.getText().toString(), new OkHttpUtils.OnCompleteListener<Result>() {
                        @Override
                        public void onSuccess(Result result) {
                            progressDialog.dismiss();
                            if (result != null && result.getRetCode() == I.RESULT.SUCCESS) {
                                device = (Device) result.getRetData();
                                MyApplication.getDevice().setStatus(device.getStatus());
//                                setArguments();
                            } else {
                                Toast.makeText(context, "请求失败！", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(String error) {
                            progressDialog.dismiss();
                            Toast.makeText(context, "检查网络", Toast.LENGTH_SHORT).show();
                        }
                    });
                    break;
            }
        }
    }

    private void postXiujun() {
        xiujunHoler = new xiujunHoler();
        dialog.setContentView(xiujunHoler.getV());
        dialog.setTitle(null);
        dialog.show();
    }

    private void postxunjian() {
        xunjianHolder = new xunjianHolder();
        dialog.setContentView(xunjianHolder.getV());
        dialog.setTitle(null);
        dialog.show();
    }




}
