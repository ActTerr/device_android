package mac.yk.devicemanagement.bean;

import java.io.Serializable;
import java.util.Date;

public class Check implements Serializable {
    Date date;
    String user;
    String status;
    String cause;

    public Check(Date date, String user, String status, String cause) {
        this.date = date;
        this.user = user;
        this.status = status;
        this.cause = cause;
    }

    public Check(){

    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
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
