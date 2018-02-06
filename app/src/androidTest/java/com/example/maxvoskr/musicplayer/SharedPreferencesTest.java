package com.example.maxvoskr.musicplayer;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.test.InstrumentationRegistry;
import android.test.suitebuilder.annotation.SmallTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests that modify the shared preferences.
 */
@SmallTest
public class SharedPreferencesTest {
    private static final String PREFS_NAME = "SongData";
    private static final String KEY_PREF = "KEY_PREF";
    private SharedPreferences sharedPreferences;
    private DataAccess dataAccess;
    private Song song1;
    private Song song2;
    private Context context;

    @Before
    public void before() {
        context = InstrumentationRegistry.getTargetContext();

        song1 = new Song("Song Title","Album Title","Artist",0);
        song1.setTimeMS(0);
        song1.setDayOfWeek(0);
        song1.setTimeOfDay(0);
        song1.setLikeDislike(0);
        song1.setLocation("default");

        song2 = new Song("Song Title","Album Title","Artist",0);
        song2.setTimeMS(0);
        song2.setDayOfWeek(0);
        song2.setTimeOfDay(0);
        song2.setLikeDislike(0);
        song2.setLocation("");

        dataAccess = new DataAccess(context);
    }

    @Test
    public void PlaceOne() throws Exception {
        dataAccess.writeData(song1);
        dataAccess.updateData(song2);

        assertEquals(song1.getLocation(), song2.getLocation());
    }

    @After
    public void after() {
        sharedPreferences.edit().putString(KEY_PREF, null).apply();
    }
}