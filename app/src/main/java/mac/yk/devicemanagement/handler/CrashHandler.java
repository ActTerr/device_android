package mac.yk.devicemanagement.handler;

import android.content.Context;

/**
 * Created by mac-yk on 2017/3/20.
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler {
    Thread.UncaughtExceptionHandler handler;

    final static CrashHandler INSTANCE=new CrashHandler();

    private Context mContext;

    public static CrashHandler getInstance(){
        return INSTANCE;
    }

    public CrashHandler() {
    }

    public void init(Context context){
        mContext=context;
        handler=Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }
    @Override
    public void uncaughtException(Thread t, Throwable e) {

    }
}
