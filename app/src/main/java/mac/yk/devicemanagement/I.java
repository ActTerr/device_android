package mac.yk.devicemanagement;

public interface I {
    String TABLENAME="tableName";
    String UNIT="unit";
    String YEAR="year";
    String MEMORY="memory";
    String TYPE="type";
    String BEAN="bean";
    String PAGE="page";
    String STATION_TYPE="sType";
    String RANGE="range";
    String SIZE="size";

    interface DEVICE_RESUME{
        String USE_DATE="USE_DATE";
        String SCRAP_DATE="SCRAP_DATE";
        String USE_UNIT="USE_UNIT";
        String USE_LOCAL="USE_LOCAL";
        String REMARK="REMARK";
        String DID="DID";
        String CHECK_DATE="CHECK_DATE";
    }

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
        String CHECK_BATTERY="checkBattery";
        String UPLOAD_UNCAUGHT="uploadUncaught";
        String PATH="Server?request=";
        String DOWN_SERVICE="downloadService";
        String DOWN_CHECK="downloadCheck";
        String WARING="waring";
        String GET_TOTAL_COUNT="getTotalCount";
        String FIND="find";
        String CONTROL="control";
        String SAVE="save";
        String LOGOUT="logOut";
        String SERVER_ROOT="http://192.168.0.37:8080/deviceManagement/";
        String CHECK="check";
        String REPAIR="repair";
        String LOGIN="login";
        String SCRAP="scrap";
        String DOWN_SCRAP="downScrap";
        String DOWN_PIC="downPic";
        String GET_PIC_COUNT="getPicCount";
        String CONTROL_BAT="controlBat";
        String GET_STATUS_COUNT="getStatusCount";
        String GET_SCRAP_COUNT="getScrapCount";
        String GET_SERVICE_COUNT="getServiceCount";
        String GET_NOTICE="getNotice";
        String GET_NOTICES="getNotices";
        String DELETE_NOTICE="deleteNotice";
        String UPDATE_NOTICE="updateNotice";
        String GET_ATTACHMENT="getAttachment";
        String DELETE_ATTACHMENT="deleteAttachment";
        String ADD_ATTACHMENT="addAttachment";
        String UPDATE_ATTACHMENT="updateAttachment";
        String DOWNLOAD_FILE="downFile";
        String GET_DEVICE_RESUME="getDeviceResume";
        String INACTIVE="inactive";
        String DELETE_FILE="deleteFile";
        String GET_LINE_TOTAL="getLineTotal";
        String GET_LINE_DETAIL="getLineDetail";
    }
    interface CONTROL_BAT{
        String CONTROL_TYPE="control_type";
        String USING="using";
        String BAT_INACTIVE="bat_inactive";
        String CHARGE="charging";
    }

    interface PIC{
        String AVATAR_SUFFIX_JPG="JPG";
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
    }
    interface DNAME{
        int TRANSCEIVER=1;
        int MACHINE_CONTROLLER=2;
        int ZONE_CONTROLLER=3;
        int BATTERY=4;
    }
    interface DOWNLOAD{
        String PAGE="page";
        String SIZE="size";
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


    interface SCRAP{
        String TYPE="TYPE";
        String TABLENAME="SCRAP";
        String DID="DID";
        String DNAME="DNAME";
        String REMARK="REMARK";
        String USER="USER";
        String DATE="DATE";
        String STATION="STATION";
    }
    interface SERVICE{
        String ID="ID";
        String TABLENAME="SERVICE";
        String SERVICE_DATE="SERVICE_DATE";
        String DID="DID";
        String USER="USER";
        String TRANSLATE="TRANSLATE";
        String REMARK="REMARK";
        String REPAIR_DATE="REPAIR_DATE";
        String TYPE="WX_TYPE";
    }
    interface CHECK{
        String TABLENAME="CHECK";
        String DATE="DATE";
        String STATUS="STATUS";
        String REMARK="REMARK";
        String USER="USER";
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
        String SERVICE_STATION="SERVICE_STATION";
    }
    interface BATTERY{
        String TABLENAME="BATTERY";
        String DID="DID";
        String START_TIME="START_TIME";
        String USED_DURATION="USED_DURATION";
        String THEORY_DURATION="THEORY_DURATION";
        String STATUS="STATUS";
        String UNIT_ID="UNIT_ID";
        String TYPE="TYPE";
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
        String COMPLETED_SIZE="completedSize";
        String URL="url";
        String DIR_PATH="DirPath";
        String FILENAME="fileName";
        String STATUS="downloadStatus";
        String NID="Nid";
    }

    interface DOWNLOAD_STATUS{
        public static final int INIT = -1;
        public static final int PREPARE = 0;
        public static final int START = 1;
        public static final int DOWNLOADING = 2;
        public static final int CANCEL = 3;
        public static final int ERROR = 4;
        public static final int COMPLETED = 5;
        public static final int PAUSE = 6;
    }

    interface END_LINE{
        String UNIT="UNIT";
        String LINE_ID="LINE_ID";
        String TEMPERATURE="TEMPERATURE";
        String SENSOR="SENSOR";
        String RADIO_STATION="RADIO_STATION";
        String TIME="TIME";
        String BATTERY="BATTERY";
    }
}