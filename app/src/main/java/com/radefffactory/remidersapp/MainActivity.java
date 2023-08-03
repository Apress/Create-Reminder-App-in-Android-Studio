package com.radefffactory.remidersapp;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    TextView tv_time;
    EditText et_message;
    Button b_time, b_schedule;

    Calendar calendar;
    int day, month, year, hour, minutes, seconds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //notification permission
        SharedPreferences preferences = getSharedPreferences("PREFS", 0);
        boolean firstRun = preferences.getBoolean("firstRun", true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && firstRun) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("firstRun", false);
            editor.apply();

            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
            if (!notificationManagerCompat.areNotificationsEnabled()) {
                requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1000);
            }
        }

        //ui
        tv_time = findViewById(R.id.tv_time);
        et_message = findViewById(R.id.et_message);
        b_time = findViewById(R.id.b_time);
        b_schedule = findViewById(R.id.b_schedule);

        initCalendar();

        b_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this, MainActivity.this,
                        year, month, day);
                datePickerDialog.show();
            }
        });

        b_schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = et_message.getText().toString();

                if (!text.equals("")) {
                    AlarmHandler alarmHandler = new AlarmHandler(MainActivity.this);
                    alarmHandler.createAlarm(calendar, text);

                    et_message.setText("");
                    Toast.makeText(MainActivity.this, "Scheduled!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "No message!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initCalendar() {
        calendar = Calendar.getInstance();

        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minutes = calendar.get(Calendar.MINUTE);
        seconds = 0;

        tv_time.setText("Now");
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        this.day = dayOfMonth;
        this.month = month;
        this.year = year;

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, this, hour, minute, DateFormat.is24HourFormat(this));
        timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        this.hour = hourOfDay;
        this.minutes = minute;
        this.seconds = 0;

        Calendar calendar = Calendar.getInstance();
        calendar.set(this.year, this.month, this.day, this.hour, this.minutes, this.seconds);

        if (calendar.getTimeInMillis() <= Calendar.getInstance().getTimeInMillis()) {
            initCalendar();
        } else {
            tv_time.setText(this.year + "/" + this.month + "/" + this.day + " " +
                    this.hour + ":" + this.minutes);
            this.calendar.set(this.year, this.month, this.day, this.hour, this.minutes, this.seconds);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        if (!notificationManagerCompat.areNotificationsEnabled()) {
            Toast.makeText(this, "You should enable the app notifications!", Toast.LENGTH_SHORT).show();
        }
    }
}