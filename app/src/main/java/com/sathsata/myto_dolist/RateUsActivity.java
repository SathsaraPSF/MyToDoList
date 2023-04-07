package com.sathsata.myto_dolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import com.sathsata.myto_dolist.ui.notifications.NotificationsFragment;

public class RateUsActivity extends AppCompatActivity {

    RatingBar ratingBar;
    Button button;
    CardView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_us);

        ratingBar = findViewById(R.id.ratingBar);
        button = findViewById(R.id.button2);
        backButton = findViewById(R.id.back);



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stars = String.valueOf(ratingBar.getRating());
                Toast.makeText(getApplicationContext(),stars + " Stars ",Toast.LENGTH_SHORT).show();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.replace(R.id.notification,new NotificationsFragment()).commit();
                startActivity(new Intent(getApplicationContext(),NotificationsFragment.class));
            }
        });
    }
}