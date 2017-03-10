package mac.yk.testserver.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.Observable;

/**
 * Created by mac-yk on 2017/3/1.
 */

public class Device extends Observable implements Serializable{
    int Did,Dname,status;
    Date chuchang,xunjian;

    public Device() {

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

    public int getName() {
        return Dname;
    }

    public void setName(int name) {
        this.Dname = name;
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
                "当前状态:"+status+"\n"+
                        "出厂日期:"+chuchang+"\n"+
                        "上次巡检日期:"+xunjian;
    }
}
