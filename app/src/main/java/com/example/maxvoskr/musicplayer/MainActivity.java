package com.example.maxvoskr.musicplayer;

import android.Manifest;
import android.app.ListActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;
import android.widget.ListView;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static Context contextOfApplication;


    public static LocationService locationService;
    public static DateService dateService;
    private boolean locBound = false;
    private boolean dateBound = false;
    private Button storeButton;
    private Button retrieveButton;
    private EditText keyText;
    private EditText storeText;
    private TextView message;
    private DataAccess dataAccess;
    private Song exampleSong;

    //private ArrayList<Song> musicList;
    private MusicAdapter adapter;
    private ListView trackList;

    private MusicArrayList musicList;


    private ServiceConnection locConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder binder) {
            LocationService.LocationBinder locationBinder = (LocationService.LocationBinder) binder;
            locationService = locationBinder.getLocationService();
            locBound = true;
        }
        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            locBound = false;
        }
    };
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
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
            Log.d("test1", "in");
            return;
        } else {
            Log.d("test2", "out");
            Intent locIntent = new Intent(this, LocationService.class);
            bindService(locIntent, locConnection, Context.BIND_AUTO_CREATE);
        }
        Intent dateIntent = new Intent(this, DateService.class);
        bindService(dateIntent, dateConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Intent anotherActivityIntent  = new Intent(this, SongPlayerScreen.class);

        trackList = (ListView) findViewById(R.id.trackList);

        musicList = new MusicArrayList();

        musicList.musicList.add(new Song("Windows Are the Eyes", "Trevor", "Forum", R.raw.windowsaretheeyestothehouse));
        musicList.musicList.add(new Song("Dead Dove, Do Not Eat", "Max","Forum", R.raw.deaddovedonoteat));
        musicList.musicList.add(new Song("Sisters of the Sun", "Adi","Forum",  R.raw.sistersofthesun));
        musicList.musicList.add(new Song("Sky Full of Ghosts", "Matt", "Forum",  R.raw.skyfullofghosts));
        musicList.musicList.add(new Song("Dreamatorium", "Tim","Forum", R.raw.dreamatorium));
        musicList.musicList.add(new Song("I just Want to Tell You", "Jorge","Forum", R.raw.ijustwanttotellyoubothgoodluck));

        adapter = new MusicAdapter(this, R.layout.custom_track_cell, musicList.musicList);
        trackList.setAdapter(adapter);

        trackList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                anotherActivityIntent.putExtra("Position", i);

                startActivity(anotherActivityIntent);

            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(locBound) {
            unbindService(locConnection);
            locBound = false;
        }
        if(dateBound) {
            unbindService(dateConnection);
            dateBound = false;
        }
    }
}
