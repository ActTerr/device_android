package mac.yk.devicemanagement;

import android.app.Application;

import mac.yk.devicemanagement.bean.Device;

/**
 * Created by mac-yk on 2017/3/1.
 */

public class MyApplication extends Application {
    public static MyApplication instance=new MyApplication();
    public static MyApplication getInstance(){
        return instance;
    }
    private   Device device=new Device();
    private   Device getDevice(){
        return  device;
    }
    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
