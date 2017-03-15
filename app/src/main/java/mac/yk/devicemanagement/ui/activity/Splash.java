package mac.yk.devicemanagement.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import mac.yk.devicemanagement.MyApplication;
import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.util.SpUtil;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        final Context context=this;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String name=SpUtil.getLoginUser(context);
                if (name.equals("")){
                    Intent intent=new Intent(context,LoginActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    MyApplication.getInstance().setUserName(name);
                    Intent intent=new Intent(context,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }).start();

    }
}
