package mac.yk.devicemanagement.application;

import android.app.Activity;

import java.util.ArrayList;

import mac.yk.devicemanagement.bean.Status;
import mac.yk.devicemanagement.bean.User;
import mac.yk.devicemanagement.observable.Update;
import rx.Subscription;

/**
 * Created by mac-yk on 2017/3/1.
 */

public class MyMemory {
    private static MyMemory instance;
    public static MyMemory getInstance(){
        if (instance==null){
            instance=new MyMemory();
        }
        return instance;
    }
    private   boolean isBattery;

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
        isBattery=b;
    }
    public  boolean getFlag(){
        return isBattery;
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

    private Subscription subscribe;

    public Subscription getSubscribe() {
        return subscribe;
    }

    public void setSubscribe(Subscription subscribe) {
        this.subscribe = subscribe;
    }



    private boolean check;

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    private Update update;

    public Update getUpdate() {
        return update;
    }

    public void setUpdate(Update update) {
        this.update = update;
    }

    private int visible_status;

    public int getVisible_status() {
        return visible_status;
    }

    public void setVisible_status(int visible_status) {
        this.visible_status = visible_status;
    }
}
