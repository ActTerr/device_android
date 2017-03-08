package mac.yk.devicemanagement.model;

import android.content.Context;

import mac.yk.devicemanagement.I;
import mac.yk.devicemanagement.bean.Device;
import mac.yk.devicemanagement.bean.Result;
import mac.yk.devicemanagement.bean.Weixiu;
import mac.yk.devicemanagement.bean.Xunjian;
import mac.yk.devicemanagement.util.OkHttpUtils;

/**
 * Created by mac-yk on 2017/3/1.
 */

public class Model implements IModel {
    static Model INSTANCE=new Model();
    public static Model getInstance(){
        return INSTANCE;
    }
    @Override
    public void getYujing(Context context, OkHttpUtils.OnCompleteListener<Result> callback) {
        OkHttpUtils<Result> OK=new OkHttpUtils<>(context);
        OK.setRequestUrl(I.REQUEST.YUJING)
                .targetClass(Result.class)
                .execute(callback);
    }

    @Override
    public void getTongji(Context context, OkHttpUtils.OnCompleteListener<Result> callback) {
        OkHttpUtils<Result> OK=new OkHttpUtils<>(context);
        OK.setRequestUrl(I.REQUEST.TONGJI)
                .targetClass(Result.class)
                .execute(callback);
    }

    @Override
    public void chaxun(Context context, String id, OkHttpUtils.OnCompleteListener<Result> callback) {
        OkHttpUtils<Result> OK=new OkHttpUtils<>(context);
        OK.setRequestUrl(I.REQUEST.CHAXUN)
                .addFormParam(I.PARAM.ID, String.valueOf(id))
                .targetClass(Result.class)
                .execute(callback);

    }

    @Override
    public void control(Context context, boolean t, String userName, String vid, String id, OkHttpUtils.OnCompleteListener<Result> callback) {
        OkHttpUtils<Result> OK=new OkHttpUtils<>(context);
        OK.setRequestUrl(I.REQUEST.CONTROL)
                .addFormParam(I.PARAM.ID,userName)
                .addFormParam(I.PARAM.ISDIANCHI, String.valueOf(t))
                .addFormParam(I.PARAM.CREQ,vid)
                .addFormParam(I.PARAM.Device, String.valueOf(id))
                .targetClass(Result.class)
                .execute(callback);
    }

    @Override
    public void saveDevice(Context context, String name, Device device, OkHttpUtils.OnCompleteListener<Result> callback) {
        OkHttpUtils<Result> OK=new OkHttpUtils<>(context);
        OK.setRequestUrl(I.REQUEST.SAVE)
                .addFormParam(I.PARAM.Device, device.toString())
                .addFormParam(I.PARAM.USERNAME,name)
                .targetClass(Result.class)
                .execute(callback);
    }


    @Override
    public void Login(Context context, String name, String passwd, OkHttpUtils.OnCompleteListener<Result> callback) {
        OkHttpUtils<Result> OK=new OkHttpUtils<>(context);
        OK.setRequestUrl(I.REQUEST.SAVE)
                .addFormParam(I.PARAM.USERNAME,name)
                .addFormParam(I.PARAM.PASSWD,passwd)
                .targetClass(Result.class)
                .execute(callback);
    }

    @Override
    public void LogOut(Context context, String name, OkHttpUtils.OnCompleteListener<Result> callback) {
        OkHttpUtils<Result> ok=new OkHttpUtils<>(context);
        ok.setRequestUrl(I.REQUEST.LOGOUT)
                .addFormParam(I.PARAM.USERNAME,name)
                .targetClass(Result.class)
                .execute(callback);
    }

    @Override
    public void downloadWeixiu(Context context, String id, int page, OkHttpUtils.OnCompleteListener<Weixiu[]> callback) {
       OkHttpUtils<Weixiu[]> ok=new OkHttpUtils<>(context);
        ok.setRequestUrl(I.REQUEST.DOWNWEIXIU)
                .addFormParam(I.PARAM.Device, String.valueOf(id))
                .addFormParam(I.PARAM.PAGE, String.valueOf(page))
                .addFormParam(I.PARAM.SIZE, String.valueOf(10))
                .targetClass(Weixiu[].class)
                .execute(callback);
    }

    @Override
    public void downloadXunjian(Context context, String id, int page, OkHttpUtils.OnCompleteListener<Xunjian[]> callback) {
        OkHttpUtils<Xunjian[]> ok =new OkHttpUtils<>(context);
        ok.setRequestUrl(I.REQUEST.DOWNXUNJIAN)
                .addFormParam(I.PARAM.Device, String.valueOf(id))
                .addFormParam(I.PARAM.Device, String.valueOf(page))
                .addFormParam(I.PARAM.SIZE, String.valueOf(10))
                .targetClass(Xunjian[].class)
                .execute(callback);
    }

    @Override
    public void xunjian(Context context, String userName, boolean t, String id,String zhuangtai, String remark, OkHttpUtils.OnCompleteListener<Result> callback) {
        OkHttpUtils<Result> ok=new OkHttpUtils<>(context);
        ok.setRequestUrl(I.REQUEST.XUNJIAN)
                .addFormParam(I.PARAM.USERNAME,userName)
                .addFormParam(I.PARAM.ISDIANCHI, String.valueOf(t))
                .addFormParam(I.PARAM.Device, String.valueOf(id))
                .addFormParam(I.PARAM.REMARK,remark)
                .addFormParam(I.PARAM.ZHUANGTAI,zhuangtai)
                .targetClass(Result.class)
                .execute(callback);
    }

    @Override
    public void xiujun(Context context, String userName, boolean t, String id, String remark, OkHttpUtils.OnCompleteListener<Result> callback) {
        OkHttpUtils<Result> ok=new OkHttpUtils<>(context);
        ok.setRequestUrl(I.REQUEST.XIUJUN)
                .addFormParam(I.PARAM.USERNAME,userName)
                .addFormParam(I.PARAM.ISDIANCHI, String.valueOf(t))
                .addFormParam(I.PARAM.Device, String.valueOf(id))
                .addFormParam(I.PARAM.REMARK,remark)
                .targetClass(Result.class)
                .execute(callback);
    }


}
