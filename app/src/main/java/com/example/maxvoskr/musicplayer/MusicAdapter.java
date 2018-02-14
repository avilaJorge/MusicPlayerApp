package com.example.maxvoskr.musicplayer;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by maxvoskr on 2/4/18.
 */

public class MusicAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<Song> musicList;
    private MediaPlayer mediaPlayer;
    private Boolean flag = true;

    public MusicAdapter(Context context, int layout, ArrayList<Song> musicList) {
        this.context = context;
        this.layout = layout;
        this.musicList = musicList;
    }

    @Override
    public int getCount() {
        return musicList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    private class ViewHolder {
        TextView trackName, artistName;
        ImageView likeButton, dislikeButton;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;

        if (view == null) {

            System.out.println("1");
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            view = layoutInflater.inflate(layout, null);
            viewHolder.trackName = (TextView) view.findViewById(R.id.trackName);
            viewHolder.artistName = (TextView) view.findViewById(R.id.artistName);
            viewHolder.likeButton = (ImageView) view.findViewById(R.id.like);
            viewHolder.dislikeButton = (ImageView) view.findViewById(R.id.dislike);

            view.setTag(viewHolder);


        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        final Song music = musicList.get(i);
        viewHolder.trackName.setText(music.getName());
        viewHolder.artistName.setText(music.getArtist());

        if(music.getLikeDislike() == -1)
        {
            viewHolder.likeButton.setImageResource(R.drawable.like_black);
            viewHolder.dislikeButton.setImageResource(R.drawable.dislike_red);
        }
        else if(music.getLikeDislike() == 1)
        {
            viewHolder.likeButton.setImageResource(R.drawable.like_green);
            viewHolder.dislikeButton.setImageResource(R.drawable.dislike_black);
        }
        else
        {
            viewHolder.likeButton.setImageResource(R.drawable.like_black);
            viewHolder.dislikeButton.setImageResource(R.drawable.dislike_black);
        }

        // play music
        viewHolder.likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (music.getLikeDislike() <= 0) {
                    viewHolder.likeButton.setImageResource(R.drawable.like_green);
                    viewHolder.dislikeButton.setImageResource(R.drawable.dislike_black);
                    music.setLikeDislike(1);
                }
                else
                {
                    viewHolder.likeButton.setImageResource(R.drawable.like_black);
                    music.setLikeDislike(0);
                }
            }
        });

        // pause music
        viewHolder.dislikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( music.getLikeDislike() >= 0) {
                    viewHolder.likeButton.setImageResource(R.drawable.like_black);
                    viewHolder.dislikeButton.setImageResource(R.drawable.dislike_red);
                    music.setLikeDislike(-1);
                }
                else
                {
                    viewHolder.dislikeButton.setImageResource(R.drawable.dislike_black);
                    music.setLikeDislike(0);
                }
            }
        });

        return view;
    }

    @Nullable
    @Override
    public CharSequence[] getAutofillOptions() {
        return new CharSequence[0];
    }
}
