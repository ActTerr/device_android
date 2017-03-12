package mac.yk.devicemanagement.util;

import mac.yk.devicemanagement.model.IModel;
import mac.yk.devicemanagement.model.Model;

/**
 * Created by mac-yk on 2017/3/7.
 */

public class TestUtil {

    public static IModel getData(){
        return Model.getInstance();
    }
}
