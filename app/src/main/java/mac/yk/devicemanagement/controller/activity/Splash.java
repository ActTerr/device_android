package mac.yk.devicemanagement.controller.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import mac.yk.devicemanagement.MyApplication;
import mac.yk.devicemanagement.R;

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
                SharedPreferences sp=getSharedPreferences("user",MODE_PRIVATE);
                String name=sp.getString("name",null);
                if (name==null){
                    Intent intent=new Intent(context,LoginActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    MyApplication.getInstance().setUserName(name);
                    Intent intent=new Intent(context,MainActivity.class);
                    startActivity(intent);
                }
            }
        }).start();

    }
}
