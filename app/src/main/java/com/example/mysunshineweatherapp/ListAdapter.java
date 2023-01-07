package com.example.mysunshineweatherapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder>
{

    Dailyforecast item[];

    IWeatherView listener;

    public ListAdapter(Dailyforecast weatherData[], IWeatherView listener)
    {
        this.item = weatherData;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.list_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                listener.OnItemClickListener(item[viewHolder.getAdapterPosition()]);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ListAdapter.ViewHolder holder, int position)
    {
        String weatherDescription = item[position].getWeatherDescription();
        holder.dayDateAndDayTextView.setText(item[position].getDate());
        holder.dayWeatherDescriptionTextView.setText(weatherDescription);
        holder.dayMaximumTempretureTextView.setText("" + item[position].getMaxTempreture()+ "\u00B0");
        holder.dayMinimumTempretureTextView.setText("" + item[position].getMinTempreture()+ "\u00B0");

        if(weatherDescription.equals("overcast clouds"))
        {
            holder.weatherImageView.setImageResource(R.drawable.fewclouds);
        }
        else if(weatherDescription.equals("clear sky"))
        {
            holder.weatherImageView.setImageResource(R.drawable.clearsky);
        }
        else if(weatherDescription.equals("scattered clouds") || weatherDescription.equals("few clouds"))
        {
            holder.weatherImageView.setImageResource(R.drawable.fewclouds);
        }
        else if(weatherDescription.equals("thunderstorm"))
        {
            holder.weatherImageView.setImageResource(R.drawable.thunderstorm);
        }
        else
        {
            holder.weatherImageView.setImageResource(R.drawable.fewclouds);
        }
    }

    @Override
    public int getItemCount()
    {
        return item.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView weatherImageView;
        TextView dayDateAndDayTextView;
        TextView dayWeatherDescriptionTextView;
        TextView dayMaximumTempretureTextView;
        TextView dayMinimumTempretureTextView;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            weatherImageView = itemView.findViewById(R.id.weatherImage);
            dayDateAndDayTextView = itemView.findViewById(R.id.dateAndDayTextview);
            dayWeatherDescriptionTextView = itemView.findViewById(R.id.weatherDescriptionTextView);
            dayMaximumTempretureTextView = itemView.findViewById(R.id.maximumTempretureTextView);
            dayMinimumTempretureTextView = itemView.findViewById(R.id.minimumTempretureTextView);
        }
    }
}

interface IWeatherView
{
    public void OnItemClickListener(Dailyforecast weatherData);
}
