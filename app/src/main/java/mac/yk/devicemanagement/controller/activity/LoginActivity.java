package mac.yk.devicemanagement.controller.activity;

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
import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.bean.Result;
import mac.yk.devicemanagement.model.IModel;
import mac.yk.devicemanagement.util.OkHttpUtils;
import mac.yk.devicemanagement.util.SpUtil;
import mac.yk.devicemanagement.util.TestUtil;

public class LoginActivity extends AppCompatActivity {
    Context context;
    @BindView(R.id.userName)
    EditText name;
    @BindView(R.id.passwd)
    EditText passwd;
    IModel model;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        context = this;
        model= TestUtil.getData();
    }

    public void onLogin(View view) {
        model.Login(this, name.getText().toString(), passwd.getText().toString(), new OkHttpUtils.OnCompleteListener<Result>() {
            @Override
            public void onSuccess(Result result) {
                if (result != null && result.getRetCode() == I.RESULT.SUCCESS) {
                    SpUtil.saveLoginUser(context,name.getText().toString());
                    Intent intent = new Intent(context, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "登陆失败" + result.getRetData(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String error) {
                Toast.makeText(LoginActivity.this, "登陆失败，请重试", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
