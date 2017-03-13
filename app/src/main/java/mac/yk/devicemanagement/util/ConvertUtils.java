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

    public static int px2dp(Context context, int px){
        int density = (int) context.getResources().getDisplayMetrics().density;
        return px/density;
    }
    public static int dp2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    public static String getjson(Object object){
        Gson gson=new Gson();
       String json= gson.toJson(object);

        return json;
    }
    public static Date String2Date(String s){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        try {
            return  sdf.parse(s);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
    public static String Date2String(Date date){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    public static String getDname(int Dname){
        switch (Dname){
            case I.DNAME.DIANCHI:
                return "电池";
            case I.DNAME.DIANTAI:
                return "带台";
            case I.DNAME.JIKONGQI:
                return "机控器";
            case I.DNAME.QUKONGQI:
                return "区控器";
        }
        return null;
    }
    public static String getStatus(boolean isDianchi,int status){
        switch (status){
            case I.CONTROL.BAOFEI:
                return "报废";
            case I.CONTROL.BEIYONG:
                return "备用";
            case I.CONTROL.DAIYONG:
                return "待用";
            case I.CONTROL.WEIXIU:
                if (isDianchi){
                    return "充电";
                }
                return "维修";
            case I.CONTROL.XUNJIAN:
                if (isDianchi){
                    return "用后";
                }
                return "巡检";
            case I.CONTROL.YUNXING:
                return "运行";
        }
        return null;
    }
}