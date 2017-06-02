package mac.yk.devicemanagement.service.down;

public interface FileTaskListener {

    void onStartDownload();

    void onStartUpload();


    boolean onPauseUpload();

    boolean onPauseDownload();

    boolean onCancelUpload();

    boolean onCancelDownload();

    void onCompletedUpload(long newId);

    void onCompletedDownload();

    void onError(FileTask fileTask, int errorCode);


    int DOWNLOAD_ERROR_FILE_NOT_FOUND = -1;
    int DOWNLOAD_ERROR_IO_ERROR = -2;

}
