package com.sathsata.myto_dolist.Database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sathsata.myto_dolist.Model.TodoModelFamily;
import com.sathsata.myto_dolist.Model.TodoModelPersonal;
import com.sathsata.myto_dolist.Model.TodoModelTravel;
import com.sathsata.myto_dolist.Model.TodoModelWork;

import java.util.ArrayList;
import java.util.List;

public class TodoDatabase extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String NAME = "ToDoListDatabase";

    private static final String TODO_TABLE_WORK = "work_todo";
    private static final String TODO_TABLE_PERSONAL = "personal_todo";
    private static final String TODO_TABLE_FAMILY = "family_todo";
    private static final String TODO_TABLE_TRAVEL = "travel_todo";


    private static final String ID = "id";
    private static final String TASK = "task";
    private static final String STATUS = "status";
    private static final String DATE = "date";
    private static final String TIME = "time";

    // SQL statement to create the todo table
    private static final String CREATE_TODO_TABLE_WORK = "CREATE TABLE " + TODO_TABLE_WORK + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TASK + " TEXT, "
            + STATUS + " INTEGER," + DATE + " String," + TIME + " String)";
    private static final String CREATE_TODO_TABLE_PERSONAL = "CREATE TABLE " + TODO_TABLE_PERSONAL + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TASK + " TEXT, "
            + STATUS + " INTEGER," + DATE + " String," + TIME + " String)";
    private static final String CREATE_TODO_TABLE_FAMILY = "CREATE TABLE " + TODO_TABLE_FAMILY + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TASK + " TEXT, "
            + STATUS + " INTEGER," + DATE + " String," + TIME + " String)";
    private static final String CREATE_TODO_TABLE_TRAVEL = "CREATE TABLE " + TODO_TABLE_TRAVEL + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TASK + " TEXT, "
            + STATUS + " INTEGER," + DATE + " String," + TIME + " String)";

    private SQLiteDatabase db;

    public TodoDatabase(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the todo table
        db.execSQL(CREATE_TODO_TABLE_WORK);
        db.execSQL(CREATE_TODO_TABLE_PERSONAL);
        db.execSQL(CREATE_TODO_TABLE_FAMILY);
        db.execSQL(CREATE_TODO_TABLE_TRAVEL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop the todo table if it exists
        db.execSQL("DROP TABLE IF EXISTS " + TODO_TABLE_WORK);
        db.execSQL("DROP TABLE IF EXISTS " + TODO_TABLE_PERSONAL);
        db.execSQL("DROP TABLE IF EXISTS " + TODO_TABLE_FAMILY);
        db.execSQL("DROP TABLE IF EXISTS " + TODO_TABLE_TRAVEL);
        // Create the tables again
        onCreate(db);
    }

    public void openDatabase() {
        db = this.getWritableDatabase();
    }

    // Insert a new task into the work to-do table
    public void insertTaskWork(TodoModelWork task) {
        ContentValues cv = new ContentValues();
        cv.put(TASK, task.getTask());
        cv.put(DATE, task.getDate());
        cv.put(TIME, task.getTime());
        cv.put(STATUS, 0);
        // Insert a new task into the todo table
        db.insert(TODO_TABLE_WORK, null, cv);
    }

    // Insert a new task into the personal to-do table
    public void insertTaskPersonal(TodoModelPersonal task) {
        ContentValues cv = new ContentValues();
        cv.put(TASK, task.getTask());
        cv.put(DATE, task.getDate());
        cv.put(TIME, task.getTime());
        cv.put(STATUS, 0);
        db.insert(TODO_TABLE_PERSONAL, null, cv);
    }

    // Insert a new task into the travel to-do table
    public void insertTaskTravel(TodoModelTravel task) {
        ContentValues cv = new ContentValues();
        cv.put(TASK, task.getTask());
        cv.put(DATE, task.getDate());
        cv.put(TIME, task.getTime());
        cv.put(STATUS, 0);
        db.insert(TODO_TABLE_TRAVEL, null, cv);
    }

    // Insert a task into the family todo table
    public void insertTaskFamily(TodoModelFamily task) {
        ContentValues cv = new ContentValues();
        cv.put(TASK, task.getTask());
        cv.put(DATE, task.getDate());
        cv.put(TIME, task.getTime());
        cv.put(STATUS, 0);
        db.insert(TODO_TABLE_FAMILY, null, cv);
    }

    // Get all tasks from the work todo table
    @SuppressLint("Range")
    public List<TodoModelWork> getAllTasksWork() {
        List<TodoModelWork> taskList = new ArrayList<>();
        Cursor cur = null;
        db.beginTransaction();
        try {
            // Retrieve all tasks from the todo table
            cur = db.query(TODO_TABLE_WORK, null, null, null, null, null, null, null);

            if (cur != null) {
                if (cur.moveToFirst()) {
                    do {
                        TodoModelWork task = new TodoModelWork();
                        task.setId(cur.getInt(cur.getColumnIndex(ID)));
                        task.setTask(cur.getString(cur.getColumnIndex(TASK)));
                        task.setStatus(cur.getInt(cur.getColumnIndex(STATUS)));
                        task.setDate(cur.getString(cur.getColumnIndex(DATE)));
                        task.setTime(cur.getString(cur.getColumnIndex(TIME)));
                        taskList.add(task);
                    }
                    while (cur.moveToNext());
                }
            }
        } finally {
            db.endTransaction();
            assert cur != null;
            cur.close();
        }
        return taskList;
    }

    // Get all tasks from the personal todo table
    @SuppressLint("Range")
    public List<TodoModelPersonal> getAllPersonalTasks() {
        List<TodoModelPersonal> taskListPersonal = new ArrayList<>();
        Cursor cur = null;
        db.beginTransaction();
        try {
            cur = db.query(TODO_TABLE_PERSONAL, null, null, null, null, null, null, null); // Execute the query to retrieve all rows from the personal to-do table

            if (cur != null) {
                if (cur.moveToFirst()) {
                    do {
                        TodoModelPersonal task = new TodoModelPersonal();
                        task.setId(cur.getInt(cur.getColumnIndex(ID)));
                        task.setTask(cur.getString(cur.getColumnIndex(TASK)));
                        task.setStatus(cur.getInt(cur.getColumnIndex(STATUS)));
                        task.setDate(cur.getString(cur.getColumnIndex(DATE)));
                        task.setTime(cur.getString(cur.getColumnIndex(TIME)));
                        taskListPersonal.add(task);
                    } while (cur.moveToNext());
                }
            }
        } finally {
            db.endTransaction();
            assert cur != null;
            cur.close();
        }
        return taskListPersonal;
    }

    // Get all tasks from the travel todo table
    @SuppressLint("Range")
    public List<TodoModelTravel> getAllTravelTasks() {
        List<TodoModelTravel> taskListTravel = new ArrayList<>();
        Cursor cur = null;
        db.beginTransaction();
        try {
            // Retrieve all tasks from the todo_travel table
            cur = db.query(TODO_TABLE_TRAVEL, null, null, null, null, null, null, null);

            if (cur != null) {
                if (cur.moveToFirst()) {
                    do {
                        TodoModelTravel task = new TodoModelTravel();
                        task.setId(cur.getInt(cur.getColumnIndex(ID)));
                        task.setTask(cur.getString(cur.getColumnIndex(TASK)));
                        task.setStatus(cur.getInt(cur.getColumnIndex(STATUS)));
                        task.setDate(cur.getString(cur.getColumnIndex(DATE)));
                        task.setTime(cur.getString(cur.getColumnIndex(TIME)));
                        taskListTravel.add(task);
                    }
                    while (cur.moveToNext());
                }
            }
        } finally {
            db.endTransaction();
            assert cur != null;
            cur.close();
        }
        return taskListTravel;
    }

    // Get all tasks from the family todo table
    @SuppressLint("Range")
    public List<TodoModelFamily> getAllFamilyTasks() {
        List<TodoModelFamily> taskListFamily = new ArrayList<>();
        Cursor cur = null;
        db.beginTransaction();
        try {
            // Execute the query to retrieve all rows from the family todo table
            cur = db.query(TODO_TABLE_FAMILY, null, null, null, null, null, null, null);

            if (cur != null) {
                if (cur.moveToFirst()) {
                    // Iterate over the cursor to extract task details and create TodoModelFamily objects
                    do {
                        TodoModelFamily task = new TodoModelFamily();
                        task.setId(cur.getInt(cur.getColumnIndex(ID)));
                        task.setTask(cur.getString(cur.getColumnIndex(TASK)));
                        task.setStatus(cur.getInt(cur.getColumnIndex(STATUS)));
                        task.setDate(cur.getString(cur.getColumnIndex(DATE)));
                        task.setTime(cur.getString(cur.getColumnIndex(TIME)));
                        taskListFamily.add(task);
                    } while (cur.moveToNext());
                }
            }
        } finally {
            // End the transaction and close the cursor
            db.endTransaction();
            assert cur != null;
            cur.close();
        }
        // Return the list of tasks
        return taskListFamily;
    }


    public void updateStatusWork(int id, int status) {
        ContentValues cv = new ContentValues();
        cv.put(STATUS, status);
        // Update the status of a task in the todo table
        db.update(TODO_TABLE_WORK, cv, ID + "= ?", new String[]{String.valueOf(id)});
    }

    public void updateStatusPersonal(int id, int status) {
        // Update the status of a task in the personal to-do table
        ContentValues cv = new ContentValues();
        cv.put(STATUS, status);
        db.update(TODO_TABLE_PERSONAL, cv, ID + "= ?", new String[]{String.valueOf(id)});
    }

    public void updateStatusTravel(int id, int status) {
        ContentValues cv = new ContentValues();
        cv.put(STATUS, status);
        // Update the status of a task in the todo_travel table
        db.update(TODO_TABLE_TRAVEL, cv, ID + "= ?", new String[]{String.valueOf(id)});
    }

    public void updateStatusFamily(int id, int status) {
        ContentValues cv = new ContentValues();
        cv.put(STATUS, status);
        db.update(TODO_TABLE_FAMILY, cv, ID + "=?", new String[]{String.valueOf(id)});
    }


    public void updateTaskWork(int id, String task) {
        ContentValues cv = new ContentValues();
        cv.put(TASK, task);
        // Update the task description in the todo table
        db.update(TODO_TABLE_WORK, cv, ID + "= ?", new String[]{String.valueOf(id)});
    }

    public void updateTaskPersonal(int id, String task) {
        // Update the task in the personal to-do table
        ContentValues cv = new ContentValues();
        cv.put(TASK, task);
        db.update(TODO_TABLE_PERSONAL, cv, ID + "= ?", new String[]{String.valueOf(id)});
    }

    public void updateTaskTravel(int id, String task) {
        ContentValues cv = new ContentValues();
        cv.put(TASK, task);
        // Update the task description in the todo_travel table
        db.update(TODO_TABLE_TRAVEL, cv, ID + "= ?", new String[]{String.valueOf(id)});
    }

    public void updateTaskFamily(int id, String task) {
        ContentValues cv = new ContentValues();
        cv.put(TASK, task);
        db.update(TODO_TABLE_FAMILY, cv, ID + "=?", new String[]{String.valueOf(id)});
    }


    public void updateDateWork(int id, String date) {
        ContentValues cv = new ContentValues();
        cv.put(DATE, date);
        // Update the date of a task in the todo table
        db.update(TODO_TABLE_WORK, cv, ID + "= ?", new String[]{String.valueOf(id)});
    }

    public void updateDateTravel(int id, String date) {
        ContentValues cv = new ContentValues();
        cv.put(DATE, date);
        // Update the date of a task in the todo_travel table
        db.update(TODO_TABLE_TRAVEL, cv, ID + "= ?", new String[]{String.valueOf(id)});
    }


    public void updateDatePersonal(int id, String date) {
        // Update the date of a task in the personal to-do table
        ContentValues cv = new ContentValues();
        cv.put(DATE, date);
        db.update(TODO_TABLE_PERSONAL, cv, ID + "= ?", new String[]{String.valueOf(id)});
    }

    public void updateDateFamily(int id, String date) {
        ContentValues cv = new ContentValues();
        cv.put(DATE, date);
        db.update(TODO_TABLE_FAMILY, cv, ID + "=?", new String[]{String.valueOf(id)});
    }


    public void updateTimeWork(int id, String time) {
        ContentValues cv = new ContentValues();
        cv.put(TIME, time);
        // Update the time of a task in the todo table
        db.update(TODO_TABLE_WORK, cv, ID + "= ?", new String[]{String.valueOf(id)});
    }

    public void updateTimePersonal(int id, String time) {
        // Update the time of a task in the personal to-do table
        ContentValues cv = new ContentValues();
        cv.put(TIME, time);
        db.update(TODO_TABLE_PERSONAL, cv, ID + "= ?", new String[]{String.valueOf(id)});
    }

    public void updateTimeTravel(int id, String time) {
        ContentValues cv = new ContentValues();
        cv.put(TIME, time);
        // Update the time of a task in the todo_travel table
        db.update(TODO_TABLE_TRAVEL, cv, ID + "= ?", new String[]{String.valueOf(id)});
    }

    public void updateTimeFamily(int id, String time) {
        ContentValues cv = new ContentValues();
        cv.put(TIME, time);
        db.update(TODO_TABLE_FAMILY, cv, ID + "=?", new String[]{String.valueOf(id)});
    }

    public void deleteTaskWork(int id) {
        // Delete a task from the todo table based on its ID
        db.delete(TODO_TABLE_WORK, ID + "= ?", new String[]{String.valueOf(id)});
    }

    public void deleteTaskPersonal(int id) {
        // Delete a task from the personal to-do table
        db.delete(TODO_TABLE_PERSONAL, ID + "= ?", new String[]{String.valueOf(id)});
    }

    public void deleteTaskTravel(int id) {
        // Delete a task from the todo_travel table based on its ID
        db.delete(TODO_TABLE_TRAVEL, ID + "= ?", new String[]{String.valueOf(id)});
    }

    public void deleteTaskFamily(int id) {
        db.delete(TODO_TABLE_FAMILY, ID + "=?", new String[]{String.valueOf(id)});
    }
}

