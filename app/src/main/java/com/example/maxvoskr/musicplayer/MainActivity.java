package com.example.maxvoskr.musicplayer;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Color;
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

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MusicPlayerService.Callbacks{

    public static Context contextOfApplication;

    private final int SONG_MODE = 0;
    private final int ALBUM_MODE = 1;
    private final int FLASHBACK_MODE = 2;

    public MusicPlayerService musicPlayerService;
    private boolean musicPlayerBound = false;
    private Button storeButton;
    private Button retrieveButton;
    private EditText keyText;
    private EditText storeText;
    private TextView message;
    private SongHistorySharedPreferenceManager songHistorySharedPreferenceManager;
    private Song exampleSong;
    private View songMode;
    private View albumMode;
    private View flashbackMode;
    private Intent songPlayer;
    private Intent songList;
    private Intent albumIntent;

    private ImageView play;
    private ImageView next;
    private ImageView previous;
    private ImageView like;
    private ImageView dislike;

    private ListView background;

    //private ArrayList<Song> musicList;
    private MusicAdapter adapter;
    private ListView trackList;

    private ArrayList<Album> albumList;

    private MusicArrayList musicList;

    private Song currentSong;
    private Album currentAlbum;
    private boolean playing;
    private int albumPosition = -1;

    private SongHistorySharedPreferenceManager sharedPref;

    private ServiceConnection musicPlayerConnection= new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MusicPlayerService.MusicPlayerBinder musicPlayerBinder =
                    (MusicPlayerService.MusicPlayerBinder) iBinder;
            musicPlayerService = musicPlayerBinder.getMusicPlayerService();
            musicPlayerService.registerClient(MainActivity.this);
            musicPlayerBound = true;
            Log.d("log", "Service almost connected");

            currentSong = musicPlayerService.getCurrentSong();

            updateUI(currentSong);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) { musicPlayerBound = false; }
    };

    @Override
    protected void onStart() {
        super.onStart();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        }
        Intent musicPlayerIntent = new Intent(this, MusicPlayerService.class);
        bindService(musicPlayerIntent, musicPlayerConnection, Context.BIND_AUTO_CREATE);
        startService(musicPlayerIntent);
        Log.d("log", "Service Now connected");

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        // get passed in intent values
        Intent intent = getIntent();
        playing = intent.getBooleanExtra("playingStatus", false);
        albumPosition = intent.getIntExtra("Position", -1);

        sharedPref = new SongHistorySharedPreferenceManager(getApplicationContext());

        final Intent anotherActivityIntent  = new Intent(this, SongPlayerScreen.class);
        songList = new Intent(this, MainActivity.class);
        songPlayer = new Intent(this, SongPlayerScreen.class);
        albumIntent = new Intent(this, AlbumListActivity.class);

        trackList = (ListView) findViewById(R.id.trackList);
        songMode = findViewById(R.id.navLeft);
        albumMode = findViewById(R.id.navMid);
        flashbackMode = findViewById(R.id.navRight);
        play = findViewById(R.id.play);
        next = findViewById(R.id.next);
        previous = findViewById(R.id.previous);
        like = findViewById(R.id.like);
        dislike = findViewById(R.id.dislike);

        background = findViewById(R.id.trackList);

        if(!playing)
            play.setImageResource(R.drawable.play);
        else
            play.setImageResource(R.drawable.pause);

        if (albumPosition != -1) {
            currentAlbum = musicList.albumList.get(albumPosition);
            adapter = new MusicAdapter(this, R.layout.custom_track_cell, currentAlbum.getMusicList());
            background.setBackgroundColor(Color.parseColor("#5a0208c6"));
            trackList.setAdapter(adapter);
        } else {
            adapter = new MusicAdapter(this, R.layout.custom_track_cell, musicList.musicList);
            background.setBackgroundColor(Color.parseColor("#5a47025c"));
            trackList.setAdapter(adapter);
        }

        trackList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (albumPosition != -1) {
                    if(currentAlbum.getMusicList().get(i).getLikeDislike() != -1) {
                        anotherActivityIntent.putExtra("album", albumPosition);
                        anotherActivityIntent.putExtra("track", i);
                        anotherActivityIntent.putExtra("changeSong", true);
                        anotherActivityIntent.putExtra("playerMode", ALBUM_MODE);
                        anotherActivityIntent.putExtra("playingStatus", true);
                        startActivity(anotherActivityIntent);
                    }
                } else {
                    if (musicList.musicList.get(i).getLikeDislike() != -1) {
                        anotherActivityIntent.putExtra("Position", i);
                        anotherActivityIntent.putExtra("changeSong", true);
                        anotherActivityIntent.putExtra("playerMode", SONG_MODE);
                        anotherActivityIntent.putExtra("playingStatus", true);
                        startActivity(anotherActivityIntent);
                    }
                }
            }
        });

        songMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                songPlayer.putExtra("changeSong", false);
                songPlayer.putExtra("playerMode", SONG_MODE); // should be song mode
                songPlayer.putExtra("playingStatus", playing);
                startActivity(songPlayer);
            }
        });

        albumMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(albumIntent);
            }
        });

        flashbackMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                songPlayer.putExtra("playerMode", FLASHBACK_MODE);
                startActivity(songPlayer);
            }
        });




        play.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                if(playing)
                    play.setImageResource(R.drawable.play);
                else
                    play.setImageResource(R.drawable.pause);

                if(!playing) {
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
                if(currentSong.getLikeDislike() <= 0) {
                    like.setImageResource(R.drawable.like_green);
                    dislike.setImageResource(R.drawable.dislike_black);
                    currentSong.setLikeDislike(1);
                }
                else
                {
                    like.setImageResource(R.drawable.like_black);
                    dislike.setImageResource(R.drawable.dislike_black);
                    currentSong.setLikeDislike(0);
                }

                sharedPref.writeData(currentSong);
                songList.putExtra("Position", -1);
                startActivity(songList);
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
                songList.putExtra("Position", -1);
                startActivity(songList);
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

    }

    public void updateUI(Song song) {
        currentSong = song;

        if (song != null && song.getLikeDislike() == -1) {
            like.setImageResource(R.drawable.like_black);
            dislike.setImageResource(R.drawable.dislike_red);
        } else if (song != null && song.getLikeDislike() == 1) {
            like.setImageResource(R.drawable.like_green);
            dislike.setImageResource(R.drawable.dislike_black);
        } else {
            like.setImageResource(R.drawable.like_black);
            dislike.setImageResource(R.drawable.dislike_black);
        }

    }

    @Override
    protected void onDestroy() {
        if(musicPlayerBound) {
            unbindService(musicPlayerConnection);
            musicPlayerBound = false;
        }
        super.onDestroy();
    }

    @Override
    public void onStop(){
        super.onStop();
        if(isChangingConfigurations()){
            ;
        }
    }
}
