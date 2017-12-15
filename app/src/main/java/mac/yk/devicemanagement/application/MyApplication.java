package mac.yk.devicemanagement.application;

import android.app.Application;
import android.os.Environment;

import com.squareup.leakcanary.RefWatcher;

import java.io.File;

import cn.jpush.android.api.JPushInterface;
import mac.yk.devicemanagement.bean.User;
import mac.yk.devicemanagement.db.dbUser;
import mac.yk.devicemanagement.util.L;
import mac.yk.devicemanagement.util.SpUtil;

/**
 * Created by mac-yk on 2017/5/14.
 */

public class MyApplication extends Application {
    private static MyApplication instance;

    public static MyApplication getInstance(){
        return instance;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
        mkdir();
        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时关闭日志
        JPushInterface.init(this);
        String name= SpUtil.getLoginUser(getApplicationContext());
        User user= dbUser.getInstance(getApplicationContext()).select2(name);
        MyMemory.getInstance().setUser(user);
        L.e("user",user.toString());

    }

    private void mkdir() {

      File file=new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Attachment/");
        if (!file.exists()){
            file.mkdir();
        }
    }

    private RefWatcher refWatcher;



}
