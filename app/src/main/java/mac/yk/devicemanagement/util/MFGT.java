package mac.yk.devicemanagement.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.wujay.fund.GestureEditActivity;
import com.wujay.fund.GestureVerifyActivity;

import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.bean.DeviceOld;
import mac.yk.devicemanagement.ui.activity.DetailActivity;
import mac.yk.devicemanagement.ui.activity.GestureActivity;
import mac.yk.devicemanagement.ui.activity.LoginActivity;
import mac.yk.devicemanagement.ui.activity.MainActivity;
import mac.yk.devicemanagement.ui.activity.RecordActivity;
import mac.yk.devicemanagement.ui.activity.SaveActivity;
import mac.yk.devicemanagement.ui.activity.SetActivity;

/**
 * Created by mac-yk on 2017/3/2.
 */

public class MFGT {
    public static void gotoDetailActivity(Context context, DeviceOld deviceOld){
        Intent intent=new Intent(context,DetailActivity.class);
        intent.putExtra("deviceOld", deviceOld);
        L.e("main", deviceOld.toString());
        startActivity(context,intent);
    }
    public static void gotoSaveActivity(Context context,String id){
        Intent intent=new Intent(context,SaveActivity.class);
        intent.putExtra("id",id);
        startActivity(context,intent);
    }
    public static void startActivity(Context context,Intent intent){
        context.startActivity(intent);
        ((Activity)context).overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
    }
    public static void gotoLoginActivity(Context context) {
        Intent intent=new Intent(context, LoginActivity.class);
        startActivity(context,intent);
    }

    public static void gotoRecordActivity(Context context,DeviceOld deviceOld){
        Intent intent=new Intent(context,RecordActivity.class);
        intent.putExtra("deviceOld", deviceOld);
        startActivity(context,intent);
    }
    public static void gotoSetActivity(Context context){
        Intent intent=new Intent(context, SetActivity.class);
        startActivity(context,intent);
    }

    public static void gotoGestureActivity(Context context){
        Intent intent=new Intent(context, GestureActivity.class);
        startActivity(context,intent);
    }
    public static void gotoSetGestureActivity(Context context){
        Intent intent=new Intent(context,GestureEditActivity.class);
        startActivity(context,intent);
    }
    public static void gotoValidateGestureActivity(Activity context){
        Intent intent=new Intent(context,GestureVerifyActivity.class);
        context.startActivityForResult(intent,0);
    }
    public static void gotoMainActivity(Context context){
        Intent intent=new Intent(context, MainActivity.class);
        startActivity(context,intent);
    }
    public static void finish(Activity activity){
        activity.finish();
        activity.overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
    }
}
