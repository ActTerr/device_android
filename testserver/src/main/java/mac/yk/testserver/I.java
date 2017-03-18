package mac.yk.testserver;

public interface I {
    String TABLENAME="tableName";
    interface REQUEST{
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
        String SERVER_ROOT="http://192.168.1.100:8080/deviceManagement/";
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
        String DID="Did";
        String DNAME="Dname";
        String REMARK="remark";
        String USER="user";
        String DATE="Date";
    }
    interface PIC{
        String AVATAR_SUFFIX_JPG="JPG";
        String DEVICE="Device";
        String PID="pId";
        String TYPE="picType";
    }


    interface RESULT{
        int SUCCESS=0;
        int DEFEAT=1;
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
        String DID="Did";
        String DNAME="Dname";
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
        String DID="Did";
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
        String DID="Did";
    }
    interface GESTURE{
        int MANUAL=1;
        int AUTO=2;
    }
}