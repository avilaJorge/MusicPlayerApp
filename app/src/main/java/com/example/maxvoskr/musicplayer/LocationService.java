package com.example.maxvoskr.musicplayer;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationService extends Service {

    private static Location lastLocation = null;
    private static String locName = "";
    private static double longitude = 0;
    private static double latitude = 0;
    private LocationListener listener;
    private LocationManager locManager;
    private Geocoder geocoder;

    public LocationService() {
    }

    private final IBinder binder = new LocationBinder();

    public class LocationBinder extends Binder {
        LocationService getLocationService() {
            return LocationService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        // Code to setup up the listener
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (lastLocation == null) {
                    lastLocation = location;
                }
                lastLocation = location;
                longitude = location.getLongitude();
                latitude = location.getLatitude();
                try {
                    List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
                    if (null != addressList && addressList.size() > 0) {
                        String tempLocName = addressList.get(0).getAddressLine(0);
                        if (!tempLocName.isEmpty()) {
                            locName = tempLocName;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onProviderDisabled(String arg0) {
            }

            @Override
            public void onProviderEnabled(String arg0) {
            }

            @Override
            public void onStatusChanged(String arg0, int arg1, Bundle bundle) {
            }
        };
        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        try {
            locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, listener);
            geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        } catch (SecurityException se) {
            se.printStackTrace();
        }
    }

    /* For testing */
    public void setLocation(String location) {
        locName = location;
    }

    @Override
    public void onDestroy() {
        if(locManager != null && listener != null) {
            locManager.removeUpdates(listener);
            locManager = null;
            listener = null;
        }
    }

    public String getLocationName() { return this.locName; }
}
