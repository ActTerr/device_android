package mac.yk.devicemanagement.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by mac-yk on 2017/5/17.
 */

public class DeviceResume implements Serializable{
    Date use_date;
    Date scrap_date;
    String use_unit;
    String use_local;
    Date check_date;
    String remark;
    String did;

    public DeviceResume(Date use_date, Date scrap_date, String use_unit, String use_local, Date check_date, String remark, String did) {
        this.use_date = use_date;
        this.scrap_date = scrap_date;
        this.use_unit = use_unit;
        this.use_local = use_local;
        this.check_date = check_date;
        this.remark = remark;
        this.did = did;
    }

    public Date getUse_date() {
        return use_date;
    }

    public void setUse_date(Date use_date) {
        this.use_date = use_date;
    }

    public Date getScrap_date() {
        return scrap_date;
    }

    public void setScrap_date(Date scrap_date) {
        this.scrap_date = scrap_date;
    }

    public String getUse_unit() {
        return use_unit;
    }

    public void setUse_unit(String use_unit) {
        this.use_unit = use_unit;
    }

    public String getUse_local() {
        return use_local;
    }

    public void setUse_local(String use_local) {
        this.use_local = use_local;
    }

    public Date getCheck_date() {
        return check_date;
    }

    public void setCheck_date(Date check_date) {
        this.check_date = check_date;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    public DeviceResume() {
    }

}
