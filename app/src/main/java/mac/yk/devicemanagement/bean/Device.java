package mac.yk.devicemanagement.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.Observable;

import mac.yk.devicemanagement.MyApplication;
import mac.yk.devicemanagement.util.ConvertUtils;

/**
 * Created by mac-yk on 2017/3/1.
 */

public class Device extends Observable implements Serializable{
    int did,  dname,status;
    Date chuchang,xunjian;

//    boolean dianchi;

//    public boolean isDianchi() {
//        return dianchi;
//    }
//
//    public void setDianchi(boolean dianchi) {
//        this.dianchi = dianchi;
//    }

    public Device() {

    }

    public void setDname(int dname) {
        this.dname = dname;
    }

    public int getDname() {
        return dname;
    }

    public Device(int did, int Dname, int status, Date chuchang, Date xunjian) {
        this.did = did;
        this.dname = Dname;
        this.status = status;
        this.chuchang = chuchang;
        this.xunjian = xunjian;
    }

    public int getDid() {
        return did;
    }

    public void setDid(int did) {
        this.did = did;
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
        setChanged();
        notifyObservers();

    }

    public Date getChuchang() {
        return chuchang;
    }

    public void setChuchang(Date chuchang) {
        this.chuchang = chuchang;
    }

    public Date getXunjian() {
        return xunjian;
    }

    public void setXunjian(Date xunjian) {
        this.xunjian = xunjian;
    }

    @Override
    public String toString() {
        return
//                "name:"+ConvertUtils.getDname(dname)+"\n"+
                "设备ID:" + did + "\n" +
                "当前状态:"+ConvertUtils.getStatus(MyApplication.getFlag(),status)+"\n"+
                "出厂日期:"+ ConvertUtils.Date2String(chuchang)+"\n"+
                "上次巡检日期:"+ConvertUtils.Date2String(xunjian);
    }
}
