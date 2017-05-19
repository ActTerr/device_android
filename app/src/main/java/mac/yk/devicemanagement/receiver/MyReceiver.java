package mac.yk.devicemanagement.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import mac.yk.devicemanagement.service.BatteryService;

/**
 * Created by mac-yk on 2017/5/19.
 */

public class MyReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intent1=new Intent(context, BatteryService.class);
        context.startService(intent1);
    }
}