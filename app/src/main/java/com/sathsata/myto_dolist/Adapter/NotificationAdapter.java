package com.sathsata.myto_dolist.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.sathsata.myto_dolist.Model.NotificationModel;
import com.sathsata.myto_dolist.R;
import com.sathsata.myto_dolist.UIs.NotificationActivity;
import com.sathsata.myto_dolist.Database.NotificationDB;

import java.util.List;


public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private List<NotificationModel> notificationModel; // List to store NotificationModel objects
    private NotificationDB db; // Notification database handler object
    private NotificationActivity activity; // Activity object

    public NotificationAdapter(NotificationDB db, NotificationActivity activity) {
        this.db = db;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the layout for each item in the RecyclerView
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        db.openDatabaseNotification(); // Open the notification database connection

        final NotificationModel item = notificationModel.get(position); // Get the NotificationModel object at the given position

        holder.notification.setText(item.getNotification()); // Set the notification text in the TextView
    }

    @Override
    public int getItemCount() {
        return notificationModel.size(); // Return the number of items in the notificationModel
    }

    private boolean toBoolean(int n) {
        return n != 0; // Convert an integer to boolean (0 = false, 1 = true)
    }

    public NotificationActivity getContext() {
        return activity; // Return the activity associated with the adapter
    }

    public void setTasks(List<NotificationModel> notificationModel) {
        this.notificationModel = notificationModel; // Set the new list of notification models
        notifyDataSetChanged(); // Notify the adapter that the data has changed
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView notification;
        CardView card;

        ViewHolder(View view) {
            super(view);
            notification = view.findViewById(R.id.notifi); // Get the reference to the TextView
            card = view.findViewById(R.id.card); // Get the reference to the CardView
        }
    }
}
