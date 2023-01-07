package com.example.mysunshineweatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class ForecastWeatherDetailsActivity extends AppCompatActivity
{

    private String LOG_MESSAGE = "ForecastWeatherDetailsActivity";
    private TextView weatherDetailsView;
    private TextView dateAndDayTextView;
    private TextView maximumTempretureTextView;
    private TextView minimumTempretureTextView;
    private TextView humidityTextView;
    private TextView pressureTextView;
    private TextView windSpeedTextView;
    private ImageView weatherDetailsImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast_weather_details);

        //Setting the color of the Action Bar
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0388fc")));

        //Initializing the variables
        dateAndDayTextView = findViewById(R.id.forecastActivityDateAndTimeTextView);
        weatherDetailsView = findViewById(R.id.forecastActivityWeatherDescriptionTextView);
        maximumTempretureTextView = findViewById(R.id.forecastActivityMaximumTempretureTextView);
        minimumTempretureTextView = findViewById(R.id.forecastActivityMinimumTempretureTextView);
        humidityTextView = findViewById(R.id.forecastHumidityTextView);
        pressureTextView = findViewById(R.id.forecastPressureTextView);
        windSpeedTextView = findViewById(R.id.forecastWindTextView);
        weatherDetailsImageView = findViewById(R.id.forecastActivityImageView);

        //Getting the Weather Details From the intent
        Intent intent = getIntent();
        Dailyforecast forecastData = (Dailyforecast) intent.getSerializableExtra("WeatherData");

        //Settings the data on forecast Activity
        dateAndDayTextView.setText(forecastData.getDate());
        weatherDetailsView.setText(forecastData.getWeatherDescription());
        maximumTempretureTextView.setText("" + forecastData.getMaxTempreture()+ "\u00B0");
        minimumTempretureTextView.setText("" + forecastData.getMinTempreture()+ "\u00B0");
        humidityTextView.setText("" + forecastData.getHumidity() + "%");
        pressureTextView.setText("" + forecastData.getPressure() + "hPa");
        windSpeedTextView.setText("" + forecastData.getWindSpeed() + "KPH");
        String weatherDescription = forecastData.getWeatherDescription();

        if(weatherDescription.equals("overcast clouds"))
        {
            weatherDetailsImageView.setImageResource(R.drawable.fewclouds);
        }
        else if(weatherDescription.equals("clear sky"))
        {
            weatherDetailsImageView.setImageResource(R.drawable.clearsky);
        }
        else if(weatherDescription.equals("scattered clouds") || weatherDescription.equals("few clouds"))
        {
            weatherDetailsImageView.setImageResource(R.drawable.fewclouds);
        }
        else if(weatherDescription.equals("thunderstorm"))
        {
            weatherDetailsImageView.setImageResource(R.drawable.thunderstorm);
        }
        else
        {
            weatherDetailsImageView.setImageResource(R.drawable.fewclouds);
        }
    }
}