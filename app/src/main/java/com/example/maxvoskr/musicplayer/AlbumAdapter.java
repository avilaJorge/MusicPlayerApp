package com.example.maxvoskr.musicplayer;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by maxvoskr on 2/14/18.
 */

public class AlbumAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<Album> albumList;
    private MediaPlayer mediaPlayer;
    private Boolean flag = true;

    public AlbumAdapter(Context context, int layout, ArrayList<Album> albumList) {
        this.context = context;
        this.layout = layout;
        this.albumList = albumList;
    }

    @Override
    public int getCount() {
        return albumList.size();
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
        TextView albumName, artistName;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final AlbumAdapter.ViewHolder viewHolder;

        if (view == null) {

            viewHolder = new AlbumAdapter.ViewHolder();
            LayoutInflater layoutInflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            view = layoutInflater.inflate(layout, null);
            //viewHolder.albumName = (TextView) view.findViewById(R.id.albumName);
            //viewHolder.artistName = (TextView) view.findViewById(R.id.artistName);

            view.setTag(viewHolder);


        } else {
            viewHolder = (AlbumAdapter.ViewHolder) view.getTag();
        }

        final Album album = albumList.get(i);
        viewHolder.albumName.setText(album.getAlbumName());
        viewHolder.artistName.setText(album.getArtist());

        return view;
    }

    @Nullable
    @Override
    public CharSequence[] getAutofillOptions() {
        return new CharSequence[0];
    }

}
