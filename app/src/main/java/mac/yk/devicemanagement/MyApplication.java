package mac.yk.devicemanagement;

import android.app.Application;

/**
 * Created by mac-yk on 2017/3/1.
 */

public class MyApplication extends Application {
    public static MyApplication instance=new MyApplication();
    public static MyApplication getInstance(){
        return instance;
    }
}
