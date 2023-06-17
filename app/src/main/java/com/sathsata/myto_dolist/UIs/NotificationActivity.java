package com.sathsata.myto_dolist.UIs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;

import com.sathsata.myto_dolist.Adapter.NotificationAdapter;
import com.sathsata.myto_dolist.Model.NotificationModel;
import com.sathsata.myto_dolist.R;
import com.sathsata.myto_dolist.Database.NotificationDB;

import java.util.Collections;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {

    private RecyclerView notificationList;
    private NotificationAdapter notificationAdapter;
    private NotificationDB db;
    private List<NotificationModel> notificationModelList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        lockOrientation();


        db = new NotificationDB(getApplicationContext());
        db.openDatabaseNotification();

        notificationList = findViewById(R.id.notificationRecyclerView);
        notificationList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        notificationAdapter = new NotificationAdapter(db, NotificationActivity.this);
        notificationList.setAdapter(notificationAdapter);

        notificationModelList = db.getAllNotification();
        Collections.reverse(notificationModelList);

        notificationAdapter.setTasks(notificationModelList);


    }

    private void lockOrientation() {
        int orientation = getResources().getConfiguration().orientation;

        // Lock to portrait if the device is in landscape mode
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

}