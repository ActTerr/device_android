package mac.yk.devicemanagement.net;

import java.util.ArrayList;
import java.util.Map;

import mac.yk.devicemanagement.I;
import mac.yk.devicemanagement.bean.Attachment;
import mac.yk.devicemanagement.bean.Battery;
import mac.yk.devicemanagement.bean.DeviceOld;
import mac.yk.devicemanagement.bean.DeviceResume;
import mac.yk.devicemanagement.bean.Notice;
import mac.yk.devicemanagement.bean.Result;
import mac.yk.devicemanagement.bean.Scrap;
import mac.yk.devicemanagement.bean.User;
import mac.yk.devicemanagement.bean.Weixiu;
import mac.yk.devicemanagement.bean.Xunjian;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by mac-yk on 2017/3/18.
 */

public interface ServerAPI {
    @GET(I.REQUEST.PATH+I.REQUEST.YUJING)
    Observable<Result<String>> getyujing();

    @GET(I.REQUEST.PATH+I.REQUEST.GET_DEVICE_RESUME)
    Observable<Result<ArrayList<DeviceResume>>> getDeviceResume(@Query(I.STATION_TYPE) String stype,@Query(I.DEVICE2.UNIT_ID) String unit,@Query(I.DEVICE2.CATEGROY) String category,
                                                                @Query(I.DEVICE2.TYPE) String type,@Query(I.DEVICE2.STATUS) String status,
                                                                @Query(I.PAGE) int page);

    @GET(I.REQUEST.PATH+I.REQUEST.TONGJI)
    Observable<Result<ArrayList<String[]>>> getTotalCount(@Query(I.UNIT) int unit,@Query("year") String year ,@Query(I.TYPE) String type);

    @GET(I.REQUEST.PATH+I.REQUEST.GET_STATUS_COUNT)
    Observable<Result<ArrayList<String[]>>> getStatusCount(@Query(I.UNIT) int unit,@Query("year") String year ,
                                                           @Query(I.MEMORY) int memory);

    @GET(I.REQUEST.PATH+I.REQUEST.GET_BAOFEI_COUNT)
    Observable<Result<ArrayList<String[]>>> getBaofeiCount(@Query(I.UNIT) int unit,@Query("year") String year ,@Query(I.TYPE)String type);

    @GET(I.REQUEST.PATH+I.REQUEST.GET_SERVICE_COUNT)
    Observable<Result<ArrayList<String[]>>> getServiceCount(@Query(I.UNIT) int unit);

    @GET(I.REQUEST.PATH+I.REQUEST.CHAXUN)
    Observable<Result<String[]>> chaxun(@Query(I.DEVICE.DID) String Did);

    @GET(I.REQUEST.PATH+I.REQUEST.SAVE)
    Observable<Result<String>> saveDevice(@Query(I.USER.NAME) String name,@Query(I.DEVICE.TABLENAME) String device);

    @GET(I.REQUEST.PATH+I.REQUEST.LOGIN)
    Observable<Result<User>> login(@Query(I.USER.ACCOUNTS) String name, @Query(I.USER.PASSWD) String passwd);

    @GET(I.REQUEST.PATH+I.REQUEST.LOGOUT)
    Observable<Result<String>> logOut(@Query(I.USER.NAME) String name);

    @GET(I.REQUEST.PATH+I.REQUEST.DOWNWEIXIU)
    Observable<Result<Weixiu[]>> downLoadWeixiu(@Query(I.DEVICE.DID) String did, @Query(I.DOWNLOAD.PAGE) int page,
                                                @Query(I.DOWNLOAD.SIZE) int size);

    @GET(I.REQUEST.PATH+I.REQUEST.DOWNXUNJIAN)
    Observable<Result<Xunjian[]>> downloadXunJian (@Query(I.DEVICE.DID) String did, @Query(I.DOWNLOAD.PAGE) int page,
                                           @Query(I.DOWNLOAD.SIZE) int size);

    @GET(I.REQUEST.PATH+I.REQUEST.XUNJIAN)
    Observable<Result<String>> xunjian(@Query(I.USER.NAME) String userName
            ,@Query(I.DEVICE2.DID) String did, @Query(I.DEVICE2.STATUS) String status,
                                       @Query(I.XUNJIAN.REMARK) String remark,@Query(I.USER.UNIT) String unit);

    @GET(I.REQUEST.PATH+I.REQUEST.XIUJUN)
    Observable<Result<String>> xiujun(@Query(I.WEIXIU.USER) String userName
            ,@Query(I.DEVICE.DID) String did, @Query(I.WEIXIU.TRANSLATE) boolean translate,
                                      @Query(I.WEIXIU.TYPE) String type,
                                       @Query(I.WEIXIU.REMARK) String remark);

