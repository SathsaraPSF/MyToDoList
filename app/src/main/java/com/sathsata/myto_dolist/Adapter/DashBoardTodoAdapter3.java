package com.sathsata.myto_dolist.Adapter;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.recyclerview.widget.RecyclerView;


import com.sathsata.myto_dolist.Database.TodoDatabase;
import com.sathsata.myto_dolist.Model.TodoModelTravel;
import com.sathsata.myto_dolist.NewTask.AddNewTaskTravel;
import com.sathsata.myto_dolist.R;
import com.sathsata.myto_dolist.BottomNavigationUIs.Dashboard.DashboardFragment;


import java.util.List;

public class DashBoardTodoAdapter3 extends RecyclerView.Adapter<DashBoardTodoAdapter3.ViewHolder> {

    private List<TodoModelTravel> todoList; // List to store TodoModelTravel objects
    private TodoDatabase db; // Database handler object
    private DashboardFragment activity; // Fragment object

    public DashBoardTodoAdapter3(TodoDatabase db, DashboardFragment activity) {
        this.db = db;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the layout for each item in the RecyclerView
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_layout_travel_to_dashboard, parent, false);
        return new DashBoardTodoAdapter3.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        db.openDatabase(); // Open the travel database connection

        final TodoModelTravel item = todoList.get(position); // Get the TodoModelTravel object at the given position

        holder.task.setText(item.getTask() + "\n\n" + item.getTime() + "\n\n" + item.getDate());
        holder.task.setChecked(toBoolean(item.getStatus())); // Set the checkbox status

        holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Update the status of the TodoModelTravel object in the database based on checkbox state
                if (isChecked) {
                    db.updateStatusTravel(item.getId(), 1);
                } else {
                    db.updateStatusTravel(item.getId(), 0);
                }
            }
        });
    }

    private boolean toBoolean(int n) {
        return n != 0; // Convert an integer to boolean (0 = false, 1 = true)
    }

    @Override
    public int getItemCount() {
        return todoList.size(); // Return the number of items in the todoList
    }

    public DashboardFragment getContext() {
        return activity; // Return the activity associated with the adapter
    }

    public void setTasks(List<TodoModelTravel> todoList) {
        this.todoList = todoList; // Set the new list of tasks
        notifyDataSetChanged(); // Notify the adapter that the data has changed
    }

    public void deleteItem(int position) {
        TodoModelTravel item = todoList.get(position); // Get the TodoModelTravel object at the given position
        db.deleteTaskTravel(item.getId()); // Delete the task from the travel database
        todoList.remove(position); // Remove the task from the list
        notifyItemRemoved(position); // Notify the adapter about the removed item
    }

    public void editItem(int position) {
        TodoModelTravel item = todoList.get(position); // Get the TodoModelTravel object at the given position
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("task", item.getTask());

        AddNewTaskTravel fragment = new AddNewTaskTravel();
        fragment.setArguments(bundle); // Set the bundle as arguments for the fragment
        fragment.show(activity.getSupportFragmentManager(), AddNewTaskTravel.TAG); // Show the AddNewTaskTravel fragment
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox task;

        ViewHolder(View view) {
            super(view);
            MediaPlayer mediaPlayer = MediaPlayer.create(view.getContext(), R.raw.beep);
            task = view.findViewById(R.id.todoCheckBoxTravel);
            task.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mediaPlayer.start(); // Start playing the beep sound when the checkbox is clicked
                }
            });
        }
    }
}