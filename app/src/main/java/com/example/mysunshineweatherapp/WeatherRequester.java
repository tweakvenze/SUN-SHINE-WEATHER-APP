package com.example.mysunshineweatherapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Date;

public class WeatherRequester
{
    private iRequestFinished listener;
    private String BASE_URL = "api.openweathermap.org";
    private RequestQueue queue;
    private String cityName;
    Context applicationContext;
    private String url;
   String temp = "api.openweathermap.org/data/2.5/onecall?lat=23.160938" +
           "&lon=79.949717&exclude=minutely,alerts,hourly,current" +
           "&units=metric&appid=02ffda257731b12fc88d6a26602950b7";

    public WeatherRequester(Context applicationContext, iRequestFinished listener)
    {
        this.applicationContext = applicationContext;
        this.listener = listener;
    }


    public void buildUrl(String latitude, String longitude)
    {
        Uri.Builder builder = new Uri.Builder();
        builder.authority(BASE_URL)
                .scheme("https")
                .appendPath("data")
                .appendPath("2.5")
                .appendPath("onecall")
                .appendQueryParameter("lat", "" + latitude)
                .appendQueryParameter("lon", "" + longitude)
                .appendQueryParameter("exclude", "minutely,alerts,hourly,current")
                .appendQueryParameter("units", "metric")
                .appendQueryParameter("appid", "02ffda257731b12fc88d6a26602950b7");

        this.url = builder.build().toString();

        Log.i("WeatherRequester", url);
    }

    public void makeRequest(String latitude, String longitude)
    {
        buildUrl(latitude, longitude);
        queue = MySingleton.getInstance(applicationContext).getRequestQueue();

        queue.start();

        Dailyforecast weatherData[] = new Dailyforecast[8];
        JsonObjectRequest object = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                //Here We Are Getting the Array which contains information about daliy forecast
                JSONArray forecastArray = response.optJSONArray("daily");
                for (int i = 0; i < forecastArray.length(); i++)
                {
                    JSONObject weatherOfTheDay = forecastArray.optJSONObject(i);
                    long date = weatherOfTheDay.optLong("dt");
                    Date day = new Date(date * 1000);
                    String dayObject = day.toString();
                    int pressure = weatherOfTheDay.optInt("pressure");
                    int humidity = weatherOfTheDay.optInt("humidity");
                    double windSpeed = weatherOfTheDay.optDouble("wind_speed");

                    JSONObject currentDayTempreture = weatherOfTheDay.optJSONObject("temp");
                    double minimumTempreture = 0.00;
                    double maximumTempreture = 0.00;
                    if(currentDayTempreture != null)
                    {
                        minimumTempreture = currentDayTempreture.optDouble("min");
                        maximumTempreture = currentDayTempreture.optDouble("max");
                    }

                    JSONArray currentDayWeatherDetails = weatherOfTheDay.optJSONArray("weather");
                    String weatherDetails = "";
                    if(currentDayWeatherDetails != null)
                    {
                        JSONObject weatherObject = currentDayWeatherDetails.optJSONObject(0);
                        weatherDetails = weatherObject.optString("description");
                    }

                    Dailyforecast obj = new Dailyforecast(minimumTempreture, maximumTempreture,
                            weatherDetails, dayObject.substring(0, 10),
                            humidity, pressure, windSpeed);
                    weatherData[i] = obj;

                }
                queue.stop();
                listener.OnGettingData(weatherData);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                //Toast.makeText(MainActivity.class, "Something is Wrong", Toast.LENGTH_SHORT).show();
                Log.i("MainActivity", error.toString());
                queue.stop();
                //spinner.setVisibility(View.INVISIBLE);
            }
        });
        MySingleton.getInstance(applicationContext).addToRequestQueue(object);
    }
}
interface iRequestFinished
{
    public void OnGettingData(Dailyforecast weatherData[]);
}

