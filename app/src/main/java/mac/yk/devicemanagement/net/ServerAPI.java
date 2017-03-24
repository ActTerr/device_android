package mac.yk.devicemanagement.net;

import mac.yk.devicemanagement.I;
import mac.yk.devicemanagement.bean.Device;
import mac.yk.devicemanagement.bean.Result;
import mac.yk.devicemanagement.bean.Scrap;
import mac.yk.devicemanagement.bean.Weixiu;
import mac.yk.devicemanagement.bean.Xunjian;
import okhttp3.RequestBody;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by mac-yk on 2017/3/18.
 */

public interface ServerAPI {
    @GET(I.REQUEST.PATH+"?request="+I.REQUEST.YUJING)
    Observable<Result<String>> getyujing();

    @GET(I.REQUEST.PATH+"?request="+I.REQUEST.TONGJI)
    Observable<Result<Integer[]>> getTongji(@Query(I.TABLENAME) String tableName);

    @GET(I.REQUEST.PATH+"?request="+I.REQUEST.CHAXUN)
    Observable<Result<Device>> chaxun(@Query(I.DEVICE.DID) String Did);

    @GET(I.REQUEST.PATH+"?request="+I.REQUEST.SAVE)
    Observable<Result<String>> saveDevice(@Query(I.USER.NAME) String name,@Query(I.DEVICE.TABLENAME) String device);

    @GET(I.REQUEST.PATH+"?request="+I.REQUEST.LOGIN)
    Observable<Result<String>> login(@Query(I.USER.NAME) String name,@Query(I.USER.PASSWD) String passwd);

    @GET(I.REQUEST.PATH+"?request="+I.REQUEST.LOGOUT)
    Observable<Result<String>> logOut(@Query(I.USER.NAME) String name);

    @GET(I.REQUEST.PATH+"?request="+I.REQUEST.DOWNWEIXIU)
    Observable<Result<Weixiu[]>> downLoadWeixiu(@Query(I.DEVICE.DID) String did, @Query(I.DOWNLOAD.PAGE) int page,
                                                @Query(I.DOWNLOAD.SIZE) int size);

    @GET(I.REQUEST.PATH+"?request="+I.REQUEST.DOWNXUNJIAN)
    Observable<Result<Xunjian[]>> downloadXunJian (@Query(I.DEVICE.DID) String did, @Query(I.DOWNLOAD.PAGE) int page,
                                           @Query(I.DOWNLOAD.SIZE) int size);

    @GET(I.REQUEST.PATH+"?request="+I.REQUEST.XUNJIAN)
    Observable<Result<Integer>> xunjian(@Query(I.DEVICE.ISDIANCHI) boolean isdianchi,@Query(I.USER.NAME) String userName
            ,@Query(I.DEVICE.DID) String did,
                               @Query(I.XUNJIAN.STATUS) String status,@Query(I.XUNJIAN.REMARK) String remark);

    @GET(I.REQUEST.PATH+"?request="+I.REQUEST.XIUJUN)
    Observable<Result<Integer>> xiujun(@Query(I.DEVICE.ISDIANCHI) boolean isdianchi,@Query(I.USER.NAME) String userName
            ,@Query(I.DEVICE.DID) String did, @Query(I.WEIXIU.TRANSLATE) boolean translate,
                                       @Query(I.WEIXIU.REMARK) String remark);

    @GET(I.REQUEST.PATH+"?request="+I.REQUEST.BAOFEI)
    Observable<Result<Integer>> baofei(@Query(I.USER.NAME) String name,@Query(I.BAOFEI.DNAME) String Dname
            ,@Query(I.BAOFEI.DID) String Did,@Query(I.BAOFEI.REMARK) String remark);

    @GET(I.REQUEST.PATH+"?request="+I.REQUEST.CONTROL)
    Observable<Result<Integer>> control(@Query(I.DEVICE.ISDIANCHI)boolean isDianchi,
                                        @Query(I.DEVICE.STATUS) int Cid,@Query(I.DEVICE.DID) String Did);

    @GET(I.REQUEST.PATH+"?request="+I.REQUEST.YONGHOU)
    Observable<Result<Integer>> yonghou(@Query(I.DEVICE.DID) String Did);

    @GET(I.REQUEST.PATH+"?request="+I.REQUEST.DOWNSCRAP)
    Observable<Result<Scrap[]>> downScrap(@Query(I.DOWNLOAD.PAGE) int page,@Query(I.DOWNLOAD.SIZE) int size,
                                          @Query(I.BAOFEI.DNAME) int dName);

    @GET(I.REQUEST.PATH+"?request="+I.REQUEST.DOWNDEVICE)
    Observable<Result<Device[]>> downDevice(@Query(I.DOWNLOAD.PAGE) int page,@Query(I.DOWNLOAD.SIZE) int size,
                                            @Query(I.DEVICE.DNAME)int dname,
                                            @Query(I.DEVICE.STATUS) int status);

    @GET(I.REQUEST.PATH+"?request="+I.REQUEST.GETPICCOUNT)
    Observable<Result<Integer>> getCount(@Query(I.DEVICE.DNAME )int dName,@Query(I.PIC.TYPE) String type);

    @POST(I.REQUEST.PATH+"?request="+I.REQUEST.UPLOADUNCAUGHT)
    @Multipart
    Observable<Result<String>> uploadCrash(
            @Part("file\";filename=\"throwable.log\"") RequestBody file,
            @Query(I.UNCAUGHT.PATH) String path, @Query(I.UNCAUGHT.FILE_NAME)String name);
}
