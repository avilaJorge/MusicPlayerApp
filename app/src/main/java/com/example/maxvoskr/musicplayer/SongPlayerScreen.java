package com.example.maxvoskr.musicplayer;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class SongPlayerScreen extends AppCompatActivity {

    private final int SONG_MODE = 0;
    private final int ALBUM_MODE = 1;
    private final int FLASHBACK_MODE = 2;

    private int playerMode = SONG_MODE;

    private boolean playing = true;
    private int likeDislike = 0;
    private int pos = 0;

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

    private TextView songTitleTextView;
    private TextView albumTitleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_player_screen);

        play = findViewById(R.id.play);
        next = findViewById(R.id.next);
        previous = findViewById(R.id.previous);
        like = findViewById(R.id.like);
        dislike = findViewById(R.id.dislike);
        songMode = findViewById(R.id.navLeft);
        albumMode = findViewById(R.id.navMid);
        flashbackMode = findViewById(R.id.navRight);
        background = findViewById(R.id.background);


        songList = new Intent(this, MainActivity.class);
        songPlayer = new Intent(this, SongPlayerScreen.class);
        playerMode = getIntent().getIntExtra("playerMode", SONG_MODE);
        if(playerMode == SONG_MODE)
            background.setBackgroundColor(Color.parseColor("#6e47025c"));
        else if(playerMode == ALBUM_MODE)
            background.setBackgroundColor(Color.parseColor("#6e0208c6"));
        else
            background.setBackgroundColor(Color.parseColor("#6eff6701"));


        final Intent intent = getIntent();
        pos = intent.getExtras().getInt("Position");

        songTitleTextView = findViewById(R.id.songTitle);
        albumTitleTextView = findViewById(R.id.albumTitle);

        songTitleTextView.setText(MusicArrayList.musicList.get(pos).getName());
        albumTitleTextView.setText(MusicArrayList.musicList.get(pos).getAlbum());

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(playing)
                    play.setImageResource(R.drawable.play);
                else
                    play.setImageResource(R.drawable.pause);

                playing = !playing;
            }
        });


        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(likeDislike <= 0) {
                    like.setImageResource(R.drawable.like_green);
                    dislike.setImageResource(R.drawable.dislike_black);
                    likeDislike = 1;
                }
                else
                {
                    like.setImageResource(R.drawable.like_black);
                    dislike.setImageResource(R.drawable.dislike_black);
                    likeDislike = 0;
                }
            }
        });


        dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(likeDislike >= 0) {
                    like.setImageResource(R.drawable.like_black);
                    dislike.setImageResource(R.drawable.dislike_red);
                    likeDislike = -1;
                }
                else
                {
                    like.setImageResource(R.drawable.like_black);
                    dislike.setImageResource(R.drawable.dislike_black);
                    likeDislike = 0;
                }
            }
        });


        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        songMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(songList);
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
            if(playerMode != FLASHBACK_MODE)
            {
                songPlayer.putExtra("playerMode", FLASHBACK_MODE);
                startActivity(songPlayer);
            }
            }
        });
    }



}


