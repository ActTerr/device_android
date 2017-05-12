package mac.yk.devicemanagement.bean;

/**
 * Created by mac-yk on 2017/5/12.
 */

public class FileEntry {

    private Long downloadId;
    private Long toolSize;
    private Long completedSize;
    private String url;
    private String saveDirPath;
    private String fileName;
    private int downloadStatus;

    public FileEntry(Long downloadId, Long toolSize, Long completedSize, String url,
                     String saveDirPath, String fileName, int downloadStatus) {
        this.toolSize = toolSize;
        this.completedSize = completedSize;
        this.url = url;
        this.saveDirPath = saveDirPath;
        this.fileName = fileName;
        this.downloadStatus = downloadStatus;
        this.downloadId=downloadId;
    }

    public Long getDownloadId() {
        return downloadId;
    }

    public void setDownloadId(Long downloadId) {
        this.downloadId = downloadId;
    }

    public Long getToolSize() {
        return toolSize;
    }

    public void setToolSize(Long toolSize) {
        this.toolSize = toolSize;
    }

    public Long getCompletedSize() {
        return completedSize;
    }

    public void setCompletedSize(Long completedSize) {
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
