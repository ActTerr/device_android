package com.wujay.fund.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by mac-yk on 2017/3/16.
 */

public class SpUtil {
    private static SharedPreferences getDefault(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
    public static String getGesPasswd(Context context){
        return getDefault(context).getString("Gpasswd","");
    }
    public static void setGesPasswd(Context context,String passwd){
        getDefault(context).edit().putString("Gpasswd",passwd).apply();
    }
}
