package com.example.maxvoskr.musicplayer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.widget.Toast;

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

    public String getLocation(){
        return locationService.getLocationName();
    }

    public int getDayOfWeek(){
        return dateService.getCurrentDayOfWeek();
    }

    public int getTimeOfDay(){
        return dateService.getCurrentTimeOfDay();
    }

    public long getTimeMS() {
        return dateService.getCurrentTime();
    }

    //Use at start of song
    public void updateTempData(){
        tempLocation = getLocation();
        tempTimeMS = getTimeMS();
        tempDayOfWeek = getDayOfWeek();
        tempTimeOfDay = getTimeOfDay();
        Toast.makeText(context, "Your location: " + tempLocation, Toast.LENGTH_SHORT).show();
    }

    //Use if song ends
    public void updateSongUsingTemp(Song song) {
        song.setLocation(tempLocation);
        song.setDayOfWeek(tempDayOfWeek);
        song.setTimeMS(tempTimeMS);
        song.setTimeOfDay(tempTimeOfDay);
        Toast.makeText(context, "Your location: " + tempLocation, Toast.LENGTH_SHORT).show();
    }

}
