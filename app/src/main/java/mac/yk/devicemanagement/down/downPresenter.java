package mac.yk.devicemanagement.down;

import android.content.Intent;
import android.support.annotation.NonNull;

import com.ipaulpro.afilechooser.utils.FileUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import mac.yk.devicemanagement.I;
import mac.yk.devicemanagement.bean.Attachment;
import mac.yk.devicemanagement.bean.FileEntry;
import mac.yk.devicemanagement.db.IdbFileEntry;
import mac.yk.devicemanagement.net.IDown;
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
    private final BaseSchedulerProvider mSchedulerProvider;

    @NonNull
    long nid;

    CompositeSubscription subscription;


    public ArrayList<FileEntry> entries;
    public downPresenter(@NonNull downContract.View view, @NonNull IDown Down,IdbFileEntry dbEntry
            ,@NonNull BaseSchedulerProvider mSchedulerProvider,@NonNull long nid) {
        this.view = view;
        this.IDown = Down;
        this.mSchedulerProvider = mSchedulerProvider;
        this.nid = nid;
        this.dbEntry=dbEntry;
        subscription=new CompositeSubscription();
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
                        return entry(attachment);
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
                        view.dismissProgressDialog();
                        entries= (ArrayList<FileEntry>) fileEntries;
                        sort(entries);
                        view.setEntries(entries);
                        view.refreshView();
                    }
                }));


    }

    /**
     * 根据服务器获得列表，转换成entry实体，未下载就新建一个空的，最后按时间进行排序
     *
     */
    private FileEntry entry(Attachment a){
        FileEntry fileEntry = dbEntry.getFileEntry(a.getName());
        if (fileEntry ==null){
            fileEntry= new FileEntry(a.getAid(), 0L, 0L,
                    OpenFileUtil.getPath(a.getName()),
                    a.getName(), I.DOWNLOAD_STATUS.NOT, a.getNid());

        }
        return fileEntry;
    }


    private void sort(List<FileEntry> fileEntries) {
        Collections.sort(fileEntries, new Comparator<FileEntry>() {
            @Override
            public int compare(FileEntry o1, FileEntry o2) {
                return (int) ((int) o2.getAid()-o1.getAid());
            }
        });
    }


    @Override
    public void deleteAttachment(FileEntry entry) {
        view.showProgressDialog();
        subscription.add(IDown.deleteAttachment(entry)
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
                if (dbEntry.deleteFileEntry(entry.getFileName())){
                    entries.remove(entry);
                    view.refreshView();
                    view.toastString("删除成功！");
                }

            }
        }));

    }

    @Override
    public void updateAttachment(FileEntry entry, String text, String type, AttachmentFragment.AttachmentAdapter.AttachmentViewHolder holder) {
        view.showProgressDialog();
        subscription.add(IDown.updateAttachment(entry,text,type)
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
                if (type.equals("1")){
                    Intent getContentIntent = FileUtils.createGetContentIntent();
                    Intent intent = Intent.createChooser(getContentIntent, "Select a file");
                    view.showProgress(holder);
                    dbEntry.deleteFileEntry(entry.getFileName());
                    entries.remove(entry);
                    view.refreshView();
                    view.choseFile(intent);
                }else {
                    entry.setFileName(text);
                    entry.setAid(System.currentTimeMillis());
                    if (dbEntry.updateFileName(entry.getFileName(),text)){
                        view.refreshView();
                    }
                }

            }
        }));
    }

    @Override
    public void uploadFile(FileEntry entry) {

    }

    @Override
    public void downloadFile(FileEntry entry) {

    }

    @Override
    public void downloadFiles(ArrayList<FileEntry> entries) {

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
}
