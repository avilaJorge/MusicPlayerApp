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
    private final int VIBE_MODE = 2;

    private final int SORT_TITLE = 0;
    private final int SORT_ALBUM = 1;
    private final int SORT_ARTIST = 2;
    private final int SORT_LIKE = 3;

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
    private View vibeMode;
    private View settingsMode;
    private View sortTitle;
    private View sortAlbum;
    private View sortArtist;
    private View sortLike;
    private Intent songPlayer;
    private Intent songList;
    private Intent albumIntent;
    private Intent settingsIntent;

    private ImageView play;
    private ImageView next;
    private ImageView previous;
    private ImageView like;
    private ImageView dislike;

    private ListView background;

    //private ArrayList<Song> localMusicList;
    private MusicAdapter adapter;
    private ListView trackList;

    private ArrayList<Album> albumList;
    private ArrayList<Song> sortedSongList;

    private MusicArrayList musicList;

    private Song currentSong;
    private Album currentAlbum;
    private boolean playing;
    private int albumPosition = -1;
    private int sortMode;

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
        sortMode = intent.getIntExtra("sortMode", SORT_TITLE);

        sharedPref = new SongHistorySharedPreferenceManager(getApplicationContext());

        final Intent anotherActivityIntent  = new Intent(this, SongPlayerScreen.class);
        songList = new Intent(this, MainActivity.class);
        songPlayer = new Intent(this, SongPlayerScreen.class);
        albumIntent = new Intent(this, AlbumListActivity.class);
        settingsIntent = new Intent(this, SettingsActivity.class);

        trackList = (ListView) findViewById(R.id.trackList);
        songMode = findViewById(R.id.navLeft);
        albumMode = findViewById(R.id.navMid);
        vibeMode = findViewById(R.id.navRight);
        settingsMode = (View) findViewById(R.id.settingsMode);
        play = findViewById(R.id.play);
        next = findViewById(R.id.next);
        previous = findViewById(R.id.previous);
        like = findViewById(R.id.like);
        dislike = findViewById(R.id.dislike);
        sortTitle = findViewById(R.id.sortTitle);
        sortAlbum = findViewById(R.id.sortAlbum);
        sortArtist = findViewById(R.id.sortArtist);
        sortLike = findViewById(R.id.sortLike);

        background = findViewById(R.id.trackList);

        if(!playing)
            play.setImageResource(R.drawable.play);
        else
            play.setImageResource(R.drawable.pause);

        if (albumPosition != -1) {
            // Album mode
            currentAlbum = musicList.albumList.get(albumPosition);
            sortedSongList = sort(currentAlbum.getMusicList());
            adapter = new MusicAdapter(this, R.layout.custom_track_cell, sortedSongList);
            background.setBackgroundColor(Color.parseColor("#5a0208c6"));
            trackList.setAdapter(adapter);
        } else {
            // Song mode
            sortedSongList = (ArrayList<Song>) sort(musicList.localMusicList);
            adapter = new MusicAdapter(this, R.layout.custom_track_cell, sortedSongList);
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


                    if (musicList.localMusicList.get(i).getLikeDislike() != -1) {
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
                    songList.putExtra("Position", -1);
                    songList.putExtra("sortMode", sortMode);
                    startActivity(songList);
                }



            }
        });


        dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentSong != null) {
                    if (currentSong.getLikeDislike() >= 0) {
                        like.setImageResource(R.drawable.like_black);
                        dislike.setImageResource(R.drawable.dislike_red);
                        currentSong.setLikeDislike(-1);
                        musicPlayerService.skip();
                    } else {
                        like.setImageResource(R.drawable.like_black);
                        dislike.setImageResource(R.drawable.dislike_black);
                        currentSong.setLikeDislike(0);
                    }

                    sharedPref.writeData(currentSong);
                    songList.putExtra("Position", -1);
                    songList.putExtra("sortMode", sortMode);
                    startActivity(songList);
                }
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


        sortTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sortMode != SORT_TITLE) {
                    songList.putExtra("Position", -1);
                    songList.putExtra("sortMode", SORT_TITLE);
                    startActivity(songList);
                }
            }
        });

        sortAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sortMode != SORT_ALBUM) {
                    songList.putExtra("Position", -1);
                    songList.putExtra("sortMode", SORT_ALBUM);
                    startActivity(songList);
                }
            }
        });

        sortArtist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sortMode != SORT_ARTIST) {
                    songList.putExtra("Position", -1);
                    songList.putExtra("sortMode", SORT_ARTIST);
                    startActivity(songList);
                }
            }
        });

        sortLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sortMode != SORT_LIKE) {
                    songList.putExtra("Position", -1);
                    songList.putExtra("sortMode", SORT_LIKE);
                    startActivity(songList);
                }
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


    private ArrayList<Song> sort(ArrayList<Song> songList) {
        for (int j = 0; j < songList.size(); j++) {
            int switched = 0;
            for (int i = 0; i < songList.size()-1; i++) {
                int compare = 0;
                switch (sortMode) {
                    case SORT_TITLE:
                        compare = songList.get(i).getName().compareTo(songList.get(i+1).getName());
                        break;
                    case SORT_ALBUM:
                        compare = songList.get(i).getAlbum().compareTo(songList.get(i+1).getAlbum());
                        break;
                    case SORT_ARTIST:
                        compare = songList.get(i).getArtist().compareTo(songList.get(i+1).getArtist());
                        break;
                    case SORT_LIKE:
                        compare = songList.get(i+1).getLikeDislike() - songList.get(i).getLikeDislike();
                        break;
                }


                if (compare > 0) {
                    Song temp = songList.get(i);
                    songList.set(i, songList.get(i+1));
                    songList.set(i+1, temp);
                    switched++;
                }
            }

            if(switched == 0)
                break;
        }


        return songList;
    }
}
