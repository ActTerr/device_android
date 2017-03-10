package mac.yk.testserver.model;

import android.content.Context;

import mac.yk.testserver.ConvertUtils;
import mac.yk.testserver.I;
import mac.yk.testserver.OkHttpUtils;
import mac.yk.testserver.bean.Device;
import mac.yk.testserver.bean.Result;
import mac.yk.testserver.bean.Weixiu;
import mac.yk.testserver.bean.Xunjian;


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
    public void control(Context context, String vid, String did, OkHttpUtils.OnCompleteListener<Result> callback) {
        OkHttpUtils<Result> OK=new OkHttpUtils<>(context);
        OK.setRequestUrl(I.REQUEST.CONTROL)
                .addParam(I.DEVICE.DID,did)
                .addParam(I.DEVICE.STATUS,vid)
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
    public void xunjian(Context context, String userName, String Did,String status, String remark, OkHttpUtils.OnCompleteListener<Result> callback) {
        OkHttpUtils<Result> ok=new OkHttpUtils<>(context);
        ok.setRequestUrl(I.REQUEST.XUNJIAN)
                .addParam(I.XUNJIAN.STATUS,status)
                .addParam(I.XUNJIAN.USER,userName)
                .addParam(I.XUNJIAN.REMARK,remark)
                .addParam(I.XUNJIAN.DID,Did)
                .targetClass(Result.class)
                .execute(callback);
    }

    @Override
    public void xiujun(Context context, String userName, String Did, boolean translate,String remark, OkHttpUtils.OnCompleteListener<Result> callback) {
        OkHttpUtils<Result> ok=new OkHttpUtils<>(context);
        ok.setRequestUrl(I.REQUEST.XIUJUN)
                .addParam(I.WEIXIU.REMARK,remark)
                .addParam(I.WEIXIU.USER,userName)
                .addParam(I.WEIXIU.TRANSLATE,String.valueOf(translate))
                .addParam(I.WEIXIU.DID,Did)
                .targetClass(Result.class)
                .execute(callback);
    }


}
