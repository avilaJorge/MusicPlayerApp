package com.example.maxvoskr.musicplayer;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
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

import java.util.Date;
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

    private ImageView play;
    private ImageView next;
    private ImageView previous;
    private ImageView like;
    private ImageView dislike;

    //private ArrayList<Song> musicList;
    private MusicAdapter adapter;
    private ListView trackList;

    private ArrayList<Album> albumList;

    private MusicArrayList musicList;

    private  Song currentSong;

    private ServiceConnection musicPlayerConnection= new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MusicPlayerService.MusicPlayerBinder musicPlayerBinder =
                    (MusicPlayerService.MusicPlayerBinder) iBinder;
            musicPlayerService = musicPlayerBinder.getMusicPlayerService();
            musicPlayerService.registerClient(MainActivity.this);
            musicPlayerBound = true;
            Toast.makeText(MainActivity.this, "Service almost connected", Toast.LENGTH_SHORT).show();

            currentSong = musicPlayerService.getCurrentSong();
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
        Toast.makeText(MainActivity.this, "Service now connected", Toast.LENGTH_SHORT).show();
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        final Intent anotherActivityIntent  = new Intent(this, SongPlayerScreen.class);
        songList = new Intent(this, MainActivity.class);
        songPlayer = new Intent(this, SongPlayerScreen.class);

        trackList = (ListView) findViewById(R.id.trackList);
        songMode = findViewById(R.id.navLeft);
        albumMode = findViewById(R.id.navMid);
        flashbackMode = findViewById(R.id.navRight);
        play = findViewById(R.id.play);
        next = findViewById(R.id.next);
        previous = findViewById(R.id.previous);
        like = findViewById(R.id.like);
        dislike = findViewById(R.id.dislike);

        Date currentDate = new Date();
        long time = currentDate.getTime();

/*        musicList = new MusicArrayList();

        musicList.musicList.add(new Song("Giesel", time, 3, 1, 0, "Windows Are the Eyes", "Trevor", "Forum", R.raw.windowsaretheeyestothehouse));
        musicList.musicList.add(new Song("Dead Dove, Do Not Eat", "Max","Forum", R.raw.deaddovedonoteat));
        musicList.musicList.add(new Song("Sisters of the Sun", "Adi","Forum",  R.raw.sistersofthesun));
        musicList.musicList.add(new Song("Sky Full of Ghosts", "Matt", "Forum",  R.raw.skyfullofghosts));
        musicList.musicList.add(new Song("Dreamatorium", "Tim","Forum", R.raw.dreamatorium));
        musicList.musicList.add(new Song("I just Want to Tell You", "Jorge","Forum", R.raw.ijustwanttotellyoubothgoodluck));
*/
        adapter = new MusicAdapter(this, R.layout.custom_track_cell, musicList.musicList);
        trackList.setAdapter(adapter);


        trackList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if(musicList.musicList.get(i).getLikeDislike() != -1) {
                    anotherActivityIntent.putExtra("Position", i);
                    anotherActivityIntent.putExtra("changeSong", true);
                    songPlayer.putExtra("playerMode", ALBUM_MODE);
                    startActivity(anotherActivityIntent);
                }

            }
        });

        songMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                songPlayer.putExtra("changeSong", false);
                songPlayer.putExtra("playerMode", SONG_MODE); // should be song mode
                startActivity(songPlayer);
            }
        });

        albumMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
                Toast.makeText(SongPlayerScreen.this, "Should play!", Toast.LENGTH_SHORT).show();
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

// TODO: Will implement these when we have multiple activities binding to the same service.
//    @Override
//    protected void onPause() {
//        super.onPause();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//    }

    //@Override
    public void updateUI(Song song) {
        Toast.makeText(MainActivity.this, "This is not a good sign....", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(musicPlayerBound) {
            unbindService(musicPlayerConnection);
            musicPlayerBound = false;
        }
    }
}
