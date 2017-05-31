package mac.yk.devicemanagement.service.down;

import java.util.ArrayList;

import mac.yk.devicemanagement.bean.FileEntry;

/**
 * Created by mac-yk on 2017/5/13.
 */

public interface IFile {
    void uploadFile(FileEntry entry);
    void downloadFile(FileEntry entry);
    void downloadFiles(ArrayList<FileEntry> entries);
    void stopDownload(String name);
    void cancelDownload(FileEntry entry);
    void stopUpload(String name);
    void cancelUpload(FileEntry entry);
}
