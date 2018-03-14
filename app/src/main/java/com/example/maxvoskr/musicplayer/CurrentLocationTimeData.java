package com.example.maxvoskr.musicplayer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by Tim on 2/10/2018.
 */

public class CurrentLocationTimeData {

    private Context context;

    private DateService dateService;
    private LocationService locationService;

    private String tempLocation;
    private long tempTimeMS;
    private int tempDayOfWeek;
    private int tempTimeOfDay;

    private String location;
    private int dayOfWeek;
    private int timeOfDay;
    private long timeMS;

/*
    //Tested Using GPX
    public CurrentLocationTimeData(DateService dateService, LocationService locationService){
        this.dateService = dateService;
        this.locationService = locationService;
*/

    private boolean locBound = false;
    private boolean dateBound = false;


    private ServiceConnection locConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder binder) {
            LocationService.LocationBinder locationBinder = (LocationService.LocationBinder) binder;
            locationService = locationBinder.getLocationService();
            locBound = true;
        }
        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            locBound = false;
        }
    };
    private ServiceConnection dateConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            DateService.DateBinder dateBinder = (DateService.DateBinder) iBinder;
            dateService = dateBinder.getDateService();
            dateBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) { dateBound = false; }
    };

    public CurrentLocationTimeData(Context context){
        this.context = context;
        Intent locIntent = new Intent(context, LocationService.class);
        context.bindService(locIntent, locConnection, Context.BIND_AUTO_CREATE);
        Intent dateIntent = new Intent(context, DateService.class);
        context.bindService(dateIntent, dateConnection, Context.BIND_AUTO_CREATE);
    }

    //Used for tests involving CurrentLocationTimeData
    public CurrentLocationTimeData(String location, int dayOfWeek, int timeOfDay, long timeMS){
        this.location = location;
        this.dayOfWeek = dayOfWeek;
        this.timeOfDay = timeOfDay;
        this.timeMS = timeMS;
    }

    public String getLocation(){
        if(locationService != null)
            location =  locationService.getLocationName();
        else
            location = "";
        return location;
    }

    public int getDayOfWeek(){
        if (dateService != null)
        dayOfWeek = dateService.getCurrentDayOfWeek();
        return dayOfWeek;
    }

    public int getTimeOfDay(){
        if (dateService != null)
        timeOfDay = dateService.getCurrentTimeOfDay();
        return timeOfDay;
    }

    public long getTimeMS() {
        if (dateService != null)
        timeMS = dateService.getCurrentTime();
        return timeMS;
    }

    //Use at start of song
    public void updateTempData(){
        tempLocation = getLocation();
        tempTimeMS = getTimeMS();
        tempDayOfWeek = getDayOfWeek();
        tempTimeOfDay = getTimeOfDay();
        Log.d("log", "Your location: " + tempLocation + "Day of Week " + tempDayOfWeek);
    }

    //Use if song ends
    public void updateSongUsingTemp(Song song) {
        Log.d("STATE", "tempDayOfWeek contains " + Integer.toString(tempDayOfWeek));

        song.setLocation(tempLocation);
        song.setDayOfWeek(tempDayOfWeek);
        song.setTimeMS(tempTimeMS);
        song.setTimeOfDay(tempTimeOfDay);
    }

    public void unBindServices() {
        if(locBound) {
            context.unbindService(locConnection);
            locBound = false;
        }
        if(dateBound) {
            context.unbindService(dateConnection);
            dateBound = false;
        }
    }

    public void setTestCurrentLocationTimeData(String location, int dayOfWeek, int timeOfDay, long timeMS){
        this.location = location;
        this.dayOfWeek = dayOfWeek;
        this.timeOfDay = timeOfDay;
        this.timeMS = timeMS;
    }

}
