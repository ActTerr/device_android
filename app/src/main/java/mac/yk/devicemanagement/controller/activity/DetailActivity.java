package mac.yk.devicemanagement.controller.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.xys.libzxing.zxing.activity.CaptureActivity;

import mac.yk.devicemanagement.I;
import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.bean.Device;
import mac.yk.devicemanagement.bean.Result;
import mac.yk.devicemanagement.model.IModel;
import mac.yk.devicemanagement.model.Model;
import mac.yk.devicemanagement.util.OkHttpUtils;

import static mac.yk.devicemanagement.R.id.dianchi;

public class DetailActivity extends Activity implements View.OnClickListener {

    Button weixiu, xunjian, daiyong, yunxing, xiujun, baofei, chaxun;
    int vId;
    IModel model;
    Device device;
    TextView detail, deviceName;
    boolean isDianchi;
    String dId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        model = new Model();
        weixiu = (Button) findViewById(R.id.weixiu);
        xunjian = (Button) findViewById(R.id.xunjian);
        vId = getIntent().getIntExtra("id", 0);
        if (vId == dianchi) {
            weixiu.setText("充电");
            xunjian.setText("用后");
            isDianchi = true;
        }
        deviceName = (TextView) findViewById(R.id.deviceName);
        daiyong = (Button) findViewById(R.id.daiyong);
        yunxing = (Button) findViewById(R.id.yunxing);
        xiujun = (Button) findViewById(R.id.xiujun);
        baofei = (Button) findViewById(R.id.baofei);
        chaxun = (Button) findViewById(R.id.chaxun);
        detail = (TextView) findViewById(R.id.detail);
        daiyong.setOnClickListener(this);
        weixiu.setOnClickListener(this);
        xunjian.setOnClickListener(this);
        yunxing.setOnClickListener(this);
        xiujun.setOnClickListener(this);
        baofei.setOnClickListener(this);
        chaxun.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String cId= String.valueOf(v.getId());
        if (device == null) {
            scan();
        } else {
            model.saoma(this, isDianchi, dId,cId, new OkHttpUtils.OnCompleteListener<Result>() {
                @Override
                public void onSuccess(Result result) {
                    if (result != null && result.getRetCode() == 0) {
                        device= (Device) result.getRetData();
                        detail.setText(device.toString());
                    }
                }

                @Override
                public void onError(String error) {
                    detail.setText("查询失败，请重试");
                }
            });
        }
    }


    public void scan() {
        startActivityForResult(new Intent(this, CaptureActivity.class), 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                String result = bundle.getString("result");
                model.chaxun(this, isDianchi, result, new OkHttpUtils.OnCompleteListener<Result>() {
                    @Override
                    public void onSuccess(Result result) {
                        if (result != null && result.getRetCode() == I.SUCCESS) {
                            device = (Device) result.getRetData();
                            detail.setText(device.toString());
                            deviceName.setText(device.getName());
                        }
                    }

                    @Override
                    public void onError(String error) {
                        detail.setText("查询失败，请重试");
                    }
                });

            }
        }


    }


}
