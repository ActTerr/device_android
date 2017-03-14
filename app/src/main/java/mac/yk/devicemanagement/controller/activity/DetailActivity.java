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
        L.e("main", "detail:" + device.toString());
        if (device == null) {
            finish();
        } else {
            id = String.valueOf(device.getDid());
            if (device.getDname() == I.DNAME.DIANCHI) {
                isDianchi = true;
//                device.setDianchi(true);
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
            TextView textView = (TextView) navView.getHeaderView(0).findViewById(R.id.nav_name);
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
            navView.getMenu().getItem(3).setTitle("充电");
            navView.getMenu().getItem(4).setTitle("充满");
        }else{
            //如果不是电池该项不可见
            navView.getMenu().getItem(6).setVisible(false);
        }
//        setArguments();
        Log.e("main", "setArgument执行");
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
                    case R.id.beiyong:
                    case R.id.daiyong:
                    case R.id.yunxing:
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
                    case R.id.baofei:
                        postBaofei();
                        break;
                    case R.id.yonghou:
                        postYonghou(id);
                }
                item.setChecked(true);
                drawLayout.closeDrawers();
                return false;
            }
        });
    }

    private void postYonghou(String id) {
        progressDialog.show();
        model.yonghou(context, id, new OkHttpUtils.OnCompleteListener<Result>() {
            @Override
            public void onSuccess(Result result) {
                progressDialog.dismiss();
                if(result.getRetCode()==I.RESULT.SUCCESS){
                    int status= (int) result.getRetData();
                    device.setStatus(status);
                }else {
                    Toast.makeText(context, "当前状态不可执行该操作！", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    public class BaofeiHolder {
        View v;
        @BindView(R.id.dia_title)
        TextView diaTitle;
        @BindView(R.id.cb_no)
        CheckBox cbNo;
        @BindView(R.id.cb_yes)
        CheckBox cbYes;
        @BindView(R.id.remark)
        EditText remark;

        public BaofeiHolder() {
            v = View.inflate(context, R.layout.dialog_currency, null);
            ButterKnife.bind(v);
            cbNo.setVisibility(View.GONE);
            cbYes.setVisibility(View.GONE);
            diaTitle.setText("报废提交");
            remark.setHint("请说明报废原因");

        }

        public View getV() {
            return v;
        }

        public void setV(View v) {
            this.v = v;
        }

        @OnClick(R.id.btn_commit)
        public void onClick(View view) {
           progressDialog.show();
            model.baofei(context, MyApplication.getInstance().getUserName(), String.valueOf(device.getDname()), id, remark.getText().toString(), new OkHttpUtils.OnCompleteListener<Result>() {
                @Override
                public void onSuccess(Result result) {
                    if (result.getRetCode()==I.RESULT.SUCCESS&&result.isRetMsg()){
                        int status= (int) result.getRetData();
                        device.setStatus(status);
                    }else {
                        Toast.makeText(context, "提交失败", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
               public void onError(String error) {
                    Toast.makeText(context, "请检查网络", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void postBaofei() {
        BaofeiHolder baofei=new BaofeiHolder();
        dialog.setContentView(baofei.getV());
        dialog.setTitle(null);
        dialog.show();
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
        model.control(context, isDianchi,String.valueOf(Cid), id, new OkHttpUtils.OnCompleteListener<Result>() {
            @Override
            public void onSuccess(Result result) {
                progressDialog.dismiss();
                if (result != null && result.getRetCode() == I.RESULT.SUCCESS) {
                    int status = (int) result.getRetData();
                    device.setStatus(status);
//                    setArguments();
                } else {
                    Toast.makeText(context, "当前状态不可执行该操作！", Toast.LENGTH_SHORT).show();
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
            v = View.inflate(context, R.layout.dialog_currency, null);
            ButterKnife.bind(v);
        }

        public View getV() {
            return v;
        }

        @OnClick({R.id.cb_no, R.id.cb_yes, R.id.btn_commit})
        public void onClick(View view) {
            boolean translate = false;
            switch (view.getId()) {
                case R.id.cb_no:
                    cbNo.setChecked(true);
                    cbYes.setChecked(false);
                    translate = false;
                    break;
                case R.id.cb_yes:
                    cbYes.setChecked(true);
                    cbNo.setChecked(false);
                    translate = true;
                    break;
                case R.id.btn_commit:
                    dialog.dismiss();
                    progressDialog.show();
                    model.xiujun(context, isDianchi,MyApplication.getInstance().getUserName(), id, translate, remark.getText().toString(), new OkHttpUtils.OnCompleteListener<Result>() {
                        @Override
                        public void onSuccess(Result result) {
                            progressDialog.dismiss();
                            if (result != null && result.getRetCode() == I.RESULT.SUCCESS) {
                                int status = (int) result.getRetData();
                                device.setStatus(status);
//                                setArguments();
                            } else {
                                Toast.makeText(context, "当前状态不可执行该操作", Toast.LENGTH_SHORT).show();
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

        View v;
        @BindView(R.id.dia_title)
        TextView diaTitle;
        @BindView(R.id.cb_no)
        CheckBox cbNo;
        @BindView(R.id.cb_yes)
        CheckBox cbYes;
        @BindView(R.id.remark)
        EditText remark;

        public xunjianHolder() {
            v = View.inflate(context, R.layout.dialog_currency, null);
            ButterKnife.bind(v);
            diaTitle.setText("设备状态");
            remark.setHint("如有问题请备注");
            cbNo.setText("异常");
            cbYes.setText("良好");
        }

        public View getV() {
            return v;
        }


        @OnClick({R.id.cb_no, R.id.cb_yes, R.id.btn_commit})
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.cb_no:
                    cbNo.setChecked(true);
                    cbYes.setChecked(false);
                    break;
                case R.id.cb_yes:
                    cbYes.setChecked(true);
                    cbNo.setChecked(false);
                    break;
                case R.id.btn_commit:
                    dialog.dismiss();
                    progressDialog.show();
                    String status;
                    if (cbYes.isChecked()) {
                        status = "1";
                    } else {
                        status = "0";
                    }
                    model.xunjian(context, isDianchi,MyApplication.getInstance().getUserName(), id, status, remark.getText().toString(), new OkHttpUtils.OnCompleteListener<Result>() {
                        @Override
                        public void onSuccess(Result result) {
                            progressDialog.dismiss();
                            if (result != null && result.getRetCode() == I.RESULT.SUCCESS) {
                                int status = (int) result.getRetData();
                                device.setStatus(status);
//                                MyApplication.getDevice().setStatus(device.getStatus());
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
       xiujunHoler xiujunHoler = new xiujunHoler();
        dialog.setContentView(xiujunHoler.getV());
        dialog.setTitle(null);
        dialog.show();
    }

    private void postxunjian() {
        xunjianHolder xunjianHolder = new xunjianHolder();
        dialog.setContentView(xunjianHolder.getV());
        dialog.setTitle(null);
        dialog.show();
    }


}
