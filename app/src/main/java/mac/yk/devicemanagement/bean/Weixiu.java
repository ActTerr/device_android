package mac.yk.devicemanagement.bean;

import java.io.Serializable;

/**
 * Created by mac-yk on 2017/3/2.
 */

public class Weixiu implements Serializable {
    String wxDate;
    String xjData;
    String controlUser;
    String remark;
    public Weixiu(String wxDate, String xjData, String controlUser, String remark) {
        this.wxDate = wxDate;
        this.xjData = xjData;
        this.controlUser = controlUser;
        this.remark = remark;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
