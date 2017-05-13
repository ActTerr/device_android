package mac.yk.devicemanagement.bean;

import java.io.Serializable;

/**
 * Created by mac-yk on 2017/5/12.
 */

public class FileEntry implements Serializable{

    private long Aid;
    private long toolSize;
    private long completedSize;
    private String url;
    private String saveDirPath;
    private String fileName;
    private int downloadStatus;
    private long Nid;
    

    public FileEntry(long Aid, long toolSize, long completedSize, String url,
                     String saveDirPath, String fileName, int downloadStatus,long Nid) {
        this.toolSize = toolSize;
        this.completedSize = completedSize;
        this.url = url;
        this.saveDirPath = saveDirPath;
        this.fileName = fileName;
        this.downloadStatus = downloadStatus;
        this.Aid=Aid;
        this.Nid=Nid;
    }

    public long getAid() {
        return Aid;
    }

    public void setAid(long aid) {
        Aid = aid;
    }

    public long getNid() {
        return Nid;
    }

    public void setNid(long nid) {
        Nid = nid;
    }

    public long getToolSize() {
        return toolSize;
    }

    public void setToolSize(long toolSize) {
        this.toolSize = toolSize;
    }

    public long getCompletedSize() {
        return completedSize;
    }

    public void setCompletedSize(long completedSize) {
        this.completedSize = completedSize;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSaveDirPath() {
        return saveDirPath;
    }

    public void setSaveDirPath(String saveDirPath) {
        this.saveDirPath = saveDirPath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getDownloadStatus() {
        return downloadStatus;
    }

    public void setDownloadStatus(int downloadStatus) {
        this.downloadStatus = downloadStatus;
    }





    public FileEntry() {
    }
}
