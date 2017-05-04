package mac.yk.devicemanagement.bean;

import java.util.Observable;

/**
 * Created by mac-yk on 2017/5/5.
 */

public class Status extends Observable{
    String did;
    String status;

    public Status(String did, String status) {
        this.did = did;
        this.status = status;
    }

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
