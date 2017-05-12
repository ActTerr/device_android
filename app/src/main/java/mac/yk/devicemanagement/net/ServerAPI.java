package mac.yk.devicemanagement.net;

import java.util.ArrayList;

import mac.yk.devicemanagement.I;
import mac.yk.devicemanagement.bean.Attachment;
import mac.yk.devicemanagement.bean.DeviceOld;
import mac.yk.devicemanagement.bean.Notice;
import mac.yk.devicemanagement.bean.Result;
import mac.yk.devicemanagement.bean.Scrap;
import mac.yk.devicemanagement.bean.User;
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
    Observable<Result<ArrayList<String[]>>> getTotalCount(@Query(I.UNIT) int unit,@Query("year") String year ,@Query(I.TYPE) String type);

    @GET(I.REQUEST.PATH+"?request="+I.REQUEST.GET_STATUS_COUNT)
    Observable<Result<ArrayList<String[]>>> getStatusCount(@Query(I.UNIT) int unit,@Query("year") String year ,
                                                           @Query(I.MEMORY) int memory);

    @GET(I.REQUEST.PATH+"?request="+I.REQUEST.GET_BAOFEI_COUNT)
    Observable<Result<ArrayList<String[]>>> getBaofeiCount(@Query(I.UNIT) int unit,@Query("year") String year ,@Query(I.TYPE)String type);

    @GET(I.REQUEST.PATH+"?request="+I.REQUEST.CHAXUN)
    Observable<Result<String[]>> chaxun(@Query(I.DEVICE.DID) String Did);

    @GET(I.REQUEST.PATH+"?request="+I.REQUEST.SAVE)
    Observable<Result<String>> saveDevice(@Query(I.USER.NAME) String name,@Query(I.DEVICE.TABLENAME) String device);

    @GET(I.REQUEST.PATH+"?request="+I.REQUEST.LOGIN)
    Observable<Result<User>> login(@Query(I.USER.ACCOUNTS) String name, @Query(I.USER.PASSWD) String passwd);

    @GET(I.REQUEST.PATH+"?request="+I.REQUEST.LOGOUT)
    Observable<Result<String>> logOut(@Query(I.USER.NAME) String name);

    @GET(I.REQUEST.PATH+"?request="+I.REQUEST.DOWNWEIXIU)
    Observable<Result<Weixiu[]>> downLoadWeixiu(@Query(I.DEVICE.DID) String did, @Query(I.DOWNLOAD.PAGE) int page,
                                                @Query(I.DOWNLOAD.SIZE) int size);

    @GET(I.REQUEST.PATH+"?request="+I.REQUEST.DOWNXUNJIAN)
    Observable<Result<Xunjian[]>> downloadXunJian (@Query(I.DEVICE.DID) String did, @Query(I.DOWNLOAD.PAGE) int page,
                                           @Query(I.DOWNLOAD.SIZE) int size);

    @GET(I.REQUEST.PATH+"?request="+I.REQUEST.XUNJIAN)
    Observable<Result<String>> xunjian(@Query(I.USER.NAME) String userName
            ,@Query(I.DEVICE2.DID) String did,
                               @Query(I.DEVICE2.STATUS) String status,@Query(I.XUNJIAN.REMARK) String remark);

    @GET(I.REQUEST.PATH+"?request="+I.REQUEST.XIUJUN)
    Observable<Result<String>> xiujun(@Query(I.WEIXIU.USER) String userName
            ,@Query(I.DEVICE.DID) String did, @Query(I.WEIXIU.TRANSLATE) boolean translate,
                                       @Query(I.WEIXIU.REMARK) String remark);

    @GET(I.REQUEST.PATH+"?request="+I.REQUEST.BAOFEI)
    Observable<Result<String>> baofei(@Query(I.USER.NAME) String name
            ,@Query(I.BAOFEI.DID) String Did,@Query(I.BAOFEI.REMARK) String remark,@Query(I.BAOFEI.STATION) String unit
    ,@Query(I.BAOFEI.TYPE) String type);

    @GET(I.REQUEST.PATH+"?request="+I.REQUEST.CONTROL)
    Observable<Result<String>> control(@Query(I.DEVICE2.STATUS) String status,@Query(I.DEVICE2.DID) String Did);


    @GET(I.REQUEST.PATH+"?request="+I.REQUEST.DOWNSCRAP)
    Observable<Result<Scrap[]>> downScrap(@Query(I.DOWNLOAD.PAGE) int page,@Query(I.DOWNLOAD.SIZE) int size,
                                          @Query(I.BAOFEI.DNAME) int dName);

    @GET(I.REQUEST.PATH+"?request="+I.REQUEST.DOWNDEVICE)
    Observable<Result<DeviceOld[]>> downDevice(@Query(I.DOWNLOAD.PAGE) int page, @Query(I.DOWNLOAD.SIZE) int size,
                                               @Query(I.DEVICE.DNAME)int dname,
                                               @Query(I.DEVICE.STATUS) int status);

    @GET(I.REQUEST.PATH+"?request="+I.REQUEST.GETPICCOUNT)
    Observable<Result<Integer>> getPicCount(@Query(I.DEVICE.DNAME )int dName,@Query(I.PIC.TYPE) String type);

    @POST(I.REQUEST.PATH+"?request="+I.REQUEST.UPLOADUNCAUGHT)
    @Multipart
    Observable<Result<String>> uploadCrash(
            @Part("file\";filename=\"throwable.log\"") RequestBody file,
            @Query(I.UNCAUGHT.PATH) String path, @Query(I.UNCAUGHT.FILE_NAME)String name);

    @GET(I.REQUEST.PATH+"?request="+I.REQUEST.CONTROL_D)
    Observable<Result<String>> controlD(@Query(I.CONTROL_D.CONTROL_TYPE)String control,@Query(I.DEVICE2.DID) String Did);

    @GET(I.REQUEST.PATH+"?request="+I.REQUEST.GET_NOTICE)
    Observable<Result<ArrayList<Notice>>> getNotice(@Query(I.MEMORY) int memory);

    @GET(I.REQUEST.PATH+"?request="+I.REQUEST.DELETE_NOTICE)
    Observable<Result<String>> deleteNotice(@Query(I.NOTICE.NID)long  Nid);

    @GET(I.REQUEST.PATH+"?request="+I.REQUEST.UPDATE_NOTICE)
    Observable<Result<String>> updateNotice(@Query(I.NOTICE.NID) long Nid, @Query(I.BEAN) String notice);


    @GET(I.REQUEST.PATH+"?request="+I.REQUEST.GET_ATTACHMENT)
    Observable<Result<ArrayList<Attachment>>> getAttachment(@Query(I.ATTACHMENT.NID) long Nid);

    @GET(I.REQUEST.PATH+"?request="+I.REQUEST.UPLOAD_ATTACHMENT)
    Observable<Result<String>> updateAttachment(@Query(I.ATTACHMENT.AID) long Aid,@Query(I.ATTACHMENT.NAME) String name);

    @GET(I.REQUEST.PATH+"?request="+I.REQUEST.DELETE_ATTACHMENT)
    Observable<Result<String>> deleteAttachment(@Query(I.ATTACHMENT.AID) long Aid);
}
