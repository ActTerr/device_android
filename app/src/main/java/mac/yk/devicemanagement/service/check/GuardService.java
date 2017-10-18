package mac.yk.devicemanagement.service.check;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.widget.Toast;

import mac.yk.devicemanagement.StrongService;
import mac.yk.devicemanagement.util.L;
import mac.yk.devicemanagement.util.schedulers.MonitorUtil;

public class GuardService extends Service {
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    startMonitor();
                    break;

                default:
                    break;
            }

        };
    };

    /**
     * 使用aidl 启动MonitorService
     */
    private StrongService startMonitor = new StrongService.Stub() {
        @Override
        public void stopService() throws RemoteException {
            Intent i = new Intent(getBaseContext(), MonitorService.class);
            getBaseContext().stopService(i);
        }

        @Override
        public void startService() throws RemoteException {
            Intent i = new Intent(getBaseContext(), MonitorService.class);
            getBaseContext().startService(i);
        }
    };

    /**
     * 在内存紧张的时候，系统回收内存时，会回调OnTrimMemory， 重写onTrimMemory当系统清理内存时从新启动MonitorService
     */
    @Override
    public void onTrimMemory(int level) {
        /*
         * 启动service2
         */
        startMonitor();

    }

    @Override
    public void onCreate() {
        Toast.makeText(GuardService.this, "MonitorService 正在启动...", Toast.LENGTH_SHORT)
                .show();
        startMonitor();
        /*
         * 此线程用监听MonitorService的状态
         */
        new Thread() {
            public void run() {
                while (true) {
                    boolean isRun = MonitorUtil.isServiceWork(GuardService.this,
                            "mac.yk.devicemanagement.service.check.MonitorService");
                    if (!isRun) {
                        Message msg = Message.obtain();
                        msg.what = 1;
                        handler.sendMessage(msg);
                    }
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            };
        }.start();
    }

    /**
     * 判断MonitorService是否还在运行，如果不是则启动MonitorService
     */
    private void startMonitor() {
        boolean isRun = MonitorUtil.isServiceWork(GuardService.this,
                "mac.yk.devicemanagement.service.check.MonitorService");
        if (isRun == false) {
            try {
                startMonitor.startService();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return (IBinder) startMonitor;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        L.e("wodown","guard die");
    }
}