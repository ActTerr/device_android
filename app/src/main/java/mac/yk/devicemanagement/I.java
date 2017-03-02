package mac.yk.devicemanagement;

/**
 * Created by mac-yk on 2017/3/1.
 */

public interface I {
    String YUJING="yujing";
    String TONGJI="tongji";
    String CHAXUN="chaxun";
    String SAOMA="saoma";
    String SERVER_ROOT="";
    String SAVE="save";
    String ID="id";
    String Device="device";
    String ISDIANCHI="isDianchi";
    String USERNAME="userName";
    String PASSWD="passwd";
    int SUCCESS=0;
    String CREQ="controlReq";

    interface SCAN{
        int QUERY=1111;
        int SAVE=3333;
    }
}
