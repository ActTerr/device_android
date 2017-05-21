import android.app.Application;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by mac-yk on 2017/5/21.
 */

public class JPushApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();

        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush
    }
}
