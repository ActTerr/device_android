package mac.yk.devicemanagement.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import mac.yk.devicemanagement.I;
import mac.yk.devicemanagement.bean.FileEntry;
import mac.yk.devicemanagement.util.L;

import static android.content.ContentValues.TAG;

/**
 * Created by mac-yk on 2017/5/12.
 */

public class dbFile implements db{
    private  static dbFile instance;
    DBHelper dbHelper;
    public static dbFile getInstance(Context context){
        if (instance==null){
            instance=new dbFile(context);
        }
        return instance;
    }

    public dbFile(Context context) {
        dbHelper=DBHelper.getInstance(context);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuilder sb=new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS ")
                .append(I.FILE.TABLENAME).append("(")
                .append(I.FILE.AID).append(" LONG,")
                .append(I.FILE.TOOLSIZE).append(" LONG,")
                .append(I.FILE.COMPLETED_SIZE).append(" LONG,")
                .append(I.FILE.FILENAME).append(" TEXT,")
                .append(I.FILE.DIR_PATH).append(" TEXT,")
                .append(I.FILE.STATUS).append(" INT,")
                .append(I.FILE.NID).append(" TEXT)");
        db.execSQL(sb.toString());
    }

    public FileEntry getFile(String name){
        FileEntry fileEntry=null;
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        String sql="SELECT * FROM "+I.FILE.TABLENAME+" where "+I.FILE.FILENAME+"=?";
        Cursor cursor=null;
        try{
            cursor=db.rawQuery(sql,new String[]{String.valueOf(name)});
        }catch (SQLiteException e){
            L.e(TAG,"没找到！");
            return fileEntry;
        }
       if(cursor!=null){
         if(cursor.moveToNext()){
             fileEntry=new FileEntry();
             fileEntry.setCompletedSize(cursor.getLong(cursor.getColumnIndex(I.FILE.COMPLETED_SIZE)));
             fileEntry.setAid(cursor.getLong(cursor.getColumnIndex(I.FILE.AID)));
             fileEntry.setDownloadStatus(cursor.getInt(cursor.getColumnIndex(I.FILE.STATUS)));
             fileEntry.setFileName(cursor.getString(cursor.getColumnIndex(I.FILE.FILENAME)));
             fileEntry.setSaveDirPath(cursor.getString(cursor.getColumnIndex(I.FILE.DIR_PATH)));
             fileEntry.setToolSize(cursor.getLong(cursor.getColumnIndex(I.FILE.TOOLSIZE)));
             fileEntry.setNid(cursor.getLong(cursor.getColumnIndex(I.FILE.NID)));
             L.e(TAG,"查询创建完成！");
         }
       }
       cursor.close();
       return fileEntry;
    }

    public boolean insertFile(FileEntry fileEntry){
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(I.FILE.AID,fileEntry.getAid());
        contentValues.put(I.FILE.TOOLSIZE,fileEntry.getToolSize());
        contentValues.put(I.FILE.COMPLETED_SIZE,fileEntry.getCompletedSize());
        contentValues.put(I.FILE.FILENAME,fileEntry.getFileName());
        contentValues.put(I.FILE.STATUS,fileEntry.getDownloadStatus());
        contentValues.put(I.FILE.NID,fileEntry.getNid());
        contentValues.put(I.FILE.DIR_PATH,fileEntry.getSaveDirPath());
        if (db.isOpen()){
          return   db.insert(I.FILE.TABLENAME,null,contentValues)!=-1;
        }else {
            return false;
        }
    }

    public boolean deleteFile(String name){
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        return db.delete(I.FILE.TABLENAME,I.FILE.FILENAME+"=?",new String[]{name})==1;
    }


}
