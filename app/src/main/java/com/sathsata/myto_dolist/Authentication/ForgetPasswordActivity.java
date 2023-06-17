package com.sathsata.myto_dolist.Authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sathsata.myto_dolist.R;
import com.sathsata.myto_dolist.Database.DBHelper;

public class ForgetPasswordActivity extends AppCompatActivity {


    private EditText userName; // EditText field for entering the username
    private Button resetPassword; // Button for initiating the password reset process

    private DBHelper db; // Database helper object

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password); // Set the layout for the activity
        lockOrientation(); // Lock the orientation of the activity to portrait mode

        userName = findViewById(R.id.Username); // Get the reference to the EditText field for username
        resetPassword = findViewById(R.id.btnReset); // Get the reference to the reset password button

        db = new DBHelper(this); // Create a new instance of the database helper

        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = userName.getText().toString(); // Get the entered username
                Boolean checkUser = db.checkUsername(user); // Check if the username exists in the database
                if (checkUser) {
                    // If the username exists, start the ResetActivity and pass the username as an extra
                    Intent intent = new Intent(getApplicationContext(), ResetActivity.class);
                    intent.putExtra("username", user);
                    startActivity(intent);
                } else {
                    Toast.makeText(ForgetPasswordActivity.this, "User does not exist", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void lockOrientation() {
        int orientation = getResources().getConfiguration().orientation;

        // Lock the orientation to portrait if the device is in landscape mode
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }
}

