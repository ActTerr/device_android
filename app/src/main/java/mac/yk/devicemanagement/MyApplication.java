package mac.yk.devicemanagement;

import android.app.Application;
import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.Observer;

import mac.yk.devicemanagement.bean.Device;

/**
 * Created by mac-yk on 2017/3/1.
 */

public class MyApplication extends Application {
    public static MyApplication instance=new MyApplication();
    public static MyApplication getInstance(){
        return instance;
    }

    public static Device getDevice() {
        return device;
    }

    public static void setDevice(Device device) {
        MyApplication.device = device;
    }

    private static Device device=new Device();
    static ArrayList<Observer> observers;
    public static void addObserver(Fragment fragment){
        Observer observer= (Observer) fragment;
        observers.add(observer);
        device.addObserver(observer);
    }



    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
