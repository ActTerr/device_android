package mac.yk.devicemanagement.net;

import java.util.ArrayList;
import java.util.Map;

import mac.yk.devicemanagement.I;
import mac.yk.devicemanagement.bean.Attachment;
import mac.yk.devicemanagement.bean.Battery;
import mac.yk.devicemanagement.bean.Check;
import mac.yk.devicemanagement.bean.DeviceResume;
import mac.yk.devicemanagement.bean.Notice;
import mac.yk.devicemanagement.bean.Result;
import mac.yk.devicemanagement.bean.Scrap;
import mac.yk.devicemanagement.bean.Service;
import mac.yk.devicemanagement.bean.User;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
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
    @GET(I.REQUEST.PATH+I.REQUEST.WARING)
    Observable<Result<String>> getWarning();

    @GET(I.REQUEST.PATH+I.REQUEST.GET_DEVICE_RESUME)
    Observable<Result<ArrayList<DeviceResume>>> getDeviceResume(@Query(I.STATION_TYPE) String stype,@Query(I.DEVICE2.UNIT_ID) String unit,@Query(I.DEVICE2.CATEGROY) String category,
                                                                @Query(I.DEVICE2.TYPE) String type,@Query(I.DEVICE2.STATUS) String status,
                                                                @Query(I.PAGE) int page);

    @GET(I.REQUEST.PATH+I.REQUEST.GET_TOTAL_COUNT)
    Observable<Result<ArrayList<String[]>>> getTotalCount(@Query(I.UNIT) int unit,@Query(I.YEAR) String year ,@Query(I.TYPE) String type);

    @GET(I.REQUEST.PATH+I.REQUEST.GET_STATUS_COUNT)
    Observable<Result<ArrayList<String[]>>> getStatusCount(@Query(I.UNIT) int unit,@Query("year") String year ,
                                                           @Query(I.MEMORY) int memory);

    @GET(I.REQUEST.PATH+I.REQUEST.GET_SCRAP_COUNT)
    Observable<Result<ArrayList<String[]>>> getScrapCount(@Query(I.UNIT) int unit,@Query("year") String year ,@Query(I.TYPE)String type);

    @GET(I.REQUEST.PATH+I.REQUEST.GET_SERVICE_COUNT)
    Observable<Result<ArrayList<String[]>>> getServiceCount(@Query(I.UNIT) int unit);

    @GET(I.REQUEST.PATH+I.REQUEST.FIND)
    Observable<Result<String[]>> check(@Query(I.DEVICE2.DID) String Did);

    @GET(I.REQUEST.PATH+I.REQUEST.SAVE)
    Observable<Result<String>> saveDevice(@Query(I.USER.NAME) String name,@Query(I.DEVICE2.TABLE_NAME) String device);

    @GET(I.REQUEST.PATH+I.REQUEST.LOGIN)
    Observable<Result<User>> login(@Query(I.USER.ACCOUNTS) String name, @Query(I.USER.PASSWD) String passwd);

    @GET(I.REQUEST.PATH+I.REQUEST.LOGOUT)
    Observable<Result<String>> logOut(@Query(I.USER.NAME) String name);

    @GET(I.REQUEST.PATH+I.REQUEST.DOWN_SERVICE)
    Observable<Result<Service[]>> downLoadService(@Query(I.SERVICE.DID) String did, @Query(I.DOWNLOAD.PAGE) int page,
                                                 @Query(I.DOWNLOAD.SIZE) int size);

    @GET(I.REQUEST.PATH+I.REQUEST.DOWN_CHECK)
    Observable<Result<Check[]>> downloadCheck (@Query(I.CHECK.DID) String did, @Query(I.DOWNLOAD.PAGE) int page,
                                                 @Query(I.DOWNLOAD.SIZE) int size);

    @GET(I.REQUEST.PATH+I.REQUEST.CHECK)
    Observable<Result<String>> check(@Query(I.CHECK.USER) String userName
            ,@Query(I.DEVICE2.DID) String did, @Query(I.CHECK.STATUS) String status,
                                       @Query(I.CHECK.REMARK) String remark,@Query(I.USER.UNIT) String unit);

    @GET(I.REQUEST.PATH+I.REQUEST.REPAIR)
    Observable<Result<String>> repair(@Query(I.SERVICE.USER) String userName
            ,@Query(I.DEVICE2.DID) String did, @Query(I.SERVICE.TRANSLATE) boolean translate,
                                      @Query(I.SERVICE.TYPE) String type,
                                       @Query(I.SERVICE.REMARK) String remark);

    @GET(I.REQUEST.PATH+I.REQUEST.SCRAP)
    Observable<Result<String>> scrap(@Query(I.SCRAP.USER) String name,@Query(I.SCRAP.DNAME) String category
            ,@Query(I.DEVICE2.DID) String Did,@Query(I.SCRAP.REMARK) String remark,@Query(I.SCRAP.STATION) String unit
    ,@Query(I.SCRAP.TYPE) String type);

    @GET(I.REQUEST.PATH+I.REQUEST.CONTROL)
    Observable<Result<String>> control(@Query(I.DEVICE2.STATUS) String status,@Query(I.DEVICE2.DID) String Did);


    @GET(I.REQUEST.PATH+I.REQUEST.DOWN_SCRAP)
    Observable<Result<Scrap[]>> downScrap(@Query(I.DOWNLOAD.PAGE) int page,@Query(I.DOWNLOAD.SIZE) int size,
                                          @Query(I.SCRAP.DNAME) int dName);


    @GET(I.REQUEST.PATH+I.REQUEST.GET_PIC_COUNT)
    Observable<Result<Integer>> getPicCount(@Query(I.DEVICE2.CATEGROY )int dName,@Query(I.PIC.TYPE) String type);

    @POST(I.REQUEST.PATH+I.REQUEST.UPLOAD_UNCAUGHT)
    @Multipart
    Observable<Result<String>> uploadCrash(
            @Part("file\";filename=\"throwable.log\"") RequestBody file,
            @Query(I.UNCAUGHT.PATH) String path, @Query(I.UNCAUGHT.FILE_NAME)String name);

    @GET(I.REQUEST.PATH+I.REQUEST.CONTROL_BAT)
    Observable<Result<String>> controlD(@Query(I.CONTROL_BAT.CONTROL_TYPE)String control,@Query(I.DEVICE2.DID) String Did);


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
                                                @Query(I.ATTACHMENT.NEW_NAME) String newName);

    @GET(I.REQUEST.PATH+I.REQUEST.DELETE_ATTACHMENT)
    Observable<Result<String>> deleteAttachment(@Query(I.ATTACHMENT.NAME) String filename);



    @POST(I.REQUEST.PATH+I.REQUEST.ADD_ATTACHMENT)
    @Multipart
    Observable<Result<Attachment>> addAttachment(@PartMap Map<String, RequestBody> externalFileParameters,
                                             @Query(I.FILE.FILENAME) String name,
                                           @Query(I.ATTACHMENT.NID) long Nid,@Query(I.FILE.COMPLETED_SIZE) long total);


    @GET(I.REQUEST.PATH+I.REQUEST.DOWNLOAD_FILE)
    Observable<Response<ResponseBody>> downloadFile(@Query(I.FILE.FILENAME) String name, @Query(I.FILE.COMPLETED_SIZE) long completed );


    @GET(I.REQUEST.PATH+I.REQUEST.DELETE_FILE)
    Observable<Result<String>> deleteFile(@Query(I.FILE.FILENAME) String fileName);
    
    @GET(I.REQUEST.PATH+I.REQUEST.CHECK_BATTERY)
    Observable<Result<ArrayList<Battery>>> checkBattery(@Query(I.BATTERY.UNIT_ID) String unit);

    @GET(I.REQUEST.PATH+I.REQUEST.INACTIVE)
    Observable<Result<String>> inactive(@Query(I.DEVICE2.DID) String did,@Query(I.DEVICE2.USE_POSITION) String usePosition);


}
