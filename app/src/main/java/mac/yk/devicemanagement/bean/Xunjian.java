package mac.yk.devicemanagement.bean;

import java.io.Serializable;
import java.util.Date;

public class Xunjian implements Serializable {
    Date xjDate;
    String xjUser;
    String status;
    String cause;
    public Xunjian(Date xjDate, String xjUser, String status, String cause) {
        super();
        this.xjDate = xjDate;
        this.xjUser = xjUser;
        this.status = status;
        this.cause = cause;
    }
    public Xunjian(){

    }
    public Date getXjDate() {
        return xjDate;
    }
    public void setXjDate(Date xjDate) {
        this.xjDate = xjDate;
    }

    public String getXjUser() {
        return xjUser;
    }
    public void setXjUser(String xjUser) {
        this.xjUser = xjUser;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getCause() {
        return cause;
    }
    public void setCause(String cause) {
        this.cause = cause;
    }
}
