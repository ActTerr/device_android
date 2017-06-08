package mac.yk.devicemanagement.service.check;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import java.util.ArrayList;
import java.util.Date;

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

public class MonitorService extends Service {
    Context context;
    String TAG = "MonitorService";


    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
//                    startGuard();
                    break;

                default:
                    break;
            }

        };
    };

    /**
     * 使用aidl 启动守护Service
     */
//    private StrongService startGuard = new StrongService.Stub() {
//        @Override
//        public void stopService() throws RemoteException {
//            Intent i = new Intent(getBaseContext(), MonitorService.class);
//            getBaseContext().stopService(i);
//        }
//
//        @Override
//        public void startService() throws RemoteException {
//            Intent i = new Intent(getBaseContext(), MonitorService.class);
//            getBaseContext().startService(i);
//        }
//    };

    /**
     * 在内存紧张的时候，系统回收内存时，会回调OnTrimMemory， 重写onTrimMemory当系统清理内存时从新启动守护service
     */
    @Override
    public void onTrimMemory(int level) {
        /*
         * 启动service2
         */
//        startGuard();

    }

    @Override
    public void onCreate() {

//        startGuard();
//        /*
//         * 此线程用监听守护Service的状态
//         */
//        new Thread() {
//            public void run() {
//                while (true) {
//                    boolean isRun = MonitorUtil.isServiceWork(MonitorService.this,
//                            "mac.yk.devicemanagement.service.check.GuardService)");
//                    if (!isRun) {
//                        Message msg = Message.obtain();
//                        msg.what = 1;
//                        handler.sendMessage(msg);
//                    }
//                    try {
//                        Thread.sleep(1);
//                    } catch (InterruptedException e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }
//                }
//            };
//        }.start();
    }

    /**
     * 判断守护Service是否还在运行，如果不是则启动守护Service
     */
//    private void startGuard() {
//        boolean isRun = MonitorUtil.isServiceWork(MonitorService.this,
//                "mac.yk.devicemanagement.service.check.GuardService");
//        if (isRun == false) {
//            try {
//                startGuard.startService();
//            } catch (RemoteException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        check(intent);

        return START_STICKY;
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    private void check(Intent intent){

        boolean alarm=intent.getBooleanExtra("alarm",false);
        L.e(TAG, "get intent and start");
        context = this;
        final ApiWrapper<ServerAPI> wrapper = new ApiWrapper<>();
        String user = mac.yk.devicemanagement.util.SpUtil.getLoginUser(getApplicationContext());
        long hour=  60*60 * 1000;
        long currentTime=System.currentTimeMillis();
        Date date=new Date(currentTime);
        boolean nightMode= SpUtil.getNightMode(context);
        boolean rest=false;
        if (nightMode){
            if (date.getHours()>22||date.getHours()<8){
                rest=true;
            }
        }
        int count= (int) (currentTime/hour) +1;
        long futureTime= count*hour;

        if (!user.equals("")&&!rest&&alarm) {
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
                                        .setSmallIcon(R.drawable.warning)
                                        .setContentIntent(pendingIntent)
                                        .build();
                                notification.flags |= Notification.FLAG_AUTO_CANCEL;
                                NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                mNotificationManager.notify(0, notification);

                            }

                        }
                    });

        }

        Intent i = new Intent(this, MyReceiver.class);
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        i.putExtra("alarm",true);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        long executeTime;
        if (alarm){
            executeTime=System.currentTimeMillis()+(hour/120);

        }else {
            executeTime=futureTime;
        }
        manager.set(AlarmManager.RTC_WAKEUP, executeTime, pi);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        L.e(TAG, "service Destroy");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
