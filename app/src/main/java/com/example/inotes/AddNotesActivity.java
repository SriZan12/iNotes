package com.example.inotes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.inotes.Notification.ReceiveNotification;
import com.example.inotes.databinding.ActivityAddNotesBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import DatabaseFiles.NotesEntities;
import ViewModel.iNotes_ViewModel;

public class AddNotesActivity extends AppCompatActivity {

    iNotes_ViewModel viewModel;
    boolean oldNotes = true;
    NotesEntities notes;
    public boolean status;
    Calendar calendar = Calendar.getInstance();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ActivityAddNotesBinding activityAddNotesBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAddNotesBinding = ActivityAddNotesBinding.inflate(getLayoutInflater());
        setContentView(activityAddNotesBinding.getRoot());



        sharedPreferences = getSharedPreferences("save",MODE_PRIVATE);
        activityAddNotesBinding.switch1.setChecked(sharedPreferences.getBoolean( "key",false));


        createNotificationChannel();

        viewModel = ViewModelProviders.of(this).get(iNotes_ViewModel.class);

        try {
            updateNotes();
        } catch (Exception e) {
            e.printStackTrace();
        }



        activityAddNotesBinding.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String Title = activityAddNotesBinding.editTextTitle.getText().toString();
                String subTitle = activityAddNotesBinding.editTextSubtitle.getText().toString();
                String Description = activityAddNotesBinding.editTextDescription.getText().toString();

                if (Title.isEmpty()) {
                    activityAddNotesBinding.editTextTitle.requestFocus();
                    activityAddNotesBinding.editTextTitle.setError("Title is Compulsory");
                    return;
                }

                setNotes(Title, subTitle, Description);


                if (activityAddNotesBinding.switch1.isChecked()) {

                    setNotification(Title,subTitle);
                    status = true;
                }

            }
        });

        activityAddNotesBinding.setDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate();
            }
        });

        activityAddNotesBinding.setTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTime();
            }
        });

        activityAddNotesBinding.switch1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (activityAddNotesBinding.switch1.isChecked()) {
                    // When switch checked
                    editor = getSharedPreferences("save", MODE_PRIVATE).edit();
                    editor.putBoolean("key", true);
                    editor.apply();
                    activityAddNotesBinding.switch1.setChecked(true);
                } else {
                    // When switch unchecked
                    editor = getSharedPreferences("save", MODE_PRIVATE).edit();
                    editor.putBoolean("key", false);
                    editor.apply();
                    activityAddNotesBinding.switch1.setChecked(false);
                }
            }

        });




    }

    private void setNotes(String title, String subTitle, String description) {

        @SuppressLint("SimpleDateFormat") SimpleDateFormat fmt = new SimpleDateFormat("EEE,yyyy-MM-dd HH:mm a");
        Date date = new Date();

        if (oldNotes) {
            notes = new NotesEntities();
            notes.setNotes_Title(title);
            notes.setNotes_SubTitle(subTitle);
            notes.setDescription(description);
            notes.setNotes_Date(fmt.format(date));

            viewModel.InsertNotes(notes);
            Toast.makeText(this, "Added Successfully", Toast.LENGTH_SHORT).show();
        } else {
            notes.setNotes_Title(title);
            notes.setNotes_SubTitle(subTitle);
            notes.setDescription(description);
            notes.setNotes_Date(fmt.format(date));

            viewModel.UpdateNotes(notes);
            Toast.makeText(this, "Updated Successfully", Toast.LENGTH_SHORT).show();
        }


        finish();
    }

    private void updateNotes() {
        Intent intent = getIntent();
        notes = (NotesEntities) intent.getSerializableExtra("updateNotes");
        String Title = notes.getNotes_Title();
        String subTitle = notes.getNotes_SubTitle();
        String Description = notes.getDescription();

        activityAddNotesBinding.editTextTitle.setText(Title);
        activityAddNotesBinding.editTextSubtitle.setText(subTitle);
        activityAddNotesBinding.editTextDescription.setText(Description);


        oldNotes = false;
    }

    private void setDate(){
        int Year = calendar.get(Calendar.YEAR);
        int Month = calendar.get(Calendar.MONTH);
        int date = calendar.get(Calendar.DATE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(AddNotesActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int YEAR, int MONTH, int DATE) {
                calendar.set(Calendar.YEAR,YEAR);
                calendar.set(Calendar.MONTH,MONTH);
                calendar.set(Calendar.DATE,DATE);
            }
        },Year,Month,date);

        datePickerDialog.show();
    }

    private void setTime(){
        int Hour = calendar.get(Calendar.HOUR_OF_DAY);
        int Minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(AddNotesActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hour, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY,hour);
                calendar.set(Calendar.MINUTE,minute);
                calendar.set(Calendar.SECOND,0);
                calendar.set(Calendar.MILLISECOND,0);
            }
        },Hour,Minute,false);

        timePickerDialog.show();
    }

    private void setNotification(String Title,String subTitle){
        Intent intent = new Intent(AddNotesActivity.this,ReceiveNotification.class);
        intent.setAction(Title);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(AddNotesActivity.this,101,intent,0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);

    }


    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "srijanAndroid";
            String description = "To Notify";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("srijanAndroid", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}
