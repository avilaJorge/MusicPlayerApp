package com.example.maxvoskr.musicplayer;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ServiceTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import java.util.concurrent.TimeoutException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsInstanceOf.any;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * Created by avila on 3/15/2018.
 *
 * VALID TEST, KEEP
 */

public class UserDefinedDateTest {

    private Context context;
    private DateService dateService;
    private CurrentLocationTimeData currentLocationTimeData;

    @Rule
    public final ServiceTestRule mServiceRule = new ServiceTestRule();

    @Before
    public void before() throws TimeoutException {
        context = InstrumentationRegistry.getTargetContext();
        currentLocationTimeData = new CurrentLocationTimeData(context);
        LoadingActivity.userDefinedTime = true;
        Intent dateServiceIntent =
                new Intent(InstrumentationRegistry.getTargetContext(), DateService.class);
        IBinder binder = mServiceRule.bindService(dateServiceIntent);
        dateService = ((DateService.DateBinder) binder).getDateService();
        assertThat(dateService.getCurrentDayOfWeek(), is(any(Integer.class)));
    }

    @Test
    public void userDefinedDateTest() {
        long time = 10000000;
        dateService.setCurrentTime(time);
        currentLocationTimeData.updateTempData();
        assertEquals(time, currentLocationTimeData.getTimeMS());
    }

    @After
    public void after() {
        LoadingActivity.userDefinedTime = false;
    }
}
