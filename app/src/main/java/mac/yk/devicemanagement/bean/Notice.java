package mac.yk.devicemanagement.bean;

/**
 * Created by mac-yk on 2017/5/9.
 */

public class Notice {
    String Nid;
    String title;
    String date;
    String common;

    public Notice(String id, String title, String date, String common) {
        this.Nid = id;
        this.title = title;
        this.date = date;
        this.common = common;
    }

    public Notice() {
    }

    public String getId() {
        return Nid;
    }

    public void setId(String id) {
        this.Nid = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCommon() {
        return common;
    }

    public void setCommon(String common) {
        this.common = common;
    }
}
