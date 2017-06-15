package mac.yk.devicemanagement.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import mac.yk.devicemanagement.service.check.MonitorService;
import mac.yk.devicemanagement.util.schedulers.MonitorUtil;

/**
 * Created by mac-yk on 2017/5/19.
 */

public class MyReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean isRun = MonitorUtil.isServiceWork(context,
                "mac.yk.devicemanagement.service.check.MonitorService");
        if (isRun == false) {
            boolean alarm = intent.getBooleanExtra("alarm", false);
            Intent intent1 = new Intent(context, MonitorService.class);
            intent1.putExtra("alarm", alarm);
            if (intent.getAction() != null) {
                intent1.setAction("check");
            }
            context.startService(intent1);
        }
    }
}