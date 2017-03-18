package mac.yk.devicemanagement.ui.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import mac.yk.devicemanagement.net.ServerAPI;
import mac.yk.devicemanagement.net.netWork;
import mac.yk.devicemanagement.util.SpUtil;
import mac.yk.devicemanagement.util.TestUtil;
import mac.yk.devicemanagement.util.ToastUtil;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class LoginActivity extends BaseActivity{
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

     Observer<Integer> observer=new Observer<Integer>() {
         @Override
         public void onCompleted() {

         }

         @Override
         public void onError(Throwable e) {
             progressDialog.dismiss();
             ToastUtil.showNetWorkBad(context);
         }

         @Override
         public void onNext(Integer s) {
             progressDialog.dismiss();
             if (s== I.RESULT.SUCCESS){
                 SpUtil.saveLoginUser(context,name.getText().toString());
                 Intent intent = new Intent(context, MainActivity.class);
                 MyApplication.getInstance().setUserName(name.getText().toString());
                 startActivity(intent);
                 finish();
             }else {
                 Toast.makeText(LoginActivity.this, "密码或者用户名错误!" , Toast.LENGTH_SHORT).show();
             }
         }
     };
    public void onLogin(View view) {
        progressDialog.show();
        netWork<ServerAPI> netWork=new netWork<>();
        subscription= netWork.targetClass(ServerAPI.class).getAPI().login(name.getText().toString(),
                passwd.getText().toString()).map(new Func1<Result,Integer>() {
            @Override
            public Integer call(Result result) {
                return result.getRetCode();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}
