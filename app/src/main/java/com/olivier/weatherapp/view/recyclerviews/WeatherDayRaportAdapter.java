package com.olivier.weatherapp.view.recyclerviews;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.olivier.weatherapp.R;
import com.olivier.weatherapp.model.DailyWeather;
import com.squareup.picasso.Picasso;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.TimeZone;

public class WeatherDayRaportAdapter extends RecyclerView.Adapter<WeatherDayRaportAdapter.ViewHolder> {

    private final ArrayList<DailyWeather> dailyDataModels;

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView dateTextView;
        private TextView dayTextView;
        private ImageView weatherImageView;
        private TextView weatherDescriptionTextView;
        private TextView temperatureTextViewItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            dateTextView = itemView.findViewById(R.id.dateTextView);
            dayTextView = itemView.findViewById(R.id.dayTextView);
            weatherImageView = itemView.findViewById(R.id.weatherImageViewItem);
            weatherDescriptionTextView = itemView.findViewById(R.id.weatherDescriptionTextViewItem);
            temperatureTextViewItem = itemView.findViewById(R.id.temperatureTextViewItem);
        }
    }

    public WeatherDayRaportAdapter(ArrayList<DailyWeather> dailyDataModels){
        this.dailyDataModels = dailyDataModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewHolder = LayoutInflater.from(parent.getContext()).inflate(R.layout.weather_day_item, null);
        return new ViewHolder(viewHolder);
    }

    @Override
    public int getItemCount() {
        return dailyDataModels.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //setting data SimpleDataFormat //read more about this
        long dt = dailyDataModels.get(position).getDt();
        java.util.Date time = new java.util.Date(dt*1000);

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM");
        sdf.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));
        sdf.setDateFormatSymbols(new DateFormatSymbols(new Locale("en","US")));

        holder.dateTextView.setText(sdf.format(time));

        if(position == 0){
            holder.dayTextView.setText("Today");
        }else{
            SimpleDateFormat dayDataFormat = new SimpleDateFormat("E");
            dayDataFormat.setDateFormatSymbols(new DateFormatSymbols(new Locale("en","US")));
            holder.dayTextView.setText(dayDataFormat.format(time));
        }

        String description = dailyDataModels.get(position).getDescription();
        holder.weatherDescriptionTextView.setText(description);

        int tempMin = (int) dailyDataModels.get(position).getMinTemp();
        int tempMax = (int) dailyDataModels.get(position).getMaxTemp();
        holder.temperatureTextViewItem.setText(tempMin + "/" + tempMax +"\u2103");

        String urlDisplay = "http://openweathermap.org/img/wn/" + dailyDataModels.get(position).getIcon() + "@2x.png";
        Picasso.get().load(urlDisplay).into(holder.weatherImageView);
    }

}
