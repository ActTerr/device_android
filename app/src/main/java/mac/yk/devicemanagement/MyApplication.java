package mac.yk.devicemanagement;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.RefWatcher;

import java.util.ArrayList;

import mac.yk.devicemanagement.bean.Status;
import mac.yk.devicemanagement.bean.User;

/**
 * Created by mac-yk on 2017/3/1.
 */

public class MyApplication extends Application {
    public static Context context;
    public  static MyApplication instance=new MyApplication();
    public static MyApplication getInstance(){
        return instance;
    }
    private   boolean isDianchi;

    private RefWatcher refWatcher;
    @Override
    public void onCreate() {
        super.onCreate();
        context=this;
//        refWatcher= LeakCanary.install(this);
//        CrashHandler.getInstance().init(getApplicationContext());
    }

    public static Context getAppContext(){
        return context;
    }

    private Status status;

    public  Status getStatus() {
        return status;
    }

    public  void setStatus(Status s) {
        status = s;
    }

    private User user;

    private String[] data;

    public String[] getData() {
        return data;
    }

    public void setData(String[] data) {
        this.data = data;
    }

    public User getUser () {
        return user;
    }

    public void setUser(User user) {
        this.user=user;
    }


    public  void setFlag(boolean b) {
        isDianchi=b;
    }
    public  boolean getFlag(){
        return isDianchi;
    }

    static ArrayList<Activity> activities=new ArrayList<>();
    public  void addActivity(Activity activity){
        activities.add(activity);
    }
    public  void rmActivity(Activity activity){
        activities.remove(activity);
    }

    public  ArrayList<Activity> getActivities() {
        return activities;
    }

}
