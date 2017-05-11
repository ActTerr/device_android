package mac.yk.devicemanagement.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by mac-yk on 2017/5/9.
 */

public class Notice implements Serializable {
    long Nid;
    String title;
    Date date;
    String common;

    public Notice(long nid, String title, Date date, String common) {
        Nid = nid;
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
        return Nid;
    }

    public void setNid(long nid) {
        Nid = nid;
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
