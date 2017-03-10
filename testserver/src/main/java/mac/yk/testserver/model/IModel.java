package mac.yk.testserver.model;


import android.content.Context;

import mac.yk.testserver.OkHttpUtils;
import mac.yk.testserver.bean.Device;
import mac.yk.testserver.bean.Result;
import mac.yk.testserver.bean.Weixiu;
import mac.yk.testserver.bean.Xunjian;


/**
 * Created by mac-yk on 2017/3/1.
 */

public interface IModel {
    void getYujing(Context context, OkHttpUtils.OnCompleteListener<Result> callback);

    void getTongji(Context context, OkHttpUtils.OnCompleteListener<Result> callback);

    void chaxun(Context context, String Did, OkHttpUtils.OnCompleteListener<Result> callback);

    void control(Context context, String Cid, String Did, OkHttpUtils.OnCompleteListener<Result> callback);

    void saveDevice(Context context, String name, Device device, OkHttpUtils.OnCompleteListener<Result> callback);

    void Login(Context context, String name, String passwd, OkHttpUtils.OnCompleteListener<Result> callback);

    void LogOut(Context context, String name, OkHttpUtils.OnCompleteListener<Result> callback);

    void downloadWeixiu(Context context, String Did, int page, OkHttpUtils.OnCompleteListener<Weixiu[]> callback);

    void downloadXunjian(Context context, String Did, int page, OkHttpUtils.OnCompleteListener<Xunjian[]> callback);

    void xunjian(Context context, String userName, String Did, String status, String remark, OkHttpUtils.OnCompleteListener<Result> callback);

    void xiujun(Context context, String userName, String Did, boolean translate, String remark, OkHttpUtils.OnCompleteListener<Result> callback);
}
