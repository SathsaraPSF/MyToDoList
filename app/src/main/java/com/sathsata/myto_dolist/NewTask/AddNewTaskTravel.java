package com.sathsata.myto_dolist.NewTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.sathsata.myto_dolist.AlarmReceiver;
import com.sathsata.myto_dolist.Database.TodoDatabase;
import com.sathsata.myto_dolist.DialogCloseListener;
import com.sathsata.myto_dolist.Model.TodoModelTravel;
import com.sathsata.myto_dolist.R;

import java.util.Calendar;

public class AddNewTaskTravel extends BottomSheetDialogFragment {

    public static final String TAG = "ActionBottomDialog";
    private EditText newTaskText;
    private Button newTaskSaveButton;
    private TodoDatabase db2;
    private ImageButton calender, time;
    private EditText date, time2;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private MaterialTimePicker timePicker;
    private Calendar cal;

    public static AddNewTaskTravel newInstance() {
        return new AddNewTaskTravel();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.DialogStyle);
        createNotificationChannel();
    }

    // Create a notification channel for high importance notifications
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "mytodolist";
            String description = "My todo";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("todolist", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_task_travel, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        newTaskText = requireView().findViewById(R.id.newTaskTravelText);
        newTaskSaveButton = getView().findViewById(R.id.newTaskTravelButton);
        calender = getView().findViewById(R.id.calender);
        time = getView().findViewById(R.id.time);
        date = requireView().findViewById(R.id.date);
        time2 = requireView().findViewById(R.id.time2);

        boolean isUpdate = false;

        final Bundle bundle = getArguments();
        if (bundle != null) {
            isUpdate = true;
            String task = bundle.getString("task");
            String todoDate = bundle.getString("date");
            String todoTime = bundle.getString("time");
            newTaskText.setText(task);
            date.setText(todoDate);
            time2.setText(todoTime);
            newTaskText.setText(task);

            assert task != null;

            if (task.length() > 0) {
                newTaskSaveButton.setTextColor(ContextCompat.getColor(getContext(), R.color.colorprimary));
            }
        }

        db2 = new TodoDatabase(getActivity());
        db2.openDatabase();

        // Add a text changed listener to enable/disable the save button based on text input
        newTaskText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    newTaskSaveButton.setEnabled(false);
                    newTaskSaveButton.setTextColor(Color.GRAY);
                } else {
                    newTaskSaveButton.setEnabled(true);
                    newTaskSaveButton.setTextColor(ContextCompat.getColor(getContext(), R.color.major_2));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        final boolean finalIsUpdate = isUpdate;
        // Add click listener to the save button
        newTaskSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String text = newTaskText.getText().toString();
                String date1 = date.getText().toString();
                String Time = time2.getText().toString();

                if (TextUtils.isEmpty(date1)) {
                    // Date field is empty
                    Toast.makeText(getContext(), "Select Date", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(Time)) {
                    // Time field is empty
                    Toast.makeText(getContext(), "Select Reminder Time", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Set the alarm
                setAlarm();


                if (finalIsUpdate) {
                    db2.updateTaskTravel(bundle.getInt("id"), text);
                    db2.updateDateTravel(bundle.getInt("id"), date1);
                    db2.updateTimeTravel(bundle.getInt("id"), Time);
                } else {
                    TodoModelTravel task = new TodoModelTravel();
                    task.setTask(text);
                    task.setTime(Time);
                    task.setDate(date1);
                    task.setStatus(0);
                    db2.insertTaskTravel(task);
                }
                dismiss();
            }
        });

        // Add click listeners to the calendar and time buttons
        calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker(); // Open date picker dialog
            }
        });

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTimePicker(); // Open time picker dialog
            }
        });
    }

    // Set an alarm using AlarmManager
    private void setAlarm() {
        alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);

        // Create an intent for the AlarmReceiver class
        Intent intent = new Intent(getContext(), AlarmReceiver.class);
        intent.putExtra("TODO_MESSAGE", "You have a todo reminder. Finish the task!");

        // Create a PendingIntent for the AlarmReceiver
        pendingIntent = PendingIntent.getBroadcast(getContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE);

        // Get the AlarmManager and set the alarm to repeat daily
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

        // Show a toast message indicating successful alarm setting
        Toast.makeText(getContext(), "Reminder set Successfully", Toast.LENGTH_SHORT).show();
    }

    // Open the time picker dialog
    private void openTimePicker() {
        timePicker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(12)
                .setMinute(0)
                .setTitleText("Select Reminder Time")
                .build();

        timePicker.show(getParentFragmentManager(), "todolist");

        timePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onClick(View v) {
                if (timePicker.getHour() > 12) {
                    time2.setText(String.format("%02d : %02d PM", timePicker.getHour() - 12, timePicker.getMinute()));
                } else {
                    time2.setText(String.format("%02d : %02d AM", timePicker.getHour(), timePicker.getMinute()));
                }

                cal = Calendar.getInstance();
                cal.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
                cal.set(Calendar.MINUTE, timePicker.getMinute());
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
            }
        });
    }

    // Open the date picker dialog
    private void openDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // Show the picked value in the textView
                date.setText(String.valueOf(year) + "." + "0" + month + "." + String.valueOf(day));
            }
        }, 2023, 01, 20);

        datePickerDialog.show();
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        Activity activity = getActivity();
        if (activity instanceof DialogCloseListener)
            ((DialogCloseListener) activity).handleDialogClose(dialog);
    }

}