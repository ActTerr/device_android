package mac.yk.devicemanagement.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.wujay.fund.GestureEditActivity;
import com.wujay.fund.GestureVerifyActivity;

import mac.yk.devicemanagement.bean.Device;
import mac.yk.devicemanagement.ui.activity.DetailActivity;
import mac.yk.devicemanagement.ui.activity.GestureActivity;
import mac.yk.devicemanagement.ui.activity.LoginActivity;
import mac.yk.devicemanagement.ui.activity.RecordActivity;
import mac.yk.devicemanagement.ui.activity.SaveActivity;
import mac.yk.devicemanagement.ui.activity.SetActivity;

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
    public static void gotoSetGestureActivity(Context context){
        Intent intent=new Intent(context,GestureEditActivity.class);
        context.startActivity(intent);
    }
    public static void gotoValidateGestureActivity(Activity context){
        Intent intent=new Intent(context,GestureVerifyActivity.class);
        context.startActivityForResult(intent,0);
    }
}
