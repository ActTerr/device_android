package mac.yk.devicemanagement.controller.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.bean.Result;
import mac.yk.devicemanagement.model.IModel;
import mac.yk.devicemanagement.model.Model;
import mac.yk.devicemanagement.util.OkHttpUtils;

public class tongjiActivity extends AppCompatActivity {
    TextView textView;
    IModel model=new Model();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tongji);
         textView= (TextView) findViewById(R.id.text);
        getMessage();
    }

    private void getMessage() {
        model.getTongji(this,new OkHttpUtils.OnCompleteListener<Result>() {
            @Override
            public void onSuccess(Result result) {
                if (result!=null&&result.getRetCode()==0){
                    textView.setText((CharSequence) result.getRetData());
                }else {
                    textView.setText("请求失败，请刷新重试");
                }
            }

            @Override
            public void onError(String error) {
                textView.setText("请求失败，请刷新重试");
            }
        });
    }

    public void refresh(View v){
        getMessage();
    }
}
