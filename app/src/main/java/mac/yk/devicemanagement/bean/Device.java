package mac.yk.devicemanagement.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.Observable;

import mac.yk.devicemanagement.util.ConvertUtils;

/**
 * Created by mac-yk on 2017/3/1.
 */

public class Device extends Observable implements Serializable{
    int Did,Dname,status;
    Date chuchang,xunjian;

    boolean isDianchi=false;

    public boolean isDianchi() {
        return isDianchi;
    }

    public void setDianchi(boolean dianchi) {
        isDianchi = dianchi;
    }

    public Device() {

    }

    public int getDname() {
        return Dname;
    }

    public Device(int did, int Dname, int status, Date chuchang, Date xunjian) {
        Did = did;
        this.Dname = Dname;
        this.status = status;
        this.chuchang = chuchang;
        this.xunjian = xunjian;
    }

    public int getDid() {
        return Did;
    }

    public void setDid(int did) {
        Did = did;
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
        return  "name:"+Dname+"\n"+
                "ID:" + Did + "\n" +
                "当前状态:"+ ConvertUtils.getStatus(isDianchi,status)+"\n"+
                        "出厂日期:"+chuchang+"\n"+
                        "上次巡检日期:"+xunjian;
    }
}
