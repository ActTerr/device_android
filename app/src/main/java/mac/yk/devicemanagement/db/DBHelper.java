package mac.yk.devicemanagement.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import mac.yk.devicemanagement.util.SpUtil;

/**
 * Created by mac-yk on 2017/5/4.
 */

public class DBHelper extends SQLiteOpenHelper {
    static final int DBVersion=1;
    static DBHelper instance;
    static final String DB_NAME = "app.db";
    static db DB;
    Context context;
    public DBHelper(Context context) {
        super(context,DB_NAME, null, DBVersion);
        this.context=context;
    }
    public static synchronized DBHelper getInstance(Context context){
        if (instance==null){
            instance=new DBHelper(context);

        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        if (!SpUtil.getLoginUser(context).equals("")){
            dbFile.getInstance(context).onCreate(db);
        }
        dbUser.getInstance(context).onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        dbUser.getInstance(context).onUpgrade(db,oldVersion,newVersion);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        dbUser.getInstance(context).onDowngrade(db, oldVersion, newVersion);
    }
}
