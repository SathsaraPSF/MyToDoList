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
import com.sathsata.myto_dolist.Model.TodoModelFamily;
import com.sathsata.myto_dolist.R;

import java.util.Calendar;

public class AddNewTaskFamily extends BottomSheetDialogFragment {

    public static final String TAG = "ActionBottomDialog";
    private EditText newTaskText, date, time2;
    private Button newTaskSaveButton;
    private TodoDatabase db2;
    private ImageButton calender, time;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private MaterialTimePicker timePicker;
    private Calendar cal;


    public static AddNewTaskFamily newInstance() {

        return new AddNewTaskFamily();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.DialogStyle);
        createNotificationChanel();
    }

    private void createNotificationChanel() {
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
        View view = inflater.inflate(R.layout.new_task_family, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        newTaskText = requireView().findViewById(R.id.newTaskFamilyText);
        newTaskSaveButton = getView().findViewById(R.id.newTaskFamilyButton);
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

            newTaskText.setText(task); // Set the task text
            date.setText(todoDate); // Set the date text
            time2.setText(todoTime); // Set the time text

            assert task != null;
            if (task.length() > 0)
                newTaskSaveButton.setTextColor(ContextCompat.getColor(getContext(), R.color.colorprimary));
            // Set the text color of the save button to a specific color if the task length is greater than 0
        }

        db2 = new TodoDatabase(getActivity());
        db2.openDatabase();

        // TextWatcher for the new task text field
        newTaskText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    newTaskSaveButton.setEnabled(false);
                    newTaskSaveButton.setTextColor(Color.GRAY);
                    // Disable the save button and set its text color to gray if the task text is empty
                } else {
                    newTaskSaveButton.setEnabled(true);
                    newTaskSaveButton.setTextColor(ContextCompat.getColor(getContext(), R.color.major_2));
                    // Enable the save button and set its text color to a specific color if the task text is not empty
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        final boolean finalIsUpdate = isUpdate;
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

                // Check if it is an update or a new task
                if (finalIsUpdate) {
                    // Update the task in the database
                    db2.updateTaskFamily(bundle.getInt("id"), text);
                    db2.updateDateFamily(bundle.getInt("id"), date1);
                    db2.updateTimeFamily(bundle.getInt("id"), Time);
                } else {
                    // Create a new task and insert it into the database
                    TodoModelFamily task = new TodoModelFamily();
                    task.setTask(text);
                    task.setTime(Time);
                    task.setDate(date1);
                    task.setStatus(0);
                    db2.insertTaskFamily(task);
                }

                // Set the alarm
                setAlarm();
                dismiss(); // Dismiss the dialog
            }
        });

        calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openDatePicker(); // Open date picker dialog

            }

        });

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openTimePicker(); //Open time picker dialog


            }

        });
    }

    private void setAlarm() {


        alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);

        // Create an intent for the AlarmReceiver class
        Intent intent = new Intent(getContext(), AlarmReceiver.class);
        intent.putExtra("TODO_MESSAGE", "You have a todo reminder.Finish the task!");

        // Create a PendingIntent for the AlarmReceiver
        pendingIntent = PendingIntent.getBroadcast(getContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE);

        // Get the AlarmManager and set the alarm
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

        // Set toast to get msg when successfully alarm set
        Toast.makeText(getContext(), "Reminder set Successfully", Toast.LENGTH_SHORT).show();

    }

    private void openTimePicker() {

        // Create a MaterialTimePicker instance
        timePicker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H) // Set the time format to 12-hour clock
                .setHour(12) // Set the default hour value to 12
                .setMinute(0) // Set the default minute value to 0
                .setTitleText("Select Reminder Time") // Set the title text for the time picker
                .build();

        timePicker.show(getParentFragmentManager(), "todolist"); // Show the time picker dialog

        timePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onClick(View v) {
                if (timePicker.getHour() > 12) {
                    // If the selected hour is greater than 12, display the time in PM format
                    time2.setText(String.format("%02d : %02d PM", timePicker.getHour() - 12, timePicker.getMinute()));
                } else {
                    // Otherwise, display the time in AM format
                    time2.setText(String.format("%02d : %02d AM", timePicker.getHour(), timePicker.getMinute()));
                }

                // Create a Calendar instance and set the selected hour and minute values
                cal = Calendar.getInstance();
                cal.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
                cal.set(Calendar.MINUTE, timePicker.getMinute());
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
            }
        });
    }


    private void openDatePicker() {

        // Create a new DatePickerDialog instance
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // Update the date TextView with the selected date
                // Note: The month value is 0-based, so we add 1 to it
                date.setText(String.valueOf(year) + "." + "0" + (month + 1) + "." + String.valueOf(day));
            }
        }, 2023, 01, 20); // Set the initial date to February 20, 2023

        datePickerDialog.show(); // Show the date picker dialog
    }


    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        Activity activity = getActivity();
        if (activity instanceof DialogCloseListener)
            ((DialogCloseListener) activity).handleDialogClose(dialog);
    }
}