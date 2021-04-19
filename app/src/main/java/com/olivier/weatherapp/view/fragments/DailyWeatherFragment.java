package com.olivier.weatherapp.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.olivier.weatherapp.R;
import com.olivier.weatherapp.model.FutureWeather;
import com.olivier.weatherapp.view.recyclerviews.WeatherDayRaportAdapter;

import java.util.ArrayList;

public class DailyWeatherFragment extends Fragment {

    private RecyclerView weatherDayRaportRecyclerView;
    private RecyclerView.Adapter weatherDayRaportAdapter;
    private RecyclerView.LayoutManager weatherDayRaportLayoutManager;

    private FragmentActivity contextDaily;

    private ArrayList<FutureWeather> weathers;

    public DailyWeatherFragment(Context contextDaily) {
        this.contextDaily = (FragmentActivity) contextDaily;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Data from Main Activity Bundel
        weathers = (ArrayList<FutureWeather>) this.getArguments().getSerializable("dailyWeathers");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.daily_weather_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        weatherDayRaportRecyclerView = view.findViewById(R.id.weatherDayRaportRecyclerView);
        weatherDayRaportLayoutManager = new LinearLayoutManager(contextDaily, LinearLayoutManager.VERTICAL, false);

        //Initializing data on daily recyclerView
        DayRecyclerView(weathers);
    }

    private void DayRecyclerView(ArrayList<FutureWeather> dailyDataModels){
        weatherDayRaportRecyclerView.setHasFixedSize(true);
        weatherDayRaportRecyclerView.setLayoutManager(weatherDayRaportLayoutManager);

        weatherDayRaportAdapter = new WeatherDayRaportAdapter(dailyDataModels);
        weatherDayRaportRecyclerView.setAdapter(weatherDayRaportAdapter);
    }

}
