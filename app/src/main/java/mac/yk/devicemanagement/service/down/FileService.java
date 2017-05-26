package mac.yk.devicemanagement.service.down;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import mac.yk.devicemanagement.bean.FileEntry;
import mac.yk.devicemanagement.down.downContract;
import mac.yk.devicemanagement.ui.holder.AttachmentViewHolder;
import mac.yk.devicemanagement.util.L;
import rx.Subscription;

/**
 * Created by mac-yk on 2017/5/13.
 */

public class FileService extends Service implements IFile {
    public static int DOWNLOAD=1;
    public static int UPLOAD=2;
    Context context;
    String TAG="FileService";
    long finished;
    Subscription subscribe;
    static downContract.Presenter presenter;
    ArrayList<FileTask> fileTasks=new ArrayList<>();
    ArrayList<AttachmentViewHolder> holders=new ArrayList<>();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        L.e(TAG,"service start");
        EventBus.getDefault().register(this);
        context=getApplicationContext();
//        int type=intent.getIntExtra("type",0);
//        FileEntry entry= (FileEntry) intent.getSerializableExtra("entry");

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void uploadFile(FileEntry entry,AttachmentViewHolder holder) {
        FileTask fileTask=new FileTask(entry,context,fileTasks.size());
        fileTasks.add(fileTask);
        holders.add(holder);
        fileTask.onStartUpload();
    }




    @Override
    public void downloadFile(FileEntry entry,AttachmentViewHolder holder) {
        FileTask fileTask=new FileTask(entry,context,fileTasks.size());
        fileTasks.add(fileTask);
        holders.add(holder);
        fileTask.onStartDownload();
    }

    @Override
    public void downloadFiles(ArrayList<FileEntry> entries,ArrayList<AttachmentViewHolder> holders) {
        int i=0;
        for (FileEntry entry:entries){
            FileTask fileTask=new FileTask(entry,context,fileTasks.size());
            fileTasks.add(fileTask);
            fileTask.onStartDownload();
        }
        for (AttachmentViewHolder holder:holders){
            this.holders.add(holder);
        }
    }


    @Override
    public void stopDownload(String name) {
        FileTask task=getTask(name);
        if (task!=null){
            task.onPause();
        }
    }

    private FileTask getTask(String name) {
        for (FileTask task : fileTasks) {
            if (task.entry.getFileName().equals(name)) {
                return task;
            }
        }
        return null;
    }

    @Override
    public void cancelDownload(String name) {
        FileTask task=getTask(name);
        if (task!=null){
            task.onCancelDownload();
        }
    }

    @Override
    public void stopUpload(String name) {
        FileTask task=getTask(name);
        if (task!=null){
            task.onPause();
        }
    }

    @Override
    public void cancelUpload(String name) {
        FileTask task=getTask(name);
        if (task!=null){
            task.onCancelUpload();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        L.e(TAG,"destroy");
    }


    public static void setPresenter(downContract.Presenter pre){
        presenter=pre;
    }

    @Subscribe
   public void getProcess(int id,int percent){
    presenter.updateProgress(percent,holders.get(id));
   }

   @Subscribe
    public void onCompleted(int id){
       presenter.transferFinish(holders.get(id));
   }






}
