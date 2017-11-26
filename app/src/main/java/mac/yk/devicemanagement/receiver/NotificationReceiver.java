package mac.yk.devicemanagement.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import mac.yk.devicemanagement.application.MyMemory;
import mac.yk.devicemanagement.observable.Update;
import mac.yk.devicemanagement.ui.activity.MainActivity;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by mac-yk on 2017/6/9.
 */

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Intent attachmentIntent = new Intent(context, MainActivity.class);
        switch (MyMemory.getInstance().getVisible_status()) {
                //进入到activity
            case MainActivity.OTHER:
                long nid = intent.getLongExtra("nid", 0);
                attachmentIntent.putExtra("nid", nid);
                attachmentIntent.putExtra("edit", false);
                attachmentIntent.putExtra("fromNtf", true);
                break;
                //直接关闭ntf就好了
            case MainActivity.ATTACHMENT_VISIBLE:
                attachmentIntent = null;
                break;
            case MainActivity.ATTACHMENT_INVISIBLE:
                //该属性由后台切入前台时使用
                attachmentIntent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                break;
                //切换至前台后切换item
            case MainActivity.NOTICE_INVISIBLE:
                attachmentIntent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                MyMemory.getInstance().getUpdate().setType(Update.updateItem);
                break;
                //使用观察者，让它切换item
            case MainActivity.NOTICE_VISIBLE:
                attachmentIntent=null;
                MyMemory.getInstance().getUpdate().setType(Update.updateItem);
                break;
        }
        if (attachmentIntent != null) {
            //由于Receiver是没有activity类型的context的，所以新开一个task来存Activity
            attachmentIntent.addFlags(FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(attachmentIntent);
        }

    }
}
