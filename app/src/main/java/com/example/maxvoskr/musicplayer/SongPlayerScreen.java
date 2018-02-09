package com.example.maxvoskr.musicplayer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

public class SongPlayerScreen extends AppCompatActivity {

    private boolean playing = true;
    private int likeDislike = 0;

    private ImageView play;
    private ImageView next;
    private ImageView previous;
    private ImageView like;
    private ImageView dislike;
    private ImageView songMode;
    private ImageView albumMode;
    private ImageView flashbackMode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_player_screen);

        play = findViewById(R.id.play);
        next = findViewById(R.id.next);
        previous = findViewById(R.id.previous);
        like = findViewById(R.id.like);
        dislike = findViewById(R.id.dislike);
        songMode = findViewById(R.id.songsMode);
        albumMode = findViewById(R.id.albumMode);
        flashbackMode = findViewById(R.id.flashbackMode);

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

            }
        });
    }
}

