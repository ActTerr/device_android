package mac.yk.devicemanagement.controller.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mac.yk.devicemanagement.I;
import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.bean.Device;
import mac.yk.devicemanagement.bean.Result;
import mac.yk.devicemanagement.model.IModel;
import mac.yk.devicemanagement.model.Model;
import mac.yk.devicemanagement.util.MFGT;
import mac.yk.devicemanagement.util.OkHttpUtils;


public class DetailActivity extends AppCompatActivity {
    ProgressDialog progressDialog;
    IModel model;
    Device device;
    boolean isDianchi = false;
    @BindView(R.id.deviceName)
    TextView deviceName;
    @BindView(R.id.detail)
    TextView detail;

    @BindView(R.id.refresh)
    Button chaxun;
    @BindView(R.id.daiyong)
    Button daiyong;
    @BindView(R.id.yunxing)
    Button yunxing;
    @BindView(R.id.baofei)
    Button baofei;
    @BindView(R.id.weixiu)
    Button weixiu;
    @BindView(R.id.xiujun)
    Button xiujun;
    @BindView(R.id.xunjian)
    Button xunjian;
    @BindView(R.id.jilu)
    Button jilu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        progressDialog = new ProgressDialog(this);
        device = (Device) getIntent().getSerializableExtra("device");
        if (device == null) {
            finish();
        } else {
            deviceName.setText(device.getName());
            if (device.getName().equals("电池")) {
                weixiu.setText("充电");
                xiujun.setText("用后");
                isDianchi = true;
            }
        }
        model = new Model();

    }


    @OnClick({R.id.refresh, R.id.daiyong, R.id.yunxing, R.id.baofei, R.id.weixiu, R.id.xiujun, R.id.xunjian, R.id.jilu})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.refresh:
                progressDialog.show();
                model.chaxun(this, Integer.parseInt(device.getId()), new OkHttpUtils.OnCompleteListener<Result>() {
                    @Override
                    public void onSuccess(Result result) {
                        progressDialog.dismiss();
                        if (result != null && result.getRetCode() == I.SUCCESS) {
                            device = (Device) result.getRetData();
                            detail.setText(device.toString());
                            //对toString进行修改即可
                        } else {
                            Toast.makeText(DetailActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(String error) {
                        progressDialog.dismiss();
                        Toast.makeText(DetailActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.daiyong:
            case R.id.yunxing:
            case R.id.baofei:
            case R.id.weixiu:
            case R.id.xiujun:
            case R.id.xunjian:
                progressDialog.show();
                model.control(this, isDianchi, device.getId(), String.valueOf(view.getId()), new OkHttpUtils.OnCompleteListener<Result>() {
                    @Override
                    public void onSuccess(Result result) {
                        progressDialog.dismiss();
                        if (result != null && result.getRetCode() == I.SUCCESS) {
                            device = (Device) result.getRetData();
                            detail.setText(device.toString());
                            //对toString进行修改即可
                        } else {
                            Toast.makeText(DetailActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(String error) {
                        progressDialog.dismiss();
                        Toast.makeText(DetailActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.jilu:
                MFGT.gotoMessageActivity(this, Integer.parseInt(device.getId()));
                break;
        }
    }


}
