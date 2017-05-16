package mac.yk.devicemanagement.bean;

import java.util.Date;

/**
 * Created by mac-yk on 2017/5/9.
 */

public class Attachment {
    long nid;
    long aid;
    Date date;
    String name;


    public Attachment() {
    }

    public Attachment(long nid, long aid, Date date, String name) {
        this.nid = nid;
        this.aid = aid;
        this.date = date;
        this.name = name;
    }

    public long getNid() {
        return nid;
    }

    public void setNid(long nid) {
        this.nid = nid;
    }

    public long getAid() {
        return aid;
    }

    public void setAid(long aid) {
        this.aid = aid;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Attachment{" +
                "nid=" + nid +
                ", aid=" + aid +
                ", date=" + date +
                ", name='" + name + '\'' +
                '}';
    }
}
