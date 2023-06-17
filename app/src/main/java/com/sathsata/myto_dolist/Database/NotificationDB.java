package com.sathsata.myto_dolist.Database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sathsata.myto_dolist.Model.NotificationModel;

import java.util.ArrayList;
import java.util.List;

public class NotificationDB extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String NAME = "NotificationDatabase.db";

    private static final String TABLE_NAME = "notifications";

    private static final String ID = "id";
    private static final String NOTIFICATION = "notification";



    private static final String CREATE_NOTIFICATION_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NOTIFICATION + " TEXT)";
    private SQLiteDatabase db;

    public NotificationDB(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_NOTIFICATION_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        // Create tables again
        onCreate(db);
    }

    public void openDatabaseNotification() {
        db = this.getWritableDatabase();
    }


    public void insertNotification(NotificationModel notification){
        ContentValues cv = new ContentValues();
        cv.put(NOTIFICATION, notification.getNotification());
        db.insert(TABLE_NAME, null, cv);
    }


    @SuppressLint("Range")
    public List<NotificationModel> getAllNotification(){
        List<NotificationModel> notificationList = new ArrayList<>();
        Cursor cur = null;
        db.beginTransaction();
        try{
            cur = db.query(TABLE_NAME, null, null, null, null, null, null, null);

            if(cur != null){
                if(cur.moveToFirst()){
                    do{
                        NotificationModel task = new NotificationModel();
                        task.setId(cur.getInt(cur.getColumnIndex(ID)));
                        task.setNotification(cur.getString(cur.getColumnIndex(NOTIFICATION)));
                        notificationList.add(task);
                    }
                    while(cur.moveToNext());
                }
            }
        }
        finally {
            db.endTransaction();
            assert cur != null;
            cur.close();
        }
        return notificationList;
    }


    public void deleteNotification(int id){
        db.delete(TABLE_NAME, ID + "= ?", new String[] {String.valueOf(id)});

    }


}

