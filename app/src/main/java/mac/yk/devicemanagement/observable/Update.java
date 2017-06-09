package mac.yk.devicemanagement.observable;

import java.io.Serializable;

/**
 * Created by mac-yk on 2017/6/2.
 */

public class Update extends java.util.Observable implements Serializable{
    public final static int updateData=1;
    public final static int updateItem=2;

    int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
        setChanged();
        notifyObservers();
    }
}
