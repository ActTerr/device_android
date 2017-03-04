package mac.yk.devicemanagement.bean;

import java.io.Serializable;
import java.util.Observable;

/**
 * Created by mac-yk on 2017/3/1.
 */

public class Device extends Observable implements Serializable{
    String id,name,xunjian,chuchang,zhuangtai;

    public Device() {
    }

    public Device(String id, String name, String xunjian, String chuchang, String zhuangtai) {
        this.id = id;
        this.name = name;
        this.xunjian = xunjian;
        this.chuchang = chuchang;
        this.zhuangtai = zhuangtai;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
        setChanged();
        notifyObservers();

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        setChanged();
        notifyObservers();
    }

    public String getXunjian() {
        return xunjian;
    }

    public void setXunjian(String xunjian) {
        this.xunjian = xunjian;
        setChanged();
        notifyObservers();
    }

    public String getChuchang() {
        return chuchang;
    }

    public void setChuchang(String chuchang) {
        this.chuchang = chuchang;
        setChanged();
        notifyObservers();
    }

    public String getZhuangtai() {
        return zhuangtai;
    }

    public void setZhuangtai(String zhuangtai) {
        this.zhuangtai = zhuangtai;
        setChanged();
        notifyObservers();
    }

    @Override
    public String toString() {
        return
                "ID:" + id + "\n" +
                "当前状态:"+zhuangtai+"\n"+
                        "出厂日期:"+chuchang+"\n"+
                        "上次巡检日期:"+xunjian;
    }
}
