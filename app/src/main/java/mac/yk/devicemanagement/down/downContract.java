package mac.yk.devicemanagement.down;

import android.content.Intent;

import java.util.ArrayList;

import mac.yk.devicemanagement.BasePresenter;
import mac.yk.devicemanagement.BaseView;
import mac.yk.devicemanagement.bean.FileEntry;

/**
 * Created by mac-yk on 2017/5/25.
 */

public interface downContract {
    interface View extends BaseView<Presenter>{
        void showProgress(int i,AttachmentFragment.AttachmentAdapter.AttachmentViewHolder holder);
        void refreshView();
        void setUnEditStatus(AttachmentFragment.AttachmentAdapter.AttachmentViewHolder holder);
        void setEditStatus(AttachmentFragment.AttachmentAdapter.AttachmentViewHolder holder);
        void showProgressDialog();
        void dismissProgressDialog();
        void showProgress(AttachmentFragment.AttachmentAdapter.AttachmentViewHolder holder);
        void toastException();
        void toastString(String s);
        void choseFile(Intent intent);
        void setEntries(ArrayList<FileEntry> entries);
    }
    interface Presenter extends BasePresenter{
        /**
         * 调用apiWrapper
         */

        void getAttachments();
        void deleteAttachment(FileEntry entry);
        void updateAttachment(FileEntry entry, String text,  String type,  AttachmentFragment.AttachmentAdapter.AttachmentViewHolder holder);

        /**
         * 调用service
          */
        void uploadFile(FileEntry entry);
        void downloadFile(FileEntry entry);
        void downloadFiles(ArrayList<FileEntry> entries);
        /**
         * 调用db
         */
        void insertEntry(FileEntry fileEntry);
        void updateEntry(FileEntry fileEntry);
        void selectEntry(String name);
        void deleteEntry(String name);
    }
}
