package mac.yk.devicemanagement.service.down;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.RemoteViews;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import mac.yk.devicemanagement.bean.FileEntry;
import mac.yk.devicemanagement.down.downContract;
import mac.yk.devicemanagement.down.downPresenter;
import mac.yk.devicemanagement.ui.holder.AttachmentViewHolder;
import mac.yk.devicemanagement.util.L;
import rx.Subscription;

/**
 * Created by mac-yk on 2017/5/13.
 */

public class FileService extends Service implements IFile {
    public static String START_DOWN = "startDownload";
    public static String START_DOWN_FILES = "startDownloadFiles";
    public static String START_UPLOAD = "startUpload";
    public static String PAUSE_DOWN = "pauseDownload";
    public static String PAUSE_UPLOAD = "pauseUpload";
    public static String TRANSFER = "transferring";
    public static String COMPLETED_DOWN="downloadCompleted";
    public static String COMPLETED_UPLOAD="uploadCompleted";

    public static int DOWNLOAD = 1;
    public static int UPLOAD = 2;
   Context context;
    String TAG = "FileService";
    long finished;
    Subscription subscribe;
    downContract.Presenter presenter;
    ArrayList<FileTask> fileTasks = new ArrayList<>();
    ArrayList<AttachmentViewHolder> holders = new ArrayList<>();
    private RemoteViews remoteViews;
    private NotificationManager mNotificationManager;
    private ExecutorService executorService;

    public void setPresenter(downPresenter presenter){
        this.presenter=presenter;
    }
    IServiceListener listener=new IServiceListener() {
        @Override
        public void startDownload(String name,long total) {
            presenter.startDownload(name,total);
        }

        @Override
        public void upDateNotification() {

        }

        @Override
        public void cancelNotification() {

        }

        @Override
        public void onTransferring(String name, long completed) {
            L.e(TAG,"get progress");
            presenter.updateProgress(name,completed);
        }

        @Override
        public void onCompletedUpload(FileEntry entry) {
            L.e(TAG,"execute completed upload");
            presenter.transferFinish(entry);
        }

        @Override
        public void onCompletedDownload(FileEntry entry) {
            presenter.transferFinish(entry);
        }
    };
//    IServiceListener listener=new IServiceListener() {
//        @Override
//        public void upDateNotification() {
//
//            if (!isForeground) {
//                startForeground(notificationid, getNotification(false));
//                isForeground = true;
//            } else {
//                mNotificationManager.notify(notificationid, getNotification(false));
//            }
//        }
//
//        @Override
//        public void cancleNotification() {
//            stopForeground(true);
//            isForeground = false;
//            mNotificationManager.notify(notificationid, getNotification(true));
//            downTaskCount = 0;
//            downTaskDownloaded = -1;
//        }
//    };

//    private Notification getNotification(boolean complete) {
//
////        if (downTaskCount == 0) {
////            downTaskCount = prepareTaskList.size();
////        }
////        L.d( TAG, "notification downtaskcount = " + downTaskCount);
////        if (downTaskDownloaded == -1) {
////            downTaskDownloaded = 0;
////        }
//        remoteViews = new RemoteViews(context.getPackageName(), R.layout.down_notification);
//
////        PendingIntent pendingIntent = PendingIntent.getActivity(context.getApplicationContext(), 0,
////                new Intent(context.getApplicationContext(), DownActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
//
//
//        final Intent nowPlayingIntent = new Intent();
//        nowPlayingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        nowPlayingIntent.setComponent(new ComponentName("com.wm.remusic", "com.wm.remusic.activity.DownActivity"));
//        PendingIntent clickIntent = PendingIntent.getActivity(context,0,nowPlayingIntent,PendingIntent.FLAG_UPDATE_CURRENT);
//        remoteViews.setImageViewResource(R.id.image, R.drawable.placeholder_disk_210);
//        if(complete){
//            remoteViews.setTextViewText(R.id.title, "remusic" );
//            remoteViews.setTextViewText(R.id.text, "下载完成，点击查看" );
//            remoteViews.setTextViewText(R.id.time, showDate());
//        }else {
//            remoteViews.setTextViewText(R.id.title, "下载进度：" + finished + "/" + file.length());
//            remoteViews.setTextViewText(R.id.text, "正在下载：" + file.getName());
//            remoteViews.setTextViewText(R.id.time, showDate());
//        }
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(context).setContent(remoteViews)
//                .setSmallIcon(R.drawable.ic_notification)
//                .setContentIntent(clickIntent);
//
////        if (CommonUtils.isJellyBeanMR1()) {
////            builder.setShowWhen(false);
////        }
//        return builder.build();
//    }
    @Override
    public void onCreate() {
        super.onCreate();
//        mNotificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        executorService = Executors.newSingleThreadExecutor();

    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        if (intent.getAction().equals("startDownload")){
//        };
        L.e(TAG, "service start");
//        EventBus.getDefault().register(this);
        context = this;
//        int type=intent.getIntExtra("type",0);
//        FileEntry entry= (FileEntry) intent.getSerializableExtra("entry");

        return super.onStartCommand(intent, flags, startId);
    }

    File file;
    @Override
    public void uploadFile(final FileEntry entry) {
        final FileTask fileTask = new FileTask(entry, context,listener);
        fileTasks.add(fileTask);
        L.e("cao","task start");
        fileTask.onStartUpload();

    }


    @Override
    public void downloadFile(FileEntry entry) {
        FileTask fileTask = new FileTask(entry, context,listener);
        fileTasks.add(fileTask);
        fileTask.onStartDownload();
    }

    @Override
    public void downloadFiles(ArrayList<FileEntry> entries) {
        for (FileEntry entry : entries) {
            FileTask fileTask = new FileTask(entry, context, listener);
            fileTasks.add(fileTask);
            fileTask.onStartDownload();
        }

    }


    @Override
    public void stopDownload(String name) {
        FileTask task = getTask(name);
        if (task != null) {
            task.onPauseDownload();
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
    public void cancelDownload(FileEntry entry) {
        FileTask task = getTask(entry.getFileName());
        if (task == null) {
            task=new FileTask(entry,context,listener);
            task.onCancelDownload();
        }
    }

    @Override
    public void stopUpload(String name) {
        FileTask task = getTask(name);
        if (task != null) {
            task.onPauseUpload();
        }
    }



    @Override
    public void cancelUpload(FileEntry entry) {
        FileTask task = getTask(entry.getFileName());
        if (task == null) {
            task=new FileTask(entry,context,listener);
            task.onCancelUpload();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        L.e(TAG, "destroy");
    }





}
