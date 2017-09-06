package mac.yk.devicemanagement.util;

import android.content.Context;

import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import mac.yk.devicemanagement.I;

public class ConvertUtils {
    public static <T> ArrayList<T> array2List(T[] array) {
        List<T> list = Arrays.asList(array);
        ArrayList<T> arrayList = new ArrayList<>(list);
        return arrayList;
    }

    public static int px2dp(Context context, int px) {
        int density = (int) context.getResources().getDisplayMetrics().density;
        return px / density;
    }

    public static int dp2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        L.e("main", "scale:" + scale);
        return (int) (dpValue * scale + 0.5f);
    }

    public static String getjson(Object object) {
        Gson gson = new Gson();
        String json = gson.toJson(object);

        return json;
    }

    public static Date String2Date(String s) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        try {
            return sdf.parse(s);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static String Date2String(Date date) {
        if (date!=null){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String s=sdf.format(date);
            return s;
        }else{
            return "";
        }
    }

    public static String getdevName(int devName) {
        switch (devName) {
            case I.DNAME.BATTERY:
                return "电池";
            case I.DNAME.TRANSCEIVER:
                return "电台";
            case I.DNAME.MACHINE_CONTROLLER:
                return "机控器";
            case I.DNAME.ZONE_CONTROLLER:
                return "区控器";
        }
        return null;
    }

    /**
     * 待完善
     *
     * @param devName
     * @return
     */
    public static int getdevName(String devName) {
        switch (devName) {
            case "电池":
                return I.DNAME.BATTERY;
            case "手持台":
                return I.DNAME.TRANSCEIVER;
            case "机控器":
                return I.DNAME.MACHINE_CONTROLLER;
            case "区控器":
                return I.DNAME.ZONE_CONTROLLER;
        }
        return 0;
    }



    public static String getUnitName(int i) {
        switch (i) {
            case 1:
                return "玉泉站";
            case 2:
                return "哈尔滨东站";
            case 3:
                return "哈尔滨西站";
            case 4:
                return "哈尔滨南站";
            case 5:
                return "哈尔滨车务段";
            case 6:
                return "绥化车务段";
            case 7:
                return "齐齐哈尔站";
            case 8:
                return "三间房站";
            case 9:
                return "齐齐哈尔车务段";
            case 10:
                return "大庆车务段";
            case 11:
                return "加格达奇车务段";
            case 12:
                return "牡丹江站";
            case 13:
                return
                        "七台河站";
            case 14:
                return
                        "绥芬河站";
            case 15:
                return
                        "牡丹江车务段";
            case 16:
                return
                        "鸡西车务段";
            case 17:
                return
                        "佳木斯车务段";
            case 18:
                return
                        "佳木斯站";
            case 19:
                return "海拉尔车务段";
            case 20:
                return
                        "满洲里站";
        }
        return null;
    }

    public static String getServiceStation(int i) {
        switch (i) {
            case 1:
                return "哈尔滨站";
            case 2:
                return "哈尔滨东站";
            case 3:
                return "哈尔滨南站";
            case 4:
                return "绥化站";
            case 5:
                return
                        "齐齐哈尔站";
            case 6:
                return
                        "三间房站";
            case 7:
                return "大庆站";
            case 8:
                return
                        "加格达奇站";
            case 9:
                return "牡丹江站";
            case 10:
                return
                        "鸡西站";
            case 11:
                return
                        "佳木斯站";
            case 12:

                return "海拉尔站";
            case 13:
                return
                        "依图里河站";
            case 14:
                return "满洲里站";
        }
        return null;
    }


}