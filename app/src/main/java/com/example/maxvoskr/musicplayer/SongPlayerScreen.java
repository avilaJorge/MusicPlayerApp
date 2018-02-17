package com.example.maxvoskr.musicplayer;

import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

@TargetApi(24)
public class SongPlayerScreen extends AppCompatActivity implements MusicPlayerService.Callbacks {

    private final int SONG_MODE = 0;
    private final int ALBUM_MODE = 1;
    private final int FLASHBACK_MODE = 2;

    private int playerMode = SONG_MODE;
    private boolean changeSong = false;

    private boolean playing = true;
    private Song currentSong;

    private ImageView play;
    private ImageView next;
    private ImageView previous;
    private ImageView like;
    private ImageView dislike;
    private View songMode;
    private View albumMode;
    private View flashbackMode;
    private View background;
    private Intent songPlayer;
    private Intent songList;
    private Intent albumList;

    private TextView songTitleTextView;
    private TextView artistTextView;
    private TextView albumTitleTextView;
    private TextView LP_time;
    private TextView LP_dayOfWeek;
    private TextView LP_date;
    private TextView LP_location;

    public MusicPlayerService musicPlayerService;
    boolean musicPlayerBound = false;

    private static SongHistorySharedPreferenceManager sharedPref;

    private ServiceConnection musicPlayerConnection= new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MusicPlayerService.MusicPlayerBinder musicPlayerBinder =
                    (MusicPlayerService.MusicPlayerBinder) iBinder;
            musicPlayerService = musicPlayerBinder.getMusicPlayerService();
            musicPlayerService.registerClient(SongPlayerScreen.this);
            musicPlayerBound = true;

            musicPlayerService.setMode(playerMode);

            if(changeSong) {
                if(playerMode == SONG_MODE)
                {
                    currentSong = MusicArrayList.musicList.get(getIntent().getExtras().getInt("Position"));

                    if(currentSong.getLikeDislike() == -1) {
                        currentSong = null;
                    } else {
                        ArrayList<Song> songs = new ArrayList<Song>();
                        songs.add(currentSong);
                        musicPlayerService.setList(songs);
                        musicPlayerService.playSong();
                    }
                }
                else if(playerMode == ALBUM_MODE) {

                }
                else if (playerMode == FLASHBACK_MODE) {
                    musicPlayerService.stop();
                    musicPlayerService.setList(new ArrayList<Song>());
                    musicPlayerService.playSong();

                    currentSong = musicPlayerService.getCurrentSong();
                }
            }
            else {
                currentSong = musicPlayerService.getCurrentSong(); // get currently played song from media player service

                if(currentSong != null && currentSong.getLikeDislike() == -1) {
                    currentSong = null;
                }
            }

