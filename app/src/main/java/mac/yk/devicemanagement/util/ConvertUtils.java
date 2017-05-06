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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return sdf.parse(s);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static String Date2String(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    public static String getDname(int Dname) {
        switch (Dname) {
            case I.DNAME.DIANCHI:
                return "电池";
            case I.DNAME.DIANTAI:
                return "电台";
            case I.DNAME.JIKONGQI:
                return "机控器";
            case I.DNAME.QUKONGQI:
                return "区控器";
        }
        return null;
    }

    /**
     * 待完善
     * @param Dname
     * @return
     */
    public static int getDname(String Dname) {
        switch (Dname) {
            case "电池":
                return I.DNAME.DIANCHI;
            case "手持台":
                return I.DNAME.DIANTAI;
            case "机控器":
                return I.DNAME.JIKONGQI;
            case "区控器":
                return I.DNAME.QUKONGQI;
        }
        return 0;
    }

    public static String getStatus(boolean isDianchi, int status) {
        switch (status) {
            case I.CONTROL.BAOFEI:
                return "报废";
            case I.CONTROL.BEIYONG:
                return "备用";
            case I.CONTROL.DAIYONG:
                return "待用";
            case I.CONTROL.WEIXIU:
                if (isDianchi) {
                    return "充电";
                }
                return "维修";
            case I.CONTROL.YUNXING:
                return "运行";
            case I.CONTROL.YONGHOU:
                return "用后";
        }
        return null;
    }

    public static int getStatus(String status) {
        switch (status) {
            case "报废":
                return I.CONTROL.BAOFEI;
            case "备用":
                return I.CONTROL.BEIYONG;
            case "待用":
                return I.CONTROL.DAIYONG;
            case "维修":
            case "充电":
                return I.CONTROL.WEIXIU;
            case "用后":
                return I.CONTROL.YONGHOU;
            case "运行":
                return I.CONTROL.YUNXING;
        }
        return 0;
    }

    public static String getXunjianStatus(int i) {
        if (i == 0) {
            return "异常";
        } else {
            return "良好";
        }
    }

    public static String getUnitName(int i) {
        switch (i) {
            case 1:
                return "哈尔滨站";
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
            case 21:
                return "大庆站";
            case 22:
                return "加格达奇站";
            case 23:
                return "海拉尔站";
            case 24:
                return "依图里河站";
        }
        return null;
    }

}