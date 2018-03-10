package com.example.maxvoskr.musicplayer;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by avila on 3/8/2018.
 */

public class SettingsActivity extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private final int SONG_MODE = 0;
    private final int ALBUM_MODE = 1;
    private final int FLASHBACK_MODE = 2;

    private CheckBox privacyCheckBox;
    private EditText timeEditText;
    private View songMode;
    private View albumMode;
    private View flashbackMode;
    private Intent songPlayer;
    private Intent songList;
    private Intent songListActivityIntent;

    private DownloadAdapter downloadAdapter;
    private ListView downloadListView;

    private ArrayList<Song> downloadableSongs;

    private MusicArrayList musicList;

    private int year, month, day, hourOfDay, minute;
    long userDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        year = month = day = hourOfDay = minute = 0;
        userDate = 0;

        // Get view ids
        downloadListView = (ListView) findViewById(R.id.downloadList);
        timeEditText = (EditText) findViewById(R.id.timeEditText);
        songMode = (View) findViewById(R.id.navLeft);
        albumMode = (View) findViewById(R.id.navMid);
        flashbackMode = (View) findViewById(R.id.navRight);
        privacyCheckBox = (CheckBox) findViewById(R.id.privacyCheckBox);

        songList = new Intent(this, MainActivity.class);
        songPlayer = new Intent(this, SongPlayerScreen.class);
        songListActivityIntent = new Intent(this, MainActivity.class);

        Intent intent = getIntent();

        downloadAdapter = new DownloadAdapter(this, R.layout.custom_track_download_cell, musicList.musicList);
        downloadListView.setAdapter(downloadAdapter);

        timeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datepicker = new DatePickerFragment();
                datepicker.show(getFragmentManager(), "datepicker");
            }
        });

        downloadListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(), "You clicked on a list item!",
                        Toast.LENGTH_SHORT).show();
            }
        });

        songMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                songList.putExtra("Position", -1);
                startActivity(songList);
            }
        });

        albumMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                songPlayer.putExtra("albumMode", ALBUM_MODE);
                startActivity(songPlayer);
            }
        });

        flashbackMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               songPlayer.putExtra("albumMode", FLASHBACK_MODE);
               startActivity(songPlayer);
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;

        DialogFragment timepicker = new TimePickerFragment();
        timepicker.show(getFragmentManager(), "timepicker");
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        this.hourOfDay = hourOfDay;
        this.minute = minute;
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, this.hourOfDay, this.minute);
        userDate = calendar.getTimeInMillis();

        // Update the UI textbox
        Date date = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat();
        String dateString = format.format(date);
        timeEditText.setText(dateString);

        //TODO: Store the user defined date somewhere
    }

    public void onPrivacyCheckBoxClicked(View view) {
        if(privacyCheckBox.isChecked()) {
            //TODO: Update this setting somewhere else
            Toast.makeText(getApplicationContext(), "Private Mode On!", Toast.LENGTH_SHORT).show();
        } else {
            //TODO: Update this setting somewhere else
            Toast.makeText(getApplicationContext(), "Private Mode Off!", Toast.LENGTH_SHORT).show();
        }
    }
}