            updateUI(currentSong);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) { musicPlayerBound = false; }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_player_screen);
        sharedPref = new SongHistorySharedPreferenceManager(getApplicationContext());

        // buttons
        play = findViewById(R.id.play);
        next = findViewById(R.id.next);
        previous = findViewById(R.id.previous);
        like = findViewById(R.id.like);
        dislike = findViewById(R.id.dislike);
        songMode = findViewById(R.id.navLeft);
        albumMode = findViewById(R.id.navMid);
        flashbackMode = findViewById(R.id.navRight);
        background = findViewById(R.id.background);

        // text fields
        songTitleTextView = findViewById(R.id.songTitle);
        artistTextView = findViewById(R.id.artistName);
        albumTitleTextView = findViewById(R.id.albumTitle);
        LP_time = findViewById(R.id.time);
        LP_dayOfWeek = findViewById(R.id.dayOfWeek);
        LP_date = findViewById(R.id.date);
        LP_location = findViewById(R.id.location);


        Intent musicPlayerIntent = new Intent(this, MusicPlayerService.class);
        bindService(musicPlayerIntent, musicPlayerConnection, Context.BIND_AUTO_CREATE);
        startService(musicPlayerIntent);
        Toast.makeText(SongPlayerScreen.this, "Service now connected", Toast.LENGTH_SHORT).show();

        // get passed in intent values
        Intent intent = getIntent();
        playing = intent.getBooleanExtra("playingStatus", false);
        playerMode = intent.getIntExtra("playerMode", SONG_MODE);
        changeSong = intent.getBooleanExtra("changeSong", true);

        //create intent references
        songList = new Intent(this, MainActivity.class);
        songPlayer = new Intent(this, SongPlayerScreen.class);



        // set background
        if(playerMode == SONG_MODE)
            background.setBackgroundColor(Color.parseColor("#5a47025c"));
        else if(playerMode == ALBUM_MODE)
            background.setBackgroundColor(Color.parseColor("#6e0208c6"));
        else
            background.setBackgroundColor(Color.parseColor("#6eff6701"));

        if(!playing)
            play.setImageResource(R.drawable.play);
        else
            play.setImageResource(R.drawable.pause);





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

                //sharedPref.writeData(currentSong);
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

                //sharedPref.writeData(currentSong);
                startActivity(songList);
            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (playerMode != FLASHBACK_MODE)
                    musicPlayerService.previous();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                musicPlayerService.skip();
            }
        });

        songMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(playerMode == FLASHBACK_MODE) {
                    musicPlayerService.stop();
                }
                songList.putExtra("playingStatus", playing);
                startActivity(songList);
            }
        });

        albumMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(playerMode == FLASHBACK_MODE) {
                    musicPlayerService.stop();
                }
                startActivity(albumList);
            }
        });

        flashbackMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if(playerMode != FLASHBACK_MODE) {
                songPlayer.putExtra("playerMode", FLASHBACK_MODE);
                startActivity(songPlayer);
            }
            }
        });
    }

    private void updateText() {
        String[] day = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        String[] month = {"January", "February", "March", "April", "May", "June", "July", "August",
                "September", "October", "November", "December"};

        if(currentSong != null) {
            songTitleTextView.setText(currentSong.getName());
            artistTextView.setText(currentSong.getArtist());
            albumTitleTextView.setText(currentSong.getAlbum());
            if (currentSong.getTimeMS() != 0) {
                Date songDate = new Date(currentSong.getTimeMS());

                String minutes = Integer.toString(songDate.getMinutes());
                if (songDate.getMinutes() < 10)
                    minutes = "0" + songDate.getMinutes();

                String AM_PM = "am";
                int hour = songDate.getHours();
                if (hour > 12) {
                    hour -= 12;
                    AM_PM = "pm";
                }

                String location = currentSong.getLocation();

                if (location.length() > 30)
                    location = location.substring(0, 26) + "...";

                LP_time.setText((hour + ":" + minutes + " " + AM_PM));
                LP_dayOfWeek.setText(day[songDate.getDay()]);
                LP_date.setText((month[songDate.getMonth()] + " " +
                        songDate.getDate() + ", " + (1900 + songDate.getYear())));
                LP_location.setText(location);
            } else {
                LP_time.setText(("Song has not been played before"));
                LP_dayOfWeek.setText("");
                LP_date.setText("");
                LP_location.setText("");
            }
        }
        else {
            songTitleTextView.setText("No Song");
            artistTextView.setText("");
            albumTitleTextView.setText("");
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
    public void updateUI(Song nextSong)
    {
        updateText();

        currentSong = nextSong;

        if(currentSong != null) {
            updateText();

            if (currentSong.getLikeDislike() == -1) {
                like.setImageResource(R.drawable.like_black);
                dislike.setImageResource(R.drawable.dislike_red);
            } else if (currentSong.getLikeDislike() == 1) {
                like.setImageResource(R.drawable.like_green);
                dislike.setImageResource(R.drawable.dislike_black);
            } else {
                like.setImageResource(R.drawable.like_black);
                dislike.setImageResource(R.drawable.dislike_black);
            }
        }
        else {
            songTitleTextView.setText("No Song Playing");
            artistTextView.setText("");
            albumTitleTextView.setText("");
            LP_time.setText("");
            LP_dayOfWeek.setText("");
            LP_date.setText("");
            LP_location.setText("");

            like.setImageResource(R.drawable.like_black);
            dislike.setImageResource(R.drawable.dislike_black);
        }
    }
}