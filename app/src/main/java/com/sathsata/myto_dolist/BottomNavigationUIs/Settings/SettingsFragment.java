package com.sathsata.myto_dolist.BottomNavigationUIs.Settings;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.sathsata.myto_dolist.MapsActivity;
import com.sathsata.myto_dolist.UIs.ProfileActivity;
import com.sathsata.myto_dolist.R;
import com.sathsata.myto_dolist.UIs.RateUsActivity;
import com.sathsata.myto_dolist.UIs.UserGuideActivity;


public class SettingsFragment extends Fragment {

    private Button sharButton, feedbackButton, rateUsButton, profileButton, checkLocation,button ;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTheme(R.style.Theme_MyToDoList);
        lockOrientation();

// Inflating the fragment layout
        View root = inflater.inflate(R.layout.fragment_settings, container, false);

// Initializing buttons
        button = root.findViewById(R.id.button);
        sharButton = root.findViewById(R.id.shareApp);
        feedbackButton = root.findViewById(R.id.feedback);
        rateUsButton = root.findViewById(R.id.rateUs);
        profileButton = root.findViewById(R.id.profile);
        checkLocation = root.findViewById(R.id.checkLocation);

// Setting up click listeners for the share button
        sharButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Creating an intent to share content
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String body = "Download This App";
                String sub = "https://play.google.com";
                intent.putExtra(Intent.EXTRA_TEXT, body);
                intent.putExtra(Intent.EXTRA_TEXT, sub);
                startActivity(Intent.createChooser(intent, "Share using"));
            }
        });

// Setting up click listeners for the feedback button
        feedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Creating an intent to send feedback via email
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                String uriText = "mailto: " + Uri.encode("sathsara.psf99@gmail.com") + "?subject=" +
                        Uri.encode("Feedback") + "$body=" + Uri.encode(" ");
                Uri uri = Uri.parse(uriText);
                intent.setData(uri);
                startActivity(Intent.createChooser(intent, "Send email"));
            }
        });

// Setting up click listeners for the rate us button
        rateUsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Starting the RateUsActivity
                startActivity(new Intent(getContext(), RateUsActivity.class));
            }
        });

// Setting up click listeners for the check location button
        checkLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Starting the MapsActivity
                Intent intent = new Intent(getContext(), MapsActivity.class);
                startActivity(intent);
            }
        });

// Setting up click listeners for the button
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Starting the UserGuideActivity
                Intent intent = new Intent(getContext(), UserGuideActivity.class);
                startActivity(intent);
            }
        });

// Setting up click listeners for the profile button
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Starting the ProfileActivity
                Intent intent = new Intent(getContext(), ProfileActivity.class);
                startActivity(intent);
            }
        });

// Returning the inflated fragment layout
        return root;
    }

    // Method to lock the screen orientation to portrait if the device is in landscape mode
    private void lockOrientation() {
        int orientation = getResources().getConfiguration().orientation;

        // Lock to portrait if the device is in landscape mode
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

}