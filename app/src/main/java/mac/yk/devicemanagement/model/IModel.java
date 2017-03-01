package mac.yk.devicemanagement.model;


import mac.yk.devicemanagement.util.OkHttpUtils;

/**
 * Created by mac-yk on 2017/3/1.
 */

public interface IModel {
    String getMessage(OkHttpUtils.OnCompleteListener<String> callback);

    String getYujing(OkHttpUtils.OnCompleteListener<String> callback);


}
