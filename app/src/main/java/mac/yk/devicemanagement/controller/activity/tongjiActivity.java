package mac.yk.devicemanagement.controller.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.model.IModel;
import mac.yk.devicemanagement.model.Model;
import mac.yk.devicemanagement.util.OkHttpUtils;

public class tongjiActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tongji);
        IModel model=new Model();
        model.getYujing(new OkHttpUtils.OnCompleteListener<String>() {
            @Override
            public void onSuccess(String result) {

            }

            @Override
            public void onError(String error) {

            }
        });
    }
}
