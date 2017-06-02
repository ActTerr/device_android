package mac.yk.devicemanagement.observable;

import java.io.Serializable;

/**
 * Created by mac-yk on 2017/6/2.
 */

public class Update extends java.util.Observable implements Serializable{
    boolean isUpdate;

    public boolean isUpdate() {
        return isUpdate;
    }

    public void setUpdate(boolean update) {
        isUpdate = update;
        setChanged();
        notifyObservers();
    }
}
