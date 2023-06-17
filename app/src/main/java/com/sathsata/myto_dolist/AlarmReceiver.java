package com.sathsata.myto_dolist;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.sathsata.myto_dolist.Model.NotificationModel;
import com.sathsata.myto_dolist.UIs.NotificationActivity;
import com.sathsata.myto_dolist.Database.NotificationDB;

public class AlarmReceiver extends BroadcastReceiver {

    private NotificationDB db;
    @Override
    @SuppressLint("RemoteViewLayout")
    public void onReceive(Context context, Intent intent) {

        db = new NotificationDB(context);
        db.openDatabaseNotification();

        // Extract the TODO message from the intent
        String todoMessage = intent.getStringExtra("TODO_MESSAGE");


        NotificationModel notificationModel = new NotificationModel(todoMessage);
        db.insertNotification(notificationModel);


        // Create an explicit intent for the notification's UI
        Intent notificationIntent  = new Intent(context, NotificationActivity.class);


        notificationIntent .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,notificationIntent ,0);

        // Display the notification using NotificationCompat.Builder
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "todolist")
                .setSmallIcon(R.drawable.bel_icon)
                .setContentTitle("Notification")
                .setContentText("You have a todo reminder..")
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);

        // Get the NotificationManager and display the notification
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);

        // Get the specific permissions and display the notification
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;

        }

        notificationManagerCompat.notify(123, builder.build());




    }
}
