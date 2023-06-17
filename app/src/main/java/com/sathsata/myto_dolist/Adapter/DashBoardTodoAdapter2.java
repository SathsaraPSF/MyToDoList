package com.sathsata.myto_dolist.Adapter;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.recyclerview.widget.RecyclerView;


import com.sathsata.myto_dolist.Database.TodoDatabase;
import com.sathsata.myto_dolist.Model.TodoModelPersonal;
import com.sathsata.myto_dolist.NewTask.AddNewTaskPersonal;
import com.sathsata.myto_dolist.R;
import com.sathsata.myto_dolist.BottomNavigationUIs.Dashboard.DashboardFragment;


import java.util.List;

public class DashBoardTodoAdapter2 extends RecyclerView.Adapter<DashBoardTodoAdapter2.ViewHolder> {

    private List<TodoModelPersonal> todoList; // List to store TodoModelPersonal objects

    private TodoDatabase db; // Database handler object

    private DashboardFragment activity; // Fragment object

    public DashBoardTodoAdapter2(TodoDatabase db, DashboardFragment activity) {
        this.db = db;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the layout for each item in the RecyclerView
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_layout_personal_to_dashboard, parent, false);
        return new DashBoardTodoAdapter2.ViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        db.openDatabase(); // Open the personal database connection

        final TodoModelPersonal item = todoList.get(position); // Get the TodoModelPersonal object at the given position

        holder.task.setText(item.getTask() + "\n\n" + item.getTime() + "\n\n" + item.getDate());
        holder.task.setChecked(toBoolean(item.getStatus())); // Set the checkbox status

        holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Update the status of the TodoModelPersonal object in the database based on checkbox state
                if (isChecked) {
                    db.updateStatusPersonal(item.getId(), 1);
                } else {
                    db.updateStatusPersonal(item.getId(), 0);
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

    public void setTasks(List<TodoModelPersonal> todoList) {
        this.todoList = todoList; // Set the new list of tasks
        notifyDataSetChanged(); // Notify the adapter that the data has changed
    }

    public void deleteItem(int position) {
        TodoModelPersonal item = todoList.get(position); // Get the TodoModelPersonal object at the given position
        db.deleteTaskPersonal(item.getId()); // Delete the task from the personal database
        todoList.remove(position); // Remove the task from the list
        notifyItemRemoved(position); // Notify the adapter about the removed item
    }

    public void editItem(int position) {
        TodoModelPersonal item = todoList.get(position); // Get the TodoModelPersonal object at the given position
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("task", item.getTask());

        AddNewTaskPersonal fragment = new AddNewTaskPersonal();
        fragment.setArguments(bundle); // Set the bundle as arguments for the fragment
        fragment.show(activity.getSupportFragmentManager(), AddNewTaskPersonal.TAG); // Show the AddNewTaskPersonal fragment
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox task;

        ViewHolder(View view) {
            super(view);
            MediaPlayer mediaPlayer = MediaPlayer.create(view.getContext(), R.raw.beep);
            task = view.findViewById(R.id.todoCheckBoxPersonal);
            task.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mediaPlayer.start(); // Start playing the beep sound when the checkbox is clicked
                }
            });
        }
    }
}