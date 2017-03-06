package mac.yk.devicemanagement;

/**
 * Created by mac-yk on 2017/3/1.
 */

public interface I {

    interface REQUEST{

        String YUJING="yujing";
        String TONGJI="tongji";
        String CHAXUN="chaxun";
        String CONTROL="saoma";
        String SAVE="save";
        String LOGOUT="logOut";
        String SERVER_ROOT="";
    }
    interface PARAM{

        String ID="id";
        String Device="device";
        String ISDIANCHI="isDianchi";
        String USERNAME="userName";
        String PASSWD="passwd";
        String CREQ="controlReq";
    }
    interface RESULT{

        int SUCCESS=0;

    }

    interface SCAN{
        int QUERY=1111;
        int SAVE=3333;
    }
}
