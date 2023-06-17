package com.sathsata.myto_dolist.TodoUIs;


import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sathsata.myto_dolist.Adapter.FamilyTodoAdapter;
import com.sathsata.myto_dolist.Database.TodoDatabase;
import com.sathsata.myto_dolist.Model.TodoModelFamily;
import com.sathsata.myto_dolist.NewTask.AddNewTaskFamily;
import com.sathsata.myto_dolist.DialogCloseListener;
import com.sathsata.myto_dolist.R;
import com.sathsata.myto_dolist.RecyclerItemTouchHelper.RecyclerItemTouchHelperFour;
import com.sathsata.myto_dolist.BottomNavigationUIs.Dashboard.DashboardFragment;

import java.util.Collections;
import java.util.List;

public class FamilyTodoActivity extends AppCompatActivity implements DialogCloseListener {


    private TodoDatabase db;

    private RecyclerView tasksRecyclerView;
    private FamilyTodoAdapter tasksAdapter;
    private FloatingActionButton fab;


    private List<TodoModelFamily> taskListFamily;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_todo);

        lockOrientation();

        Window window = getWindow();

// Add the FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window to indicate that it should draw the system bar backgrounds
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// Set the status bar color using the ContextCompat.getColor() method, passing the color resource ID as the second argument
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.family));


        CardView backButton = findViewById(R.id.back);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), DashboardFragment.class));

            }
        });


        db = new TodoDatabase(this);
        db.openDatabase();

        tasksRecyclerView = findViewById(R.id.tasksRecyclerViewFamily);

        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        tasksAdapter = new FamilyTodoAdapter(db, FamilyTodoActivity.this);
        tasksRecyclerView.setAdapter(tasksAdapter);

        fab = findViewById(R.id.fab4);

// Get all family tasks from the database and reverse the list
        taskListFamily = db.getAllFamilyTasks();
        Collections.reverse(taskListFamily);

// Set the tasks list to the adapter
        tasksAdapter.setTasks(taskListFamily);

// Attach an ItemTouchHelper to the RecyclerView for swipe-to-delete functionality
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerItemTouchHelperFour(tasksAdapter));
        itemTouchHelper.attachToRecyclerView(tasksRecyclerView);

// Set a click listener for the FAB (Floating Action Button) to add a new task
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewTaskFamily.newInstance().show(getSupportFragmentManager(), AddNewTaskFamily.TAG);
            }
        });

    }

    private void lockOrientation() {
        int orientation = getResources().getConfiguration().orientation;

        // Lock to portrait if the device is in landscape mode
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void handleDialogClose(DialogInterface dialog) {

        taskListFamily = db.getAllFamilyTasks();
        Collections.reverse(taskListFamily);
        tasksAdapter.setTasks(taskListFamily);
        tasksAdapter.notifyDataSetChanged();

    }
}




