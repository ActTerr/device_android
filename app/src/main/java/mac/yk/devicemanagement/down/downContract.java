package mac.yk.devicemanagement.down;

import android.content.Intent;

import java.io.File;
import java.util.ArrayList;

import mac.yk.devicemanagement.BasePresenter;
import mac.yk.devicemanagement.BaseView;
import mac.yk.devicemanagement.bean.FileEntry;
import mac.yk.devicemanagement.ui.holder.AttachmentViewHolder;

/**
 * Created by mac-yk on 2017/5/25.
 */

public interface downContract {
    interface View extends BaseView<Presenter>{
        void refreshView();
//        void setUnEditStatus(AttachmentViewHolder holder);
//        void setEditStatus(AttachmentViewHolder holder);
        void showProgressDialog();
        void dismissProgressDialog();
        void toastException();
        void toastString(String s);
        void choseFile(Intent intent);
        void setEntries(ArrayList<FileEntry> entries);
        void startService(FileEntry entry);
        void completedDownload(FileEntry entry);
    }
    interface Presenter extends BasePresenter{
        /**
         * 调用apiWrapper
         */

        void getAttachments();
        void deleteAttachment(FileEntry entry);
        void updateAttachment(FileEntry entry, String text,  String type,  AttachmentViewHolder holder);

        /**
         * 调用service
          */
        void uploadFile(File file);
        void downloadFile(FileEntry entry);
        void downloadFiles(ArrayList<FileEntry> entries);
        /**
         * 调用db
         */
        void insertEntry(FileEntry fileEntry);
        void updateEntry(FileEntry fileEntry);
        void selectEntry(String name);
        void deleteEntry(String name);

        void stopDownload(String name);
        void cancelDownload(FileEntry entry);

        void stopUpload(String name);
        void cancelUpload(FileEntry entry);

        void refreshView();
        void updateProgress(String name,long completed);
        void completedUpload(FileEntry entry);
        void completedDownload(FileEntry entry);
        void startDownload(String name,long totalSize);
    }
}
