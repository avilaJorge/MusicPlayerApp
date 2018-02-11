package com.example.maxvoskr.musicplayer;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * Created by avila on 2/10/2018.
 */

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class SongTest {
    private Context context;
    private SharedPreferences sharedPreferences;
    private DataAccess dataAccess;
    private Song song1;
    private Song song2;

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.example.maxvoskr.musicplayer", appContext.getPackageName());
    }

    @Before
    public void before() {
        context = InstrumentationRegistry.getTargetContext();

        song1 = new Song("Song Title", "Album Title", "Artist", 0);
        song1.setTimeMS();
    }

}
