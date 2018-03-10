package com.example.maxvoskr.musicplayer;

import android.content.Context;
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

class DownloadAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Song> musicList;
    int layout;

    public DownloadAdapter(Context context, int layout, ArrayList<Song> musicList) {
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
        ImageView downloadButton;
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
            viewHolder.downloadButton = (ImageView) view.findViewById(R.id.download);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        final Song music = musicList.get(i);
        viewHolder.trackName.setText(music.getName());
        viewHolder.artistName.setText(music.getArtist());

        // TODO: Get download info to set icon color and clickability

        return view;
    }
}
