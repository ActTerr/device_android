package mac.yk.devicemanagement.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import mac.yk.devicemanagement.service.BatteryService;
import mac.yk.devicemanagement.util.L;

/**
 * Created by mac-yk on 2017/5/19.
 */

public class MyReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "complete", Toast.LENGTH_SHORT).show();
        L.e("receiver","get action");
        Intent intent1=new Intent(context, BatteryService.class);
        context.startService(intent1);
    }
}