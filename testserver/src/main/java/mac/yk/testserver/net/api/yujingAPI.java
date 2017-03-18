package mac.yk.testserver.net.api;


import mac.yk.testserver.I;
import mac.yk.testserver.bean.Result;
import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by mac-yk on 2017/3/18.
 */

public interface yujingAPI {
    @GET(I.REQUEST.PATH+"?request="+I.REQUEST.YUJING)
    Observable<Result> getyujing();
//@GET("Server?request=yujing")
//Observable<Result> getyujing();
}
