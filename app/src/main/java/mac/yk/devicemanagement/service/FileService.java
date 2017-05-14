package mac.yk.devicemanagement.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

import mac.yk.devicemanagement.bean.FileEntry;
import mac.yk.devicemanagement.net.ApiWrapper;
import mac.yk.devicemanagement.net.ProgressListener;
import mac.yk.devicemanagement.net.ServerAPI;
import mac.yk.devicemanagement.net.UploadFileRequestBody;
import mac.yk.devicemanagement.util.ExceptionFilter;
import mac.yk.devicemanagement.util.ToastUtil;
import rx.Subscriber;

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
