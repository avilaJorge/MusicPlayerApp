package com.example.maxvoskr.musicplayer;

import android.content.Context;
import android.os.Environment;
import android.support.test.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;

/**
 * Created by trevor on 3/12/2018.
 *
 *
 *
 * **************************************************************************************************
 * NOTE: YOU MUST DOWNLOAD THE FILE IN THE LINK BLEW AND PUT IT IN ../TESTING FOLDER NAMED "testSong"
 * **************************************************************************************************
 */

public class DowloaderTest {

    Downloader downloader;
    Context context;
    String path;
    Song testSong;

    @Before
    public void before(){
        context = InstrumentationRegistry.getTargetContext();
        downloader = new Downloader(context, context.getResources());
        SongFactory songFactory = new SongFactory(context.getResources());
        File x = context.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        String path = x.getPath();
        path  = path.substring(0, path.lastIndexOf("/"));
        path += "TESTING/testSong";
        songFactory.makeSongFromPath(path);
    }

    @Test
    public void downloadSong() {
        path = downloader.download("https://freemusicarchive.org/music/download/b7a5e80ed9d87daf7a9418f5bfea54d0693878ec");

        while(!downloader.downloadRunning());

        while(!downloader.downloadSuccessful());

        Song downloaded = downloader.getLastDownloadSong();
        assertEquals(downloaded.getName(), testSong.getName());
        assertEquals(downloaded.getAlbum(), testSong.getAlbum());
        assertEquals(downloaded.getArtist(), testSong.getArtist());
    }


}
