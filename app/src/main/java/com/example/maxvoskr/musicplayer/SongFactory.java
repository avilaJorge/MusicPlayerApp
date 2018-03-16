package com.example.maxvoskr.musicplayer;

import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.media.MediaMetadataRetriever;

/**
 * Created by evor on 3/8/2018.
 */

public class SongFactory {

    private MediaMetadataRetriever retriever;
    private Resources res;

    SongFactory(Resources res) {
        retriever = new MediaMetadataRetriever();
        this.res = res;
    }

    public Song makeSongFromPath(String path) {
        retriever.setDataSource(path);
        String[] metaData = getMetaData();
        Song song = new SongFile(metaData[0], metaData[1], metaData[2], path);
        return song;
    }

    public Song makeSongFromResID(int resID) {
        AssetFileDescriptor afd = res.openRawResourceFd(resID);
        retriever.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());

        String[] metaData = getMetaData();
        Song song = new SongRes(metaData[0], metaData[1], metaData[2], resID);

        return song;
    }

    private String[] getMetaData() {
        String[] metaData = {retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE), retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM),
                retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST)};
        if(metaData[0] == null)
            metaData[0] = "unknown title";
        if(metaData[1] == null)
            metaData[1] = "unknown album";
        if(metaData[2] == null)
            metaData[2] = "unknown artist";

        return  metaData;

    }
}

