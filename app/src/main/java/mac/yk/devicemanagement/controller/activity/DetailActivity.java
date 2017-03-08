package mac.yk.devicemanagement.controller.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
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

import static mac.yk.devicemanagement.R.id.remark;


public class DetailActivity extends AppCompatActivity {
    ProgressDialog progressDialog;
    IModel model;
    Device device;
    Context context;
    boolean isDianchi = false;
    fragDetail fragD;
    Dialog dialog;
    @BindView(R.id.toolBar)
    Toolbar toolBar;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawLayout)
    DrawerLayout activityDetail;

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
        L.e("main", device.toString());
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
            setUpNavView(navView);
            navView.inflateMenu(R.menu.menu_detail);
            ImageView imageView = (ImageView) navView.getHeaderView(0).findViewById(R.id.avatar);
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
        setArguments();
    }


    private void setArguments() {
        setArguments();
        Bundle bundle = new Bundle();
        bundle.putSerializable("device", device);
        fragD.setArguments(bundle);
    }

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
                activityDetail.closeDrawers();
                return true;
            }
        });
    }

    private void postControl(int Cid) {
        progressDialog.show();
        model.control(context, isDianchi, MyApplication.getInstance().getUserName(), String.valueOf(Cid), id, new OkHttpUtils.OnCompleteListener<Result>() {
            @Override
            public void onSuccess(Result result) {
                progressDialog.dismiss();
                if (result != null && result.getRetCode() == I.RESULT.SUCCESS) {
                    device = (Device) result.getRetData();
                    setArguments();
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


    private class xiujunHoler{
        @BindView(R.id.cb_no)
        CheckBox cbNo;
        @BindView(R.id.cb_yes)
        CheckBox cbYes;


        View v;
        public xiujunHoler() {
            v = View.inflate(context, R.layout.dialog_xiujun, null);
            ButterKnife.bind(v);
        }

        public View getV() {
            return v;
        }
    }

    private class xunjianHolder{
        @BindView(R.id.cb_good)
        CheckBox cbGood;
        @BindView(R.id.cb_abnormal)
        CheckBox cbAbnormal;
        @BindView(R.id.remark)
        EditText remark;

    }
    private void postXiujun() {
        xiujunHoler xiujunHoler=new xiujunHoler();
        dialog.setContentView(xiujunHoler.getV());
        dialog.setTitle(null);
        dialog.show();
    }

    private void postxunjian() {

        View v = View.inflate(context, R.layout.dialog_xunjian, null);
        ButterKnife.bind(v);
        dialog.setContentView(v);
        dialog.setTitle(null);
        dialog.show();
    }


    @OnClick({R.id.cb_good, R.id.cb_abnormal, R.id.btn_commit, R.id.cb_no, R.id.cb_yes, R.id.btn_commit2})
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
                            setArguments();
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
            case R.id.cb_no:
                cbNo.setChecked(true);
                cbYes.setChecked(false);
                break;
            case R.id.cb_yes:
                cbYes.setChecked(true);
                cbNo.setChecked(false);
                break;
            case R.id.btn_commit2:
                dialog.dismiss();
                progressDialog.show();
                model.xiujun(context, MyApplication.getInstance().getUserName(), isDianchi, id, remark.getText().toString(), new OkHttpUtils.OnCompleteListener<Result>() {
                    @Override
                    public void onSuccess(Result result) {
                        progressDialog.dismiss();
                        if (result != null && result.getRetCode() == I.RESULT.SUCCESS) {
                            device = (Device) result.getRetData();
                            setArguments();
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
