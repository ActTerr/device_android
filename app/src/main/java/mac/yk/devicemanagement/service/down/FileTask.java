package mac.yk.devicemanagement.service.down;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import mac.yk.devicemanagement.I;
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
 * Created by mac-yk on 2017/5/26.
 */

public class FileTask implements FileTaskListener {

    FileEntry entry;
    File file;
    long finished;
    Subscription subscribe;
    Context context;
    dbFile dbfile;
    boolean flag;
    boolean downFlag=true;
    IServiceListener listener;

    String TAG="FileTask";
    public FileTask(FileEntry fileEntry,Context context,IServiceListener listener) {
        entry=fileEntry;
        this.context=context;
        dbfile=dbFile.getInstance(context);
        this.listener=listener;

    }
    Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {

                listener.onTransferring(file.getName(), (Long) msg.obj);
        }
    };


    public static String showDate() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("a hh:mm");
        String date = sDateFormat.format(new Date());
        return date;
    }


    @Override
    public void onStartDownload() {
        L.e(TAG,"开始下载");
        ServerAPI server=RetrofitUtil.createService(ServerAPI.class);
        Call<ResponseBody> call = server.downloadFile(entry.getFileName(),entry.getCompletedSize());

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    writeResponseBodyToDisk(response.body(),entry.getFileName());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                ToastUtil.showToast(context,"下载出现异常");
            }
        });
    }
    private boolean writeResponseBodyToDisk(ResponseBody body,String name) {
        try {
            // todo change the file location/name according to your needs
             file =new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Attachment/"+name);
            L.e(TAG,"放特么这了："+file.getAbsolutePath());
            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[8192];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(file);
                listener.startDownload(entry.getFileName(),fileSize);
                while (downFlag) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;
                    listener.onTransferring(file.getName(),fileSizeDownloaded);
                    L.e(TAG,"进度:"+fileSizeDownloaded+"/"+fileSize);
                }
                outputStream.flush();
                onDownCompleted();
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
    public void onStartUpload() {
        L.e("caonima","执行");
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
        UploadFileRequestBody fileRequestBody = new UploadFileRequestBody(file, new DefaultProgressListener(mHandler,
                entry.getCompletedSize()));
        requestBodyMap.put("file\"; filename=\"" + entry.getFileName(), fileRequestBody);
        ServerAPI serverAPI = RetrofitUtil.createService(ServerAPI.class);
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
                        onCompletedUpload(s.getAid());

                    }
                });

    }

//    @Override
//    public void onTransferring(int i) {
//        EventBus.getDefault().post(i);
//    }

    @Override
    public boolean onPauseUpload() {
        subscribe.unsubscribe();
        L.e(TAG,"已暂停，当前上传："+finished);
        entry.setCompletedSize(finished);
        entry.setDownloadStatus(I.DOWNLOAD_STATUS.PAUSE);
        if (dbfile.updateFileStatus(entry.getFileName(),finished,I.DOWNLOAD_STATUS.PAUSE)){
            File divisionFile=new File(OpenFileUtil.getPath("division"+entry.getFileName()));
            divisionFile.delete();
            return true;
        }
        return false;
    }

    @Override
    public boolean onPauseDownload(){
        downFlag=false;
        L.e(TAG,"已暂停，当前上传："+finished);
        entry.setCompletedSize(finished);
        entry.setDownloadStatus(I.DOWNLOAD_STATUS.PAUSE);
        if (dbfile.updateFileStatus(entry.getFileName(),finished,I.DOWNLOAD_STATUS.PAUSE)){
            return true;
        }
        return false;
    }

    @Override
    public boolean onCancelDownload() {
        downFlag=false;
        if(dbfile.updateFileStatus(entry.getFileName(),0,I.DOWNLOAD_STATUS.INIT)&&file.delete()){
            return true;
        }
        return false;
    }

    @Override
    public boolean onCancelUpload(){
        L.e(TAG,"cancel upload");
        subscribe.unsubscribe();
        if (dbfile.deleteFileEntry(entry.getFileName())&&deleteFile()){
           return true;
        }
        return false;
    }

    private boolean deleteFile(){
        final CountDownLatch countdown=new CountDownLatch(1);
        ApiWrapper<ServerAPI> wrapper=new ApiWrapper<>();
        wrapper.targetClass(ServerAPI.class).getAPI()
                .deleteFile(entry.getFileName())
                .compose(wrapper.<String>applySchedulers())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        flag=false;
                        countdown.countDown();
                    }

                    @Override
                    public void onNext(String s) {
                        flag=true;
                        countdown.countDown();

                    }
                });
        try {
            countdown.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return flag;
    }

    @Override
    public void onCompletedUpload(long newId) {
        L.e(TAG,"execute comp");
        if (dbfile.updateFileStatus(file.getName(),file.length(),I.DOWNLOAD_STATUS.COMPLETED)){
            L.e(TAG,"update1 suc");
            if(dbfile.updateFileId(entry.getAid(),newId)){
        entry.setDownloadStatus(I.DOWNLOAD_STATUS.COMPLETED);
                L.e(TAG,"db execute suc");
                subscribe=null;
                File divisionFile=new File(OpenFileUtil.getPath("division"+entry.getFileName()));
                if (divisionFile.exists()){
                    divisionFile.delete();
                }
                listener.onCompletedUpload(entry);

            }

        }
    }

    @Override
    public void onDownCompleted() {
        if (dbfile.updateFileStatus(file.getName(),file.length(),I.DOWNLOAD_STATUS.COMPLETED)){
            entry.setDownloadStatus(I.DOWNLOAD_STATUS.COMPLETED);
            listener.onCompletedDownload(entry);
        }
    }

    @Override
    public void onError(FileTask fileTask, int errorCode) {

    }



    private File getDivisionFile(FileEntry entry) {
        RandomAccessFile aFile;
        File file=new File(entry.getSaveDirPath());
        File divisionFile=new File(OpenFileUtil.getPath("division"+entry.getFileName()));

        FileOutputStream out;
        try {
            if (!divisionFile.exists()){
                divisionFile.createNewFile();
            }
            out=new FileOutputStream(divisionFile);
            aFile = new RandomAccessFile(file,"r");
            aFile.seek(entry.getCompletedSize());
            byte [] buffer=new byte[1024*16];
            int len;
            while((len=aFile.read(buffer))!=-1){
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

    class divisionThread extends Thread{
        CountDownLatch countdown;
        FileEntry entry;
        divisionThread( CountDownLatch countdown,FileEntry entry){
            this.countdown=countdown;
            this.entry=entry;
        }

    }

   class DefaultProgressListener implements ProgressListener {

       private Handler mHandler;
       long completed;
       public DefaultProgressListener(Handler mHandler, long completed) {
           this.completed=completed;
           this.mHandler = mHandler;
       }

        @Override
        public void onProgress(long hasWrittenLen, long totalLen, boolean hasFinish) {
            System.out.println("----the current " + hasWrittenLen + "----" + totalLen + "-----" + (hasWrittenLen * 100 / totalLen));
            finished=completed+hasWrittenLen;
            int percent = (int) ((completed+hasWrittenLen) * 100 / (totalLen+completed));
            if (percent > 100) percent = 100;
            if (percent < 0) percent = 0;
            L.e(TAG,"execute transferring");
            Message msg = Message.obtain();
            msg.obj=finished;
            mHandler.sendMessage(msg);
        }
    }
}
