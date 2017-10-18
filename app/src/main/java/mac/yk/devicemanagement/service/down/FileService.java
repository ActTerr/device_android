package mac.yk.devicemanagement.service.down;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.widget.RemoteViews;

import java.util.ArrayList;

import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.bean.FileEntry;
import mac.yk.devicemanagement.db.dbFile;
import mac.yk.devicemanagement.down.DownContract;
import mac.yk.devicemanagement.down.DownPresenter;
import mac.yk.devicemanagement.receiver.NotificationReceiver;
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
    IServiceListener listener = new IServiceListener() {
        @Override
        public void onUpdateItem(FileEntry entry) {
            presenter.updateItem(entry);
//            if (entry.getCompletedSize()== I.DOWNLOAD_STATUS.COMPLETED){
//                presenter.refreshView();
//            }

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
            ToastUtil.showToast(context, "下载失败！");
        }

    };

    RemoteViews remoteViews;


    private Notification getNotification(boolean complete, FileEntry entry) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        remoteViews = new RemoteViews(this.getPackageName(), R.layout.down_notification);

        remoteViews.setImageViewResource(R.id.iv_attachment, OpenFileUtil.getPic(entry.getSaveDirPath()));
        String title;
        mBuilder.setContent(remoteViews)
                .setWhen(System.currentTimeMillis())// 通知栏时间，一般是直接用系统的
                .setPriority(Notification.DEFAULT_ALL)// 设置通知栏优先级
                .setAutoCancel(true)// 用户单击面板后消失
                .setOngoing(false)// true,设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此
                // 占用设备(如一个文件下载，同步操作，主动网络连接)
//                .setDefaults(Notification.DEFAULT_SOUND)
                // 向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用default属性，可以组合

                // Notification.DEFAULT_ALL Notification.DEFAULT_SOUND 添加声音
                // requires VIBRATE permission
                .setSmallIcon(R.drawable.ic_notification);
        if (complete) {
            title="下载完成,请点击查看!";
            remoteViews.setTextViewText(R.id.title, "下载完成，点击查看");
            remoteViews.setProgressBar(R.id.pb, 100, 100, false);
            remoteViews.setTextViewText(R.id.time, showDate());
            mBuilder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
        } else {
            title="开始下载"+entry.getFileName();
            remoteViews.setTextViewText(R.id.title, "正在下载：" + entry.getFileName());
            int i = (int) ((entry.getCompletedSize() * 100) / entry.getToolSize());
            remoteViews.setProgressBar(R.id.pb, 100, i, false);
            remoteViews.setTextViewText(R.id.time, showDate());
        }
       mBuilder.setTicker(title);// 通知栏首次出现在通知栏，带上动画效果

        Notification notification = mBuilder.build();
        notification.flags = Notification.FLAG_ONGOING_EVENT;
        notification.flags = Notification.FLAG_NO_CLEAR;// 点击清除的时候不清除
//      Intent realIntent = new Intent(getApplicationContext(), MainActivity.class);
//      realIntent.addCategory(Intent.CATEGORY_LAUNCHER);
//      realIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        Intent clickIntent = new Intent("action_click",null,context, NotificationReceiver.class);
        clickIntent.putExtra("nid", entry.getNid());
//      clickIntent.setAction("action_click");
        Intent dismissIntent = new Intent("action_dismiss", null, context, NotificationReceiver.class);
//      intent.putExtra("realIntent", realIntent);
//      PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent,
//              0);
        PendingIntent clickPendingIntent = PendingIntent.getBroadcast(this, 0, clickIntent,
                0);
//        PendingIntent dismissPendingIntent = PendingIntent.getBroadcast(this, 0, dismissIntent,
//                0);
        mBuilder.setContentIntent(clickPendingIntent);
//        mBuilder.setDeleteIntent(dismissPendingIntent);

        return mBuilder.build();
    }



    public void setPresenter(DownPresenter presenter) {
        this.presenter = presenter;
        mdbFile = presenter.getDbEntry();

    }

    public void setEntries(ArrayList<FileEntry> entries) {
        this.entries = entries;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        L.e(TAG, "service onCreate");
        context = this;
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

    }


    private FileBinder fileBinder = new FileBinder();

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

        final FileTask fileTask = new FileTask(entry, mdbFile, listener, mNotificationManager);
        fileTasks.add(fileTask);
        L.e(TAG, "task start");
        new Thread(new Runnable() {
            @Override
            public void run() {
                fileTask.onStartUpload();
            }
        }).start();

    }

    @Override
    public void downloadFile(final FileEntry entry) {

        final FileTask task = new FileTask(entry, mdbFile, listener, mNotificationManager);
        fileTasks.add(task);
        new Thread(new Runnable() {
            @Override
            public void run() {
                L.e(TAG, "点击下载2" + task.entry.getFileName());
                task.onStartDownload();
            }
        }).start();

    }

    @Override
    public void downloadFiles(ArrayList<FileEntry> entries) {
        for (final FileEntry entry : entries) {
            final FileTask task = new FileTask(entry, mdbFile, listener, mNotificationManager);
            fileTasks.add(task);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    L.e(TAG, entry.getFileName() + "开始下载");
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
            task = new FileTask(entry, mdbFile, listener, mNotificationManager);
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
            task = new FileTask(entry, mdbFile, listener, mNotificationManager);
            task.onCancelUpload();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        L.e(TAG, "destroy");
    }

    public class FileBinder extends Binder {
        public FileService getService() {
            return FileService.this;
        }
    }

}
