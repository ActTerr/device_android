package mac.yk.devicemanagement.util;

import android.content.Context;
import android.content.Intent;

import mac.yk.devicemanagement.bean.Device;
import mac.yk.devicemanagement.controller.activity.DetailActivity;
import mac.yk.devicemanagement.controller.activity.GestureActivity;
import mac.yk.devicemanagement.controller.activity.LoginActivity;
import mac.yk.devicemanagement.controller.activity.RecordActivity;
import mac.yk.devicemanagement.controller.activity.SaveActivity;
import mac.yk.devicemanagement.controller.activity.SetActivity;

/**
 * Created by mac-yk on 2017/3/2.
 */

public class MFGT {
    public static void gotoDetailActivity(Context context, Device device){
        Intent intent=new Intent(context,DetailActivity.class);
        intent.putExtra("device",device);
        L.e("main",device.toString());
        context.startActivity(intent);
    }
    public static void gotoSaveActivity(Context context,String id){
        Intent intent=new Intent(context,SaveActivity.class);
        intent.putExtra("id",id);
        context.startActivity(intent);
    }

    public static void gotoLoginActivity(Context context) {
        Intent intent=new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    public static void gotoRecordActivity(Context context,String id){
        Intent intent=new Intent(context,RecordActivity.class);
        intent.putExtra("id",id);
        context.startActivity(intent);
    }
    public static void gotoSetActivity(Context context){
        Intent intent=new Intent(context, SetActivity.class);
        context.startActivity(intent);
    }

    public static void gotoGestureActivity(Context context){
        Intent intent=new Intent(context, GestureActivity.class);
        context.startActivity(intent);
    }

}
