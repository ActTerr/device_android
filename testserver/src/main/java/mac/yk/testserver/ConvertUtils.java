package mac.yk.testserver;

import android.content.Context;

import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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
}