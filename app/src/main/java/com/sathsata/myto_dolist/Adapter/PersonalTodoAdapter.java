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
import com.sathsata.myto_dolist.NewTask.AddNewTaskPersonal;
import com.sathsata.myto_dolist.Model.TodoModelPersonal;
import com.sathsata.myto_dolist.TodoUIs.PersonalTodoActivity;
import com.sathsata.myto_dolist.R;


import java.util.List;

public class PersonalTodoAdapter extends RecyclerView.Adapter<PersonalTodoAdapter.ViewHolder> {

    private List<TodoModelPersonal> todoList; // List to store TodoModelPersonal objects
    private final TodoDatabase db; // Personal todo database handler object
    private final PersonalTodoActivity activity; // Activity object

    public PersonalTodoAdapter(TodoDatabase db, PersonalTodoActivity activity) {
        this.db = db;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the layout for each item in the RecyclerView
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_layout_personal, parent, false);
        return new PersonalTodoAdapter.ViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        db.openDatabase(); // Open the personal todo database connection

        final TodoModelPersonal item = todoList.get(position); // Get the TodoModelPersonal object at the given position

        holder.task.setText(item.getTask() + "\n\n" + item.getTime() + "\n\n" + item.getDate()); // Set the task text in the CheckBox
        holder.task.setChecked(toBoolean(item.getStatus())); // Set the status (checked or unchecked) of the CheckBox

        holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    db.updateStatusPersonal(item.getId(), 1); // Update the status of the personal task to checked (1)
                } else {
                    db.updateStatusPersonal(item.getId(), 0); // Update the status of the personal task to unchecked (0)
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

    public void setTasks(List<TodoModelPersonal> todoList) {
        this.todoList = todoList; // Set the new list of todo models
        notifyDataSetChanged(); // Notify the adapter that the data has changed
    }

    public void deleteItem(int position) {
        TodoModelPersonal item = todoList.get(position); // Get the TodoModelPersonal object at the given position
        db.deleteTaskPersonal(item.getId()); // Delete the personal task from the database
        todoList.remove(position); // Remove the personal task from the todoList
        notifyItemRemoved(position); // Notify the adapter that an item has been removed
    }

    public void editItem(int position) {
        TodoModelPersonal item = todoList.get(position); // Get the TodoModelPersonal object at the given position
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("task", item.getTask());
        bundle.putString("date", item.getDate());
        bundle.putString("time", item.getTime());

        AddNewTaskPersonal fragment = new AddNewTaskPersonal();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(), AddNewTaskPersonal.TAG);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox task;

        ViewHolder(View view) {
            super(view);
            MediaPlayer mediaPlayer = MediaPlayer.create(view.getContext(), R.raw.beep);
            task = view.findViewById(R.id.todoCheckBoxPersonal); // Get the reference to the CheckBox
            task.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mediaPlayer.start(); // Play a sound when the CheckBox is clicked
                }
            });
        }
    }

}