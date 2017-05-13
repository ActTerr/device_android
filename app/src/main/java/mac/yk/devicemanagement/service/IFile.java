package mac.yk.devicemanagement.service;

import mac.yk.devicemanagement.bean.FileEntry;

/**
 * Created by mac-yk on 2017/5/13.
 */

public interface IFile {
    void uploadFile(FileEntry entry);
    void downloadFile(FileEntry entry);
}
