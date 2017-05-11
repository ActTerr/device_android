package mac.yk.devicemanagement.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by mac-yk on 2017/5/9.
 */

public class Notice implements Serializable {
    long nid;
    String title;
    Date date;
    String common;

    public Notice(long nid, String title, Date date, String common) {
        this.nid = nid;
        this.title = title;
        this.date = date;
        this.common = common;
    }

    public Notice() {
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getNid() {
        return nid;
    }

    public void setNid(long nid) {
        this.nid = nid;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getCommon() {
        return common;
    }

    public void setCommon(String common) {
        this.common = common;
    }
}
