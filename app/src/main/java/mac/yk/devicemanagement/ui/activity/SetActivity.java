package mac.yk.devicemanagement.ui.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mac.yk.devicemanagement.I;
import mac.yk.devicemanagement.MyApplication;
import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.bean.Result;
import mac.yk.devicemanagement.model.IModel;
import mac.yk.devicemanagement.util.MFGT;
import mac.yk.devicemanagement.util.OkHttpUtils;
import mac.yk.devicemanagement.util.SpUtil;
import mac.yk.devicemanagement.util.TestUtil;

public class SetActivity extends AppCompatActivity {

    @BindView(R.id.user)
    TextView user;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.lock)
    ImageView lock;
    @BindView(R.id.shoushiText)
    TextView shoushiText;
    @BindView(R.id.right_btn)
    ImageView rightBtn;

    Context context;
    ProgressDialog pd;
//    @BindView(R.id.toolBar)
//    Toolbar toolBar;
    IModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        ButterKnife.bind(this);
        context = this;
        pd = new ProgressDialog(context);
        model= TestUtil.getData();
//        setSupportActionBar(toolBar);
//        ActionBar ab=getSupportActionBar();
//        ab.setDisplayHomeAsUpEnabled(true);
    }

    @OnClick({R.id.rlUser, R.id.rlName, R.id.rlPasswd, R.id.logOut})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rlUser:
                Toast.makeText(this, "账号不可修改", Toast.LENGTH_SHORT).show();
                break;
            case R.id.rlName:
                Toast.makeText(this, "姓名不可修改", Toast.LENGTH_SHORT).show();
                break;
            case R.id.rlPasswd:
                MFGT.gotoGestureActivity(context);
                break;
            case R.id.logOut:
                pd.show();
                model.LogOut(context, MyApplication.getInstance().getUserName(), new OkHttpUtils.OnCompleteListener<Result>() {
                    @Override
                    public void onSuccess(Result result) {
                        pd.dismiss();
                        if (result != null && result.getRetCode() == I.RESULT.SUCCESS) {
                            MyApplication.getInstance().setUserName(null);
                            SpUtil.saveLoginUser(context, null);
                            MFGT.gotoLoginActivity(context);
                            finish();
                        } else {
                            Toast.makeText(context, "退出失败，请重试", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(String error) {
                        pd.dismiss();
                        Toast.makeText(context, "检查网络状况", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
        }
    }
}
