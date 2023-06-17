package com.sathsata.myto_dolist.Authentication;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sathsata.myto_dolist.UIs.NavigationActivity;
import com.sathsata.myto_dolist.R;
import com.sathsata.myto_dolist.Database.DBHelper;

public class LoginActivity extends AppCompatActivity {

    private EditText username, password; // EditText fields for entering username and password
    private Button btnLogin, btnSignup, forgetPassword; // Buttons for login, signup, and forget password

    private DBHelper DB; // Database helper object

    private SharedPreferences sharedPreferences; // SharedPreferences object for storing user's name
    private String KEY_NAME = "name"; // Key for storing user's name in SharedPreferences
    private String SHARED_PREF_NAME = "myPref"; // Name of the SharedPreferences file

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // Set the layout for the activity
        lockOrientation(); // Lock the orientation of the activity to portrait mode

        username = findViewById(R.id.username1); // Get the reference to the EditText field for username
        password = findViewById(R.id.password1); // Get the reference to the EditText field for password
        btnLogin = findViewById(R.id.btnsignin1); // Get the reference to the login button
        btnSignup = findViewById(R.id.btnRegister); // Get the reference to the signup button
        forgetPassword = findViewById(R.id.forgetPassword); // Get the reference to the forget password button

        DB = new DBHelper(this); // Create a new instance of the database helper
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE); // Get the SharedPreferences object

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = String.valueOf(username.getText()); // Get the entered username
                String pass = password.getText().toString(); // Get the entered password
                Boolean checkUserName = DB.checkUsername(user); // Check if the username exists in the database
                Boolean checkPassword = DB.checkPassword(pass); // Check if the password is valid

                // Check if the username and password fields are empty
                if (user.equals("") || pass.equals("")) {
                    Toast.makeText(LoginActivity.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                }
                if (!checkUserName) {
                    username.setError("Invalid username");
                }
                if (!checkPassword) {
                    Toast.makeText(LoginActivity.this, "Invalid password", Toast.LENGTH_SHORT).show();
                } else {
                    Boolean checkUserPass = DB.checkUsernamePassword(user, pass); // Check if the username and password match
                    if (checkUserPass) {
                        Toast.makeText(LoginActivity.this, "Sign in successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), NavigationActivity.class);
                        intent.putExtra("user_name", user);
                        startActivity(intent);

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(KEY_NAME, user); // Save the user's name in SharedPreferences
                        editor.apply();

                    } else {
                        Toast.makeText(LoginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ForgetPasswordActivity.class);
                startActivity(intent);
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

    // Override the onBackPressed method to handle back button press
    @Override
    public void onBackPressed() {
        // Ask the user if they want to exit the app
        new AlertDialog.Builder(this)
                .setTitle("Exit")
                .setIcon(R.drawable.logo)
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Finish the current activity and exit the app
                        finishAffinity();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}



