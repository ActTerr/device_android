package mac.yk.devicemanagement.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import mac.yk.devicemanagement.I;
import mac.yk.devicemanagement.MyMemory;
import mac.yk.devicemanagement.bean.Attachment;
import mac.yk.devicemanagement.bean.FileEntry;
import mac.yk.devicemanagement.bean.Result;
import mac.yk.devicemanagement.db.dbFile;
import mac.yk.devicemanagement.net.ApiWrapper;
import mac.yk.devicemanagement.net.ProgressListener;
import mac.yk.devicemanagement.net.RetrofitUtil;
import mac.yk.devicemanagement.net.ServerAPI;
import mac.yk.devicemanagement.net.UploadFileRequestBody;
import mac.yk.devicemanagement.util.L;
import mac.yk.devicemanagement.util.OpenFileUtil;
import mac.yk.devicemanagement.util.ToastUtil;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by mac-yk on 2017/5/13.
 */

public class FileService extends IntentService implements IFile{
    public static int DOWNLOAD=1;
    public static int UPLOAD=2;
    Context context;
    String TAG="FileService";
    long finished;
    public FileService() {
        super("");
    }
    Subscription subscribe;
   static File file;
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        L.e(TAG,"service start");
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
        L.e(TAG,"uploadfile");

        if (entry.getCompletedSize()!=0){
            CountDownLatch countdown=new CountDownLatch(1);
            L.e(TAG,"分割文件");
            new divisionThread(countdown,entry){
                @Override
                public void run() {
                    file=getDivisionFile(entry);
                    countdown.countDown();
                }
            }.start();
            try {
                countdown.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }else {
              file=new File(entry.getSaveDirPath());
        }
        L.e(TAG,file.getAbsolutePath());
        Map<String, RequestBody> requestBodyMap = new HashMap<>();
        UploadFileRequestBody fileRequestBody = new UploadFileRequestBody(file, new DefaultProgressListener(
                mHandler,1,entry.getCompletedSize()));
        L.e(TAG,"zuile:"+entry.getCompletedSize());
        requestBodyMap.put("file\"; filename=\"" + entry.getFileName(), fileRequestBody);


        ServerAPI serverAPI = RetrofitUtil.createService(ServerAPI.class);
        L.e(TAG,"execute");
       subscribe= serverAPI.addAttachment(requestBodyMap, entry.getFileName(), entry.getNid(), entry.getCompletedSize())
               .subscribeOn(Schedulers.io())
               .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Result<Attachment>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Result<Attachment> attachmentResult) {
                        L.e(TAG, "上传成功");
                        Attachment s = attachmentResult.getRetData();
                        FileEntry entry1 = new FileEntry(s.getAid(), file.length(), file.length(),
                                file.getAbsolutePath(), file.getName(), I.DOWNLOAD_STATUS.FINISH, s.getNid());
                        EventBus.getDefault().post(entry1);
                        subscribe=null;
                        File divisionFile=new File(OpenFileUtil.getPath("division"));
                        divisionFile.delete();
                    }
                });


//        if(subscribe==null){
//            Toast.makeText(context, "null null null", Toast.LENGTH_SHORT).show();
//        }

        MyMemory.getInstance().setEntry(entry);
        MyMemory.getInstance().setSubscribe(subscribe);


    }

    private File getDivisionFile(FileEntry entry) {
        RandomAccessFile afile;
        File file=new File(entry.getSaveDirPath());
        File divisionFile=new File(OpenFileUtil.getPath("division"));

        FileOutputStream out;
        try {
            if (!divisionFile.exists()){
                divisionFile.createNewFile();
            }
            out=new FileOutputStream(divisionFile);
            afile = new RandomAccessFile(file,"r");
            afile.seek(entry.getCompletedSize());
            byte [] buffer=new byte[1024*16];
            int len;
            while((len=afile.read(buffer))!=-1){
                out.write(buffer,0,len);
            }
            L.e(TAG,"源文件大小："+file.length()+" 剩余文件大小："+divisionFile.length());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return divisionFile;

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
                    int prograss= (int) (fileSizeDownloaded/fileSize*100);
                    L.e(TAG,"进度:"+fileSizeDownloaded+"/"+fileSize);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        L.e(TAG,"destroy");
    }

    @Subscribe
   public void getStop(Long l){
    L.e(TAG,"stop upload");
    MyMemory.getInstance().getSubscribe().unsubscribe();
    FileEntry fileEntry=MyMemory.getInstance().getEntry();
    L.e(TAG,"已暂停，当前上传："+finished);
    fileEntry.setCompletedSize(finished);
    fileEntry.setDownloadStatus(I.DOWNLOAD_STATUS.STOP);
    dbFile.getInstance(context).deleteFile(fileEntry.getFileName());
    L.e(TAG,fileEntry.toString());
    if(dbFile.getInstance(context).insertFile(fileEntry)){
        L.e(TAG,"insert suc");
    }
        File divisionFile=new File(OpenFileUtil.getPath("division"));
        divisionFile.delete();
}

    class DefaultProgressListener implements ProgressListener {

        private Handler mHandler;

        //多文件上传时，index作为上传的位置的标志
        private int mIndex;
        long completed;
        public DefaultProgressListener(Handler mHandler, int mIndex,long completed) {
            this.mHandler = mHandler;
            this.mIndex = mIndex;
            this.completed=completed;
        }

        @Override
        public void onProgress(long hasWrittenLen, long totalLen, boolean hasFinish) {
            System.out.println("----the current " + hasWrittenLen + "----" + totalLen + "-----" + (hasWrittenLen * 100 / totalLen));
            finished=completed+hasWrittenLen;
            int percent = (int) ((completed+hasWrittenLen) * 100 / (totalLen+completed));
            if (percent > 100) percent = 100;
            if (percent < 0) percent = 0;

            Message msg = Message.obtain();
            msg.what = mIndex;
            msg.arg1 = percent;
            mHandler.sendMessage(msg);
        }
    }

    class divisionThread extends Thread{
        CountDownLatch countdown;
        FileEntry entry;
        divisionThread( CountDownLatch countdown,FileEntry entry){
            this.countdown=countdown;
            this.entry=entry;
        }

    }
}
