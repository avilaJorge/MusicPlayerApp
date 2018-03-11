package com.example.maxvoskr.musicplayer;

import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.media.MediaMetadataRetriever;
import android.support.v7.app.AppCompatActivity;

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
        Song song = new SongFile(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE), retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM),
                retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST), path);
        return song;
    }

    public Song makeSongFromResID(int resID) {
        AssetFileDescriptor afd = res.openRawResourceFd(resID);
        retriever.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());

        Song song = new SongRes(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE), retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM),
                retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST), resID);

        return song;
    }
}

