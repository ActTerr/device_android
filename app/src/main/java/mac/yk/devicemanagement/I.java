package mac.yk.devicemanagement;

/**
 * Created by mac-yk on 2017/3/1.
 */

public interface I {

    interface REQUEST{
        String DOWNWEIXIU="downloadWeiXiu";
        String DOWNXUNJIAN="downloadXunJian";
        String YUJING="yujing";
        String TONGJI="tongji";
        String CHAXUN="chaxun";
        String CONTROL="saoma";
        String SAVE="save";
        String LOGOUT="logOut";
        String SERVER_ROOT="";
        String XUNJIAN="xunjian";
        String XIUJUN="xiujun";
    }
    interface PARAM{
        String SIZE="size";
        String PAGE="page";
        String ID="id";
        String Device="device";
        String ISDIANCHI="isDianchi";
        String USERNAME="userName";
        String PASSWD="passwd";
        String CREQ="controlReq";
        String REMARK="remark";
        String ZHUANGTAI ="zhuangtai" ;
    }
    interface RESULT{

        int SUCCESS=0;

    }



    interface CONTROL{
        int START=0;

    }
}
