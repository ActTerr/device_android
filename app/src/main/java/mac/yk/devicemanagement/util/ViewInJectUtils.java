package mac.yk.devicemanagement.util;

import android.app.Activity;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by mac-yk on 2017/2/17.
 */

public class ViewInJectUtils {
    private static final String METHOD_SET_CONTENTVIEW = "setContentView";

    private static void injectContentView(Activity activity) {
        //用Activity的子类接收传过来的Activity
        Class<? extends Activity> clazz = activity.getClass();
        // 查询类上是否存在ContentView注解
        ContentView contentView = clazz.getAnnotation(ContentView.class);
        if (contentView != null)// 存在
        {
            int contentViewLayoutId = contentView.value();
            try {
                //setContentView这个方法
                Method method = clazz.getMethod(METHOD_SET_CONTENTVIEW,
                        int.class);
                //取消java语言访问检查，用于提升速度
                method.setAccessible(true);
                //调用invoke来实例化view
                method.invoke(activity, contentViewLayoutId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static final String METHOD_FIND_VIEW_BY_ID = "findViewById";

    /**
     * 注入所有的控件
     *
     * @param activity
     */
    private static void injectViews(Activity activity) {
        //接收activity;
        Class<? extends Activity> clazz = activity.getClass();
        //取得自己声明的所有字段
        Field[] fields = clazz.getDeclaredFields();
        // 遍历所有成员变量
        for (Field field : fields) {
            //取得ViewInject注解的字段
            ViewInject viewInjectAnnotation = field
                    .getAnnotation(ViewInject.class);
            if (viewInjectAnnotation != null) {
                int viewId = viewInjectAnnotation.value();
                if (viewId != -1) {
                    Log.e("TAG", viewId + "");
                    // 初始化View
                    try {
                        //取得findViewById方法
                        Method method = clazz.getMethod(METHOD_FIND_VIEW_BY_ID,
                                int.class);
                        //取得指定控件
                        Object resView = method.invoke(activity, viewId);
                        //取消java语言访问检查，用于提升速度
                        field.setAccessible(true);
                        //将控件实例化，native
                        field.set(activity, resView);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }

        }

    }

    public static void inject(Activity activity) {

        injectContentView(activity);
        injectViews(activity);

    }
}
