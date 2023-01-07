package com.example.mysunshineweatherapp;


import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Date;

public class ReverseGeocoding
{
    private iReverseGeocoding listener;
    private RequestQueue queue;
    private Context applicationContext;
    private String BASE_URL = "http://api.positionstack.com/v1/reverse?access_key=" +
            "06999c172c3426fde2430b28599631e7";
    private String cityName;

    public ReverseGeocoding(Context applicationContext, iReverseGeocoding listener)
    {
        this.applicationContext = applicationContext;
        this.listener = listener;
    }

    private String buildUrl(String latitude, String longitude)
    {
        Uri.Builder builder = new Uri.Builder();

        builder.scheme("http")
                .authority("api.positionstack.com")
                .appendPath("v1")
                .appendPath("reverse")
                .appendQueryParameter("access_key", "06999c172c3426fde2430b28599631e7")
                .appendQueryParameter("query", "" + latitude + "," + longitude)
                .appendQueryParameter("limit", "1");

        return builder.build().toString();
    }

    public void makeRequest(String latitude, String longitude)
    {
        String url = buildUrl(latitude, longitude);

        Log.i("ReverseGeocoding", url);

        queue = MySingleton.getInstance(applicationContext).getRequestQueue();

        queue.start();


        JsonObjectRequest object = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                JSONArray dataArray = response.optJSONArray("data");

                cityName = dataArray.optJSONObject(0).optString("locality");

                queue.stop();

                Log.i("ReverseGeocoding", "inside on response" +
                        "");
                listener.afterGettingCityName(cityName);
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

interface iReverseGeocoding
{
    public void afterGettingCityName(String cityName);

}