    @GET(I.REQUEST.PATH+I.REQUEST.BAOFEI)
    Observable<Result<String>> baofei(@Query(I.USER.NAME) String name
            ,@Query(I.BAOFEI.DID) String Did,@Query(I.BAOFEI.REMARK) String remark,@Query(I.BAOFEI.STATION) String unit
    ,@Query(I.BAOFEI.TYPE) String type);

    @GET(I.REQUEST.PATH+I.REQUEST.CONTROL)
    Observable<Result<String>> control(@Query(I.DEVICE2.STATUS) String status,@Query(I.DEVICE2.DID) String Did);


    @GET(I.REQUEST.PATH+I.REQUEST.DOWNSCRAP)
    Observable<Result<Scrap[]>> downScrap(@Query(I.DOWNLOAD.PAGE) int page,@Query(I.DOWNLOAD.SIZE) int size,
                                          @Query(I.BAOFEI.DNAME) int dName);

    @GET(I.REQUEST.PATH+I.REQUEST.DOWNDEVICE)
    Observable<Result<DeviceOld[]>> downDevice(@Query(I.DOWNLOAD.PAGE) int page, @Query(I.DOWNLOAD.SIZE) int size,
                                               @Query(I.DEVICE.DNAME)int dname,
                                               @Query(I.DEVICE.STATUS) int status);

    @GET(I.REQUEST.PATH+I.REQUEST.GETPICCOUNT)
    Observable<Result<Integer>> getPicCount(@Query(I.DEVICE.DNAME )int dName,@Query(I.PIC.TYPE) String type);

    @POST(I.REQUEST.PATH+I.REQUEST.UPLOADUNCAUGHT)
    @Multipart
    Observable<Result<String>> uploadCrash(
            @Part("file\";filename=\"throwable.log\"") RequestBody file,
            @Query(I.UNCAUGHT.PATH) String path, @Query(I.UNCAUGHT.FILE_NAME)String name);

    @GET(I.REQUEST.PATH+I.REQUEST.CONTROL_D)
    Observable<Result<String>> controlD(@Query(I.CONTROL_D.CONTROL_TYPE)String control,@Query(I.DEVICE2.DID) String Did);


    @GET(I.REQUEST.PATH+I.REQUEST.GET_NOTICE)
    Observable<Result<ArrayList<Notice>>> getNotice(@Query(I.MEMORY) int memory);

    @GET(I.REQUEST.PATH+I.REQUEST.DELETE_NOTICE)
    Observable<Result<String>> deleteNotice(@Query(I.NOTICE.NID)long  Nid);

    @GET(I.REQUEST.PATH+I.REQUEST.UPDATE_NOTICE)
    Observable<Result<String>> updateNotice(@Query(I.NOTICE.NID) long Nid, @Query(I.BEAN) String notice);


    @GET(I.REQUEST.PATH+I.REQUEST.GET_ATTACHMENT)
    Observable<Result<ArrayList<Attachment>>> getAttachment(@Query(I.ATTACHMENT.NID) long Nid);

    @GET(I.REQUEST.PATH+I.REQUEST.UPDATE_ATTACHMENT)
    Observable<Result<String>> updateAttachment(@Query(I.ATTACHMENT.AID) long Aid,@Query(I.ATTACHMENT.NAME) String name,
                                                @Query(I.ATTACHMENT.NEW_NAME) String newName,@Query(I.TYPE) String type);

    @GET(I.REQUEST.PATH+I.REQUEST.DELETE_ATTACHMENT)
    Observable<Result<String>> deleteAttachment(@Query(I.ATTACHMENT.NAME) String filename);



    @POST(I.REQUEST.PATH+I.REQUEST.ADD_ATTACHMENT)
    @Multipart
    Observable<Result<Attachment>> addAttachment(@PartMap Map<String, RequestBody> externalFileParameters,
                                             @Query(I.FILE.FILENAME) String name,
                                           @Query(I.ATTACHMENT.NID) long Nid,@Query(I.FILE.COMPLETEDSIZE) long total);


    @POST(I.REQUEST.PATH+I.REQUEST.DOWNLOAD_FILE)
    Call<ResponseBody> downloadFile(@Query(I.FILE.FILENAME) String name,@Query(I.FILE.COMPLETEDSIZE) long completed );

    
    @GET(I.REQUEST.PATH+I.REQUEST.CHECK_BATTERY)
    Observable<Result<ArrayList<Battery>>> checkBattery(@Query(I.BATTERY.UNIT_ID) String unit);

    @GET(I.REQUEST.PATH+I.REQUEST.DAIYONG)
    Observable<Result<String>> daiyong(@Query(I.DEVICE2.DID) String did,@Query(I.DEVICE2.USE_POSITION) String usePosition);
}
