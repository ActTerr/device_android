package mac.yk.devicemanagement;

public interface I {
    String TABLENAME="tableName";
    interface UNCAUGHT{
        String PATH="/Users/mac-yk/Downloads/Server/";
        String FILE_NAME="fileName";
        String FILE="file";
    }
    interface REQUEST{
        String UPLOADUNCAUGHT="uploadUncaught";
        String PATH="Server";
        String PARAM="request";
        String DOWNWEIXIU="downloadWeiXiu";
        String DOWNXUNJIAN="downloadXunJian";
        String YUJING="yujing";
        String TONGJI="tongji";
        String CHAXUN="chaxun";
        String CONTROL="control";
        String SAVE="save";
        String LOGOUT="logOut";
        String SERVER_ROOT="http://192.168.1.101:8080/deviceManagement/";
        String XUNJIAN="xunjian";
        String XIUJUN="xiujun";
        String LOGIN="login";
        String BAOFEI="baofei";
        String DOWNDEVICE="downDevice";
        String DOWNSCRAP="downScrap";
        String YONGHOU="yonghou";
        String DOWNPIC="downPic";
        String GETPICCOUNT="getPicCount";
    }
    interface BAOFEI{
        String TABLENAME="baofei";
        String DID="dId";
        String DNAME="dName";
        String REMARK="remark";
        String USER="user";
        String DATE="Date";
    }
    interface PIC{
        String AVATAR_SUFFIX_JPG="JPG";
        String DEVICE="DeviceOld";
        String PID="pId";
        String TYPE="picType";
    }


    interface RESULT{
        int SUCCESS=1;
        int DEFEAT=0;
        String ERROR="error";
    }
    interface CONTROL{
        int START=0;
        int BEIYONG=1;
        int DAIYONG=2;
        int YUNXING=3;
        int WEIXIU=4;
        int XIUJUN=5;
        int XUNJIAN=6;
        int BAOFEI=7;
        int YONGHOU=8;
    }
    interface DNAME{
        int DIANTAI=1;
        int JIKONGQI=2;
        int QUKONGQI=3;
        int DIANCHI=4;
    }
    interface DOWNLOAD{
        String PAGE="page";
        String SIZE="size";
    }
    interface DEVICE{
        String TABLENAME="device";
        String DID="dId";
        String DNAME="dName";
        String CHUCHANG="chuchang";
        String STATUS="status";
        String XUNJIAN="xunjian";
        String ISDIANCHI="isDianchi";
    }
    interface USER{
        String TABLENAME="user";
        String NAME="name";
        String PASSWD="passwd";
    }

    interface WEIXIU{
        String ID="id";
        String TABLENAME="weixiu";
        String WXDATE="wxDate";
        String DID="dId";
        String USER="user";
        String TRANSLATE="translate";
        String REMARK="remark";
        String XJDATE="xjDate";
    }
    interface XUNJIAN{
        String TABLENAME="xunjian";
        String DATE="date";
        String STATUS="status";
        String REMARK="remark";
        String USER="user";
        String DID="dId";
    }
    interface GESTURE{
        int MANUAL=1;
        int AUTO=2;
    }
    interface Device{
        String DID="CHAR_DEV_TOTAL_REC_ID";
        String CATEGROY_ID="LOCK_VERSION";
        String CATEGROY="CHAR_DEV_CATEGORY";
        String MENUFACTOR="CHAR_PRODUCT_NA";
        String MODEL="CHAR_MODEL_NA";
        String OUT_DATE="DATE_OUT_FACTORY";
        String USE_DATE="DATE_IN_USE";
        String UNIT_ID   ="CHAR_UNIT_ID";
        String USE_POSITION="CHAR_USED_POSITION";
        String SERIAL_NUM="CHAR_SERIAL_NUM";
        String CODE_NUM="CHAR_CODE_NUM";
        String STATUS="ENUM_DEV_STATE";
        String REMARK="CHAR_REMARK";
        String PRICE="FLOAT_PRICE";
        String  GRADE    ="CHAR_DEV_GRADE";
        String STATION="CHAR_STATION_ID";
        String CREATE_USER="CREATE_USER_ID";
        String CREATE_DATE="CREATE_DATE";
        String CREATE_STATION="CREATE_STATION_ID";
        String USE_USER="CHAR_DEV_USER";
        String TYPE="CHAR_DEV_TYPE";
        String SALVAGE_VALUE="CHAR_SALVAGE_VALUE";
        String WHERE="CHAR_WHERE";
        String PINLV="CHAR_PINLV";
        String YA_PIN="CHAR_YA_PIN";
        String TRANSFER_STATE="CHAR_TRANSFER_STATE";
        String CHECK_DATE="CHAR_CHECK_DATE";
        String DATE_UN_USE="DATE_UN_USE";
        String DATE_IN_CHECK="DATE_IN_CHECK";

    }
}