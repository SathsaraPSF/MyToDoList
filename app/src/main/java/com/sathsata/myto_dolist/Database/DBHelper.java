package com.sathsata.myto_dolist.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.widget.Toast;


import com.sathsata.myto_dolist.Model.ModelClass;

import java.io.ByteArrayOutputStream;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DBNAME = "LoginUser.db";
    private static final String TABLE_NAME = "users";
    private static String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String EMAIL = "email";
    private static final String IMAGE = "image";
    private Context context;

    // Store image as a byte array
    private ByteArrayOutputStream byteArrayOutputStream;
    private byte[] imageInBytes;

    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + USERNAME + " TEXT PRIMARY KEY, " + PASSWORD + " TEXT, " + EMAIL + " TEXT, " + IMAGE + " BLOB)";


    public DBHelper(Context context) {

        super(context, DBNAME, null, 1);
        this.context = context;
    }

    // Called when the database is created for the first time
    @Override
    public void onCreate(SQLiteDatabase MyDB) {
        MyDB.execSQL(CREATE_TABLE);
    }

    // Called when the database needs to be upgraded
    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int i, int i1) {
        MyDB.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(MyDB);
    }

    // Method to insert user details into the database
    public Boolean insertDetails(ModelClass modelClass) {
        ContentValues contentValues = new ContentValues();
        SQLiteDatabase MyDB = this.getWritableDatabase();

        try {
            // Store image
            Bitmap imageToStoreBitmap = modelClass.getUserImage();
            byteArrayOutputStream = new ByteArrayOutputStream();
            imageToStoreBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            imageInBytes = byteArrayOutputStream.toByteArray();

            contentValues.put("username", modelClass.getUserName());
            contentValues.put("password", modelClass.getPassword());
            contentValues.put("email", modelClass.getEmail());
            contentValues.put("image", imageInBytes);
        } catch (Exception e) {
            Toast.makeText(context, "Please choose an image", Toast.LENGTH_SHORT).show();
        }

        long result = MyDB.insert(TABLE_NAME, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    // Method to get user details from the database
    public Cursor getUser() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " +
                USERNAME + " = ?";
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }

    // Method to get user details by username from the database
    public Cursor getUserDetails(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " +
                USERNAME + " = ?";
        String[] selectionArgs = {username};
        return db.rawQuery(query, selectionArgs);
    }

    // Method to check if a username exists in the database
    public Boolean checkUsername(String username) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("SELECT * FROM users WHERE username = ?", new String[]{username});
        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }

    // Method to check if a username and password combination exists in the database
    public Boolean checkUsernamePassword(String username, String password) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("SELECT * FROM users WHERE username = ? AND password = ?", new String[]{username, password});
        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }

    // Method to check if a password exists in the database
    public Boolean checkPassword(String password) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("SELECT * FROM users WHERE password = ?", new String[]{password});
        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }

    // Method to update a user's password in the database
    public Boolean updatePassword(ModelClass modelClass) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", modelClass.getUser());
        contentValues.put("password", modelClass.getNewPassword());
        long result = MyDB.update(TABLE_NAME, contentValues, USERNAME + "=?", new String[]{modelClass.getUser()});

        if (result == -1)
            return false;
        else
            return true;
    }
}
