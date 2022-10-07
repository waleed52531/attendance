package com.example.attendance.Utilis;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class GpsTracker extends Service implements LocationListener {
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1000;
    private static final long MIN_TIME_BW_UPDATES = 6000000;
    boolean canGetLocation = false;
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    double latitude;
    Location location;
    protected LocationManager locationManager;
    double longitude;
    public Context mContext;

    public GpsTracker(Context context) {
        this.mContext = context;
        getLocation();
    }

    public Location getLocation() {
        try {
            LocationManager locationManager2 = (LocationManager) this.mContext.getSystemService("location");
            this.locationManager = locationManager2;
            boolean isProviderEnabled = locationManager2.isProviderEnabled("network");
            this.isNetworkEnabled = isProviderEnabled;
            boolean z = this.isGPSEnabled;
            if (!z && !isProviderEnabled) {
                return this.location;
            }
            this.canGetLocation = true;
            if (isProviderEnabled) {
                this.locationManager.requestLocationUpdates("network", MIN_TIME_BW_UPDATES, 1000.0f, this);
                Log.d("Network", "Network");
                LocationManager locationManager3 = this.locationManager;
                if (locationManager3 != null) {
                    Location lastKnownLocation = locationManager3.getLastKnownLocation("network");
                    this.location = lastKnownLocation;
                    if (lastKnownLocation != null) {
                        this.latitude = lastKnownLocation.getLatitude();
                        this.longitude = this.location.getLongitude();
                    }
                }
            } else if (z && this.location == null) {
                this.locationManager.requestLocationUpdates("gps", MIN_TIME_BW_UPDATES, 1000.0f, this);
                Log.d("GPS Enabled", "GPS Enabled");
                LocationManager locationManager4 = this.locationManager;
                if (locationManager4 != null) {
                    Location lastKnownLocation2 = locationManager4.getLastKnownLocation("gps");
                    this.location = lastKnownLocation2;
                    if (lastKnownLocation2 != null) {
                        this.latitude = lastKnownLocation2.getLatitude();
                        this.longitude = this.location.getLongitude();
                    }
                }
            }
            return this.location;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void stopUsingGPS() {
        LocationManager locationManager2 = this.locationManager;
        if (locationManager2 != null) {
            locationManager2.removeUpdates(this);
        }
    }

    public double getLatitude() {
        Location location2 = this.location;
        if (location2 != null) {
            this.latitude = location2.getLatitude();
        }
        return this.latitude;
    }

    public double getLongitude() {
        Location location2 = this.location;
        if (location2 != null) {
            this.longitude = location2.getLongitude();
        }
        return this.longitude;
    }

    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this.mContext);
        alertDialog.setTitle("GPS is settings");
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                GpsTracker.this.mContext.startActivity(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"));
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                System.exit(1);
            }
        });
        alertDialog.show();
    }

    public void onLocationChanged(Location location2) {
    }

    public void onProviderDisabled(String provider) {
    }

    public void onProviderEnabled(String provider) {
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    public IBinder onBind(Intent arg0) {
        return null;
    }
}
