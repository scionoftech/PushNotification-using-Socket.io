package scionoftech.socketservice.Db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DataHandler {
    public static final String ID = "id";
    public static final String NOTIFICATION_SETTINGS = "nsettings";


    // public static final String M_PLAYMATE = "playmate";
    public static final String NOTIFICATION_DATA = "data";

    public static final String TABLE_NAME_NOTIFICATION = "notifications";

    public static final String DATA_BASE_NAME = "socket.db";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_CREATE_NOTIFICATION = "create table notifications(id INTEGER PRIMARY KEY,data varchar)";


    static String databaseurl = DATA_BASE_NAME;


    DataBaseHelper dbhelper;
    Context ctx;
    SQLiteDatabase db;

    public DataHandler(Context ctx) {
        this.ctx = ctx;
        dbhelper = new DataBaseHelper(ctx);
    }

    private static class DataBaseHelper extends SQLiteOpenHelper {

        public DataBaseHelper(Context ctx) {

            super(ctx, databaseurl, null, DATABASE_VERSION);
            // TODO Auto-generated constructor stub
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(TABLE_CREATE_NOTIFICATION);
            } catch (Exception e) {
                System.out.println("Error");
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
            // TODO Auto-generated method stub
            db.execSQL("DROP TABLE IF EXISTS notifications");
            onCreate(db);

        }

    }

    public DataHandler open() {
        db = dbhelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbhelper.close();
    }

    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!notifications!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!//

    public Cursor ReturnNotifications() {
        return db.query(TABLE_NAME_NOTIFICATION, new String[]{ID, NOTIFICATION_DATA},
                null, null, null, null, null);
    }

    public long InsertNotifications(String Notifications) {
        // TODO Auto-generated method stub
        ContentValues content = new ContentValues();
        content.put(NOTIFICATION_DATA, Notifications);
        return db.insertOrThrow(TABLE_NAME_NOTIFICATION, null, content);
    }
    public long UpdateNotifications(String Notifications) {
        // TODO Auto-generated method stub
        ContentValues content = new ContentValues();
        content.put(NOTIFICATION_DATA, Notifications);
        return db.update(TABLE_NAME_NOTIFICATION, content, ID + "=" + "\"" + 1 + "\"", null);
    }


    public long DeleteNotificationData() {
        return db.delete(TABLE_CREATE_NOTIFICATION, null, null);
    }

    public long DeleteNotificationData(String id) {
        return db.delete(TABLE_CREATE_NOTIFICATION, ID + "=" + "\"" + id + "\"", null);
    }
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!notifications end!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!//


}



