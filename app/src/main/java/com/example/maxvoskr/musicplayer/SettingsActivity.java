package com.example.maxvoskr.musicplayer;

import android.*;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by avila on 3/8/2018.
 */

public class SettingsActivity extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private final int SONG_MODE = 0;
    private final int ALBUM_MODE = 1;
    private final int VIBE_MODE = 2;

    private static DateService dateService;
    private boolean dateBound = false;

    private CheckBox privacyCheckBox;
    private EditText timeEditText;

    private CheckBox manualTimeCheckBox;
    private boolean manualTimeSet;

    private EditText urlEditText;
    private ImageView urlDownload;


    private View songMode;
    private View albumMode;
    private View vibeMode;
    private Intent songPlayer;
    private Intent songList;
    private Intent albumListIntent;

    private DownloadAdapter downloadAdapter;
    private ListView downloadListView;

    private ArrayList<Song> downloadableSongs;

    private MusicArrayList musicList;

    private int year, month, day, hourOfDay, minute;
    long userDate;

    private ServiceConnection dateConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            DateService.DateBinder dateBinder = (DateService.DateBinder) iBinder;
            dateService = dateBinder.getDateService();
            dateBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) { dateBound = false; }
    };

    @Override
    protected void onStart() {
        super.onStart();
        Intent dateIntent = new Intent(this, DateService.class);
        bindService(dateIntent, dateConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        year = month = day = hourOfDay = minute = 0;
        userDate = 0;

        // Get view ids
        downloadListView = (ListView) findViewById(R.id.downloadList);
        timeEditText = (EditText) findViewById(R.id.timeEditText);
        urlDownload = (ImageView) findViewById(R.id.urlDownload);
        urlEditText = (EditText) findViewById(R.id.urlEditText);
        songMode = (View) findViewById(R.id.navLeft);
        albumMode = (View) findViewById(R.id.navMid);
        vibeMode = (View) findViewById(R.id.navRight);
        //privacyCheckBox = (CheckBox) findViewById(R.id.privacyCheckBox);
        manualTimeCheckBox = (CheckBox) findViewById(R.id.manualTimeCheckBox);

        songList = new Intent(this, MainActivity.class);
        songPlayer = new Intent(this, SongPlayerScreen.class);
        albumListIntent = new Intent(this, AlbumListActivity.class);

        Intent intent = getIntent();

        HashMap<String, Song> downloadableSongs = MusicArrayList.allMusicTable;
        for(Song downloaded : MusicArrayList.localMusicList) {
            downloadableSongs.remove(MusicArrayList.getSongHash(downloaded));
        }

        downloadAdapter = new DownloadAdapter(this, R.layout.custom_track_download_cell, new ArrayList<Song>(downloadableSongs.values()));
        downloadListView.setAdapter(downloadAdapter);

        timeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datepicker = new DatePickerFragment();
                datepicker.show(getFragmentManager(), "datepicker");
            }
        });

        urlDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String downloadUrl = urlEditText.getText().toString();
                if (downloadUrl.isEmpty()) return;
                //TODO: check url in firebase, check hashmap, retreive SongFile from there instead
                Downloader downloader = new Downloader(getApplicationContext(),getResources());
                String path = downloader.download(downloadUrl);

                urlEditText.setText("");
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
                startActivity(albumListIntent);
            }
        });

        vibeMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               songPlayer.putExtra("playerMode", VIBE_MODE);
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
        dateService.setCurrentTime(userDate);

        // Update the UI textbox
        Date date = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat();
        String dateString = format.format(date);
        manualTimeSet = true;
        timeEditText.setText(dateString);

        Log.d("MANUAL_TIME","Date set: " + dateString);
        //Toast.makeText(getApplicationContext(), "Date set: " + dateString, Toast.LENGTH_SHORT).show();
    }

    /*
    public void onPrivacyCheckBoxClicked(View view) {
        if(privacyCheckBox.isChecked()) {
            //TODO: Update this setting somewhere else
            Toast.makeText(getApplicationContext(), "Private Mode On!", Toast.LENGTH_SHORT).show();
        } else {
            //TODO: Update this setting somewhere else
            Toast.makeText(getApplicationContext(), "Private Mode Off!", Toast.LENGTH_SHORT).show();
        }
    }
    */

    public void onManualTimeCheckBoxClicked(View view) {
        if (manualTimeCheckBox.isChecked()) {
            LoadingActivity.userDefinedTime = true;
            timeEditText.callOnClick();
            Log.d("MANUAL_TIME", "Manual time enabled.");
            //Toast.makeText(getApplicationContext(), "Enable Manual time!", Toast.LENGTH_SHORT).show();
        } else {
            LoadingActivity.userDefinedTime = false;
            Log.d("MANUAL_TIME", "Manual time disabled.");
            //Toast.makeText(getApplicationContext(), "Disable Manual Time!", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (dateBound) {
            unbindService(dateConnection);
            dateBound = false;
        }
    }
}
