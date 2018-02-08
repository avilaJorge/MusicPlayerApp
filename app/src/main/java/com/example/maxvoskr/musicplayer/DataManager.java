package com.example.maxvoskr.musicplayer;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

/**
 * Created by avila on 2/7/2018.
 */

class DataManager {


    private final static int MIN_MORNING = 5;
    private final static int MIN_AFTERNOON = 11;
    private final static int MIN_EVENING = 17;
    private final static int MORNING = 0;
    private final static int AFTERNOON = 1;
    private final static int EVENING = 2;
    private DataAccess dataAccess;
    private LocationService locationService;
    private LocationListener listener;
    private LocationManager locManager;
    private SimpleTimeZone timezone;
    private Date currentTime;
    private Calendar calendar;
    private static Location lastLocation;


    DataManager(Context contextOfApplication, DataAccess dataAccess, LocationService locationService) {
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                lastLocation = location;
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
        this.locationService = locationService;
        this.dataAccess = dataAccess;

        // get the supported ids for GMT-08:00 (Pacific Standard Time)
        String[] ids = TimeZone.getAvailableIDs(-8 * 60 * 60 * 1000);
        // create a Pacific Standard Time time zone
        SimpleTimeZone timeZone = new SimpleTimeZone(-8 * 60 * 60 * 1000, ids[0]);
        // set up rules for Daylight Saving Time
        timezone.setStartRule(Calendar.APRIL, 1, Calendar.SUNDAY, 2 * 60 * 60 * 1000);
        timezone.setEndRule(Calendar.OCTOBER, -1, Calendar.SUNDAY, 2 * 60 * 60 * 1000);
        calendar = new GregorianCalendar(timezone);
    }

    public void updateData(Song song) {

        currentTime = new Date();
        calendar.setTime(currentTime);
        song.setLocation(locationService.getLocationName());
        song.setDayOfWeek(Calendar.DAY_OF_WEEK);
        song.setTimeMS(currentTime.getTime());
        song.setTimeOfDay(getTimeOfDay(Calendar.HOUR_OF_DAY));
    }

    private int getTimeOfDay(int hourOfDay) {
        if(hourOfDay < MIN_MORNING) {
            return EVENING;
        } else if(hourOfDay < MIN_AFTERNOON) {
            return MORNING;
        } else if(hourOfDay < MIN_EVENING){
            return AFTERNOON;
        } else return EVENING;
    }
}