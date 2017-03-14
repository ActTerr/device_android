package mac.yk.devicemanagement.model;


import android.content.Context;

import mac.yk.devicemanagement.bean.Device;
import mac.yk.devicemanagement.bean.Result;
import mac.yk.devicemanagement.bean.Scrap;
import mac.yk.devicemanagement.bean.Weixiu;
import mac.yk.devicemanagement.bean.Xunjian;
import mac.yk.devicemanagement.util.OkHttpUtils;


/**
 * Created by mac-yk on 2017/3/1.
 */

public interface IModel {
    void getYujing(Context context, OkHttpUtils.OnCompleteListener<Result> callback);

    void getTongji(Context context, OkHttpUtils.OnCompleteListener<Result> callback);

    void chaxun(Context context, String Did, OkHttpUtils.OnCompleteListener<Result> callback);

    void saveDevice(Context context, String name, Device device, OkHttpUtils.OnCompleteListener<Result> callback);

    void Login(Context context, String name, String passwd, OkHttpUtils.OnCompleteListener<Result> callback);

    void LogOut(Context context, String name, OkHttpUtils.OnCompleteListener<Result> callback);

    void downloadWeixiu(Context context, String Did, int page, OkHttpUtils.OnCompleteListener<Weixiu[]> callback);

    void downloadXunjian(Context context, String Did, int page, OkHttpUtils.OnCompleteListener<Xunjian[]> callback);

    void xunjian(Context context,boolean isDianchi, String userName, String Did, String status, String remark, OkHttpUtils.OnCompleteListener<Result> callback);

    void xiujun(Context context,boolean isDianchi, String userName, String Did, boolean translate, String remark, OkHttpUtils.OnCompleteListener<Result> callback);

    void baofei(Context context,String name, String Dname, String Did, String remark, OkHttpUtils.OnCompleteListener<Result> callback);

    void control(Context context,boolean isDianchi, String Cid, String Did, OkHttpUtils.OnCompleteListener<Result> callback);

    void yonghou(Context context,String Did,OkHttpUtils.OnCompleteListener<Result> callback);

    void downScrap(Context context, int page, int size, OkHttpUtils.OnCompleteListener<Scrap[]> callback);

    void downDevice(Context context, int page, int size, OkHttpUtils.OnCompleteListener<Device[]> callback);

    void getCount(Context context,int Dname,String type,OkHttpUtils.OnCompleteListener<Result> callback);
}
