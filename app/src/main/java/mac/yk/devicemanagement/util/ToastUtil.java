package mac.yk.devicemanagement.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by mac-yk on 2017/3/17.
 */

public class ToastUtil {
    public static void showNetWorkBad(Context context){
        Toast.makeText(context, "网络不给力！", Toast.LENGTH_SHORT).show();
    }
}
