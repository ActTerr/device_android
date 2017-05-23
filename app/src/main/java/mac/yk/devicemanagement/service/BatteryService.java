package mac.yk.devicemanagement.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import java.util.ArrayList;

import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.bean.Battery;
import mac.yk.devicemanagement.bean.User;
import mac.yk.devicemanagement.db.dbUser;
import mac.yk.devicemanagement.net.ApiWrapper;
import mac.yk.devicemanagement.net.ServerAPI;
import mac.yk.devicemanagement.receiver.MyReceiver;
import mac.yk.devicemanagement.ui.activity.BatteryListActivity;
import mac.yk.devicemanagement.util.ExceptionFilter;
import mac.yk.devicemanagement.util.L;
import mac.yk.devicemanagement.util.SpUtil;
import mac.yk.devicemanagement.util.ToastUtil;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by mac-yk on 2017/5/18.
 */

public class BatteryService extends IntentService {
    Context context;
    String TAG = "BatteryService";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public BatteryService() {
        super("");

    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        L.e(TAG, "get intent and start");
        context = getApplicationContext();
        final ApiWrapper<ServerAPI> wrapper = new ApiWrapper<>();
        String user = mac.yk.devicemanagement.util.SpUtil.getLoginUser(getApplicationContext());

        if (!user.equals("")&& SpUtil.getCheck(context)) {
            final User user1 = dbUser.getInstance(getApplicationContext()).select2(user);

            wrapper.targetClass(ServerAPI.class).getAPI().checkBattery(String.valueOf(user1.getUnit()))
                    .compose(wrapper.<ArrayList<Battery>>applySchedulers())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ArrayList<Battery>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            if (ExceptionFilter.filter(context, e)) {
                                ToastUtil.showToast(getApplicationContext(), "异常");
                            }
                        }

                        @Override
                        public void onNext(ArrayList<Battery> batteries) {
                            L.e(TAG, batteries.size() + "");
                            if (batteries.size() > 0) {
                                Intent intent = new Intent(context, BatteryListActivity.class);
                                intent.putExtra("data", batteries);
                                PendingIntent pendingIntent = PendingIntent.getActivity(
                                        context, 0, intent,
                                        PendingIntent.FLAG_CANCEL_CURRENT);
                                Notification notification = new NotificationCompat.Builder(context)
                                        .setContentTitle("电池超时提醒！")
                                        .setContentText("有" + batteries.size() + "个电池已超时，快去充电！")
                                        .setSmallIcon(R.drawable.yujing)
                                        .setContentIntent(pendingIntent)
                                        .build();
                                notification.flags |= Notification.FLAG_AUTO_CANCEL;
                                NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                mNotificationManager.notify(0, notification);

                            }

                        }
                    });
            AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
            int hour= 60 * 60 * 1000;
            long triggerAtTime = SystemClock.elapsedRealtime() + hour;
            Intent i = new Intent(this, MyReceiver.class);
            PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
            manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);

        }



    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        L.e(TAG, "service Destroy");
    }
}
