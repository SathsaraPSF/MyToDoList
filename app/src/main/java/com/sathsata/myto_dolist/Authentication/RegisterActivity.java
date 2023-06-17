package com.sathsata.myto_dolist.Authentication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.sathsata.myto_dolist.Authentication.LoginActivity;
import com.sathsata.myto_dolist.Model.ModelClass;
import com.sathsata.myto_dolist.R;
import com.sathsata.myto_dolist.Database.DBHelper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity {

    private EditText username, password, rePassword, email;
    private Button btnRegister, signIn;
    private CircleImageView userImage;
    private CheckBox checkBox1, checkBox2;

    private DBHelper DB;

    private static final int PICK_IMAGE_REQUEST = 99;
    private Uri imagePath;
    private Bitmap imageToStore;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        lockOrientation();

        // Initialize UI elements
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        rePassword = (EditText) findViewById(R.id.conPassword);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        signIn = (Button) findViewById(R.id.btnSignIn);
        checkBox1 = findViewById(R.id.checkBox1);
        checkBox2 = findViewById(R.id.checkBox2);

        // Initialize DBHelper for database operations
        DB = new DBHelper(this);
        DB.getWritableDatabase();
        userImage = findViewById(R.id.user);
        email = findViewById(R.id.email);

        // Add click event to image to pick an image from local device storage
        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choseImage();
            }
        });

        // Register button click event
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get user input values
                String user = username.getText().toString();
                String pass = password.getText().toString();
                String repass = rePassword.getText().toString();
                String Email = email.getText().toString().trim();

                // Validate email format using regular expression
                Pattern pattern = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$");
                Matcher matcher = pattern.matcher(Email);

                // Perform input validation
                if (user.isEmpty()) {
                    username.setError("Username is required!");
                    username.requestFocus();
                    return;
                }
                if (pass.isEmpty()) {
                    password.setError("Password is required!");
                    password.requestFocus();
                    return;
                }
                if (!matcher.matches()) {
                    email.setError("Enter a valid email");
                    email.requestFocus();
                    return;
                }
                if (Email.isEmpty()) {
                    email.setError("Email is required!");
                    email.requestFocus();
                    return;
                }
                if (pass.length() < 6) {
                    password.setError("Minimum password length should be 6 characters!");
                    password.requestFocus();
                    return;
                } else if (repass.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please enter all the fields", Toast.LENGTH_SHORT).show();
                } else {
                    if (pass.equals(repass)) {
                        // Check if the username already exists in the database
                        Boolean checkUser = DB.checkUsername(user);
                        if (!checkUser) {
                            // Insert user details into the database
                            Boolean insert = DB.insertDetails(new ModelClass(user, Email, pass, imageToStore));
                            if (insert) {
                                Toast.makeText(getApplicationContext(), "Registered successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(getApplicationContext(), "Registration failed", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "User already exists! Please sign in", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // Sign in button click event
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        // Checkbox 1 (Password visibility) state change event
        checkBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    // Show password
                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    // Hide password
                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });

        // Checkbox 2 (Re-enter Password visibility) state change event
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    // Show password
                    rePassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    // Hide password
                    rePassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });
    }

    // Locks the orientation to portrait mode
    private void lockOrientation() {
        int orientation = getResources().getConfiguration().orientation;

        // Lock to portrait if the device is in landscape mode
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    // Opens a dialog to choose an image from the local device storage
    private void choseImage() {
        try {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    // Handles the result of the image selection dialog
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
                // Get the selected image's URI
                imagePath = data.getData();

                // Convert the URI to a Bitmap
                imageToStore = MediaStore.Images.Media.getBitmap(getContentResolver(), imagePath);

                // Set the selected image in the ImageView
                userImage.setImageBitmap(imageToStore);
            }
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}