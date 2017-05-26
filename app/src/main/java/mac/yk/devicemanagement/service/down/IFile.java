package mac.yk.devicemanagement.service.down;

import java.util.ArrayList;

import mac.yk.devicemanagement.bean.FileEntry;
import mac.yk.devicemanagement.ui.holder.AttachmentViewHolder;

/**
 * Created by mac-yk on 2017/5/13.
 */

public interface IFile {
    void uploadFile(FileEntry entry, AttachmentViewHolder holder);
    void downloadFile(FileEntry entry, AttachmentViewHolder holder);
    void downloadFiles(ArrayList<FileEntry> entries,ArrayList<AttachmentViewHolder> holders);
    void stopDownload(String name);
    void cancelDownload(String name);
    void stopUpload(String name);
    void cancelUpload(String name);
}
