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
        void showProgress(int i,AttachmentViewHolder holder);
        void refreshView();
//        void setUnEditStatus(AttachmentViewHolder holder);
//        void setEditStatus(AttachmentViewHolder holder);
        void showProgressDialog();
        void dismissProgressDialog();
        void showProgress(AttachmentViewHolder holder);
        void toastException();
        void toastString(String s);
        void choseFile(Intent intent);
        void setEntries(ArrayList<FileEntry> entries);
        void startService(FileEntry entry);
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
        void downloadFile(FileEntry entry,  AttachmentViewHolder holder);
        void downloadFiles(ArrayList<FileEntry> entries,  ArrayList<AttachmentViewHolder> holders);
        /**
         * 调用db
         */
        void insertEntry(FileEntry fileEntry);
        void updateEntry(FileEntry fileEntry);
        void selectEntry(String name);
        void deleteEntry(String name);

        void stopDownload(String name);
        void cancelDownload(String name);

        void stopUpload(String name);
        void cancelUpload(String name);

        void updateProgress(int i, AttachmentViewHolder holder);
        void transferFinish(AttachmentViewHolder holder);
    }
}
