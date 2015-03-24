package moonblade.bloodbankcet;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import moonblade.bloodbankcet.R;

public class sqldb{

    public static final String KEY_ROWID = "_id";
    public static final String KEY_NAME = "_name";
    public static final String KEY_BG = "_bg";
    public static final String KEY_BRANCH="_branch";
    public static final String KEY_PHONE="_phone";
    public static final String KEY_HOSTEL="_hostel";
    public static final String KEY_DATE="_date";


    public static final String KEY_ADMINID="_adminid";
    public static final String KEY_USERNAME="_username";
    public static final String KEY_PASSWORD="_password";


    private static final String DB_NAME = "_database";
    private static final String DB_TABLE = "_blood_table";
    private static final String DB_ADMIN_TABLE= "_admin_table";
    private static final int DB_VERSION = 3;

    private dbhelper ourhelper;
    private final Context ourcontext;
    private SQLiteDatabase ourdb;




    private static class dbhelper extends SQLiteOpenHelper{

        public dbhelper (Context context){
            super(context,DB_NAME,null,DB_VERSION);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + DB_ADMIN_TABLE);
            onCreate(db);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + DB_TABLE + " (" +
                            KEY_ROWID +  " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            KEY_NAME +   " TEXT NOT NULL, " +
                            KEY_BG +     " TEXT NOT NULL, " +
                            KEY_BRANCH + " TEXT NOT NULL, " +
                            KEY_PHONE +  " TEXT NOT NULL, " +
                            KEY_HOSTEL + " TEXT NOT NULL, " +
                            KEY_DATE +   " INTEGER);"
            );
            db.execSQL("CREATE TABLE " + DB_ADMIN_TABLE + " (" +
                            KEY_ROWID +  " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            KEY_USERNAME +   " TEXT NOT NULL, " +
                            KEY_PASSWORD +   " TEXT NOT NULL);"
            );
        }


    }

    public sqldb(Context c){
        ourcontext=c;
    }

    public  sqldb open(){
        ourhelper = new dbhelper(ourcontext);
        ourdb = ourhelper.getWritableDatabase();
        return this;
    }

    public void close(){
        ourhelper.close();
    }

    public long addAdmin(String username,String password){
        ContentValues cv=new ContentValues();
        cv.put(KEY_USERNAME,username);
        cv.put(KEY_PASSWORD,password);
        return ourdb.insert(DB_ADMIN_TABLE,null,cv);
    }
    public void deleteAdmin(long memberID){
        ourdb.delete(DB_ADMIN_TABLE, KEY_ROWID + "="
                + memberID, null);
    }

    public long addData(String nam,String bgr,String branch, String phone, String hostel,long date) {

        ContentValues cv = new ContentValues();
        cv.put(KEY_NAME,nam);
        cv.put(KEY_BG,bgr);
        cv.put(KEY_BRANCH,branch);
        cv.put(KEY_PHONE,phone);
        cv.put(KEY_HOSTEL,hostel);
        cv.put(KEY_DATE,date);
        return ourdb.insert(DB_TABLE,null,cv);
    }

    public Cursor readAll(){
        Cursor c=ourdb.query(DB_TABLE,new String[]{KEY_ROWID,KEY_NAME,KEY_BG,KEY_BRANCH,KEY_PHONE,KEY_HOSTEL,KEY_DATE},null,null,null,null,null);
        if (c!=null)
            c.moveToFirst();
        return c;
    }

    public Cursor readBlood(String s) {
        Cursor mc = null;

        if(s==null||s.length()==0)
        {
            mc= ourdb.query(DB_TABLE,new String[]{KEY_ROWID,KEY_NAME,KEY_BG,KEY_BRANCH,KEY_PHONE,KEY_HOSTEL,KEY_DATE},null,null,null,null,null);
        }
        else
        {
            mc= ourdb.query(DB_TABLE,new String[]{KEY_ROWID,KEY_NAME,KEY_BG,KEY_BRANCH,KEY_PHONE,KEY_HOSTEL,KEY_DATE},KEY_BG+" like '%"+ s+"%'",null,null,null,null,null);
        }
        if(mc!=null)
            mc.moveToFirst();
        return mc;
    }

    public Cursor readBranch(String s) {
        Cursor mc = null;
        if(s==null||s.length()==0)
        {
            mc= ourdb.query(DB_TABLE,new String[]{KEY_ROWID,KEY_NAME,KEY_BG,KEY_BRANCH,KEY_PHONE,KEY_HOSTEL,KEY_DATE},null,null,null,null,null);
        }
        else
        {
            mc= ourdb.query(DB_TABLE,new String[]{KEY_ROWID,KEY_NAME,KEY_BG,KEY_BRANCH,KEY_PHONE,KEY_HOSTEL,KEY_DATE},KEY_BRANCH+" like '%"+ s+"%'",null,null,null,null,null);
        }
        if(mc!=null)
            mc.moveToFirst();
        return mc;
    }

    public int updateData(long memberID, String memberName, String memberbg, String memberbranch, String memberphone, String memberhostel,long date) {
        ContentValues cvUpdate = new ContentValues();
        cvUpdate.put(KEY_NAME, memberName);
        cvUpdate.put(KEY_BG, memberbg);
        cvUpdate.put(KEY_BRANCH,memberbranch);
        cvUpdate.put(KEY_PHONE,memberphone);
        cvUpdate.put(KEY_HOSTEL,memberhostel);
        cvUpdate.put(KEY_DATE,date);
        int i = ourdb.update(DB_TABLE, cvUpdate,
                KEY_ROWID + " = " + memberID, null);
        return i;
    }

    public ResultSet getresultset(){
        ResultSet resultSet= (ResultSet) ourdb.query(DB_TABLE,new String[]{KEY_ROWID,KEY_NAME,KEY_BG,KEY_BRANCH,KEY_PHONE,KEY_HOSTEL,KEY_DATE},null,null,null,null,null);
//        if (resultSet!=null)
//            resultSet.moveToFirst();
        return resultSet;
    }
    public int get_row_count(){
        String countQuery = "SELECT  * FROM " + DB_TABLE;
        Cursor cursor = ourdb.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }
    public void deleteId(long memberID) {
        ourdb.delete(DB_TABLE, KEY_ROWID + "="
                + memberID, null);
    }
    public void delete(String name) {
        ourdb.delete(DB_TABLE, KEY_NAME + "=?", new String[] { name });
    }

}
