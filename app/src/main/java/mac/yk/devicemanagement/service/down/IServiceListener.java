package mac.yk.devicemanagement.service.down;

import mac.yk.devicemanagement.bean.FileEntry;

/**
 * Created by mac-yk on 2017/5/28.
 */

interface IServiceListener {

   void onUpdateItem(FileEntry entry);

   void updateNotification(int id, boolean b, FileEntry entry);

   void cancelNotification(int id,boolean b,FileEntry entry);


   void showDownloadDefeat();
}
