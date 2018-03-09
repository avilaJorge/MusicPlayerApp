package com.example.maxvoskr.musicplayer;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by avila on 3/8/2018.
 */

public class SettingsActivity extends AppCompatActivity {

    public static Context contextOfApplication;

    private final int SONG_MODE = 0;
    private final int ALBUM_MODE = 1;
    private final int FLASHBACK_MODE = 2;

    private Button privacyButton;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        // Get view ids
        downloadListView = (ListView) findViewById(R.id.downloadList);
        timeEditText = (EditText) findViewById(R.id.timeEditText);
        songMode = (View) findViewById(R.id.navLeft);
        albumMode = (View) findViewById(R.id.navMid);
        flashbackMode = (View) findViewById(R.id.navRight);

        songList = new Intent(this, MainActivity.class);
        songPlayer = new Intent(this, SongPlayerScreen.class);
        songListActivityIntent = new Intent(this, MainActivity.class);

        Intent intent = getIntent();

        downloadAdapter = new DownloadAdapter(this, R.layout.custom_track_download_cell, musicList.musicList);
        downloadListView.setAdapter(downloadAdapter);

        timeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment fragment = new TimePickerFragment();
                fragment.show(getFragmentManager(), "timepicker");
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
}
