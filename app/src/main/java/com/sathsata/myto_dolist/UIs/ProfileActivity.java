package com.sathsata.myto_dolist.UIs;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.sathsata.myto_dolist.Authentication.ForgetPasswordActivity;
import com.sathsata.myto_dolist.Database.DBHelper;
import com.sathsata.myto_dolist.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    TextView name,email,password;
    CircleImageView image;
    DBHelper db;
    CheckBox checkBox;
    Button changePassword;

    SharedPreferences sharedPreferences;
    String KEY_NAME = "name";
    String SHARED_PREF_NAME = "myPref";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        lockOrientation();

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        image = findViewById(R.id.user);
        checkBox = findViewById(R.id.checkBox);
        changePassword = findViewById(R.id.btnProfile);

        db = new DBHelper(this);
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);
        String username= sharedPreferences.getString(KEY_NAME,null);

        if(username != null){
            // name.setText(username);

            // Retrieve user details from the database
            Cursor cursor = db.getUserDetails(username);

            if (cursor != null && cursor.moveToFirst()) {
                // Retrieve user details
                String storedUsername = cursor.getString(0);
                String storedEmail = cursor.getString(2);
                String storedPass = cursor.getString(1);
                byte[] imageByte = cursor.getBlob(3);
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageByte,0,imageByte.length);

                // Update the UI with the retrieved user details
                name.setText(storedUsername);
                email.setText(storedEmail);
                password.setText(storedPass);
                image.setImageBitmap(bitmap);

            } else {
                // User not found
                Toast.makeText(getApplicationContext(), "User not found", Toast.LENGTH_SHORT).show();
            }

            if (cursor != null) {
                cursor.close();
            }

        }

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ForgetPasswordActivity.class));
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

}