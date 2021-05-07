package com.olivier.weatherapp.view.recyclerviews;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.olivier.weatherapp.R;
import com.olivier.weatherapp.model.HourlyWeather;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WeatherHourAdapter extends RecyclerView.Adapter<WeatherHourAdapter.ViewHolder> {

    //private final List<String> hoursString;
    private ArrayList<HourlyWeather> weatherList;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        //private static final String TAG = "WeatherHourAdapter";

        private TextView hourTextView;
        private ImageView weatherImage;
        private TextView weatherDescriptionItem;
        private TextView weatherTempItemTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //deklarowanie wszystkich widgetow z itemu z wheather_hour_item.xml
            //np. TextView textView = findViewbyId...

            hourTextView = itemView.findViewById(R.id.hourTextView);
            weatherImage = itemView.findViewById(R.id.imageView);
            weatherDescriptionItem = itemView.findViewById(R.id.weatherDescriptionItem);
            weatherTempItemTextView = itemView.findViewById(R.id.weatherTempItemTextView);

        }

    }

    public WeatherHourAdapter(ArrayList<HourlyWeather> weatherList){
        //Konstruktor do kteorego bede przesylal dane ktore chce wyswietlic w itemie w RecyclerView
        //this.hoursString = hoursString;
        this.weatherList = weatherList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weather_hour_item, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //tutaj ustawiamy widgety z wheather_hour_item.xml
        //czyli np textView.setText() itp.

        long dt = weatherList.get(position).getDt();
        Date date = new Date(dt*1000);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        String hour = sdf.format(date);
        holder.hourTextView.setText(hour);

        holder.weatherDescriptionItem.setText(weatherList.get(position).getDescription());
        holder.weatherTempItemTextView.setText((int) weatherList.get(position).getTemp() + "\u2103");

        String urlDisplay = "http://openweathermap.org/img/wn/" + weatherList.get(position).getIcon() + "@4x.png";
        Picasso.get().load(urlDisplay).into(holder.weatherImage);

    }

    @Override
    public int getItemCount() {
        //Ilosc Itemow
        return weatherList.size();
    }
}
