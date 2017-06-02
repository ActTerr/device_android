package mac.yk.devicemanagement.down;

import android.support.annotation.NonNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import mac.yk.devicemanagement.I;
import mac.yk.devicemanagement.bean.Attachment;
import mac.yk.devicemanagement.bean.FileEntry;
import mac.yk.devicemanagement.db.IdbFileEntry;
import mac.yk.devicemanagement.net.IDown;
import mac.yk.devicemanagement.service.down.FileService;
import mac.yk.devicemanagement.util.L;
import mac.yk.devicemanagement.util.OpenFileUtil;
import mac.yk.devicemanagement.util.schedulers.BaseSchedulerProvider;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by mac-yk on 2017/5/25.
 */

public class downPresenter implements downContract.Presenter{
    String TAG="downPresenter";

    @NonNull
    downContract.View view;

    @NonNull
    IDown IDown;

    @NonNull
    IdbFileEntry dbEntry;

    @NonNull
    FileService service;

    @NonNull
    private final BaseSchedulerProvider mSchedulerProvider;

    @NonNull
    long nid;

    CompositeSubscription subscription;


    public ArrayList<FileEntry> entries;
    public downPresenter(@NonNull downContract.View view, @NonNull IDown Down,IdbFileEntry dbEntry
            ,@NonNull BaseSchedulerProvider mSchedulerProvider,@NonNull long nid, @NonNull
                                 FileService service) {
        this.view = view;
        this.IDown = Down;
        this.mSchedulerProvider = mSchedulerProvider;
        this.nid = nid;
        this.dbEntry=dbEntry;
        this.service=service;
        subscription=new CompositeSubscription();
        view.setPresenter(this);
        service.setPresenter(this);
    }




    @Override
    public void subscribe() {
        getAttachments();
    }

    @Override
    public void unSubscribe() {
        subscription.unsubscribe();
    }

    @Override
    public void getAttachments() {
        view.showProgressDialog();
        subscription.add(IDown
                .getAttachments(nid)
                .subscribeOn(mSchedulerProvider.io())
                .observeOn(mSchedulerProvider.ui())
                .flatMap(new Func1<ArrayList<Attachment>, Observable<Attachment>>(){
                    @Override
                    public Observable<Attachment> call(ArrayList<Attachment> attachments) {
                        return Observable.from(attachments);
                    }
                }).map(new Func1<Attachment, FileEntry>() {
                    @Override
                    public FileEntry call(Attachment attachment) {
                        return changeEntry(attachment);
                    }
                })
                .toList()
                .subscribe(new Subscriber<List<FileEntry>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        view.dismissProgressDialog();
                        view.toastException();
                    }

                    @Override
                    public void onNext(List<FileEntry> fileEntries) {
                        showAttachments(fileEntries);
                    }
                }));
