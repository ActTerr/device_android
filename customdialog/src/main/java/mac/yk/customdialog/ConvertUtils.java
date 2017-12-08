package mac.yk.customdialog;

import android.content.Context;

public class ConvertUtils {


    public static int px2dp(Context context, int px) {
        int density = (int) context.getResources().getDisplayMetrics().density;
        return px / density;
    }

    public static int dp2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }




}