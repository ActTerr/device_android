package mac.yk.devicemanagement.data;

import android.content.Context;

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
        Result result=new Result(0,true,device);
        callback.onSuccess(result);
    }

    @Override
    public void control(Context context, boolean t, String userName, String vid, String id, OkHttpUtils.OnCompleteListener<Result> callback) {
        device.setZhuangtai(String.valueOf(vid));
        Result result=new Result(0,true,device);
        callback.onSuccess(result);
    }

    @Override
    public void saveDevice(Context context, String name, Device device, OkHttpUtils.OnCompleteListener<Result> callback) {
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
        Weixiu [] weixius=new Weixiu[]{weixiu};
        callback.onSuccess(weixius);
    }

    @Override
    public void downloadXunjian(Context context, String id, int page, OkHttpUtils.OnCompleteListener<Xunjian[]> callback) {
        Xunjian [] xunjien=new Xunjian[]{xunjian};
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
