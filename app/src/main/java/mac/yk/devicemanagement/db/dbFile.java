package mac.yk.devicemanagement.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import mac.yk.devicemanagement.I;
import mac.yk.devicemanagement.bean.FileEntry;

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
                .append(I.FILE.DOWNLOADID).append(" LONG,")
                .append(I.FILE.TOOLSIZE).append(" LONG,")
                .append(I.FILE.COMPLETEDSIZE).append(" LONG,")
                .append(I.FILE.URL).append(" TEXT,")
                .append(I.FILE.FILENAME).append(" TEXT,")
                .append(I.FILE.STATUS).append(" INT)");
    }

    public FileEntry getFile(long Aid){
        FileEntry fileEntry=new FileEntry();
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        String sql="SELECT * FROM "+I.FILE.TABLENAME+" where "+I.FILE.DOWNLOADID+"=?";
        Cursor cursor=db.rawQuery(sql,new String[]{String.valueOf(Aid)});
       if(cursor!=null){
         if(cursor.moveToNext()){
             fileEntry.setCompletedSize(cursor.getLong(cursor.getColumnIndex(I.FILE.COMPLETEDSIZE)));
             fileEntry.setDownloadId(cursor.getLong(cursor.getColumnIndex(I.FILE.DOWNLOADID)));
             fileEntry.setDownloadStatus(cursor.getInt(cursor.getColumnIndex(I.FILE.STATUS)));
             fileEntry.setFileName(cursor.getString(cursor.getColumnIndex(I.FILE.FILENAME)));
             fileEntry.setSaveDirPath(cursor.getString(cursor.getColumnIndex(I.FILE.DIRPATH)));
             fileEntry.setToolSize(cursor.getLong(cursor.getColumnIndex(I.FILE.TOOLSIZE)));
             fileEntry.setUrl(cursor.getString(cursor.getColumnIndex(I.FILE.URL)));
         }
       }
       return fileEntry;
    }

    public boolean insertFile(FileEntry fileEntry){
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(I.FILE.DOWNLOADID,fileEntry.getDownloadId());
        contentValues.put(I.FILE.TOOLSIZE,fileEntry.getToolSize());
        contentValues.put(I.FILE.COMPLETEDSIZE,fileEntry.getCompletedSize());
        contentValues.put(I.FILE.URL,fileEntry.getUrl());
        contentValues.put(I.FILE.FILENAME,fileEntry.getFileName());
        contentValues.put(I.FILE.STATUS,fileEntry.getDownloadStatus());
        if (db.isOpen()){
          return   db.replace(I.FILE.TABLENAME,null,contentValues)==1;
        }else {
            return false;
        }
    }

}
