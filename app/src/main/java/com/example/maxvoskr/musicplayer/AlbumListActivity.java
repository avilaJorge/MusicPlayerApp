package com.example.maxvoskr.musicplayer;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by maxvoskr on 2/14/18.
 */

public class AlbumListActivity extends AppCompatActivity {

    public static Context contextOfApplication;

    private final int SONG_MODE = 0;
    private final int ALBUM_MODE = 1;
    private final int VIBE_MODE = 2;

    public static LocationService locationService;
    public static DateService dateService;
    public static MusicPlayerService musicPlayerService;
    private boolean locBound = false;
    private boolean dateBound = false;
    private boolean musicPlayerBound = false;
    private Button storeButton;
    private Button retrieveButton;
    private EditText keyText;
    private EditText storeText;
    private TextView message;
    private SongHistorySharedPreferenceManager dataAccess;
    private Song exampleSong;
    private View songMode;
    private View albumMode;
    private View vibeMode;
    private View settingsMode;
    private Intent songPlayer;
    private Intent songList;
    private Intent songListActivityIntent;
    private Intent settingsIntent;

    private ImageView play;
    private ImageView next;
    private ImageView previous;
    private ImageView like;
    private ImageView dislike;

    //private ArrayList<Song> localMusicList;
    private AlbumAdapter adapter;
    private ListView albumListView;

    private ArrayList<Album> albumList;

    private MusicArrayList musicList;

    private boolean playing;
    private Song currentSong;

    private SongHistorySharedPreferenceManager sharedPref;


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

    private ServiceConnection musicPlayerConnection= new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MusicPlayerService.MusicPlayerBinder musicPlayerBinder =
                    (MusicPlayerService.MusicPlayerBinder) iBinder;
            musicPlayerService = musicPlayerBinder.getMusicPlayerService();
            musicPlayerBound = true;

            currentSong = musicPlayerService.getCurrentSong();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) { musicPlayerBound = false; }
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
        Intent musicPlayerIntent = new Intent(this, MusicPlayerService.class);
        bindService(musicPlayerIntent, musicPlayerConnection, Context.BIND_AUTO_CREATE);
        startService(musicPlayerIntent);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.album_list);
        songList = new Intent(this, MainActivity.class);
        songPlayer = new Intent(this, SongPlayerScreen.class);
        settingsIntent = new Intent(this, SettingsActivity.class);

        songListActivityIntent  = new Intent(this, MainActivity.class);

        Intent intent = getIntent();
        playing = intent.getBooleanExtra("playingStatus", false);

        sharedPref = new SongHistorySharedPreferenceManager(getApplicationContext());

        albumListView = (ListView) findViewById(R.id.albumListDisplay);
        songMode = findViewById(R.id.navLeft);
        albumMode = findViewById(R.id.navMid);
        vibeMode = findViewById(R.id.navRight);
        settingsMode = findViewById(R.id.settingsMode);
        play = findViewById(R.id.play);
        next = findViewById(R.id.next);
        previous = findViewById(R.id.previous);
        like = findViewById(R.id.like);
        dislike = findViewById(R.id.dislike);

        adapter = new AlbumAdapter(this, R.layout.custom_album_cell, musicList.albumList);
        albumListView.setAdapter(adapter);

        Log.d("ALBUM_LIST_ACT", "Successfully switched into Album mode.");


        albumListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if(musicList.localMusicList.get(i).getLikeDislike() != -1) {
                    songListActivityIntent.putExtra("Position", i);
                    startActivity(songListActivityIntent);
                }

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

        vibeMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                songPlayer.putExtra("playerMode", VIBE_MODE);
                startActivity(songPlayer);
            }
        });

        settingsMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(settingsIntent);
            }
        });



        play.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                if(!playing)
                    play.setImageResource(R.drawable.play);
                else
                    play.setImageResource(R.drawable.pause);

                if(playing) {
                    musicPlayerService.playSong();
                } else {
                    musicPlayerService.pause();
                }

                playing = !playing;
            }
        });


        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentSong != null) {
                    if (currentSong.getLikeDislike() <= 0) {
                        like.setImageResource(R.drawable.like_green);
                        dislike.setImageResource(R.drawable.dislike_black);
                        currentSong.setLikeDislike(1);
                    } else {
                        like.setImageResource(R.drawable.like_black);
                        dislike.setImageResource(R.drawable.dislike_black);
                        currentSong.setLikeDislike(0);
                    }
                    sharedPref.writeData(currentSong);
                }

            }
        });


        dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentSong.getLikeDislike() >= 0) {
                    like.setImageResource(R.drawable.like_black);
                    dislike.setImageResource(R.drawable.dislike_red);
                    currentSong.setLikeDislike(-1);
                    musicPlayerService.skip();
                }
                else
                {
                    like.setImageResource(R.drawable.like_black);
                    dislike.setImageResource(R.drawable.dislike_black);
                    currentSong.setLikeDislike(0);
                }

                sharedPref.writeData(currentSong);
            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                musicPlayerService.previous();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                musicPlayerService.skip();
            }
        });

        SharedPreferences.Editor editor = LoadingActivity.lastActivitySharedPref.edit();
        editor.putString("Activity_Name", LoadingActivity.ALBUM_MODE_STRING);
        editor.apply();
        Log.d("UI MODE", "For Testing: You are in Album Mode");
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
        if(musicPlayerBound) {
            unbindService(musicPlayerConnection);
            musicPlayerBound = false;
        }
        if(isChangingConfigurations()){
                ;
        }
    }


}

