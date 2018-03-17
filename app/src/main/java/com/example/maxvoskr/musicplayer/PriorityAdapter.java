package com.example.maxvoskr.musicplayer;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by avila on 3/8/2018.
 */

class PriorityAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Song> musicList;
    int layout;

    public PriorityAdapter(Context context, int layout, ArrayList<Song> musicList) {
        this.context = context;
        this.layout = layout;
        this.musicList = musicList;
        int i = 1;
        for(Song logSong : musicList) {
            Log.d("SONG_PRIORITY_TEST", "Priority: " + i + ": " + logSong.getName());
            i++;
        }
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
        TextView trackName, artistName, priority;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;

        if(view == null) {
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(layout, null);
            viewHolder.trackName = (TextView) view.findViewById(R.id.trackName);
            viewHolder.artistName = (TextView) view.findViewById(R.id.artistName);
            viewHolder.priority = (TextView) view.findViewById(R.id.priorityView);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        final Song music = musicList.get(i);
        viewHolder.trackName.setText(music.getName());
        viewHolder.artistName.setText(music.getArtist());
        viewHolder.priority.setText(Integer.toString(i+1));

        return view;
    }
}
