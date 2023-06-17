package com.sathsata.myto_dolist.UIs;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sathsata.myto_dolist.BottomNavigationUIs.Dashboard.DashboardFragment;
import com.sathsata.myto_dolist.R;

public class LocationTrackActivity extends AppCompatActivity {

    private EditText location,destination;
    private CardView back;
    private Button trackLocation;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_track);
        lockOrientation();

        location = findViewById(R.id.location);
        destination = findViewById(R.id.destination);
        back = findViewById(R.id.back);
        trackLocation = findViewById(R.id.getDirection);

        //Implement back button
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), DashboardFragment.class));
            }
        });

        trackLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userLocation = location.getText().toString();
                String userDestination = destination.getText().toString();

                //Check, does all the edit filed empty?
                if(userLocation.equals("") || userDestination.equals("")){
                    Toast.makeText(LocationTrackActivity.this, "Please enter your location & destination", Toast.LENGTH_SHORT).show();
                }else{
                    getDirection(userLocation,userDestination);
                }
            }
        });
    }

    private void lockOrientation() {
        int orientation = getResources().getConfiguration().orientation;

        // Lock to portrait if the device is in landscape mode
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }


    private void getDirection(String from, String to){
        try{
            Uri uri = Uri.parse("https://www.google.com/maps/dir/"+ from+ "/" + to);
            Intent intent = new Intent(Intent.ACTION_VIEW,uri);
            intent.setPackage("com.google.android.apps.maps");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }catch(ActivityNotFoundException exception){
            Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps");
            Intent intent = new Intent(Intent.ACTION_VIEW,uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }
}