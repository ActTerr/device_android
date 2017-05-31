package mac.yk.devicemanagement.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by mac-yk on 2017/3/6.
 */

public class SpUtil {
    public static String getLoginUser(Context context){
        return getDefault(context).getString("name","");
    }

    public static void saveLoginUser(Context context,String name){
        getDefault(context).edit().putString("name",name).apply();
    }

    private static SharedPreferences getDefault(Context context){
       return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static boolean getGesture(Context context){
        return getDefault(context).getBoolean("flag",false);
    }
    public static void setGesture(Context context,boolean flag){
        getDefault(context).edit().putBoolean("flag",flag).apply();
    }
    public static void savePrompt(Context context,boolean prompt){
        getDefault(context).edit().putBoolean("prompt",prompt).apply();
    }
    public static boolean getPrompt(Context context){
        return getDefault(context).getBoolean("prompt",false);
    }

    public static void setGestureType(Context context,int type){
         getDefault(context).edit().putInt("type",type).apply();
    }
    public static int getGestureType(Context context){
        return getDefault(context).getInt("type",0);
    }

    public static void setNightMode(Context context,boolean check){
        getDefault(context).edit().putBoolean("check",check).apply();
    }

    public static boolean getNightMode(Context context){
        return getDefault(context).getBoolean("check",false);
    }
}
