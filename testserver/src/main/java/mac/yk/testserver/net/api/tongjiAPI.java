package mac.yk.testserver.net.api;


import mac.yk.testserver.I;
import mac.yk.testserver.bean.Result;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by mac-yk on 2017/3/18.
 */

public interface tongjiAPI {
    @GET(I.REQUEST.PATH+"{"+ I.REQUEST.PARAM+"}&"+I.TABLENAME+"={"+I.TABLENAME+"}")
    Observable<Result> getTongji(@Path(I.REQUEST.PARAM) String request, @Path(I.TABLENAME) String tableName);

}
