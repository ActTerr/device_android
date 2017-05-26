package mac.yk.devicemanagement.service.down;

public interface FileTaskListener {

    void onStartDownload();

    void onStartUpload();

    void onTransferring(int i);

    boolean onPause();

    boolean onCancelUpload();

    boolean onCancelDownload();

    void onUploadCompleted(long newId);

    void onDownCompleted();

    void onError(FileTask fileTask, int errorCode);

    int DOWNLOAD_ERROR_FILE_NOT_FOUND = -1;
    int DOWNLOAD_ERROR_IO_ERROR = -2;

}
