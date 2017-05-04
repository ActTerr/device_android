package mac.yk.devicemanagement.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import mac.yk.devicemanagement.I;
import mac.yk.devicemanagement.bean.User;
import mac.yk.devicemanagement.util.L;

/**
 * Created by mac-yk on 2017/5/4.
 */

public class dbUser implements db {
    public static dbUser instance = null;
    DBHelper dbHelper;
    String TAG="dbUser";
    public static synchronized dbUser getInstance(Context context) {
        if (instance == null) {
            instance = new dbUser(context);
        }
        return instance;
    }

    public dbUser(Context context) {
       dbHelper=DBHelper.getInstance(context);
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        if (oldVersion < 2 && newVersion >= 2) {
            onCreate(db);
        }
    }

    @Override
    public void onDowngrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS ")
                .append(I.USER.TABLENAME).append("(")
                .append(I.USER.ACCOUNTS).append(" TEXT,")
                .append(I.USER.NAME).append(" TEXT,")
                .append(I.USER.AUTHORITY).append(" INT,")
                .append(I.USER.GRADE).append(" INT,")
                .append(I.USER.UNIT).append(" INT)");
        db.execSQL(sb.toString());
        L.e(TAG,"create suc");
    }
    public synchronized boolean insert(User user){
        SQLiteDatabase database=dbHelper.getWritableDatabase();
            ContentValues values=new ContentValues();
            values.put(I.USER.ACCOUNTS,user.getAccounts());
            values.put(I.USER.AUTHORITY,user.getAuthority());
            values.put(I.USER.GRADE,user.getGrade());
            values.put(I.USER.NAME,user.getName());
            values.put(I.USER.UNIT,user.getUnit());
        if (database.isOpen()){
            return database.replace(I.USER.TABLENAME,null,values)!=-1;
        }else {
            return false;
        }

    }
    public boolean select1(String accounts){
        L.e(TAG,"exc sel1");
        SQLiteDatabase database=dbHelper.getReadableDatabase();
        String sql="SELECT * FROM "+I.USER.TABLENAME+" WHERE "+I.USER.ACCOUNTS+"=?";
        Cursor cursor=database.rawQuery(sql,new String[]{accounts});
        if (cursor!=null) {
            L.e(TAG,"不为空");
            if(cursor.moveToNext()){
                return true;
            }
        }
        L.e(TAG,"没有");
       return false;
    }
    public User select2(String accounts){
        SQLiteDatabase database=dbHelper.getReadableDatabase();
        String sql="select * from "+I.USER.TABLENAME+" where "+I.USER.ACCOUNTS+"=?";
        Cursor cursor=database.rawQuery(sql,new String[]{accounts});
        User user=new User();
        if (cursor.moveToNext()){
            user.setGrade(cursor.getInt(cursor.getColumnIndex(I.USER.GRADE)));
            user.setUnit(cursor.getInt(cursor.getColumnIndex(I.USER.UNIT)));
            user.setName(cursor.getString(cursor.getColumnIndex(I.USER.NAME)));
            user.setAuthority(cursor.getInt(cursor.getColumnIndex(I.USER.AUTHORITY)));
        }
        return user;
    }
    public synchronized void update(User user){

    }

}
