package mac.yk.devicemanagement.uncaught;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import mac.yk.devicemanagement.ui.activity.MainActivity;
import mac.yk.devicemanagement.util.CommonUtils;
import mac.yk.devicemanagement.util.ToastUtil;


/**
 * Created by mac-yk on 2017/3/20.
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler {
    Thread.UncaughtExceptionHandler systemHandler;
    IunCaught upload;
    final static CrashHandler INSTANCE=new CrashHandler();

    private Context mContext;

    public static CrashHandler getInstance(){
        return INSTANCE;
    }

    public CrashHandler() {
    }

    public void init(Context context){
        mContext=context;
        systemHandler=Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
        upload=new unCaught(context);
    }
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        if (!sendException(e) && systemHandler != null){
            systemHandler.uncaughtException(t,e);
        }else {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            Intent intent = new Intent(mContext, MainActivity.class);
            PendingIntent restartIntent = PendingIntent.getActivity(
                    mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            //退出程序
            AlarmManager mgr = (AlarmManager)mContext.getSystemService(Context.ALARM_SERVICE);
            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 300,
                    restartIntent); // 1秒钟后重启应用
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }

    private boolean sendException(final Throwable e){
        if (e==null) {
            return false;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                ToastUtil.showToast(mContext,"非常抱歉，程序即将崩溃!");
                Looper.loop();
            }
        }).start();
        File file = new File(mContext.getCacheDir().getAbsolutePath() + "/err/");
        if (!file.exists()){
            file.mkdir();
        }
        try {
            PrintWriter writer = new PrintWriter(mContext.getCacheDir().getAbsolutePath() + "/err/" +
                    System.currentTimeMillis() + ".log");
            e.printStackTrace(writer);
            writer.close();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                CommonUtils.sendTextMail("errlog from " + CommonUtils.getUniquePsuedoID() ,
//                        CommonUtils.getDeviceInfo() + Log.getStackTraceString(e));
//            }
//        }).start();
        File file2=upload.UploadCrashInfo(mContext,e);
            upload.postServer(mContext,file2);
        return true;
    }


    private boolean handleException(final Throwable e){
        if (e==null) {
            return false;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                ToastUtil.showToast(mContext,"非常抱歉，程序即将崩溃!");
                Looper.loop();
            }
        }).start();
        File file = new File(mContext.getCacheDir().getAbsolutePath() + "/err/");
        if (!file.exists()){
            file.mkdir();
        }
        try {
            PrintWriter writer = new PrintWriter(mContext.getCacheDir().getAbsolutePath() + "/err/" +
                    System.currentTimeMillis() + ".log");
            e.printStackTrace(writer);
            writer.close();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                CommonUtils.sendTextMail("errlog from " + CommonUtils.getUniquePsuedoID() ,
                        CommonUtils.getDeviceInfo() + Log.getStackTraceString(e));
            }
        }).start();
        return true;
    }


}
