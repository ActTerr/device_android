package mac.yk.devicemanagement.uncaught;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;

import mac.yk.devicemanagement.net.ApiWrapper;
import mac.yk.devicemanagement.net.ServerAPI;
import mac.yk.devicemanagement.util.ExceptionFilter;
import mac.yk.devicemanagement.util.L;
import mac.yk.devicemanagement.util.ToastUtil;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by mac-yk on 2016/12/6.
 */

public class unCaught implements IunCaught {

//    static final String KEY_REQUEST = "request";
//    static final String REQUEST_UPLOAD_CRASH = "upload_crash";
//    static final String FILE_NAME = "file_name";
//    public static final String KEY_CRASH = "key_crash";
    static final String PATH = "/Users/mac-yk/Downloads/Server/";
    boolean isSuccess=false;
    Context mContext;

    public unCaught() {
    }

    public unCaught(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public File UploadCrashInfo(final Context context, Throwable cause) {
        StringBuilder appInfo = getAppInfo(context); //获取app版本信息
        appInfo.append(getDevicesInfo()) //获取设备信息
                .append(getCause(cause)); //获取异常的堆栈信息

        //保存至sd卡
        File file = saveToSdCard(context, appInfo);
        return file;

    }


    @Override
    public boolean postServer(final Context context, File file) {
        L.e(TAG,"postserver");
        RequestBody requestBody = RequestBody.create(MediaType.parse("txt/*"), file);
        ApiWrapper<ServerAPI> wrapper=new ApiWrapper<>();
        wrapper.targetClass(ServerAPI.class).getAPI()
                .uploadCrash(requestBody,PATH,String.valueOf(System.currentTimeMillis()))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(wrapper.<String>applySchedulers())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (ExceptionFilter.filter(mContext,e)){
                            ToastUtil.showToast(mContext,"上传异常失败");
                        }
                    }

                    @Override
                    public void onNext(String s) {

                    }
                });

        return isSuccess;
    }

    private File saveToSdCard(Context context, StringBuilder appInfo) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");

        final File file = new File("/storage/emulated/0/documents/" + format.format(new Date()) + ".log");
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            out.write(appInfo.toString().getBytes());
            Log.e("main", "保存至SD卡成功" + file.getPath().toString());
            return file;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    private StringBuilder getCause(Throwable cause) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        String exception = stringWriter.toString();
        return new StringBuilder(exception);
    }

    private StringBuilder getDevicesInfo() {
        Field[] fields = Build.class.getFields();
        StringBuilder stringBuilder = new StringBuilder();
        for (Field f : fields) {
            try {
                stringBuilder.append(f.getName())
                        .append("=")
                        .append(f.get(null))
                        .append("\n");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        Log.e("main", "phone:" + stringBuilder.toString());
        return stringBuilder;
    }

    private StringBuilder getAppInfo(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
            String versionName = packageInfo.versionName;
            int versionCode = packageInfo.versionCode;
            StringBuilder sb = new StringBuilder();
            sb.append("versionName=" + versionName).append("versionCode" + versionCode);
            Log.e("main", versionName + " " + versionCode);
            return sb;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}
