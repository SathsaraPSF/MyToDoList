package com.sathsata.myto_dolist.Authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sathsata.myto_dolist.Model.ModelClass;
import com.sathsata.myto_dolist.R;
import com.sathsata.myto_dolist.Database.DBHelper;
public class ResetActivity extends AppCompatActivity {

    private TextView userName;
    private CheckBox checkBox1, checkBox2;
    private Button confirm;
    private EditText pass, rePass;

    private DBHelper db;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);
        lockOrientation();

        // Initialize UI elements
        userName = findViewById(R.id.textView14);
        confirm = findViewById(R.id.confirmButton);
        pass = findViewById(R.id.Password);
        rePass = findViewById(R.id.rePassword);
        checkBox1 = findViewById(R.id.checkBox1);
        checkBox2 = findViewById(R.id.checkBox2);

        // Initialize DBHelper for database operations
        db = new DBHelper(this);

        // Get the username passed from the previous activity
        Intent intent = getIntent();
        userName.setText(intent.getStringExtra("username"));

        // Click event for the confirm button
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = userName.getText().toString();
                String password = pass.getText().toString();
                String rePassword = rePass.getText().toString();

                // Perform input validation
                if (password.isEmpty()) {
                    pass.setError("Password is required!");
                    pass.requestFocus();
                    return;
                }

                if (password.length() < 6) {
                    pass.setError("Minimum password length should be 6 characters!");
                    pass.requestFocus();
                    return;
                }

                if (password.equals(rePassword)) {
                    // Update the password in the database
                    Boolean check_pass_update = db.updatePassword(new ModelClass(user, password));
                    if (check_pass_update) {
                        Intent intent1 = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent1);
                        Toast.makeText(ResetActivity.this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ResetActivity.this, "Password not updated", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ResetActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                }
            }
        });


        checkBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    pass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    pass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });

        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    rePass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    rePass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
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

    // Override the onBackPressed method to handle back button press
    @Override
    public void onBackPressed() {
        // Ask the user if they want to exit the app
        new AlertDialog.Builder(this)
                .setTitle("Exit")
                .setMessage("Are you sure you don't want to reset password ?")
                .setIcon(R.drawable.logo)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Finish the current activity and exit the app
                        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

}
