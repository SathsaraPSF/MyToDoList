package com.sathsata.myto_dolist.BottomNavigationUIs.Dashboard;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sathsata.myto_dolist.Adapter.DashBoardTodoAdapter1;
import com.sathsata.myto_dolist.Adapter.DashBoardTodoAdapter2;
import com.sathsata.myto_dolist.Adapter.DashBoardTodoAdapter3;
import com.sathsata.myto_dolist.Adapter.DashBoardTodoAdapter4;
import com.sathsata.myto_dolist.Database.TodoDatabase;
import com.sathsata.myto_dolist.Model.TodoModelFamily;
import com.sathsata.myto_dolist.Model.TodoModelPersonal;
import com.sathsata.myto_dolist.Model.TodoModelTravel;
import com.sathsata.myto_dolist.RecyclerItemTouchHelper.RecyclerItemTouchHelper4;
import com.sathsata.myto_dolist.RecyclerItemTouchHelper.RecyclerItemTouchHelper5;
import com.sathsata.myto_dolist.RecyclerItemTouchHelper.RecyclerItemTouchHelper6;
import com.sathsata.myto_dolist.TodoUIs.FamilyTodoActivity;
import com.sathsata.myto_dolist.TodoUIs.TravelTodoActivity;
import com.sathsata.myto_dolist.Authentication.LoginActivity;
import com.sathsata.myto_dolist.Model.TodoModelWork;
import com.sathsata.myto_dolist.TodoUIs.PersonalTodoActivity;
import com.sathsata.myto_dolist.R;
import com.sathsata.myto_dolist.RecyclerItemTouchHelper.RecyclerItemTouchHelper3;
import com.sathsata.myto_dolist.UIs.NotificationActivity;
import com.sathsata.myto_dolist.Database.DBHelper;
import com.sathsata.myto_dolist.TodoUIs.WorkToDoActivity;

import java.util.Collections;
import java.util.List;

public class DashboardFragment extends Fragment implements SensorEventListener {



    CardView cardView, workTodo, personaTodo, familyTodo, travelTodo, notification;
    TextView textView;
    ImageView imageView, header_circle;

    private RecyclerView tasksRecyclerView, tasksRecyclerView2, tasksRecyclerView3, tasksRecyclerView4;

    private TodoDatabase db;

    private DBHelper DB;

    private DashBoardTodoAdapter1 tasksAdapter;
    private DashBoardTodoAdapter2 tasksAdapter2;
    private DashBoardTodoAdapter3 tasksAdapter3;
    private DashBoardTodoAdapter4 tasksAdapter4;


    private List<TodoModelWork> taskList;
    private List<TodoModelPersonal> taskListPersonal;
    private List<TodoModelTravel> taskListTravel;
    private List<TodoModelFamily> taskListFamily;

    private ConstraintLayout background;
    private SensorManager sensorManager;

    private float changeValue;


    @SuppressLint("MissingInflatedId")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        getActivity().setTheme(R.style.Theme_MyToDoList);

        // Lock the initial orientation
        lockOrientation();

        View root = inflater.inflate(R.layout.fragment_home, container, false);

// Initialize UI elements
        cardView = root.findViewById(R.id.logout);
        notification = root.findViewById(R.id.notification);
        textView = root.findViewById(R.id.name);
        imageView = root.findViewById(R.id.user);
        workTodo = root.findViewById(R.id.workTodo);
        personaTodo = root.findViewById(R.id.personalTodo);
        familyTodo = root.findViewById(R.id.familiyTodo);
        travelTodo = root.findViewById(R.id.travelTodo);

        tasksRecyclerView = root.findViewById(R.id.recyclerView);
        tasksRecyclerView2 = root.findViewById(R.id.recyclerView2);
        tasksRecyclerView3 = root.findViewById(R.id.recyclerView3);
        tasksRecyclerView4 = root.findViewById(R.id.recyclerView4);

        header_circle = root.findViewById(R.id.imageView7);
        background = root.findViewById(R.id.background);

    // Initialize DBHelper for database operations
        DB = new DBHelper(getContext());

    // Get the username of the logged-in user
        Intent intent = getActivity().getIntent();
        String username = intent.getStringExtra("user_name");

    // Retrieve user details from the database
        Cursor cursor = DB.getUserDetails(username);

        if (cursor != null && cursor.moveToFirst()) {
            // Retrieve user details
            String storedUsername = cursor.getString(0);
            byte[] imageByte = cursor.getBlob(3);
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length);
            imageView.setImageBitmap(bitmap);

