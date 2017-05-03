package mac.yk.devicemanagement;

import android.app.Activity;
import android.app.Application;

import com.squareup.leakcanary.RefWatcher;

import java.util.ArrayList;

import mac.yk.devicemanagement.bean.DeviceOld;

/**
 * Created by mac-yk on 2017/3/1.
 */

public class MyApplication extends Application {
    public static MyApplication instance=new MyApplication();
    public static MyApplication getInstance(){
        return instance;
    }
    public static boolean isDianchi;

    private RefWatcher refWatcher;
    @Override
    public void onCreate() {
        super.onCreate();
//        refWatcher= LeakCanary.install(this);
//        CrashHandler.getInstance().init(getApplicationContext());
    }

    public static DeviceOld getDeviceOld() {
        return deviceOld;
    }

    public static void setDeviceOld(DeviceOld deviceOld) {
        MyApplication.deviceOld = deviceOld;
    }

    private static DeviceOld deviceOld =new DeviceOld();
//    static ArrayList<Observer> observers;
//    public static void addObserver(Fragment fragment){
//        Observer observer= (Observer) fragment;
//        observers.add(observer);
//        deviceOld.addObserver(observer);
//    }



    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public static void setFlag(boolean b) {
        isDianchi=b;
    }
    public static boolean getFlag(){
        return isDianchi;
    }

    static ArrayList<Activity> activities=new ArrayList<>();
    public static void addActivity(Activity activity){
        activities.add(activity);
    }
    public static void rmActivity(Activity activity){
        activities.remove(activity);
    }

    public static ArrayList<Activity> getActivities() {
        return activities;
    }

}
