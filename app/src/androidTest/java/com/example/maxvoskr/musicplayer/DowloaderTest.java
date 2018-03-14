package com.example.maxvoskr.musicplayer;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;

/**
 * Created by trevor on 3/12/2018.
 */

public class DowloaderTest {

    Downloader downloader;
    Context context;
    String path;

    @Before
    public void before(){
        context = InstrumentationRegistry.getTargetContext();
        downloader = new Downloader(context, context.getResources());
    }

    @Test
    public void downloadSong() {
        path = downloader.download("https://freemusicarchive.org/music/download/b14b61dc01ebf1ea4110336edee42b95bfb1a255");
    }


}
