package mac.yk.devicemanagement.uncaught;

import android.content.Context;

import java.io.File;

/**
 * Created by mac-yk on 2016/12/6.
 */

public interface IunCaught {
    File UploadCrashInfo(Context context, Throwable cause);
    boolean postServer(Context context, File File);
}
