package mac.yk.testserver.model;

import mac.yk.testserver.bean.Result;
import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by mac-yk on 2017/3/18.
 */

public interface yujingAPI {
    @GET("Server?request=yujing/")
    Observable<Result> getyujing();
}
