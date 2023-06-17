package com.sathsata.myto_dolist.BottomNavigationUIs.Calander;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.sathsata.myto_dolist.R;

import java.util.Calendar;


public class calenderFragment extends Fragment {


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_calander, container, false);
        getActivity().setTheme(R.style.Theme_MyToDoList);

        // Lock the initial orientation
        lockOrientation();
        return root;


    }

    private void lockOrientation() {
        int orientation = getResources().getConfiguration().orientation;

        // Lock to portrait if the device is in landscape mode
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

}