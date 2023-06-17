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
import com.sathsata.myto_dolist.Adapter.PersonalTodoAdapter;
import com.sathsata.myto_dolist.Database.TodoDatabase;
import com.sathsata.myto_dolist.NewTask.AddNewTaskPersonal;
import com.sathsata.myto_dolist.DialogCloseListener;
import com.sathsata.myto_dolist.Model.TodoModelPersonal;
import com.sathsata.myto_dolist.R;
import com.sathsata.myto_dolist.RecyclerItemTouchHelper.RecyclerItemTouchHelperTwo;
import com.sathsata.myto_dolist.BottomNavigationUIs.Dashboard.DashboardFragment;

import java.util.Collections;
import java.util.List;

public class PersonalTodoActivity extends AppCompatActivity implements DialogCloseListener {


    private TodoDatabase db;

    private RecyclerView tasksRecyclerView;
    private PersonalTodoAdapter tasksAdapter;
    private FloatingActionButton fab;

    private List<TodoModelPersonal> taskListPersonal;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_persoanl_todo);
        lockOrientation();

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.personal));

        CardView backButton = findViewById(R.id.back);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DashboardFragment.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });

        db = new TodoDatabase(this);
        db.openDatabase();

        tasksRecyclerView = findViewById(R.id.tasksRecyclerViewPersonal);

        // Set the layout manager for the RecyclerView
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Create an instance of the PersonalTodoAdapter and set it as the adapter for the RecyclerView
        tasksAdapter = new PersonalTodoAdapter(db, PersonalTodoActivity.this);
        tasksRecyclerView.setAdapter(tasksAdapter);

        fab = findViewById(R.id.fab2);

        // Get all personal tasks from the database and reverse the list
        taskListPersonal = db.getAllPersonalTasks();
        Collections.reverse(taskListPersonal);

        // Set the tasks list to the adapter
        tasksAdapter.setTasks(taskListPersonal);

        // Attach an ItemTouchHelper to the RecyclerView for swipe-to-delete functionality
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerItemTouchHelperTwo(tasksAdapter));
        itemTouchHelper.attachToRecyclerView(tasksRecyclerView);

        // Set a click listener for the FAB (Floating Action Button) to add a new task
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewTaskPersonal.newInstance().show(getSupportFragmentManager(), AddNewTaskPersonal.TAG);
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
        taskListPersonal = db.getAllPersonalTasks();
        Collections.reverse(taskListPersonal);
        tasksAdapter.setTasks(taskListPersonal);
        tasksAdapter.notifyDataSetChanged();
    }

}



