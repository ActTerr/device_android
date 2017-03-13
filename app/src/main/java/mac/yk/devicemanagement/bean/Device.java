package mac.yk.devicemanagement.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.Observable;

/**
 * Created by mac-yk on 2017/3/1.
 */

public class Device extends Observable implements Serializable{
    int did,dname,status;
    Date chuchang,xunjian;

    boolean isDianchi;

    public boolean isDianchi() {
        return isDianchi;
    }

    public void setDianchi(boolean dianchi) {
        isDianchi = dianchi;
    }

    public Device() {

    }

    public void setDname(int dname) {
        dname = dname;
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
        did = did;
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
        return  "name:"+dname+"\n"+
                "ID:" + did + "\n" +
                "当前状态:"+status+"\n"+
                "出厂日期:"+chuchang+"\n"+
                "上次巡检日期:"+xunjian;
    }
}
