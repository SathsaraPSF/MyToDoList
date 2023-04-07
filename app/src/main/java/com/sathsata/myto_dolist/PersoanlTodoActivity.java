package com.sathsata.myto_dolist;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sathsata.myto_dolist.Adapter.PersonalTodoAdapter;
import com.sathsata.myto_dolist.Model.TodoModel;
import com.sathsata.myto_dolist.Utils.DatabaseHandler;
import com.sathsata.myto_dolist.ui.home.HomeFragment;

import java.util.Collections;
import java.util.List;

public class PersoanlTodoActivity extends AppCompatActivity implements DialogCloseListener  {


    private DatabaseHandler db;

    private RecyclerView tasksRecyclerView;
    private PersonalTodoAdapter tasksAdapter;
    private FloatingActionButton fab;


    private List<TodoModel> taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_persoanl_todo);

        CardView backButton = findViewById(R.id.back);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), HomeFragment.class));
            }
        });


        db = new DatabaseHandler(this);
        db.openDatabase();

        tasksRecyclerView = findViewById(R.id.tasksRecyclerView);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        tasksAdapter = new PersonalTodoAdapter(db, PersoanlTodoActivity.this);
        tasksRecyclerView.setAdapter(tasksAdapter);


        fab = findViewById(R.id.fab);

        taskList = db.getAllPersonalTasks();
        Collections.reverse(taskList);

        tasksAdapter.setTasks(taskList);

        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new RecyclerItemTouchHelperTwo(tasksAdapter));
        itemTouchHelper.attachToRecyclerView(tasksRecyclerView);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewTask.newInstance().show(getSupportFragmentManager(), AddNewTask.TAG);
            }
        });
    }

    @Override
    public void handleDialogClose(DialogInterface dialog) {

        taskList = db.getAllPersonalTasks();
        Collections.reverse(taskList);
        tasksAdapter.setTasks(taskList);
        tasksAdapter.notifyDataSetChanged();

    }
}




