package com.sathsata.myto_dolist.Adapter;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.sathsata.myto_dolist.Database.TodoDatabase;
import com.sathsata.myto_dolist.NewTask.AddNewTaskWork;
import com.sathsata.myto_dolist.Model.TodoModelWork;
import com.sathsata.myto_dolist.R;
import com.sathsata.myto_dolist.BottomNavigationUIs.Dashboard.DashboardFragment;


import java.util.List;

public class DashBoardTodoAdapter1 extends RecyclerView.Adapter<DashBoardTodoAdapter1.ViewHolder> {

    private List<TodoModelWork> todoList; // List to store TodoModelWork objects

    private TodoDatabase db; // Database handler object

    private DashboardFragment activity; // Fragment object


    public DashBoardTodoAdapter1(TodoDatabase db, DashboardFragment activity) {
        this.db = db;
        this.activity = activity;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the layout for each item in the RecyclerView
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_layout_work_to_dashboard, parent, false);
        return new DashBoardTodoAdapter1.ViewHolder(itemView);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        db.openDatabase(); // Open the database connection

        final TodoModelWork item = todoList.get(position); // Get the TodoModelWork object at the given position

        holder.task.setText(item.getTask() + "\n\n" + item.getTime() + "\n\n" + item.getDate());
        holder.task.setChecked(toBoolean(item.getStatus())); // Set the checkbox status
        holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Update the status of the TodoModelWork object in the database based on checkbox state
                if (isChecked) {
                    db.updateStatusWork(item.getId(), 1);
                } else {
                    db.updateStatusWork(item.getId(), 0);
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


    public void setTasks(List<TodoModelWork> todoList) {
        this.todoList = todoList; // Set the new list of tasks
        notifyDataSetChanged(); // Notify the adapter that the data has changed
    }

    public void deleteItem(int position) {
        TodoModelWork item = todoList.get(position); // Get the TodoModelWork object at the given position
        db.deleteTaskWork(item.getId()); // Delete the task from the database
        todoList.remove(position); // Remove the task from the list
        notifyItemRemoved(position); // Notify the adapter about the removed item
    }

    public void editItem(int position) {
        TodoModelWork item = todoList.get(position); // Get the TodoModelWork object at the given position
        Bundle bundle = new Bundle();

        // Create a bundle to pass data to the AddNewTaskWork fragment
        bundle.putInt("id", item.getId());
        bundle.putString("task", item.getTask());
        bundle.putString("date", item.getDate());
        bundle.putString("time", item.getTime());

        AddNewTaskWork fragment = new AddNewTaskWork();
        fragment.setArguments(bundle); // Set the bundle as arguments for the fragment
        fragment.show(activity.getSupportFragmentManager(), AddNewTaskWork.TAG);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox task;

        ViewHolder(View view) {
            super(view);
            MediaPlayer mediaPlayer = MediaPlayer.create(view.getContext(), R.raw.beep);
            task = view.findViewById(R.id.todoCheckBoxWork2);
            task.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mediaPlayer.start(); // Start playing the beep sound when the checkbox is clicked
                }
            });
        }
    }
}