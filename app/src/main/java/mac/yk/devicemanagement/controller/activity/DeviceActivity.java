package mac.yk.devicemanagement.controller.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.bean.Device;
import mac.yk.devicemanagement.bean.Result;
import mac.yk.devicemanagement.model.IModel;
import mac.yk.devicemanagement.model.Model;
import mac.yk.devicemanagement.util.OkHttpUtils;

public class DeviceActivity extends AppCompatActivity {
    EditText zhuangtai,name,chuchang,xunjian;
    String id;
    IModel model;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);
        id=getIntent().getStringExtra("id");
        if (id==null){
            finish();
        }
        zhuangtai= (EditText) findViewById(R.id.zhuangtai);
        name= (EditText) findViewById(R.id.name);
        chuchang= (EditText) findViewById(R.id.chuchang);
        xunjian= (EditText) findViewById(R.id.xunjian);

    }

    public void onSave(View view){
        Device device=new Device(id,name.getText().toString(),xunjian.getText().toString(),chuchang.getText().toString(),zhuangtai.getText().toString());
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.show();
        model=new Model();
        model.saveDevice(this,device, new OkHttpUtils.OnCompleteListener<Result>() {
            @Override
            public void onSuccess(Result result) {
                progressDialog.dismiss();
                if (result!=null&&result.getRetCode()==0){

                }else {
                    Toast.makeText(DeviceActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String error) {
                progressDialog.dismiss();
                Toast.makeText(DeviceActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
