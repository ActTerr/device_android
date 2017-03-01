package mac.yk.devicemanagement.controller.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.xys.libzxing.zxing.activity.CaptureActivity;

import mac.yk.devicemanagement.MyApplication;
import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.bean.Device;
import mac.yk.devicemanagement.bean.Result;
import mac.yk.devicemanagement.model.IModel;
import mac.yk.devicemanagement.model.Model;
import mac.yk.devicemanagement.util.OkHttpUtils;

import static android.R.attr.id;
import static mac.yk.devicemanagement.R.id.dianchi;

public class DetailActivity extends Activity implements View.OnClickListener{

    Button weixiu,xunjian,daiyong,yunxing,xiujun,baofei,chaxun;
    int dId;
    IModel model;
    Device device;
    TextView detail;
    boolean isDianchi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        model=new Model();
        device= MyApplication.getDevice();
        weixiu= (Button) findViewById(R.id.weixiu);
        xunjian= (Button) findViewById(R.id.xunjian);
        dId=getIntent().getIntExtra("id",0);
        if(dId== dianchi){
            weixiu.setText("充电");
            xunjian.setText("用后");
            isDianchi=true;
        }
        daiyong= (Button) findViewById(R.id.daiyong);
        yunxing= (Button) findViewById(R.id.yunxing);
        xiujun= (Button) findViewById(R.id.xiujun);
        baofei= (Button) findViewById(R.id.baofei);
        chaxun= (Button) findViewById(R.id.chaxun);
        detail= (TextView) findViewById(R.id.detail);
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
        scan(v.getId());


       }


    public void scan(int id) {
        startActivityForResult(new Intent(this, CaptureActivity.class), id);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode==RESULT_OK){
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                String result=bundle.getString("result");
                if (isDianchi){
                    result="dianchi"+result;
                }
                if (id==R.id.chaxun){
                    model.chaxun(this,result, new OkHttpUtils.OnCompleteListener<Result>() {
                        @Override
                        public void onSuccess(Result result) {
                            if (result!=null){
                                device= (Device) result.getRetData();
                                detail.setText(device.toString());
                            }
                        }

                        @Override
                        public void onError(String error) {
                            detail.setText("查询失败，请重试");
                        }
                    });
                }else {
                    model.saoma(this,isDianchi,result, new OkHttpUtils.OnCompleteListener<Result>() {
                        @Override
                        public void onSuccess(Result result) {
                            if (result!=null&&result.getRetCode()==0){
                                device.setZhuangtai((String) result.getRetData());
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
        }


    }


}
