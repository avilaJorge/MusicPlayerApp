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
    private ArrayList<Music> musicList;
    private MediaPlayer mediaPlayer;
    private Boolean flag = true;

    public MusicAdapter(Context context, int layout, ArrayList<Music> musicList) {
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
        ImageView playButton, resetButton;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;

        System.out.println("hey");

        if (view == null) {

            System.out.println("1");
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            view = layoutInflater.inflate(layout, null);
            viewHolder.trackName = (TextView) view.findViewById(R.id.trackName);
            viewHolder.artistName = (TextView) view.findViewById(R.id.artistName);
            viewHolder.playButton = (ImageView) view.findViewById(R.id.playImage);
            viewHolder.resetButton = (ImageView) view.findViewById(R.id.resetImage);

            view.setTag(viewHolder);


        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        final Music music = musicList.get(i);
        viewHolder.trackName.setText(music.getName());
        viewHolder.artistName.setText(music.getArtist());

        // play music
        viewHolder.playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag) {
                    mediaPlayer = MediaPlayer.create(context, music.getSong());
                    flag = false;
                }
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    viewHolder.playButton.setImageResource(R.drawable.playimage);
                } else {
                    mediaPlayer.start();
                    viewHolder.playButton.setImageResource(R.drawable.pauseimage);
                }

            }
        });

        // pause music
        viewHolder.resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!flag) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    flag = true;
                }
                viewHolder.playButton.setImageResource(R.drawable.playimage);
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
