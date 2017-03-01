package mac.yk.devicemanagement.controller.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.xys.libzxing.zxing.activity.CaptureActivity;

import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.bean.Result;
import mac.yk.devicemanagement.model.IModel;
import mac.yk.devicemanagement.util.OkHttpUtils;

import static android.R.attr.id;

public class MainActivity extends Activity implements View.OnClickListener {
    Button btn1, btn2, btn3, btn4,tongji,yujing,luru;
    IModel model;
    ProgressDialog progressDialog;
    Context context;
    AlertDialog.Builder builder=new AlertDialog.Builder(context);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context=this;
        btn1 = (Button) findViewById(R.id.diantai);
        btn2 = (Button) findViewById(R.id.jikongqi);
        btn3 = (Button) findViewById(R.id.qukongqi);
        btn4 = (Button) findViewById(R.id.dianchi);
        tongji= (Button) findViewById(R.id.tongji);
        yujing= (Button) findViewById(R.id.yunxing);
        luru= (Button) findViewById(R.id.luru);

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        tongji.setOnClickListener(this);
        luru.setOnClickListener(this);
        yujing.setOnClickListener(this);
        progressDialog=new ProgressDialog(this);
        builder.setTitle("预警信息")
                .setPositiveButton("已读", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dianchi:
            case R.id.diantai:
            case R.id.jikongqi:
            case R.id.qukongqi:
                Intent intent=new Intent(this,DetailActivity.class);
                intent.putExtra("id",v.getId());
                startActivity(intent);
            break;
            case R.id.tongji:
               startActivity(new Intent(this,tongjiActivity.class));
                break;
            case R.id.yujing:
                progressDialog.show();
              getMessage();
                break;
            case R.id.luru:
                scan();
                break;

        }
    }
    public void scan() {
        startActivityForResult(new Intent(this, CaptureActivity.class), id);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==RESULT_OK){
            Bundle bundle=data.getExtras();
            if (bundle!=null){
               String result=bundle.getString("result");
                Intent intent=new Intent(this,DeviceActivity.class);
                intent.putExtra("id",result);
                startActivity(intent);
            }
        }
    }

    private void getMessage() {
        model.getYujing(this,new OkHttpUtils.OnCompleteListener<Result>() {
            @Override
            public void onSuccess(Result result) {
                progressDialog.dismiss();
                if (result!=null&&result.getRetCode()==0){
                    String s= (String) result.getRetData();
                    builder.setMessage(s);
                    builder.show();
                }else {
                    builder.setMessage("请求失败，请重试");
                    builder.show();
                }
            }

            @Override
            public void onError(String error) {
            progressDialog.dismiss();
               builder.setMessage(error);
                builder.show();
            }
        });
    }
}
