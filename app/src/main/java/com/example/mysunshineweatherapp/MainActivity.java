package com.example.mysunshineweatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.VoiceInteractor;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.VolumeShaper;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.renderscript.ScriptIntrinsicYuvToRGB;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class MainActivity extends AppCompatActivity implements
        iRequestFinished,
        IWeatherView,
        iLocation,
        iReverseGeocoding
{
    //This One is My App

    private String BASE_URL_FORDETECT_LOCATION = "https://api.openweathermap.org";
    private String BASE_URL_FOR_LAT_LON = "http://api.positionstack.com/v1/reverse?access_key=06999c172c3426fde2430b28599631e7";
    private String BASE_URL = "https://api.openweathermap.org/data/2.5/onecall?lat=23.160938&lon=79.949717&exclude=minutely,alerts,hourly,current&units=metric&appid=02ffda257731b12fc88d6a26602950b7";
    private String default_city_name = "Jabalpur";
    RequestQueue queue;
    TextView cityNameTextView;
    ImageView todayWeatherDescriptionImageView;
    TextView todayWeatherDescriptionTextView;
    TextView todayMaximumTempretureTextView;
    TextView todayMinimumTempretureTextView;
    ProgressBar spinner;
    LocationManager locationManager;
    private double latitude;
    private double longitude;
    private boolean foundLocation;
    Context applicationContext;
    SharedPreferences preferences;
    Dailyforecast weatherData[] = new Dailyforecast[8];

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Setting the color of the Action Bar
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0388fc")));

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        //latitude = 23.1815;
        //longitude = 79.9864;
        applicationContext = getApplicationContext();
        spinner = findViewById(R.id.spinner);
        queue = MySingleton.getInstance(applicationContext).getRequestQueue();

        queue.start();
        requestWeatherData();
    }

    private void updateData(Dailyforecast weatherData[])
    {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        ListAdapter adapter = new ListAdapter(weatherData, this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(adapter);
    }

    public void setWeatherData(Dailyforecast dailyforecast[])
    {
        this.weatherData = dailyforecast;
        Log.i("MainActivity", "Inside Set Weather Data");
        Log.i("MainActivity", "" + weatherData[0].getWeatherDescription());
        if(weatherData != null &&
                getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            String weatherDescription = weatherData[0].getWeatherDescription();
            ReverseGeocoding reverseGeocoding = new ReverseGeocoding(this, this);
            reverseGeocoding.makeRequest(preferences.getString("latitude", "23.1815"),
                    preferences.getString("longitude", "79.9864"));

            todayWeatherDescriptionImageView = findViewById(R.id.todayWeatherImageView);
            todayWeatherDescriptionTextView = findViewById(R.id.todayWeatherDescriptionTextView);
            todayMaximumTempretureTextView = findViewById(R.id.todayMaxTempreture);
            todayMinimumTempretureTextView = findViewById(R.id.todayMinTempreture);


            todayWeatherDescriptionTextView.setText(weatherDescription);
            todayMaximumTempretureTextView.setText("" + weatherData[0].getMaxTempreture() + "\u00B0");
            todayMinimumTempretureTextView.setText("" + weatherData[0].getMinTempreture()+ "\u00B0");

            if(weatherDescription.equals("overcast clouds"))
            {
                todayWeatherDescriptionImageView.setImageResource(R.drawable.fewclouds);
            }
            else if(weatherDescription.equals("clear sky"))
            {
                todayWeatherDescriptionImageView.setImageResource(R.drawable.clearsky);
            }
            else if(weatherDescription.equals("scattered clouds") || weatherDescription.equals("few clouds"))
            {
                todayWeatherDescriptionImageView.setImageResource(R.drawable.fewclouds);
            }
            else if(weatherDescription.equals("thunderstorm"))
            {
                todayWeatherDescriptionImageView.setImageResource(R.drawable.thunderstorm);
            }
            else
            {
                todayWeatherDescriptionImageView.setImageResource(R.drawable.fewclouds);
            }
        }
        spinner.setVisibility(View.INVISIBLE);
        updateData(weatherData);
    }

    public void requestWeatherData()
    {
        WeatherRequester requester = new WeatherRequester(getApplicationContext(), this);
        //requester.makeRequest(latitude, longitude);

        requester.makeRequest(preferences.getString("latitude", "23.1815"),
                preferences.getString("longitude", "79.9864"));
    }

    //Triggered When the user Clicks the item in recycler view
    @Override
    public void OnItemClickListener(Dailyforecast weatherData)
    {
        Toast.makeText(this,
                "Item Clicked is" + weatherData.getWeatherDescription(),
                Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, ForecastWeatherDetailsActivity.class);
        intent.putExtra("WeatherData", weatherData);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        int id = item.getItemId();
        if(id == R.id.city)
        {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.detectLocation)
        {
            foundLocation = false;
            if(isLocationEnabled())
            {
                LocationRequester locationRequester = new LocationRequester(this, this);
                locationRequester.getLocation();
            }
            else
            {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
                Toast.makeText(this, "Enable Your Location", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isLocationEnabled()
    {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    //After Getting the location
    @Override
    public void afterGettingLatitudeAndLongitude(double latitude, double longitude)
    {
        Toast.makeText(this, "Location Fetched", Toast.LENGTH_LONG).show();
        Log.i("MainActivity", "" + latitude);
        Log.i("MainActivity", "" + longitude);
        this.latitude = latitude;
        this.longitude = longitude;

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("latitude", "" + this.latitude);
        editor.putString("longitude", "" + this.longitude);
        requestWeatherData();
    }

    //After Getting Weather Data
    @Override
    public void OnGettingData(Dailyforecast[] weatherData)
    {
        setWeatherData(weatherData);
    }

    @Override
    public void afterGettingCityName(String cityName)
    {
        Log.i("MainActivity", "cityName");
        cityNameTextView = findViewById(R.id.cityNameTextView);
        cityNameTextView.setText(cityName);
    }
}