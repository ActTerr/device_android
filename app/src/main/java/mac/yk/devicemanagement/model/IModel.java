package mac.yk.devicemanagement.model;


import android.content.Context;

import mac.yk.devicemanagement.bean.Device;
import mac.yk.devicemanagement.bean.Result;
import mac.yk.devicemanagement.util.OkHttpUtils;

/**
 * Created by mac-yk on 2017/3/1.
 */

public interface IModel {
    void getYujing(Context context,OkHttpUtils.OnCompleteListener<Result> callback);

    void getTongji(Context context,OkHttpUtils.OnCompleteListener<Result> callback);

    void chaxun(Context context,String s, OkHttpUtils.OnCompleteListener<Result> callback);

    void saoma(Context context,boolean t,String s,OkHttpUtils.OnCompleteListener<Result> callback);

    void saveDevice(Context context,Device device,OkHttpUtils.OnCompleteListener<Result>  callback);
}
