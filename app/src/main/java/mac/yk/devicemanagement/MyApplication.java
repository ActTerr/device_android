package mac.yk.devicemanagement;

import android.app.Application;
import android.content.Context;

import mac.yk.devicemanagement.bean.Device;

/**
 * Created by mac-yk on 2017/3/1.
 */

public class MyApplication extends Application {
    public static Context context;
    public static MyApplication instance=new MyApplication();
    public static MyApplication getInstance(){
        return instance;
    }

    public static Device getDevice() {
        if (device==null){
            device=new Device();
        }
        return device;
    }

    public static void setDevice(Device device) {
        MyApplication.device = device;
    }

    private static Device device;
//    static ArrayList<Observer> observers;
//    public static void addObserver(Fragment fragment){
//        Observer observer= (Observer) fragment;
//        observers.add(observer);
//        device.addObserver(observer);
//    }



    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    public static Context getContext(){
        context=getContext();
        return context;
    }
}
