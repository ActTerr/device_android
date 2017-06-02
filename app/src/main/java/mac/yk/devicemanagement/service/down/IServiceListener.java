package mac.yk.devicemanagement.service.down;

/**
 * Created by mac-yk on 2017/5/28.
 */

interface IServiceListener {

   void onStartTransfer();

     void upDateNotification();


   void cancelNotification();



   void onCompletedTransfer();


}
