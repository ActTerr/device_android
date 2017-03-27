package mac.yk.devicemanagement.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import mac.yk.devicemanagement.MyApplication;
import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.Receiver.NetBroadcastReceiver;
import mac.yk.devicemanagement.util.NetUtil;
import rx.Subscription;

/**
 * Created by mac-yk on 2017/3/16.
 */
public class BaseActivity extends AppCompatActivity implements Observer,NetBroadcastReceiver.NetEvevt{
    protected Subscription subscription;


    TextView mTv;
    protected void unsubscribe() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        evevt = this;
        inspectNet();
        forceShowOverflowMenu();
    }
    private void forceShowOverflowMenu() {
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class
                    .getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
        @Override
    public void update(Observable o, Object arg) {

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        evevt=null;
        unsubscribe();

    }
    public static NetBroadcastReceiver.NetEvevt evevt;
    /**
     * 网络类型
     */
    private int netMobile;

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.addActivity(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MyApplication.rmActivity(this);
    }

    /**
     * 初始化时判断有没有网络
     */

    public void inspectNet() {
        this.netMobile = NetUtil.getNetWorkState(BaseActivity.this);

//        return isNetConnect();

        // if (netMobile == NetUtil.NETWORK_WIFI) {
        // System.out.println("inspectNet：连接wifi");
        // } else if (netMobile == NetUtil.NETWORK_MOBILE) {
        // System.out.println("inspectNet:连接移动数据");
        // } else if (netMobile == NetUtil.NETWORK_NONE) {
        // System.out.println("inspectNet:当前没有网络");
        //
        // }
    }

    /**
     * 网络变化之后的类型
     */
    @Override
    public void onNetChange(int netMobile) {
        // TODO Auto-generated method stub
        this.netMobile = netMobile;
       ArrayList<Activity> activities=MyApplication.getActivities();
        Activity activity=activities.get(activities.size()-1);
        mTv= (TextView) activity.findViewById(R.id.netView);
        if (netMobile== NetUtil.NETWORK_NONE){
            mTv.setVisibility(View.VISIBLE);
        }else {
            mTv.setVisibility(View.GONE);
        }
//        isNetConnect();

    }

    /**
     * 判断有无网络 。
     *
     * @return true 有网, false 没有网络.
     */
    public boolean isNetConnect() {
        if (netMobile == NetUtil.NETWORK_WIFI) {
            return true;
        } else if (netMobile == NetUtil.NETWORK_MOBILE) {
            return true;
        } else if (netMobile == NetUtil.NETWORK_NONE) {
            return false;

        }
        return false;
    }




}
