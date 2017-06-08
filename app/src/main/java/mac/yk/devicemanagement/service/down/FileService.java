package mac.yk.devicemanagement.service.down;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.bean.FileEntry;
import mac.yk.devicemanagement.db.dbFile;
import mac.yk.devicemanagement.down.DownContract;
import mac.yk.devicemanagement.down.DownPresenter;
import mac.yk.devicemanagement.util.CommonUtils;
import mac.yk.devicemanagement.util.L;
import mac.yk.devicemanagement.util.OpenFileUtil;
import mac.yk.devicemanagement.util.ToastUtil;
import rx.Subscription;

import static mac.yk.devicemanagement.service.down.FileTask.showDate;

/**
 * Created by mac-yk on 2017/5/13.
 */

public class FileService extends Service implements IFile {
    public static final String START_DOWN = "startDownload";
    public static final String START_DOWN_FILES = "startDownloadFiles";
    public static final String START_UPLOAD = "startUpload";
    public static final String PAUSE_DOWN = "pauseDownload";
    public static final String PAUSE_UPLOAD = "pauseUpload";
    public static final String TRANSFER = "transferring";
    public static final String COMPLETED_DOWN = "downloadCompleted";
    public static final String COMPLETED_UPLOAD = "uploadCompleted";

    public static int DOWNLOAD = 1;
    public static int UPLOAD = 2;
    Context context;
    String TAG = "FileService";
    Subscription subscribe;
    DownContract.Presenter presenter;
    ArrayList<FileTask> fileTasks = new ArrayList<>();
    ArrayList<FileEntry> entries;
    private NotificationManager mNotificationManager;
    dbFile mdbFile;
    Timer timer = new Timer();
    TimerTask timerTask;
    boolean running=false;
    IServiceListener listener = new IServiceListener() {
        @Override
        public void onUpdateItem(FileEntry entry) {
            presenter.updateItem(entry);

        }

        boolean isForeground;

        @Override
        public void updateNotification(int id, boolean b, FileEntry entry) {
            if (!isForeground) {
                startForeground(id, getNotification(b, entry));
                isForeground = true;
            } else {
                mNotificationManager.notify(id, getNotification(false, entry));
            }

        }


        @Override
        public void cancelNotification(int id, boolean b, FileEntry entry) {
            stopForeground(true);
            isForeground = false;
            mNotificationManager.notify(id, getNotification(b, entry));
        }




        @Override
        public void showDownloadDefeat() {
            ToastUtil.showToast(context,"下载失败！");
        }

    };

    RemoteViews remoteViews;

    private Notification getNotification(boolean complete, FileEntry entry) {

        remoteViews = new RemoteViews(this.getPackageName(), R.layout.down_notification);

//        PendingIntent pendingIntent = PendingIntent.getActivity(context.getApplicationContext(), 0,
//                new Intent(context.getApplicationContext(), DownActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);


        final Intent nowPlayingIntent = new Intent();
        nowPlayingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        nowPlayingIntent.setComponent(new ComponentName("mac.yk.devicemanagement", "mac.yk.devicemanagement.ui.activity.NoticeDetailActivity"));
        PendingIntent clickIntent = PendingIntent.getActivity(this, 0, nowPlayingIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setImageViewResource(R.id.iv_attachment, OpenFileUtil.getPic(entry.getSaveDirPath()));
        if (complete) {
            remoteViews.setTextViewText(R.id.title, "remusic");
            remoteViews.setTextViewText(R.id.text, "下载完成，点击查看");
            remoteViews.setTextViewText(R.id.time, showDate());
        } else {
            remoteViews.setTextViewText(R.id.title, "正在下载：" + entry.getFileName());
            int i = (int) ((2* 100) / entry.getToolSize());
            remoteViews.setProgressBar(R.id.pb, 100, i, false);
            remoteViews.setTextViewText(R.id.time, showDate());
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this).setContent(remoteViews)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentIntent(clickIntent);

        if (CommonUtils.isJellyBeanMR1()) {
            builder.setShowWhen(false);
        }
        return builder.build();
    }

    public void setPresenter(DownPresenter presenter) {
        this.presenter = presenter;
        mdbFile = presenter.getDbEntry();

    }

    public void setEntries(ArrayList<FileEntry> entries){
        this.entries=entries;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        L.e(TAG,"service onCreate");
        context=this;
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

    }



    private FileBinder fileBinder=new FileBinder();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return fileBinder;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        L.e(TAG, "service start");

        return START_STICKY;
    }


    @Override
    public void uploadFile(final FileEntry entry) {
        final FileTask fileTask = new FileTask(entry,  mdbFile, listener, mNotificationManager);
        fileTasks.add(fileTask);
        L.e("cao", "task start");
        fileTask.onStartUpload();

    }

    @Override
    public void downloadFile(final FileEntry entry) {

        final FileTask task= new FileTask(entry,  mdbFile, listener, mNotificationManager);
        fileTasks.add(task);
        new Thread(new Runnable() {
            @Override
            public void run() {
                L.e("caonima","点击下载2"+task.entry.getFileName());
                task.onStartDownload();
            }
        }).start();

    }

    @Override
    public void downloadFiles(ArrayList<FileEntry> entries) {
        for (final FileEntry entry : entries) {
            final FileTask task= new FileTask(entry,  mdbFile, listener, mNotificationManager);
            fileTasks.add(task);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    L.e("caonima",entry.getFileName()+"开始下载");
                    task.onStartDownload();
                }
            }).start();

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
            task = new FileTask(entry,  mdbFile, listener, mNotificationManager);
            task.onCancelDownload();
            listener.onUpdateItem(entry);
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
            task = new FileTask(entry,  mdbFile, listener, mNotificationManager);
            task.onCancelUpload();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        L.e(TAG, "destroy");
    }

    public class FileBinder extends Binder {
        public FileService getService(){
            return FileService.this;
        }
    }

}
