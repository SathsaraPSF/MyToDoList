package com.sathsata.myto_dolist.TodoUIs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sathsata.myto_dolist.Adapter.WorkTodoAdapter;
import com.sathsata.myto_dolist.Database.TodoDatabase;
import com.sathsata.myto_dolist.NewTask.AddNewTaskWork;
import com.sathsata.myto_dolist.DialogCloseListener;
import com.sathsata.myto_dolist.Model.TodoModelWork;
import com.sathsata.myto_dolist.R;
import com.sathsata.myto_dolist.RecyclerItemTouchHelper.RecyclerItemTouchHelper;
import com.sathsata.myto_dolist.BottomNavigationUIs.Dashboard.DashboardFragment;

import java.util.Collections;
import java.util.List;

public class WorkToDoActivity extends AppCompatActivity implements DialogCloseListener {

    private TodoDatabase db;
    private RecyclerView tasksRecyclerView;
    private WorkTodoAdapter tasksAdapter;
    private FloatingActionButton fab;
    private CardView backButton;
    private List<TodoModelWork> taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_todo);
        lockOrientation();

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.work));

        db = new TodoDatabase(this);
        db.openDatabase();

        tasksRecyclerView = findViewById(R.id.tasksRecyclerView);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Create an instance of the WorkTodoAdapter and set it as the adapter for the RecyclerView
        tasksAdapter = new WorkTodoAdapter(db, WorkToDoActivity.this);
        tasksRecyclerView.setAdapter(tasksAdapter);

        fab = findViewById(R.id.fab);
        backButton = findViewById(R.id.back);

        // Set a click listener for the backButton CardView to start the DashboardFragment
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), DashboardFragment.class));
            }
        });

        // Get all work todo tasks from the database and reverse the list
        taskList = db.getAllTasksWork();
        Collections.reverse(taskList);

        // Set the tasks list to the adapter
        tasksAdapter.setTasks(taskList);

        // Attach an ItemTouchHelper to the RecyclerView for swipe-to-delete functionality
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerItemTouchHelper(tasksAdapter));
        itemTouchHelper.attachToRecyclerView(tasksRecyclerView);

        // Set a click listener for the FAB (Floating Action Button) to add a new task
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewTaskWork.newInstance().show(getSupportFragmentManager(), AddNewTaskWork.TAG);
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

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        taskList = db.getAllTasksWork();
        Collections.reverse(taskList);
        tasksAdapter.setTasks(taskList);
        tasksAdapter.notifyDataSetChanged();
    }
}
