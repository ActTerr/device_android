package mac.yk.devicemanagement.bean;

import java.io.Serializable;
import java.util.Date;

public class Weixiu implements Serializable {
    Date wxDate;
    Date xjData;
    String controlUser;
    String remark;
    boolean isTranslate;
    public Weixiu(Date wxDate, Date xjData, String controlUser, String remark,boolean isTranslate) {
        this.wxDate = wxDate;
        this.xjData = xjData;
        this.controlUser = controlUser;
        this.remark = remark;
        this.isTranslate=isTranslate;
    }
    public Weixiu() {
    }
    public Date getWxDate() {
        return wxDate;
    }
    public void setWxDate(Date wxDate) {
        this.wxDate = wxDate;
    }
    public Date getXjData() {
        return xjData;
    }
    public void setXjData(Date xjData) {
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

    public boolean isTranslate() {
        return isTranslate;
    }

    public void setTranslate(boolean translate) {
        isTranslate = translate;
    }
}