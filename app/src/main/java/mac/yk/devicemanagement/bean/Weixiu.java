package mac.yk.devicemanagement.bean;

import java.io.Serializable;
import java.util.Date;

public class Weixiu implements Serializable{
    Date wxDate;
    Date xjDate;
    String controlUser;
    String remark;
    boolean translate;
    public Weixiu(Date wxDate, Date xjDate, String controlUser, String remark,boolean isTranslate) {
        this.wxDate = wxDate;
        this.xjDate = xjDate;
        this.controlUser = controlUser;
        this.remark = remark;
        this.translate=isTranslate;
    }
    public Weixiu() {
    }
    public Date getWxDate() {
        return wxDate;
    }
    public void setWxDate(Date wxDate) {
        this.wxDate = wxDate;
    }
    public Date getXjDate() {
        return xjDate;
    }
    public void setXjDate(Date xjDate) {
        this.xjDate = xjDate;
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
        return translate;
    }

    public void setTranslate(boolean translate) {
        translate = translate;
    }

    @Override
    public String toString() {
        return "Weixiu{" +
                "wxDate=" + wxDate +
                ", xjDate=" + xjDate +
                ", controlUser='" + controlUser + '\'' +
                ", remark='" + remark + '\'' +
                ", translate=" + translate +
                '}';
    }
}