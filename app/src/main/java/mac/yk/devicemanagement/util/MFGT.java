package mac.yk.devicemanagement.util;

import android.content.Context;
import android.content.Intent;

import mac.yk.devicemanagement.bean.Device;
import mac.yk.devicemanagement.controller.activity.SaveActivity;
import mac.yk.devicemanagement.controller.activity.RecordActivity;

/**
 * Created by mac-yk on 2017/3/2.
 */

public class MFGT {
    public static void gotoDetailActivity(Context context, Device device){
        Intent intent=new Intent(context,SaveActivity.class);
        intent.putExtra("device",device);
        context.startActivity(intent);
    }
    public static void gotoSaveActivity(Context context,int id){
        Intent intent=new Intent(context,SaveActivity.class);
        intent.putExtra("id",id);
        context.startActivity(intent);
    }
    public static void gotoMessageActivity(Context context,int id){
        Intent intent=new Intent(context,RecordActivity.class);
        intent.putExtra("id",id);
        context.startActivity(intent);
    }
}
