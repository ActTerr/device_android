package mac.yk.devicemanagement.bean;

import java.io.Serializable;
import java.util.Date;

public class Service implements Serializable{
    Date serviceDate;
    Date repairDate;
    String user;
    String remark;
    boolean translate;

    public Service(Date serviceDate, Date repairDate, String user, String remark, boolean translate) {
        this.serviceDate = serviceDate;
        this.repairDate = repairDate;
        this.user = user;
        this.remark = remark;
        this.translate = translate;
    }

    public Service() {
    }

    public Date getServiceDate() {
        return serviceDate;
    }

    public void setServiceDate(Date serviceDate) {
        this.serviceDate = serviceDate;
    }

    public Date getRepairDate() {
        return repairDate;
    }

    public void setRepairDate(Date repairDate) {
        this.repairDate = repairDate;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
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
        this.translate = translate;
    }

    @Override
    public String toString() {
        return "Service{" +
                "wxDate=" + serviceDate +
                ", xjDate=" + repairDate +
                ", controlUser='" + user + '\'' +
                ", remark='" + remark + '\'' +
                ", translate=" + translate +
                '}';
    }
}