            // Update the UI with the retrieved user details
            textView.setText(storedUsername);
        } else {
            // User not found
            Toast.makeText(getContext(), "User not found", Toast.LENGTH_SHORT).show();
        }

        if (cursor != null) {
            cursor.close();
        }

        // Initialize SensorManager to access device sensors
        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);

        // Initialize database handlers for different todo types
        db = new TodoDatabase(getContext());
        db.openDatabase();


        // managers for the RecyclerViews
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        tasksRecyclerView2.setLayoutManager(new LinearLayoutManager(getContext()));
        tasksRecyclerView3.setLayoutManager(new LinearLayoutManager(getContext()));
        tasksRecyclerView4.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize adapters for the RecyclerViews
        tasksAdapter = new DashBoardTodoAdapter1(db, DashboardFragment.this);
        tasksAdapter2 = new DashBoardTodoAdapter2(db, DashboardFragment.this);
        tasksAdapter3 = new DashBoardTodoAdapter3(db, DashboardFragment.this);
        tasksAdapter4 = new DashBoardTodoAdapter4(db, DashboardFragment.this);

        // Set adapters for the RecyclerViews
        tasksRecyclerView.setAdapter(tasksAdapter);
        tasksRecyclerView2.setAdapter(tasksAdapter2);
        tasksRecyclerView3.setAdapter(tasksAdapter3);
        tasksRecyclerView4.setAdapter(tasksAdapter4);

        // Retrieve todo tasks from the database and set them in the adapters
        taskList = db.getAllTasksWork();
        taskListPersonal = db.getAllPersonalTasks();
        taskListTravel = db.getAllTravelTasks();
        taskListFamily = db.getAllFamilyTasks();

        Collections.reverse(taskList);
        Collections.reverse(taskListPersonal);
        Collections.reverse(taskListTravel);
        Collections.reverse(taskListFamily);

        tasksAdapter.setTasks(taskList);
        tasksAdapter2.setTasks(taskListPersonal);
        tasksAdapter3.setTasks(taskListTravel);
        tasksAdapter4.setTasks(taskListFamily);

        // Set up item touch helpers for swipe actions on the RecyclerView items
        ItemTouchHelper itemTouchHelper3 = new ItemTouchHelper(new RecyclerItemTouchHelper3(tasksAdapter));
        itemTouchHelper3.attachToRecyclerView(tasksRecyclerView);

        ItemTouchHelper itemTouchHelper4 = new ItemTouchHelper(new RecyclerItemTouchHelper4(tasksAdapter2));
        itemTouchHelper4.attachToRecyclerView(tasksRecyclerView2);

        ItemTouchHelper itemTouchHelper5 = new ItemTouchHelper(new RecyclerItemTouchHelper5(tasksAdapter3));
        itemTouchHelper5.attachToRecyclerView(tasksRecyclerView3);

        ItemTouchHelper itemTouchHelper6 = new ItemTouchHelper(new RecyclerItemTouchHelper6(tasksAdapter4));
        itemTouchHelper6.attachToRecyclerView(tasksRecyclerView4);

        // Click event for logout button
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //SignOut();
                startActivity(new Intent(getContext(), LoginActivity.class));
            }
        });

        // Click events for different todo type buttons
        workTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), WorkToDoActivity.class);
                startActivity(intent);
            }
        });

        personaTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PersonalTodoActivity.class);
                startActivity(intent);
            }
        });

        travelTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), TravelTodoActivity.class);
                startActivity(intent);
            }
        });

        familyTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), FamilyTodoActivity.class);
                startActivity(intent);
            }
        });

        // Click event for notification button
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), NotificationActivity.class));
            }
        });

        // Add an onBackPressed callback to the fragment's activity
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Ask the user if they want to exit the app
                new AlertDialog.Builder(requireContext())
                        .setTitle("Exit")
                        .setIcon(R.drawable.logo)
                        .setMessage("Are you sure you want to exit?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Finish the current activity and exit the app
                                requireActivity().finishAffinity();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });

        return root;

    }


    private void lockOrientation() {
        int orientation = getResources().getConfiguration().orientation;

        // Lock to portrait if the device is in landscape mode
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }


    @SuppressLint("ResourceType")
    @Override
    public void onSensorChanged(SensorEvent event) {
        changeValue = event.values[0];
        int value = (int) changeValue;

        if (value == 0) {
            background.setBackgroundResource(R.drawable.back_bg); // Set background to back_bg when value is 0
        } else if (value < -5) {
            background.setBackgroundResource(R.drawable.background); // Set background to background when value is less than -5
        } else if (value > 5) {
            background.setBackgroundResource(R.drawable.background); // Set background to background when value is greater than 5
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        // Register the sensor listener for accelerometer sensor with normal delay
    }

    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
        // Unregister the sensor listener to stop receiving sensor events
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // This method is called when the accuracy of the sensor changes, but no action is performed here
    }

    public FragmentManager getSupportFragmentManager() {
        return null;
        // Return null for the getSupportFragmentManager method
    }



}