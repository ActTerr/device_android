package mac.yk.devicemanagement.bean;

import java.io.Serializable;

/**
 * Created by mac-yk on 2017/3/2.
 */

public class Weixiu implements Serializable {
    String wxDate;
    String xjData;

    public Weixiu(String wxDate, String xjData) {
        this.wxDate = wxDate;
        this.xjData = xjData;
    }

    public Weixiu() {
    }

    public String getWxDate() {
        return wxDate;
    }

    public void setWxDate(String wxDate) {
        this.wxDate = wxDate;
    }

    public String getXjData() {
        return xjData;
    }

    public void setXjData(String xjData) {
        this.xjData = xjData;
    }
}
