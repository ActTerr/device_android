package mac.yk.devicemanagement.util;

import mac.yk.devicemanagement.data.Data;
import mac.yk.devicemanagement.model.IModel;

/**
 * Created by mac-yk on 2017/3/7.
 */

public class TestUtil {

    public static IModel getData(){
        return Data.getInstance();
    }
}
