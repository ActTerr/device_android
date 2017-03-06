package mac.yk.devicemanagement.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by mac-yk on 2017/3/6.
 */

public class SpUtil {
    public static String rmLoginUser(Context context){
        return getDefault(context).getString("name","");
    }

    public static void saveLoginUser(Context context,String name){
        getDefault(context).edit().putString("name",name).apply();
    }

    private static SharedPreferences getDefault(Context context){
       return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static boolean getFlag(Context context){
        return getDefault(context).getBoolean("flag",false);
    }
    public static void setFlag(Context context,boolean flag){
        getDefault(context).edit().putBoolean("flag",flag).apply();
    }

}