//                .subscribe(
//                        // onNext
//                        this::showAttachments,
//                        // onError
//                        throwable -> {view.dismissProgressDialog();
//                                        view.toastException();}
//                        ));

    }
    private void showAttachments(List<FileEntry> fileEntries){
        view.dismissProgressDialog();
        entries= (ArrayList<FileEntry>) fileEntries;
        sort();
        view.setEntries(entries);
        view.refreshView();
    }

    /**
     * 根据服务器获得列表，转换成entry实体
     * 必须统一路径
     * 1.admin:如果空的，就New一个，
     * 2.user:
     *
     */
    private FileEntry changeEntry(Attachment a) {
        FileEntry fileEntry = dbEntry.getFileEntry(a.getName());
        if (fileEntry == null) {
            fileEntry = new FileEntry(a.getAid(),0L, 0L,
                    OpenFileUtil.getPath(a.getName()),
                    a.getName(), I.DOWNLOAD_STATUS.INIT, a.getNid());
            dbEntry.insertFileEntry(fileEntry);
        }
        return fileEntry;
    }

    private void sort() {
        Collections.sort(entries, new Comparator<FileEntry>() {
            @Override
            public int compare(FileEntry o1, FileEntry o2) {
                return (int) ((int) o2.getAid()-o1.getAid());
            }
        });
    }

    FileEntry deleteEntry;
    @Override
    public void deleteAttachment(final FileEntry entry, final boolean isUpdate, final File file) {
        if (isUpdate){
            deleteEntry=memory;
        }else {
            deleteEntry=entry;
        }
        view.showProgressDialog();
        subscription.add(IDown.deleteAttachment(deleteEntry)
                .subscribeOn(mSchedulerProvider.io())
        .observeOn(mSchedulerProvider.ui())
        .subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                view.dismissProgressDialog();
                view.toastException();
            }

            @Override
            public void onNext(String s) {
                view.dismissProgressDialog();
                if (dbEntry.deleteFileEntry(deleteEntry.getFileName())){
                    entries.remove(deleteEntry);
                    sort();
                    view.refreshView();
                    if (!isUpdate){
                        view.toastString("删除成功！");
                    }else {
                        uploadFile(file);
                        memory=null;
                    }
                }
            }
        }));


    }


    @Override
    public void updateAttachment(final FileEntry entry, final String text) {
        view.showProgressDialog();
        subscription.add(IDown.updateAttachment(entry,text)
              .subscribeOn(mSchedulerProvider.io())
                .observeOn(mSchedulerProvider.ui())
        .subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                view.dismissProgressDialog();
                view.toastException();
            }

            @Override
            public void onNext(String s) {
                view.dismissProgressDialog();
                    String name=entry.getFileName();
                    entry.setFileName(text);
                    long updateTime=System.currentTimeMillis();
                    entry.setAid(updateTime);
                    if (dbEntry.updateFileName(name,text,updateTime)){
                        sort();
                        view.refreshView();
                    }
                view.toastString("保存成功！");

            }
        }));
    }


    public void filterFile(File file){
        for(FileEntry entry:entries){
            if (entry.getFileName().equals(file.getName())){
                view.showDialog(entry,file);
                return;
            }
        }
        uploadFile(file);
    }


    public void updateFile(FileEntry entry,File file){
        entries.remove(entry);
        deleteAttachment(entry,true,file);


    }
    /**
     * 1.正常上传 执行下面的方法
     * 2.更新 先删除之前的，再执行下面的方法
     *
     * @param file
     */
    @Override
    public void uploadFile(File file) {

        FileEntry entry = new FileEntry();
            entry.setFileName(file.getName());
            entry.setSaveDirPath(file.getAbsolutePath());
            entry.setCompletedSize(0L);
            entry.setNid(nid);
            entry.setAid(System.currentTimeMillis());
            entry.setDownloadStatus(I.DOWNLOAD_STATUS.INIT);
            entry.setToolSize(file.length());
            L.e(TAG, String.valueOf(file.length()));
            entries.add(entry);
            sort();
            view.refreshView();
        if (dbEntry.insertFileEntry(entry)){
            service.uploadFile(entry);
        }
    }

    @Override
    public void uploadFile(FileEntry entry) {
        service.uploadFile(entry);
    }


    @Override
    public void downloadFile(FileEntry entry) {

         service.downloadFile(entry);
    }

    @Override
    public void downloadFiles(ArrayList<FileEntry> entries) {
        service.downloadFiles(entries);
    }

    @Override
    public void insertEntry(FileEntry fileEntry) {

    }

    @Override
    public void updateEntry(FileEntry fileEntry) {

    }

    @Override
    public void selectEntry(String name) {

    }

    @Override
    public void deleteEntry(String name) {

    }

    @Override
    public void stopDownload(String name) {
        service.stopDownload(name);
    }

    @Override
    public void cancelDownload(FileEntry entry) {
        service.cancelDownload(entry);
    }

    @Override
    public void stopUpload(String name) {
        service.stopUpload(name);
    }

    @Override
    public void cancelUpload(FileEntry entry) {
        service.cancelUpload(entry);
        entries.remove(entry);
        view.refreshView();
    }

    @Override
    public void refreshView() {

        view.refreshView();
    }

    FileEntry memory;
    @Override
    public void setMemory(FileEntry entry) {
        memory=entry;
    }


    @NonNull
    public IdbFileEntry getDbEntry() {
        return dbEntry;
    }


}