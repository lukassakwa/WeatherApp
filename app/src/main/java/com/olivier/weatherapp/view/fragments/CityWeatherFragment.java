package com.olivier.weatherapp.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.olivier.weatherapp.R;
import com.olivier.weatherapp.model.CurrentWeather;
import com.olivier.weatherapp.model.DailyWeather;
import com.olivier.weatherapp.model.HourlyWeather;
import com.olivier.weatherapp.model.WeatherModel;
import com.olivier.weatherapp.presenter.contract.CityWeatherFragmentContract;
import com.olivier.weatherapp.presenter.fragmentpresenters.CityWeatherFragmentPresenter;
import com.olivier.weatherapp.view.recyclerviews.WeatherDayRaportAdapter;
import com.olivier.weatherapp.view.recyclerviews.WeatherHourAdapter;

import java.util.ArrayList;

public class CityWeatherFragment extends Fragment implements CityWeatherFragmentContract.View {
    //Presenter
    private CityWeatherFragmentPresenter mCityWeatherFragmentPresenter;

    //Context
    private final FragmentActivity contextWeather;

    //Refresh Swipe
    private SwipeRefreshLayout swipeRefreshLayout;

    //Recycler View hours
    private RecyclerView weatherHourRecyclerView;
    private RecyclerView.Adapter mWeatherHourRecyclerViewAdapter;
    private RecyclerView.LayoutManager mWeatherHourRecyclerViewLayoutManager;

    //Recycler View daily
    private RecyclerView weatherDayRaportRecyclerView;
    private RecyclerView.Adapter mWeatherDayRaportAdapter;
    private RecyclerView.LayoutManager mWeatherDayRaportLayoutManager;

    //Widgets
    private TextView mainTemperatureTextView;
    private TextView weatherDescriptionTextView;
    private TextView feels_tempTextView;
    private TextView pressureTextView;
    private TextView humidityTextView;
    private TextView visibilityTextView;
    private TextView speedTextView;
    private TextView degreeTextView;
    private TextView cityNameTextView;

    public CityWeatherFragment(Context context){
        this.contextWeather = (FragmentActivity) context;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mWeatherHourRecyclerViewLayoutManager = new LinearLayoutManager(contextWeather, LinearLayoutManager.HORIZONTAL, false);
        mWeatherDayRaportLayoutManager = new LinearLayoutManager(contextWeather, LinearLayoutManager.VERTICAL, false);

        //Weather model data from bundle;
        WeatherModel weather = (WeatherModel) getArguments().getSerializable("httpModel");

        //presenter
        mCityWeatherFragmentPresenter = new CityWeatherFragmentPresenter(weather);
        mCityWeatherFragmentPresenter.attach(this);
        mCityWeatherFragmentPresenter.getWeather();
    }

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.main_weather_fragment, container, false);

        //Init Main Activity Widgets
        InitWidgets(rootView);

        swipeRefreshLayout = rootView.findViewById(R.id.swiperefresh);
        weatherHourRecyclerView = rootView.findViewById(R.id.weatherHourRecyclerView);
        weatherDayRaportRecyclerView = rootView.findViewById(R.id.weatherDayRaportRecyclerView);

        //This function allow as to swipe recycler view on right or left without swiping fragment
        weatherHourRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                int action = e.getAction();

                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        rv.getParent().requestDisallowInterceptTouchEvent(true);
                        break;
                }
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

        //Allow user to refresh weather data
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mCityWeatherFragmentPresenter.getWeatherUpdate();
            }
        });

        return rootView;
    }

    private void InitWidgets(View view) {
        mainTemperatureTextView = view.findViewById(R.id.mainTemperatureTextView);
        weatherDescriptionTextView = view.findViewById(R.id.weatherDescriptionTextView);
        feels_tempTextView = view.findViewById(R.id.feels_tempTextView);
        pressureTextView = view.findViewById(R.id.pressureTextView);
        humidityTextView = view.findViewById(R.id.humidityTextView);
        visibilityTextView = view.findViewById(R.id.visibilityTextView);
        speedTextView = view.findViewById(R.id.windSpeedTextView);
        degreeTextView = view.findViewById(R.id.windDirectionTextView);
        cityNameTextView = view.findViewById(R.id.cityTextView);
    }

    public void mainWindowSetWidget(CurrentWeather firstWeatherElement) {
        //Initialize widget from main Window
        cityNameTextView.setText(firstWeatherElement.getName());
        mainTemperatureTextView.setText((int) firstWeatherElement.getTemp() + "");
        weatherDescriptionTextView.setText(firstWeatherElement.getDescription());
        feels_tempTextView.setText((int) firstWeatherElement.getFeels_temp() + "\u2103");
        pressureTextView.setText(firstWeatherElement.getPressure() + "hPa");
        humidityTextView.setText(firstWeatherElement.getHumidity() + "%");
        visibilityTextView.setText(firstWeatherElement.getVisibility() + "km");
        speedTextView.setText((int) firstWeatherElement.getSpeed() + "km/h");
        //windDirection
        degreeTextView.setText(firstWeatherElement.getDegree() + " wind");
    }

    private void DayRecyclerView(ArrayList<DailyWeather> dailyDataModels){
        weatherDayRaportRecyclerView.setHasFixedSize(true);
        weatherDayRaportRecyclerView.setLayoutManager(mWeatherDayRaportLayoutManager);

        mWeatherDayRaportAdapter = new WeatherDayRaportAdapter(dailyDataModels);
        weatherDayRaportRecyclerView.setAdapter(mWeatherDayRaportAdapter);
    }

    private void HourRecyclerView(ArrayList<HourlyWeather> forecastDataModels){
        //Do clasy fragmentowej Recycler View
        weatherHourRecyclerView.setHasFixedSize(true);
        weatherHourRecyclerView.setLayoutManager(mWeatherHourRecyclerViewLayoutManager);

        mWeatherHourRecyclerViewAdapter = new WeatherHourAdapter(forecastDataModels);
        weatherHourRecyclerView.setAdapter(mWeatherHourRecyclerViewAdapter);
    }

    //from Contract.View interface
    @Override
    public void showHourlyWeather(ArrayList<HourlyWeather> hourlyWeathers) {
        //Initializing data on hourly recyclerView
        HourRecyclerView(hourlyWeathers);
    }

    @Override
    public void showDailyWeather(ArrayList<DailyWeather> dailyWeathers) {
        //Initializing data on daily recyclerView
        DayRecyclerView(dailyWeathers);
    }

    @Override
    public void showCurrentWeather(CurrentWeather currentWeather) {
        //Initializing widgets
        mainWindowSetWidget(currentWeather);
    }

    @Override
    public void viewUpdate() {
        swipeRefreshLayout.setRefreshing(false);
    }

}
