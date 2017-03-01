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
    public void chaxun(Context context, String s, OkHttpUtils.OnCompleteListener<Result> callback) {
        OkHttpUtils<Result> OK=new OkHttpUtils<>(context);
        OK.setRequestUrl(I.CHAXUN)
                .addFormParam("id",s)
                .targetClass(Result.class)
                .execute(callback);

    }

    @Override
    public void saoma(Context context,boolean b, String s, OkHttpUtils.OnCompleteListener<Result> callback) {
        OkHttpUtils<Result> OK=new OkHttpUtils<>(context);
        OK.setRequestUrl(I.SAOMA)
                .addFormParam("id",s)
                .addFormParam("dianchi", String.valueOf(b))
                .targetClass(Result.class)
                .execute(callback);
    }

    @Override
    public void saveDevice(Context context, Device device, OkHttpUtils.OnCompleteListener<Result> callback) {
        OkHttpUtils<Result> OK=new OkHttpUtils<>(context);
        OK.setRequestUrl(I.SAVE)
                .addFormParam("device", device.toString())
                .targetClass(Result.class)
                .execute(callback);
    }
}
