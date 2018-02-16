package com.example.maxvoskr.musicplayer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertEquals;

/**
 * Created by avila on 2/10/2018.
 */

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 *
 *
 * TODO no need for instrumented just spoof location and time using setters in CurrentLocTimeData
 * and check get weight method
 */
@RunWith(AndroidJUnit4.class)
public class SongTest {
    private Context context;
    private CurrentLocationTimeData dataObj;
    private SharedPreferences sharedPreferences;
    private SongHistorySharedPreferenceManager songHistorySharedPreferenceManager;
    private Song song1;
    private Song song2;

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.example.maxvoskr.musicplayer", appContext.getPackageName());
    }

    @Test
    public void testWithBoundService() throws TimeoutException {
        // Create the service Intent.
        Intent serviceIntent =
                new Intent(InstrumentationRegistry.getTargetContext(),
                        LocationService.class);

        // Data can be passed to the service via the Intent.
        serviceIntent.putExtra(LocationService.SEED_KEY, 42L);

        // Bind the service and grab a reference to the binder.
        IBinder binder = mServiceRule.bindService(serviceIntent);

        // Get the reference to the service, or you can call
        // public methods on the binder directly.
        LocalService service =
                ((LocalService.LocalBinder) binder).getService();

        // Verify that the service is working correctly.
        assertThat(service.getRandomInt(), is(any(Integer.class)));
    }

    @Before
    public void before() {
        context = InstrumentationRegistry.getTargetContext();
        dataObj = new CurrentLocationTimeData();

        song1 = new Song("Song Title", "Album Title", "Artist", 0);
        song1.setTimeMS();
    }

}
