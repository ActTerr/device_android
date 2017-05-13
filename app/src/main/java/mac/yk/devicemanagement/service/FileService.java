package mac.yk.devicemanagement.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import java.io.File;

import mac.yk.devicemanagement.bean.FileEntry;
import mac.yk.devicemanagement.net.ApiWrapper;
import mac.yk.devicemanagement.net.ServerAPI;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Subscriber;

/**
 * Created by mac-yk on 2017/5/13.
 */

public class FileService extends IntentService implements IFile{
    public static int DOWNLOAD=1;
    public static int UPLOAD=2;
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
        int type=intent.getIntExtra("type",0);
        FileEntry entry= (FileEntry) intent.getSerializableExtra("entry");
        if (type==DOWNLOAD){
            downloadFile(entry);
        }else {
            uploadFile(entry);
        }
    }

    @Override
    public void uploadFile(FileEntry entry) {
        File file=new File(entry.getSaveDirPath());
        RequestBody requestBody = RequestBody.create(MediaType.parse("txt/*"), file);
        ApiWrapper<ServerAPI> wrapper=new ApiWrapper<>();
        wrapper.targetClass(ServerAPI.class).getAPI()
                .addAttachment(requestBody,entry.getNid(),entry.getToolSize())
                .compose(wrapper.<String>applySchedulers())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String s) {

                    }
                });
    }

    @Override
    public void downloadFile(FileEntry entry) {

    }
}
