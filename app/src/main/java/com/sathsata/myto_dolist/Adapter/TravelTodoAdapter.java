package com.sathsata.myto_dolist.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
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
import com.sathsata.myto_dolist.Model.TodoModelTravel;
import com.sathsata.myto_dolist.NewTask.AddNewTaskTravel;
import com.sathsata.myto_dolist.R;
import com.sathsata.myto_dolist.TodoUIs.TravelTodoActivity;


import java.util.List;

public class TravelTodoAdapter extends RecyclerView.Adapter<TravelTodoAdapter.ViewHolder> {

    private List<TodoModelTravel> todoList; // List to store TodoModelTravel objects
    private final TodoDatabase db; // Travel todo database handler object
    private final TravelTodoActivity activity; // Activity object

    public TravelTodoAdapter(TodoDatabase db, TravelTodoActivity activity) {
        this.db = db;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the layout for each item in the RecyclerView
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_layout_travel, parent, false);
        return new TravelTodoAdapter.ViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        db.openDatabase(); // Open the travel todo database connection

        final TodoModelTravel item = todoList.get(position); // Get the TodoModelTravel object at the given position

        holder.task.setText(item.getTask() + "\n\n" + item.getTime() + "\n\n" + item.getDate()); // Set the task text in the CheckBox
        holder.task.setChecked(toBoolean(item.getStatus())); // Set the status (checked or unchecked) of the CheckBox

        holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    db.updateStatusTravel(item.getId(), 1); // Update the status of the travel task to checked (1)
                } else {
                    db.updateStatusTravel(item.getId(), 0); // Update the status of the travel task to unchecked (0)
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

    public Context getContext() {
        return activity; // Return the activity associated with the adapter
    }

    public void setTasks(List<TodoModelTravel> todoList) {
        this.todoList = todoList; // Set the new list of todo models
        notifyDataSetChanged(); // Notify the adapter that the data has changed
    }

    public void deleteItem(int position) {
        TodoModelTravel item = todoList.get(position); // Get the TodoModelTravel object at the given position
        db.deleteTaskTravel(item.getId()); // Delete the travel task from the database
        todoList.remove(position); // Remove the travel task from the todoList
        notifyItemRemoved(position); // Notify the adapter that an item has been removed
    }

    public void editItem(int position) {
        TodoModelTravel item = todoList.get(position); // Get the TodoModelTravel object at the given position
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("task", item.getTask());
        bundle.putString("date", item.getDate());
        bundle.putString("time", item.getTime());

        AddNewTaskTravel fragment = new AddNewTaskTravel();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(), AddNewTaskTravel.TAG);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox task;

        ViewHolder(View view) {
            super(view);
            MediaPlayer mediaPlayer = MediaPlayer.create(view.getContext(), R.raw.beep);
            task = view.findViewById(R.id.todoCheckBoxTravel); // Get the reference to the CheckBox
            task.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mediaPlayer.start(); // Play a sound when the CheckBox is clicked
                }
            });
        }
    }
}
