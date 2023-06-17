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
import com.sathsata.myto_dolist.Model.TodoModelFamily;
import com.sathsata.myto_dolist.NewTask.AddNewTaskFamily;
import com.sathsata.myto_dolist.TodoUIs.FamilyTodoActivity;
import com.sathsata.myto_dolist.R;


import java.util.List;

public class FamilyTodoAdapter extends RecyclerView.Adapter<FamilyTodoAdapter.ViewHolder> {

    private List<TodoModelFamily> todoList; // List to store TodoModelFamily objects
    private final TodoDatabase db; // Database handler object
    private final FamilyTodoActivity activity; // Activity object

    public FamilyTodoAdapter(TodoDatabase db, FamilyTodoActivity activity) {
        this.db = db;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the layout for each item in the RecyclerView
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_layout_family, parent, false);
        return new FamilyTodoAdapter.ViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        db.openDatabase(); // Open the family database connection

        final TodoModelFamily item = todoList.get(position); // Get the TodoModelFamily object at the given position

        holder.task.setText(item.getTask() + "\n\n" + item.getTime() + "\n\n" + item.getDate());
        holder.task.setChecked(toBoolean(item.getStatus())); // Set the checkbox status

        holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Update the status of the TodoModelFamily object in the database based on checkbox state
                if (isChecked) {
                    db.updateStatusFamily(item.getId(), 1);
                } else {
                    db.updateStatusFamily(item.getId(), 0);
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

    public void setTasks(List<TodoModelFamily> todoList) {
        this.todoList = todoList; // Set the new list of tasks
        notifyDataSetChanged(); // Notify the adapter that the data has changed
    }

    public void deleteItem(int position) {
        TodoModelFamily item = todoList.get(position); // Get the TodoModelFamily object at the given position
        db.deleteTaskFamily(item.getId()); // Delete the task from the family database
        todoList.remove(position); // Remove the task from the list
        notifyItemRemoved(position); // Notify the adapter about the removed item
    }

    public void editItem(int position) {
        TodoModelFamily item = todoList.get(position); // Get the TodoModelFamily object at the given position
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("task", item.getTask());
        bundle.putString("date", item.getDate());
        bundle.putString("time", item.getTime());

        AddNewTaskFamily fragment = new AddNewTaskFamily();
        fragment.setArguments(bundle); // Set the bundle as arguments for the fragment
        fragment.show(activity.getSupportFragmentManager(), AddNewTaskFamily.TAG); // Show the AddNewTaskWork fragment
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox task;

        ViewHolder(View view) {
            super(view);
            MediaPlayer mediaPlayer = MediaPlayer.create(view.getContext(), R.raw.beep);
            task = view.findViewById(R.id.todoCheckBoxFamily);
            task.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mediaPlayer.start(); // Start playing the beep sound when the checkbox is clicked
                }
            });
        }
    }
}



