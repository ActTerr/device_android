package mac.yk.devicemanagement.service.down;

import mac.yk.devicemanagement.bean.FileEntry;

/**
 * Created by mac-yk on 2017/5/28.
 */

interface IServiceListener {

    void startDownload(String name,long total);

     void upDateNotification();


   void cancelNotification();


     void onTransferring(String name, long completed);


  void onCompletedUpload(FileEntry entry);


   void onCompletedDownload(FileEntry entry);
}
