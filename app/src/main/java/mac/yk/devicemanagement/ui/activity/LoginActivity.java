package mac.yk.devicemanagement.ui.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import mac.yk.devicemanagement.I;
import mac.yk.devicemanagement.MyApplication;
import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.bean.Result;
import mac.yk.devicemanagement.model.IModel;
import mac.yk.devicemanagement.util.OkHttpUtils;
import mac.yk.devicemanagement.util.SpUtil;
import mac.yk.devicemanagement.util.TestUtil;
import mac.yk.devicemanagement.util.ToastUtil;

public class LoginActivity extends AppCompatActivity {
    Context context;
    @BindView(R.id.userName)
    EditText name;
    @BindView(R.id.passwd)
    EditText passwd;
    IModel model;

    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        context = this;
        model= TestUtil.getData();
        progressDialog=new ProgressDialog(context);
    }

    public void onLogin(View view) {
        progressDialog.show();
        model.Login(this, name.getText().toString(), passwd.getText().toString(), new OkHttpUtils.OnCompleteListener<Result>() {
            @Override
            public void onSuccess(Result result) {
                progressDialog.dismiss();
                if (result != null && result.getRetCode() == I.RESULT.SUCCESS) {
                    SpUtil.saveLoginUser(context,name.getText().toString());
                    Intent intent = new Intent(context, MainActivity.class);
                    MyApplication.getInstance().setUserName(name.getText().toString());
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "密码或者用户名错误!" + result.getRetData(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String error) {
                progressDialog.dismiss();
                ToastUtil.showNetWorkBad(context);
            }
        });
    }
}
