package mac.yk.devicemanagement.service.check;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.Timer;
import java.util.TimerTask;

import mac.yk.devicemanagement.ui.activity.MainActivity;

/**
 * Created by mac-yk on 2017/5/31.
 */

public class GuardService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        thread.start();
        return START_STICKY;
    }


    Thread thread = new Thread(new Runnable() {

        @Override
        public void run() {
            Timer timer = new Timer();
            TimerTask task = new TimerTask() {

                @Override
                public void run() {
                    boolean b = MainActivity.isServiceWorked(GuardService.this, "mac.yk.devicemanagement.service.check.BatteryService");
                    if(!b) {
                        Intent service = new Intent(GuardService.this, BatteryService.class);
                        startService(service);
                    }
                }
            };
            timer.schedule(task, 0, 1000);
        }
    });

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
