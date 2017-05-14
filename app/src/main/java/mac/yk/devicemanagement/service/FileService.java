package mac.yk.devicemanagement.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import mac.yk.devicemanagement.bean.FileEntry;
import mac.yk.devicemanagement.net.ApiWrapper;
import mac.yk.devicemanagement.net.ProgressListener;
import mac.yk.devicemanagement.net.ServerAPI;
import mac.yk.devicemanagement.net.UploadFileRequestBody;
import mac.yk.devicemanagement.util.ExceptionFilter;
import mac.yk.devicemanagement.util.OpenFileUtil;
import mac.yk.devicemanagement.util.ToastUtil;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Subscriber;

import static android.content.ContentValues.TAG;

/**
 * Created by mac-yk on 2017/5/13.
 */

public class FileService extends IntentService implements IFile{
    public static int DOWNLOAD=1;
    public static int UPLOAD=2;
    Context context;
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public FileService(String name) {
        super(name);
    }



    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        EventBus.getDefault().register(this);
        context=getApplicationContext();
        int type=intent.getIntExtra("type",0);
        FileEntry entry= (FileEntry) intent.getSerializableExtra("entry");
        if (type==DOWNLOAD){
            downloadFile(entry);
        }else {
            uploadFile(entry);
        }
    }

    Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            EventBus.getDefault().post(msg.arg1);
        }
    };

    @Override
    public void uploadFile(FileEntry entry) {
        File file=new File(entry.getSaveDirPath());
//        Map<String, RequestBody> requestBodyMap = new HashMap<>();
        UploadFileRequestBody fileRequestBody = new UploadFileRequestBody(file, new MyProgressListener(mHandler));
//        requestBodyMap.put("file\"; filename=\"" + file.getName(), fileRequestBody);
//        RequestBody requestBody = RequestBody.create(MediaType.parse("txt/*"), file);
        ApiWrapper<ServerAPI> wrapper=new ApiWrapper<>();
        wrapper.targetClass(ServerAPI.class).getAPI()
                .addAttachment(fileRequestBody,entry.getNid(),entry.getToolSize())
                .compose(wrapper.<String>applySchedulers())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (ExceptionFilter.filter(context,e)){
                            ToastUtil.showToast(context,"上传异常");
                        }
                    }

                    @Override
                    public void onNext(String s) {

                        EventBus.getDefault().post(true);
                    }
                });
    }


    @Override
    public void downloadFile(FileEntry entry) {
        ApiWrapper<ServerAPI> wrapper=new ApiWrapper<>();
        Call<ResponseBody> call = wrapper.targetClass(ServerAPI.class).getAPI()
                .downloadFile(OpenFileUtil.getUrl(entry.getFileName()));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    writeResponseBodyToDisk(response.body());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ToastUtil.showToast(context,"下载出现异常");
            }
        });
    }


    private boolean writeResponseBodyToDisk(ResponseBody body) {
        try {
            // todo change the file location/name according to your needs
            File futureStudioIconFile = new File(getExternalFilesDir(null) + File.separator + "Future Studio Icon.png");

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;
                    int prograss= (int) (fileSizeDownloaded/fileSize);
                    EventBus.getDefault().post(prograss);
                    Log.d(TAG, "file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();
                EventBus.getDefault().post(true);
                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }



    class MyProgressListener implements ProgressListener {
        Handler handler;

        public MyProgressListener(Handler handler) {
            this.handler = handler;
        }

        @Override
        public void onProgress(long hasWrittenLen, long totalLen, boolean hasFinish) {
            Message message=handler.obtainMessage();
            message.what= (int) (hasWrittenLen/totalLen*100);
            handler.sendMessage(message);
        }
    }
}
