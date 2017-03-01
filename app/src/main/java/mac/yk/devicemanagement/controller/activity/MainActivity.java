package mac.yk.devicemanagement.controller.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.model.IModel;
import mac.yk.devicemanagement.util.OkHttpUtils;

public class MainActivity extends Activity implements View.OnClickListener {
    Button btn1, btn2, btn3, btn4,tongji,yujing;
    IModel model;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn1 = (Button) findViewById(R.id.diantai);
        btn2 = (Button) findViewById(R.id.jikongqi);
        btn3 = (Button) findViewById(R.id.qukongqi);
        btn4 = (Button) findViewById(R.id.dianchi);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        progressDialog=new ProgressDialog(this);

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
                AlertDialog.Builder builder=new AlertDialog.Builder(this);
                builder.setTitle("预警信息")
                        .setMessage(getMessage())
                        .setPositiveButton("已读", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
                break;
        }
    }

    private String getMessage() {
        model.getMessage(new OkHttpUtils.OnCompleteListener<String>() {
            @Override
            public void onSuccess(String result) {
                         
            }

            @Override
            public void onError(String error) {
            progressDialog.dismiss();
            }
        });
        return "";
    }
}
