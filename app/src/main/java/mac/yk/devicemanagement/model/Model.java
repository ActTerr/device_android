package mac.yk.devicemanagement.model;

import android.content.Context;

import mac.yk.devicemanagement.I;
import mac.yk.devicemanagement.bean.Device;
import mac.yk.devicemanagement.bean.Result;
import mac.yk.devicemanagement.bean.Scrap;
import mac.yk.devicemanagement.bean.Weixiu;
import mac.yk.devicemanagement.bean.Xunjian;
import mac.yk.devicemanagement.util.ConvertUtils;
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
                .addParam(I.DEVICE.DID, id)
                .targetClass(Result.class)
                .execute(callback);

    }

    @Override
    public void control(Context context, boolean isDianchi, String vid, String did, OkHttpUtils.OnCompleteListener<Result> callback) {
        OkHttpUtils<Result> OK=new OkHttpUtils<>(context);
        OK.setRequestUrl(I.REQUEST.CONTROL)
                .addParam(I.DEVICE.DID,did)
                .addParam(I.DEVICE.STATUS,vid)
                .addParam(I.DEVICE.ISDIANCHI,String.valueOf(isDianchi))
                .targetClass(Result.class)
                .execute(callback);
    }

    @Override
    public void yonghou(Context context, String Did, OkHttpUtils.OnCompleteListener<Result> callback) {
        OkHttpUtils<Result> ok=new OkHttpUtils<>(context);
        ok.setRequestUrl(I.REQUEST.YONGHOU)
                .addParam(I.DEVICE.DID,Did)
                .targetClass(Result.class)
                .execute(callback);
    }


    @Override
    public void saveDevice(Context context, String name, Device device, OkHttpUtils.OnCompleteListener<Result> callback) {
        OkHttpUtils<Result> OK=new OkHttpUtils<>(context);
        String json= ConvertUtils.getjson(device);
        OK.setRequestUrl(I.REQUEST.SAVE)
                .addParam(I.DEVICE.TABLENAME, json)
                .addParam(I.USER.NAME,name)
                .targetClass(Result.class)
                .execute(callback);
    }


    @Override
    public void Login(Context context, String name, String passwd, OkHttpUtils.OnCompleteListener<Result> callback) {
        OkHttpUtils<Result> OK=new OkHttpUtils<>(context);
        OK.setRequestUrl(I.REQUEST.LOGIN)
                .addParam(I.USER.NAME,name)
                .addParam(I.USER.PASSWD,passwd)
                .targetClass(Result.class)
                .execute(callback);
    }

    @Override
    public void LogOut(Context context, String name, OkHttpUtils.OnCompleteListener<Result> callback) {
        OkHttpUtils<Result> ok=new OkHttpUtils<>(context);
        ok.setRequestUrl(I.REQUEST.LOGOUT)
                .addParam(I.USER.NAME,name)
                .targetClass(Result.class)
                .execute(callback);
    }

    @Override
    public void downloadWeixiu(Context context, String id, int page, OkHttpUtils.OnCompleteListener<Weixiu[]> callback) {
       OkHttpUtils<Weixiu[]> ok=new OkHttpUtils<>(context);
        ok.setRequestUrl(I.REQUEST.DOWNWEIXIU)
                .addParam(I.DEVICE.DID,id)
                .addParam(I.DOWNLOAD.PAGE, String.valueOf(page))
                .addParam(I.DOWNLOAD.SIZE, String.valueOf(5))
                .targetClass(Weixiu[].class)
                .execute(callback);
    }

    @Override
    public void downloadXunjian(Context context, String id, int page, OkHttpUtils.OnCompleteListener<Xunjian[]> callback) {
        OkHttpUtils<Xunjian[]> ok =new OkHttpUtils<>(context);
        ok.setRequestUrl(I.REQUEST.DOWNXUNJIAN)
                .addParam(I.DEVICE.DID,id)
                .addParam(I.DOWNLOAD.PAGE, String.valueOf(page))
                .addParam(I.DOWNLOAD.SIZE, String.valueOf(5))
                .targetClass(Xunjian[].class)
                .execute(callback);
    }

    @Override
    public void xunjian(Context context,boolean isDianchi,  String userName, String Did,String status, String remark, OkHttpUtils.OnCompleteListener<Result> callback) {
        OkHttpUtils<Result> ok=new OkHttpUtils<>(context);
        ok.setRequestUrl(I.REQUEST.XUNJIAN)
                .addParam(I.XUNJIAN.STATUS,status)
                .addParam(I.XUNJIAN.USER,userName)
                .addParam(I.XUNJIAN.REMARK,remark)
                .addParam(I.XUNJIAN.DID,Did)
                .addParam(I.DEVICE.ISDIANCHI,String.valueOf(isDianchi))
                .targetClass(Result.class)
                .execute(callback);
    }

    @Override
    public void xiujun(Context context,boolean isDianchi,  String userName, String Did, boolean translate,String remark, OkHttpUtils.OnCompleteListener<Result> callback) {
        OkHttpUtils<Result> ok=new OkHttpUtils<>(context);
        ok.setRequestUrl(I.REQUEST.XIUJUN)
                .addParam(I.WEIXIU.REMARK,remark)
                .addParam(I.WEIXIU.USER,userName)
                .addParam(I.WEIXIU.TRANSLATE,String.valueOf(translate))
                .addParam(I.WEIXIU.DID,Did)
                .addParam(I.DEVICE.ISDIANCHI,String.valueOf(isDianchi))
                .targetClass(Result.class)
                .execute(callback);
    }

    @Override
    public void baofei(Context context, String name, String Dname,String Did, String remark,OkHttpUtils.OnCompleteListener<Result> callback) {
        OkHttpUtils<Result> ok=new OkHttpUtils<>(context);
        ok.setRequestUrl(I.REQUEST.BAOFEI)
                .addParam(I.BAOFEI.REMARK,remark)
                .addParam(I.BAOFEI.DID,Did)
                .addParam(I.BAOFEI.USER,name)
                .addParam(I.BAOFEI.DNAME,Dname)
                .targetClass(Result.class)
                .execute(callback);
    }

    @Override
    public void downScrap(Context context, int page, int size,OkHttpUtils.OnCompleteListener<Scrap[]> callback) {
        OkHttpUtils<Scrap[]> ok=new OkHttpUtils<>(context);
        ok.setRequestUrl(I.REQUEST.DOWNSCRAP)
                .addParam(I.DOWNLOAD.PAGE, String.valueOf(page))
                .addParam(I.DOWNLOAD.SIZE, String.valueOf(size))
                .targetClass(Scrap[].class)
                .execute(callback);
    }

    @Override
    public void downDevice(Context context, int page, int size,OkHttpUtils.OnCompleteListener<Device[]> callback) {
        OkHttpUtils<Device[]> ok=new OkHttpUtils<>(context);
        ok.setRequestUrl(I.REQUEST.DOWNDEVICE)
                .addParam(I.DOWNLOAD.PAGE, String.valueOf(page))
                .addParam(I.DOWNLOAD.SIZE, String.valueOf(size))
                .targetClass(Device[].class)
                .execute(callback);
    }


}
