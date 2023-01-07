package com.example.mysunshineweatherapp;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Dailyforecast implements Serializable
{
    private String weatherDescription;
    private int minTempreture;
    private int maxTempreture;
    private String date;
    private int humidity;
    private int pressure;
    private double windSpeed;

    public String getWeatherDescription() {
        return weatherDescription;
    }

    public void setWeatherDescription(String weatherDescription) {
        this.weatherDescription = weatherDescription;
    }

    public int getMinTempreture() {
        return minTempreture;
    }

    public void setMinTempreture(int minTempreture) {
        this.minTempreture = minTempreture;
    }

    public int getMaxTempreture() {
        return maxTempreture;
    }

    public void setMaxTempreture(int maxTempreture) {
        this.maxTempreture = maxTempreture;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public int getPressure() {
        return pressure;
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public Dailyforecast(double minTempreture, double maxTempreture, String weatherDescription, String date, int humidity, int pressure, double windSpeed)
    {
        this.minTempreture = (int) Math.ceil(minTempreture);
        this.maxTempreture = (int) Math.ceil(maxTempreture);
        this.weatherDescription = weatherDescription;
        this.date = date;
        this.humidity = humidity;
        this.pressure = pressure;
        this.windSpeed = windSpeed;
    }
}
