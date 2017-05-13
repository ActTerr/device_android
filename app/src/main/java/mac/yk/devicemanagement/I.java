package mac.yk.devicemanagement;

public interface I {
    String TABLENAME="tableName";
    String UNIT="unit";
    String YEAR="year";
    String MEMORY="memory";
    String TYPE="type";
    String BEAN="bean";
    String FLAG="flag";

    interface UPADATE_TYPE{
        String RENAME="0";
        String REFILE="1";
    }

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
        String GETCOUNT="getCount";
        String CONTROL_D="controlD";
        String GET_STATUS_COUNT="getStatusCount";
        String GET_BAOFEI_COUNT="getBaofeiCount";
        String GET_NOTICE="getNotice";
        String DELETE_NOTICE="deleteNotice";
        String UPDATE_NOTICE="updateNotice";
        String GET_ATTACHMENT="getAttachment";
        String DELETE_ATTACHMENT="deleteAttachment";
        String ADD_ATTACHMENT="addAttachment";
        String UPDATE_ATTACHMENT="updateAttachment";
        String DOWNLOAD_FILE="downFile";
        String UPLOAD_FILE="uploadFile";
    }
    interface CONTROL_D{
        String CONTROL_TYPE="control_type";
        String SHIYONG="shiyong";
        String D_DAIYONG="Ddaiyong";
        String CHONGDIAN="chongdian";
    }
    interface BAOFEI{
        String TYPE="TYPE";
        String TABLENAME="BAOFEI";
        String DID="DID";
        String DNAME="DNAME";
        String REMARK="REMARK";
        String USER="BF_USER";
        String DATE="BF_DATE";
        String STATION="STATION";
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
        String SUC="成功";
        String DEF="失败";
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
        String TABLENAME="MYUSER";
        String NAME="NAME";
        String PASSWD="MYPASSWD";
        String ACCOUNTS="ACCOUNTS";
        String UNIT="UNIT";
        String GRADE="GRADE";
        String AUTHORITY="AUTHORITY";
    }

    interface WEIXIU{
        String ID="ID";
        String TABLENAME="SERVICE";
        String WXDATE="WX_DATE";
        String DID="DID";
        String USER="WX_USER";
        String TRANSLATE="ISTRANSLATE";
        String REMARK="REMARK";
        String XJDATE="XJ_DATE";
    }
    interface XUNJIAN{
        String TABLENAME="XUNJIAN";
        String DATE="XJ_DATE";
        String STATUS="STATUS";
        String REMARK="REMARK";
        String USER="XJ_USER";
        String DID="DID";
    }
    interface GESTURE{
        int MANUAL=1;
        int AUTO=2;
    }
    interface DEVICE2{
        String TABLE_NAME="DEV_TOTAL_REC";
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
        String USE_DURATION="USE_DURATION";
    }
    interface BATTERY{
        String TABLENAME="BATTERY";
        String DID="DID";
        String START_TIME="START_TIME";
        String USED_DURATION="USED_DURATION";
        String EXPECT_TIME="EXPECT_TIME";
        String THEORY_DURATION="THEORY_DURATION";
        String STATUS="STATUS";

    }

    interface NOTICE{
        String NID="NID";
        String DATE="DATE";
        String COMMON="COMMON";
        String TITLE="TITLE";
        String TABLENAME="NOTICE";
    }

    interface ATTACHMENT{
        String AID="AID";
        String NID="NID";
        String DATE="DATE";
        String NAME="NAME";
        String TABLENAME="ATTACHMENT";
        String NEW_NAME="newName";
    }

    interface FILE{
        String TABLENAME="FILE";
        String AID="Aid";
        String TOOLSIZE="toolSize";
        String COMPLETEDSIZE="completedSize";
        String URL="url";
        String DIRPATH="DirPath";
        String FILENAME="fileName";
        String STATUS="downloadStatus";
        String NID="Nid";
    }

}