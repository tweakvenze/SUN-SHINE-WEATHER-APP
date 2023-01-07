package com.example.mysunshineweatherapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.lang.reflect.Method;


public class LocationRequester
{

    private Context mcontext;
    FusedLocationProviderClient mfusedLocationProviderClient;
    private double latitude;
    private double longitude;
    private iLocation listener;
   // private Method locationSuccessCallback;

    public LocationRequester(Context context, iLocation listener)
    {
        this.mcontext = context;
        this.listener = listener;
    }

    @SuppressLint("MissingPermission")
    public void getLocation()
    {
        mfusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(mcontext);
        if(checkPermissions())
        {
            mfusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task)
                {
                    Log.i("Location", "Inside OnComplete");
                    Location location = task.getResult();
                    if(location == null)
                    {
                        Log.i("Location", "location null");
                        requestNewLocationData();
                    }
                    else
                    {
                        Log.i("Location", "location not null");
                        Log.i("Location", "Longitude = " + location.getLongitude());
                        Log.i("Location", "Latitude = " + location.getLatitude());
                        listener.afterGettingLatitudeAndLongitude(location.getLatitude(), location.getLongitude());
                    }
                }
            });
        }
        else
        {
            requestPermissions();
            getLocation();
        }
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData()
    {
        LocationRequest locationRequest = new LocationRequest();

        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
                .setInterval(500)
                .setFastestInterval(0)
                .setNumUpdates(1);

        mfusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(mcontext);
        mfusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
    }

    private LocationCallback locationCallback = new LocationCallback()
    {
        @Override
        public void onLocationResult(LocationResult locationResult)
        {
            super.onLocationResult(locationResult);
            Location location = locationResult.getLastLocation();

            listener.afterGettingLatitudeAndLongitude(location.getLatitude(), location.getLongitude());
            Log.i("Location", "Longitude = " + location.getLongitude());
            Log.i("Location", "Latitude = " + location.getLatitude());

        }
    };

    public boolean checkPermissions()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if(mcontext.getApplicationContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED
                    || mcontext.getApplicationContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED )
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return true;
        }
    }

    private void requestPermissions()
    {
        ActivityCompat.requestPermissions((Activity) mcontext,
                new String[] {Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION},
                1);
    }

}
interface iLocation
{
    public void afterGettingLatitudeAndLongitude(double latitude, double longitude);
}
