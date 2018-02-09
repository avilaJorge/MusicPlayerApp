package com.example.maxvoskr.musicplayer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ListView;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static Context contextOfApplication;

    private Button storeButton;
    private Button retrieveButton;
    private EditText keyText;
    private EditText storeText;
    private TextView message;
    private DataAccess dataAccess;
    private Song exampleSong;
    /*private ArrayList<Song> musicList;
    private MusicAdapter adapter;
    private ListView trackList;
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent i = new Intent(this, SongPlayerScreen.class);
        startActivity(i);

        /*
        trackList = (ListView) findViewById(R.id.trackList);
        musicList = new ArrayList<>();
        musicList.add(new Song("Windows Are the Eyes", "Max", "Forum", R.raw.windowsaretheeyestothehouse));
        musicList.add(new Song("Dead Dove, Do Not Eat", "Max","Forum", R.raw.deaddovedonoteat));
        musicList.add(new Song("Sisters of the Sun", "Max","Forum",  R.raw.sistersofthesun));
        musicList.add(new Song("Sky Full of Ghosts", "Max", "Forum",  R.raw.skyfullofghosts));
        musicList.add(new Song("Dreamatorium", "Max","Forum", R.raw.dreamatorium));
        musicList.add(new Song("I just Want to Tell You", "Max","Forum", R.raw.ijustwanttotellyoubothgoodluck));

        adapter = new MusicAdapter(this, R.layout.custom_track_cell, musicList);
        trackList.setAdapter(adapter);
        */
    }


}
