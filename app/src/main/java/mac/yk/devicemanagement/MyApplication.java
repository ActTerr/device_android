package mac.yk.devicemanagement;

import android.app.Application;
import android.content.Intent;
import android.os.Environment;

import com.squareup.leakcanary.RefWatcher;

import java.io.File;

import mac.yk.devicemanagement.service.BatteryService;
import mac.yk.devicemanagement.util.L;

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
        startCheck();
    }

    private void startCheck() {
        Intent intent=new Intent(this, BatteryService.class);
        startService(intent);
    }

    private void mkdir() {
      File file=new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Attachment/");
        if (!file.exists()){
            file.mkdir();
        }
    }

    private RefWatcher refWatcher;


}
