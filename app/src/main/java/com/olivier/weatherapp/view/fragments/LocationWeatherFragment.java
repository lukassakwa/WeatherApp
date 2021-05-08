package com.olivier.weatherapp.view.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
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
import com.google.android.gms.location.*;
import com.olivier.weatherapp.R;
import com.olivier.weatherapp.model.CurrentWeather;
import com.olivier.weatherapp.model.DailyWeather;
import com.olivier.weatherapp.model.HourlyWeather;
import com.olivier.weatherapp.model.WeatherModel;
import com.olivier.weatherapp.presenter.contract.LocationWeatherFragmentContract;
import com.olivier.weatherapp.presenter.fragmentpresenters.LocationWeatherFragmentPresenter;
import com.olivier.weatherapp.view.recyclerviews.WeatherDayRaportAdapter;
import com.olivier.weatherapp.view.recyclerviews.WeatherHourAdapter;

import java.util.ArrayList;

public class LocationWeatherFragment extends Fragment implements LocationWeatherFragmentContract.View {

    //Presenter
    private LocationWeatherFragmentPresenter mLocationWeatherFragmentPresenter;
    //Model
    private WeatherModel weather;

    //Location
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LocationRequest mLocationResult;
    private LocationCallback locationCallback;
    
    //Context
    private final FragmentActivity contextWeather;

    //Refresh Swipe
    private SwipeRefreshLayout swipeRefreshLayout;

    //Recycler View hours
    private RecyclerView weatherHourRecyclerView;
    private RecyclerView.Adapter weatherHourRecyclerViewAdapter;
    private RecyclerView.LayoutManager weatherHourRecyclerViewLayoutManager;

    //Recycler View daily
    private RecyclerView weatherDayRaportRecyclerView;
    private RecyclerView.Adapter weatherDayRaportAdapter;
    private RecyclerView.LayoutManager weatherDayRaportLayoutManager;

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

    public LocationWeatherFragment(Context context){
        this.contextWeather = (FragmentActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        weatherHourRecyclerViewLayoutManager = new LinearLayoutManager(contextWeather, LinearLayoutManager.HORIZONTAL, false);
        weatherDayRaportLayoutManager = new LinearLayoutManager(contextWeather, LinearLayoutManager.VERTICAL, false);

        //location
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(contextWeather);

        //Weather model data from bundle;
        weather = (WeatherModel) getArguments().getSerializable("httpModel");

        //presenter
        mLocationWeatherFragmentPresenter = new LocationWeatherFragmentPresenter(weather);
        mLocationWeatherFragmentPresenter.attach(this);
        mLocationWeatherFragmentPresenter.getWeather();
    }

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_weather_fragment, container, false);
    }

    // This event is triggered soon after onCreateView().
    // onViewCreated() is only called if the view returned from onCreateView() is non-null.
    // Any dsadas setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Init Main Activity Widgets
        InitWidgets(view);


        //Refresh Swipe
        swipeRefreshLayout = view.findViewById(R.id.swiperefresh);
        //RecyclerView
        weatherHourRecyclerView = view.findViewById(R.id.weatherHourRecyclerView);
        weatherDayRaportRecyclerView = view.findViewById(R.id.weatherDayRaportRecyclerView);

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
                updateWeather();
            }
        });

        //Callback function which updating weather in fragment
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);

                if(locationResult == null)
                    return;

                for(Location location : locationResult.getLocations()){

                    double lon = location.getLongitude();
                    double lat = location.getLatitude();

                    WeatherModel weatherModel = new WeatherModel(lon, lat, "current");

                    mLocationWeatherFragmentPresenter.setWeatherModel(weatherModel);
                    mLocationWeatherFragmentPresenter.getWeather();

                    //update location weatherModel in main activity
                    Bundle result = new Bundle();
                    result.putSerializable("weatherObject", weatherModel);
                    getParentFragmentManager().setFragmentResult("requestWeatherObject", result);
                }

                swipeRefreshLayout.setRefreshing(false);
                //remove location Callback when update is done
                mFusedLocationProviderClient.removeLocationUpdates(locationCallback);
            }
        };
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
        //getCurrentLocation from presenter
        cityNameTextView.setText(firstWeatherElement.getName());
        //Initialize widget from main Window
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
        weatherDayRaportRecyclerView.setLayoutManager(weatherDayRaportLayoutManager);

        weatherDayRaportAdapter = new WeatherDayRaportAdapter(dailyDataModels);
        weatherDayRaportRecyclerView.setAdapter(weatherDayRaportAdapter);
    }

    private void HourRecyclerView(ArrayList<HourlyWeather> forecastDataModels){
        //Do clasy fragmentowej Recycler View
        weatherHourRecyclerView.setHasFixedSize(true);
        weatherHourRecyclerView.setLayoutManager(weatherHourRecyclerViewLayoutManager);

        weatherHourRecyclerViewAdapter = new WeatherHourAdapter(forecastDataModels);
        weatherHourRecyclerView.setAdapter(weatherHourRecyclerViewAdapter);
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

    //update user location
    @SuppressLint("MissingPermission")
    public void updateWeather() {
        setLocationRequest();
        mFusedLocationProviderClient.requestLocationUpdates(mLocationResult, locationCallback, Looper.getMainLooper());
    }

    private void setLocationRequest(){
        //Device location
        mLocationResult = LocationRequest.create();
        mLocationResult.setInterval(10000);
        mLocationResult.setFastestInterval(5000);
        mLocationResult.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }
}
