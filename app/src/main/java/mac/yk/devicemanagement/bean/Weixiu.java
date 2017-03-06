package mac.yk.devicemanagement.bean;

import java.io.Serializable;

/**
 * Created by mac-yk on 2017/3/2.
 */

public class Weixiu implements Serializable {
    String wxDate;
    String xjData;
    String controlUser;

    public Weixiu(String wxDate, String xjData, String controlUser) {
        this.wxDate = wxDate;
        this.xjData = xjData;
        this.controlUser = controlUser;
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

    public String getControlUser() {
        return controlUser;
    }

    public void setControlUser(String controlUser) {
        this.controlUser = controlUser;
    }
}
