package mac.yk.devicemanagement.util;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

/**
 * Created by mac-yk on 2017/3/17.
 */

public class ToastUtil {
    public static void showNetWorkBad(Context context){
        Toast.makeText(context, "网络不给力！", Toast.LENGTH_SHORT).show();
    }
    public static void showToast(Context context,String s){
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }
    public static void showcannotControl(Context context){
        Toast.makeText(context, "不能执行当前操作！", Toast.LENGTH_SHORT).show();
    }

    public static void showControlSuccess(Activity context) {
        Toast.makeText(context, "操作成功！", Toast.LENGTH_SHORT).show();
    }

    public static void showNoMore(Context context) {
        Toast.makeText(context, "没有更多数据", Toast.LENGTH_SHORT).show();
    }

    public static void showException(Context context){
        Toast.makeText(context, "出现异常", Toast.LENGTH_SHORT).show();
    }
}
