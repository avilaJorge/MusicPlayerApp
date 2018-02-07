package com.example.maxvoskr.musicplayer;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Music> musicList;
    private MusicAdapter adapter;
    private ListView trackList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        trackList = (ListView) findViewById(R.id.trackList);
        musicList = new ArrayList<>();
        musicList.add(new Music("Windows Are the Eyes", "Forum", R.raw.windowsaretheeyestothehouse));
        musicList.add(new Music("Dead Dove, Do Not Eat", "Forum", R.raw.deaddovedonoteat));
        musicList.add(new Music("Sisters of the Sun", "Forum",  R.raw.sistersofthesun));
        musicList.add(new Music("Sky Full of Ghosts", "Forum",  R.raw.skyfullofghosts));
        musicList.add(new Music("Dreamatorium", "Forum", R.raw.dreamatorium));
        musicList.add(new Music("I just Want to Tell You", "Forum", R.raw.ijustwanttotellyoubothgoodluck));

        adapter = new MusicAdapter(this, R.layout.custom_track_cell, musicList);
        trackList.setAdapter(adapter);

    }


}
