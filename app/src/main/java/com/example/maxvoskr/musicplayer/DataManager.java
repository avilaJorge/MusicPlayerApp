package com.example.maxvoskr.musicplayer;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by avila on 2/7/2018.
 */

class DataManager {

    private DataAccess dataAccess;
    private DateService dateService;
    private LocationService locationService;

    DataManager(Context contextOfApplication, DataAccess dataAccess,
                LocationService locationService, DateService dateService) {
        this.locationService = locationService;
        this.dataAccess = dataAccess;
        this.dateService = dateService;
    }

    public void updateData(Song song) {
        song.setLocation(locationService.getLocationName());
        song.setDayOfWeek(dateService.getCurrentDayOfWeek());
        song.setTimeMS(dateService.getCurrentTime());
        song.setTimeOfDay(dateService.getCurrentTimeOfDay());
    }


}