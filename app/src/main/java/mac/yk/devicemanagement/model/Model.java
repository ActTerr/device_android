package mac.yk.devicemanagement.model;

import android.content.Context;

import mac.yk.devicemanagement.I;
import mac.yk.devicemanagement.bean.Device;
import mac.yk.devicemanagement.bean.Result;
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
                .addFormParam(I.DEVICE.DID, String.valueOf(id))
                .targetClass(Result.class)
                .execute(callback);

    }

    @Override
    public void control(Context context, String vid, String did, OkHttpUtils.OnCompleteListener<Result> callback) {
        OkHttpUtils<Result> OK=new OkHttpUtils<>(context);
        OK.setRequestUrl(I.REQUEST.CONTROL)
                .addFormParam(I.DEVICE.DID,did)
                .addFormParam(I.DEVICE.STATUS,vid)
                .targetClass(Result.class)
                .execute(callback);
    }
//    private String getZhuangtai(String vid) {
//        int id= Integer.parseInt(vid);
//        switch (id){
//            case R.id.daiyong:
//                return "待用";
//            case R.id.yunxing:
//                return "运行";
//            case R.id.baofei:
//                return "报废";
//            case R.id.weixiu:
//                return "维修";
//        }
//        return "";
//    }

    @Override
    public void saveDevice(Context context, String name, Device device, OkHttpUtils.OnCompleteListener<Result> callback) {
        OkHttpUtils<Result> OK=new OkHttpUtils<>(context);
        String json=ConvertUtils.getjson(device);
        OK.setRequestUrl(I.REQUEST.SAVE)
                .addFormParam(I.DEVICE.TABLENAME, json)
                .addFormParam(I.USER.NAME,name)
                .targetClass(Result.class)
                .execute(callback);
    }


    @Override
    public void Login(Context context, String name, String passwd, OkHttpUtils.OnCompleteListener<Result> callback) {
        OkHttpUtils<Result> OK=new OkHttpUtils<>(context);
        OK.setRequestUrl(I.REQUEST.LOGIN)
                .addFormParam(I.USER.NAME,name)
                .addFormParam(I.USER.PASSWD,passwd)
                .targetClass(Result.class)
                .execute(callback);
    }

    @Override
    public void LogOut(Context context, String name, OkHttpUtils.OnCompleteListener<Result> callback) {
        OkHttpUtils<Result> ok=new OkHttpUtils<>(context);
        ok.setRequestUrl(I.REQUEST.LOGOUT)
                .addFormParam(I.USER.NAME,name)
                .targetClass(Result.class)
                .execute(callback);
    }

    @Override
    public void downloadWeixiu(Context context, String id, int page, OkHttpUtils.OnCompleteListener<Weixiu[]> callback) {
       OkHttpUtils<Weixiu[]> ok=new OkHttpUtils<>(context);
        ok.setRequestUrl(I.REQUEST.DOWNWEIXIU)
                .addFormParam(I.DEVICE.DID,id)
                .addFormParam(I.DOWNLOAD.PAGE, String.valueOf(page))
                .addFormParam(I.DOWNLOAD.SIZE, String.valueOf(5))
                .targetClass(Weixiu[].class)
                .execute(callback);
    }

    @Override
    public void downloadXunjian(Context context, String id, int page, OkHttpUtils.OnCompleteListener<Xunjian[]> callback) {
        OkHttpUtils<Xunjian[]> ok =new OkHttpUtils<>(context);
        ok.setRequestUrl(I.REQUEST.DOWNXUNJIAN)
                .addFormParam(I.DEVICE.DID,id)
                .addFormParam(I.DOWNLOAD.PAGE, String.valueOf(page))
                .addFormParam(I.DOWNLOAD.SIZE, String.valueOf(5))
                .targetClass(Xunjian[].class)
                .execute(callback);
    }

    @Override
    public void xunjian(Context context, String userName, String Did,String status, String remark, OkHttpUtils.OnCompleteListener<Result> callback) {
        OkHttpUtils<Result> ok=new OkHttpUtils<>(context);
        ok.setRequestUrl(I.REQUEST.XUNJIAN)
                .addFormParam(I.XUNJIAN.STATUS,status)
                .addFormParam(I.XUNJIAN.USER,userName)
                .addFormParam(I.XUNJIAN.REMARK,remark)
                .addFormParam(I.XUNJIAN.DID,Did)
                .targetClass(Result.class)
                .execute(callback);
    }

    @Override
    public void xiujun(Context context, String userName, String Did, boolean translate,String remark, OkHttpUtils.OnCompleteListener<Result> callback) {
        OkHttpUtils<Result> ok=new OkHttpUtils<>(context);
        ok.setRequestUrl(I.REQUEST.XIUJUN)
                .addFormParam(I.WEIXIU.REMARK,remark)
                .addFormParam(I.WEIXIU.USER,userName)
                .addFormParam(I.WEIXIU.TRANSLATE,String.valueOf(translate))
                .addFormParam(I.WEIXIU.DID,Did)
                .targetClass(Result.class)
                .execute(callback);
    }


}
