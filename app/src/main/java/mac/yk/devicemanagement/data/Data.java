package mac.yk.devicemanagement.data;

import android.content.Context;
import android.util.Log;

import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.bean.Device;
import mac.yk.devicemanagement.bean.Result;
import mac.yk.devicemanagement.bean.Weixiu;
import mac.yk.devicemanagement.bean.Xunjian;
import mac.yk.devicemanagement.model.IModel;
import mac.yk.devicemanagement.util.OkHttpUtils;

/**
 * Created by mac-yk on 2017/3/7.
 */

public class Data implements IModel {
    Device device=new Device();
    Xunjian xunjian;
    Weixiu weixiu;
    private static Data INSTANCE=new Data();
    public static Data getInstance(){
        return INSTANCE;
    }

    public Data() {
        device.setName("空");
    }

    @Override
    public void getYujing(Context context, OkHttpUtils.OnCompleteListener<Result> callback) {
        Result result=new Result(0,true,"来自测试数据的预警信息！");
        callback.onSuccess(result);
    }

    @Override
    public void getTongji(Context context, OkHttpUtils.OnCompleteListener<Result> callback) {
        Result result=new Result(0,true,"来自服务器的统计信息");
        callback.onSuccess(result);
    }

    @Override
    public void chaxun(Context context, String id, OkHttpUtils.OnCompleteListener<Result> callback) {
        device.setId(id);
        Log.e("main",device.toString());
        Result result=new Result(0,true,device);
        callback.onSuccess(result);
    }

    @Override
    public void control(Context context, boolean t, String userName, String vid, String id, OkHttpUtils.OnCompleteListener<Result> callback) {
        device.setZhuangtai(getZhuangtai(vid));
        Result result=new Result(0,true,device);
        callback.onSuccess(result);
    }

    private String getZhuangtai(String vid) {
        int id= Integer.parseInt(vid);
        switch (id){
            case R.id.daiyong:
                return "待用";
            case R.id.yunxing:
                return "运行";
            case R.id.baofei:
                return "报废";
            case R.id.weixiu:
                return "维修";
        }
        return "";
    }

    @Override
    public void saveDevice(Context context, String name, Device device, OkHttpUtils.OnCompleteListener<Result> callback) {
        device=new Device("1111","电台","11111","11111","备用");
        this.device=device;
        Result result=new Result(0,true,"chenggong");
        callback.onSuccess(result);
    }

    @Override
    public void Login(Context context, String name, String passwd, OkHttpUtils.OnCompleteListener<Result> callback) {
        Result result=new Result(0,true,"chenggong");
        callback.onSuccess(result);
    }

    @Override
    public void LogOut(Context context, String name, OkHttpUtils.OnCompleteListener<Result> callback) {
        Result result=new Result(0,true,"chenggong");
        callback.onSuccess(result);
    }

    @Override
    public void downloadWeixiu(Context context, String id, int page, OkHttpUtils.OnCompleteListener<Weixiu[]> callback) {
        weixiu=new Weixiu("11","22","fafa","aaf");
        Weixiu weixiu1=new Weixiu("fa","fa","f","f");
        Weixiu [] weixius=new Weixiu[]{weixiu,weixiu1};
        callback.onSuccess(weixius);
    }

    @Override
    public void downloadXunjian(Context context, String id, int page, OkHttpUtils.OnCompleteListener<Xunjian[]> callback) {
        xunjian=new Xunjian("11","22","fafa","aaf");
        Xunjian xunjian1=new Xunjian("afaf","faf","fa","fa");
        Xunjian [] xunjien=new Xunjian[]{xunjian,xunjian1};
        callback.onSuccess(xunjien);
    }

    @Override
    public void xunjian(Context context, String userName, boolean t, String id,String zhuangtai, String remark, OkHttpUtils.OnCompleteListener<Result> callback) {
        device.setZhuangtai("巡检");
        xunjian.setCause(remark);
        xunjian.setStatus(zhuangtai);
        xunjian.setXjUser(userName);
        xunjian.setXjDate(String.valueOf(System.currentTimeMillis()));
        Result result=new Result(0,true,device);
        callback.onSuccess(result);
    }

    @Override
    public void xiujun(Context context, String userName, boolean t, String id, String remark, OkHttpUtils.OnCompleteListener<Result> callback) {
        device.setZhuangtai("修竣");
        weixiu.setControlUser(userName);
        weixiu.setRemark(remark);
        weixiu.setWxDate(xunjian.getXjDate());
        weixiu.setXjData(String.valueOf(System.currentTimeMillis()));
        Result result=new Result(0,true,device);
        callback.onSuccess(result);
    }
}
