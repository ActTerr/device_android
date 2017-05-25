package mac.yk.devicemanagement.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import com.sun.mail.smtp.SMTPMessage;

import java.util.Date;
import java.util.Properties;
import java.util.UUID;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;

import mac.yk.devicemanagement.application.MyApplication;

/**
 * Created by mac-yk on 2017/3/21.
 */

public class CommonUtils {
    public static boolean sendTextMail(String title, String content) {
        try {
            Properties props = System.getProperties();
            props.put("mail.smtp.host", "164322105.qq.com");
            props.put("mail.smtp.auth", "true");
            Session session = Session.getInstance(props, null);
            Transport transport = session.getTransport("smtp");

            transport.connect("164322105.qq.com", 25, "164322105@qq.com",
                    "remusiclog1");
            Message mailMessage = new SMTPMessage(session);
            Address from = new InternetAddress("164322105@qq.com");
            mailMessage.setFrom(from);
            Address to = new InternetAddress("164322105@qq.com");
            mailMessage.setRecipient(Message.RecipientType.TO, to);
            mailMessage.setSubject(title);
            mailMessage.setSentDate(new Date());
            mailMessage.setText(content);
            transport.sendMessage(mailMessage, mailMessage.getAllRecipients());
            return true;
        } catch (MessagingException ex) {
            ex.printStackTrace();

        }
        return false;
    }

    public static int getAPPVersionCode(Context ctx) {
        int currentVersionCode = 0;
        PackageManager manager = ctx.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(ctx.getPackageName(), 0);
            String appVersionName = info.versionName; // 版本名
            currentVersionCode = info.versionCode; // 版本号
            System.out.println(currentVersionCode + " " + appVersionName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return currentVersionCode;
    }
    public static String getDeviceInfo() {
        StringBuilder builder = new StringBuilder();
        builder.append("MODEL = " + Build.MODEL + "\n");
        builder.append("PRODUCT = " + Build.PRODUCT + "\n");
        builder.append("TAGS = " + Build.TAGS + "\n");
        builder.append("CPU_ABI" + Build.CPU_ABI + "\n");
        builder.append("BOARD = " + Build.BOARD + "\n");
        builder.append("BRAND = " + Build.BRAND + "\n");
        builder.append("DEVICE = " + Build.DEVICE + "\n");
        builder.append("DISPLAY = " + Build.DISPLAY + "\n");
        builder.append("ID = " + Build.ID + "\n");
        builder.append("VERSION.RELEASE = " + Build.VERSION.RELEASE + "\n");
        builder.append("Build.VERSION.SDK_INT = " + Build.VERSION.SDK_INT + "\n");
        builder.append("VERSION.BASE_OS = " + Build.VERSION.BASE_OS + "\n");
        builder.append("Build.VERSION.SDK = " + Build.VERSION.SDK + "\n");
        builder.append("APP.VERSION = " + getAPPVersionCode(MyApplication.getInstance()) + "\n");
        builder.append("\n" + "log:" + "\n");

        return builder.toString();

    }

    //获得独一无二的Psuedo ID
    public static String getUniquePsuedoID() {
        String serial = null;

        String m_szDevIDShort = "35" +
                Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +

                Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +

                Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +

                Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +

                Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +

                Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +

                Build.USER.length() % 10; //13 位

        try {
            serial = android.os.Build.class.getField("SERIAL").get(null).toString();
            //API>=9 使用serial号
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            //serial需要一个初始化
            serial = "serial"; // 随便一个初始化
        }
        //使用硬件信息拼凑出来的15位号码
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }
}