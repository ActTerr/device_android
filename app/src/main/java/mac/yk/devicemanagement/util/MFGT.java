package mac.yk.devicemanagement.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.wujay.fund.GestureEditActivity;
import com.wujay.fund.GestureVerifyActivity;

import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.bean.Notice;
import mac.yk.devicemanagement.down.NoticeDetailActivity;
import mac.yk.devicemanagement.ui.activity.DeviceDetailActivity;
import mac.yk.devicemanagement.ui.activity.DeviceListActivity;
import mac.yk.devicemanagement.ui.activity.GestureActivity;
import mac.yk.devicemanagement.ui.activity.LoginActivity;
import mac.yk.devicemanagement.ui.activity.MainActivity;
import mac.yk.devicemanagement.ui.activity.RecordActivity;
import mac.yk.devicemanagement.ui.activity.SaveActivity;
import mac.yk.devicemanagement.ui.activity.UserActivity;

/**
 * Created by mac-yk on 2017/3/2.
 */

public class MFGT {
    public static void gotoDetailActivity(Context context, boolean isFromList,boolean isBack,String Did){
        Intent intent=new Intent(context,DeviceDetailActivity.class);
        intent.putExtra("isFromList",isFromList);
        intent.putExtra("isBack",isBack);
        intent.putExtra("Did",Did);
        startActivity(context,intent);
    }

    public static void gotoDeviceListActivity(Context context,String sType,int unit,String category,String status){
        Intent intent=new Intent(context,DeviceListActivity.class);
        intent.putExtra("unit",unit);
        intent.putExtra("category",category);
        intent.putExtra("status",status);
        intent.putExtra("gsType",sType);
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

    public static void gotoRecordActivity(Context context,String did){
        Intent intent=new Intent(context,RecordActivity.class);
        intent.putExtra("Did",did);
        startActivity(context,intent);
    }
    public static void gotoSetActivity(Context context){
        Intent intent=new Intent(context, UserActivity.class);
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
    public static void gotoNoticeDetail(Context context, boolean isEdit, Notice notice){
        Intent intent=new Intent(context, NoticeDetailActivity.class);
        intent.putExtra("isEdit",isEdit);
        intent.putExtra("notice",notice);
        startActivity(context,intent);
    }
}
