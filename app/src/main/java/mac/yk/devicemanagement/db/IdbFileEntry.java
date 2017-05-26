package mac.yk.devicemanagement.db;

import mac.yk.devicemanagement.bean.FileEntry;

/**
 * Created by mac-yk on 2017/5/25.
 */

public interface IdbFileEntry {
    boolean insertFileEntry(FileEntry fileEntry);
    boolean updateFileStatus(String fileName,long completedSize,int status);
    boolean updateFileName(String oldName,String newName,long updateTime);
    boolean updateFileId(long oldId,long newId);
    FileEntry getFileEntry(String name);
    boolean deleteFileEntry(String name);
}
