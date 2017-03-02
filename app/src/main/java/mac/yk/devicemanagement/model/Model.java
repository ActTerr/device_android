package mac.yk.devicemanagement.model;

import android.content.Context;

import mac.yk.devicemanagement.I;
import mac.yk.devicemanagement.bean.Device;
import mac.yk.devicemanagement.bean.Result;
import mac.yk.devicemanagement.util.OkHttpUtils;

/**
 * Created by mac-yk on 2017/3/1.
 */

public class Model implements IModel {


    @Override
    public void getYujing(Context context, OkHttpUtils.OnCompleteListener<Result> callback) {
        OkHttpUtils<Result> OK=new OkHttpUtils<>(context);
        OK.setRequestUrl(I.YUJING)
                .targetClass(Result.class)
                .execute(callback);
    }

    @Override
    public void getTongji(Context context, OkHttpUtils.OnCompleteListener<Result> callback) {
        OkHttpUtils<Result> OK=new OkHttpUtils<>(context);
        OK.setRequestUrl(I.TONGJI)
                .targetClass(Result.class)
                .execute(callback);
    }

    @Override
    public void chaxun(Context context, int id, OkHttpUtils.OnCompleteListener<Result> callback) {
        OkHttpUtils<Result> OK=new OkHttpUtils<>(context);
        OK.setRequestUrl(I.CHAXUN)
                .addFormParam(I.ID, String.valueOf(id))
                .targetClass(Result.class)
                .execute(callback);

    }

    @Override
    public void control(Context context,boolean b, String s,String c, OkHttpUtils.OnCompleteListener<Result> callback) {
        OkHttpUtils<Result> OK=new OkHttpUtils<>(context);
        OK.setRequestUrl(I.SAOMA)
                .addFormParam(I.ID,s)
                .addFormParam(I.ISDIANCHI, String.valueOf(b))
                .addFormParam(I.CREQ,c)
                .targetClass(Result.class)
                .execute(callback);
    }

    @Override
    public void saveDevice(Context context, Device device, OkHttpUtils.OnCompleteListener<Result> callback) {
        OkHttpUtils<Result> OK=new OkHttpUtils<>(context);
        OK.setRequestUrl(I.SAVE)
                .addFormParam(I.Device, device.toString())
                .targetClass(Result.class)
                .execute(callback);
    }

    @Override
    public void Login(Context context, String name, String passwd, OkHttpUtils.OnCompleteListener<Result> callback) {
        OkHttpUtils<Result> OK=new OkHttpUtils<>(context);
        OK.setRequestUrl(I.SAVE)
                .addFormParam(I.USERNAME,name)
                .addFormParam(I.PASSWD,passwd)
                .targetClass(Result.class)
                .execute(callback);
    }
}
