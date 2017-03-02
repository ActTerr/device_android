package mac.yk.devicemanagement.controller.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import mac.yk.devicemanagement.I;
import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.bean.Device;
import mac.yk.devicemanagement.bean.Result;
import mac.yk.devicemanagement.controller.fragment.frag_main;
import mac.yk.devicemanagement.model.IModel;
import mac.yk.devicemanagement.model.Model;
import mac.yk.devicemanagement.util.ActivityUtils;
import mac.yk.devicemanagement.util.MFGT;
import mac.yk.devicemanagement.util.OkHttpUtils;

public class MainActivity extends AppCompatActivity {
    IModel model;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        model=new Model();
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),new frag_main(),R.id.frag_main);
        progressDialog=new ProgressDialog(this);
    }


    @Override
    protected void onActivityResult(final int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                int result = Integer.parseInt(bundle.getString("result"));
                if (requestCode == I.SCAN.SAVE) {
                    MFGT.gotoSaveActivity(this,result);
                } else {
                    progressDialog.show();
                    model.chaxun(this, result, new OkHttpUtils.OnCompleteListener<Result>() {
                        @Override
                        public void onSuccess(Result result) {
                            progressDialog.dismiss();
                            if (result!=null&&result.getRetCode()==I.SUCCESS){
                                Device device= (Device) result.getRetData();
                                MFGT.gotoDetailActivity(MainActivity.this,device);
                            }else {
                                Toast.makeText(MainActivity.this, "请求失败！", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(String error) {
                            progressDialog.dismiss();
                            Toast.makeText(MainActivity.this, "请求失败！", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }
    }


}
