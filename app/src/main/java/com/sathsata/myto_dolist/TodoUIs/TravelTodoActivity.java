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
import com.sathsata.myto_dolist.Adapter.TravelTodoAdapter;
import com.sathsata.myto_dolist.Database.TodoDatabase;
import com.sathsata.myto_dolist.Model.TodoModelTravel;
import com.sathsata.myto_dolist.DialogCloseListener;
import com.sathsata.myto_dolist.NewTask.AddNewTaskTravel;
import com.sathsata.myto_dolist.R;
import com.sathsata.myto_dolist.RecyclerItemTouchHelper.RecyclerItemTouchHelperTravel;
import com.sathsata.myto_dolist.BottomNavigationUIs.Dashboard.DashboardFragment;
import com.sathsata.myto_dolist.UIs.LocationTrackActivity;

import java.util.Collections;
import java.util.List;

public class TravelTodoActivity extends AppCompatActivity implements DialogCloseListener {


    private TodoDatabase db;

    private RecyclerView tasksRecyclerView;
    private TravelTodoAdapter tasksAdapter;
    private FloatingActionButton fab;

    private List<TodoModelTravel> taskListTravel;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_todo);
        lockOrientation();

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.travel));

        CardView backButton = findViewById(R.id.back);
        CardView locationTrck = findViewById(R.id.locationTrck);

        // Set a click listener for the locationTrck CardView to start the LocationTrackActivity
        locationTrck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LocationTrackActivity.class));
            }
        });

        // Set a click listener for the backButton CardView to start the DashboardFragment
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), DashboardFragment.class));
            }
        });

        db = new TodoDatabase(this);
        db.openDatabase();

        tasksRecyclerView = findViewById(R.id.tasksRecyclerViewTravel);

        // Set the layout manager for the RecyclerView
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Create an instance of the TravelTodoAdapter and set it as the adapter for the RecyclerView
        tasksAdapter = new TravelTodoAdapter(db, TravelTodoActivity.this);
        tasksRecyclerView.setAdapter(tasksAdapter);

        fab = findViewById(R.id.fab3);

        // Get all travel tasks from the database and reverse the list
        taskListTravel = db.getAllTravelTasks();
        Collections.reverse(taskListTravel);

        // Set the tasks list to the adapter
        tasksAdapter.setTasks(taskListTravel);

        // Attach an ItemTouchHelper to the RecyclerView for swipe-to-delete functionality
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerItemTouchHelperTravel(tasksAdapter));
        itemTouchHelper.attachToRecyclerView(tasksRecyclerView);

        // Set a click listener for the FAB (Floating Action Button) to add a new task
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewTaskTravel.newInstance().show(getSupportFragmentManager(), AddNewTaskTravel.TAG);
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
        taskListTravel = db.getAllTravelTasks();
        Collections.reverse(taskListTravel);
        tasksAdapter.setTasks(taskListTravel);
        tasksAdapter.notifyDataSetChanged();
    }
}




