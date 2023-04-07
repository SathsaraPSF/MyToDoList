package com.sathsata.myto_dolist.ui.notifications;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.sathsata.myto_dolist.MainActivity;
import com.sathsata.myto_dolist.R;
import com.sathsata.myto_dolist.RateUsActivity;
import com.sathsata.myto_dolist.UserGuideActivity;
import com.sathsata.myto_dolist.databinding.FragmentNotificationsBinding;

public class NotificationsFragment extends Fragment {

    Button sharButton, feedbackButton, rateUsButton,aboutUsButton;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        getActivity().setTheme(R.style.Theme_MyToDoList);

      View root = inflater.inflate(R.layout.fragment_notifications,container,false);

        Button button = root.findViewById(R.id.button);
        sharButton= root.findViewById(R.id.shareApp);
        feedbackButton = root.findViewById(R.id.feedback);
        rateUsButton = root.findViewById(R.id.rateUs);
        aboutUsButton = root.findViewById(R.id.aboutUs);

        sharButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setType("text/plain");
                String body = "Download This App";
                String sub = "https://play.google.com";
                intent.putExtra(Intent.EXTRA_TEXT,body);
                intent.putExtra(Intent.EXTRA_TEXT,sub);
                startActivity(Intent.createChooser(intent,"Share using"));
            }
        });

        feedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                String uriText = "mailto: " + Uri.encode("sathsara.psf99@gmail.com") + "?subject ="+
                        Uri.encode("Feedback") + "$body=" + Uri.encode(" ");
                Uri uri = Uri.parse(uriText);
                intent.setData(uri);
                startActivity(Intent.createChooser(intent,"Send email"));
            }
        });

        rateUsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent(getContext(), RateUsActivity.class));
            }
        });



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), UserGuideActivity.class);
                startActivity(intent);
            }
        });




        return root;
    }


}