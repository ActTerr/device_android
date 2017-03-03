package mac.yk.devicemanagement.model;


import android.content.Context;

import mac.yk.devicemanagement.bean.Device;
import mac.yk.devicemanagement.bean.Result;
import mac.yk.devicemanagement.bean.Weixiu;
import mac.yk.devicemanagement.util.OkHttpUtils;

/**
 * Created by mac-yk on 2017/3/1.
 */

public interface IModel {
    void getYujing(Context context,OkHttpUtils.OnCompleteListener<Result> callback);

    void getTongji(Context context,OkHttpUtils.OnCompleteListener<Result> callback);

    void chaxun(Context context,int id, OkHttpUtils.OnCompleteListener<Result> callback);

    void control(Context context,boolean t,String s,String vid,OkHttpUtils.OnCompleteListener<Result> callback);

    void saveDevice(Context context,Device device,OkHttpUtils.OnCompleteListener<Result>  callback);

    void Login(Context context, String name, String passwd, OkHttpUtils.OnCompleteListener<Result> callback);

    void LogOut(Context context,String name,OkHttpUtils.OnCompleteListener<Result> callback);

    void downloadWeixiu(Context context, int id, int page, OkHttpUtils.OnCompleteListener<Weixiu[]> callback);

    void downloadXunjian(Context context,int id,int page,OkHttpUtils.OnCompleteListener<String[]> callback);
}
