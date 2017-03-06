package mac.yk.devicemanagement.bean;

/**
 * Created by mac-yk on 2017/3/6.
 */

public class Xunjian {
    String xjDate;
    String xjUser;
    String status;
    String cause;

    public Xunjian(String xjDate, String xjUser, String status, String cause) {
        this.xjDate = xjDate;
        this.xjUser = xjUser;
        this.status = status;
        this.cause = cause;
    }

    public Xunjian() {
    }

    public String getXjDate() {
        return xjDate;
    }

    public void setXjDate(String xjDate) {
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
