package com.olivier.weatherapp.view.fragments;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
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
import com.olivier.weatherapp.presenter.ContractMVP;
import com.olivier.weatherapp.presenter.WeatherController;
import com.olivier.weatherapp.model.CurrentWeather;
import com.olivier.weatherapp.model.FutureWeather;
import com.olivier.weatherapp.model.WeatherHttpModel;
import com.olivier.weatherapp.view.recyclerviews.WeatherDayRaportAdapter;
import com.olivier.weatherapp.view.recyclerviews.WeatherHourAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class WeatherFragment extends Fragment implements ContractMVP.WeatherView {
    //Presenter
    private WeatherController weatherController;

    //Context
    private final FragmentActivity contextWeather;

    //HttpModel
    private WeatherHttpModel weather;

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
    private TextView uvTextView;
    private TextView cityNameTextView;

    public WeatherFragment(Context context){
        this.contextWeather = (FragmentActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        weatherHourRecyclerViewLayoutManager = new LinearLayoutManager(contextWeather, LinearLayoutManager.HORIZONTAL, false);
        weatherDayRaportLayoutManager = new LinearLayoutManager(contextWeather, LinearLayoutManager.VERTICAL, false);

        //Weather model data from bundle;
        weather = (WeatherHttpModel) getArguments().getSerializable("httpModel");

        //presenter
        weatherController = new WeatherController(weather);
        weatherController.attach(this);
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

        //InitRecyclerView
        weatherController.getWeather(weather);

        //Refresh Swipe
        //Allow user to refresh weather data
        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                weatherController.getWeather(weather);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }



    private void InitWidgets(View view) {
        mainTemperatureTextView = view.findViewById(R.id.mainTemperatureTextView);
        weatherDescriptionTextView = view.findViewById(R.id.weatherDescriptionTextView);
        feels_tempTextView = view.findViewById(R.id.feels_tempTextView);
        pressureTextView = view.findViewById(R.id.pressureTextView);
        humidityTextView = view.findViewById(R.id.humidityTextView);
        visibilityTextView = view.findViewById(R.id.visibilityTextView);
        speedTextView = view.findViewById(R.id.windSpeedTextView);
        uvTextView = view.findViewById(R.id.uvTextView);
        degreeTextView = view.findViewById(R.id.windDirectionTextView);
        cityNameTextView = view.findViewById(R.id.cityTextView);
    }

    public void mainWindowSetWidget(CurrentWeather firstWeatherElement) {
        //Initialize widget from main Window
        mainTemperatureTextView.setText((int) firstWeatherElement.getTemp() + "");
        weatherDescriptionTextView.setText(firstWeatherElement.getDescription());
        //getLocationName
        cityNameTextView.setText(getLocationName(weather));
        feels_tempTextView.setText((int) firstWeatherElement.getFeels_temp() + "\u2103");
        pressureTextView.setText(firstWeatherElement.getPressure() + "hPa");
        humidityTextView.setText(firstWeatherElement.getHumidity() + "%");
        visibilityTextView.setText(firstWeatherElement.getVisibility() + "km");
        speedTextView.setText((int) firstWeatherElement.getSpeed() + "km/h");
        //uv ALert
        uvTextView.setText(uvAlert(firstWeatherElement.getUv()) + "");
        //windDirection
        degreeTextView.setText(windDirection(firstWeatherElement.getDegree()) + " wind");
    }

    private void DayRecyclerView(ArrayList<FutureWeather> dailyDataModels){
        weatherDayRaportRecyclerView.setHasFixedSize(true);
        weatherDayRaportRecyclerView.setLayoutManager(weatherDayRaportLayoutManager);

        weatherDayRaportAdapter = new WeatherDayRaportAdapter(dailyDataModels);
        weatherDayRaportRecyclerView.setAdapter(weatherDayRaportAdapter);
    }

    private void HourRecyclerView(ArrayList<FutureWeather> forecastDataModels){
        //Do clasy fragmentowej Recycler View
        weatherHourRecyclerView.setHasFixedSize(true);
        weatherHourRecyclerView.setLayoutManager(weatherHourRecyclerViewLayoutManager);

        weatherHourRecyclerViewAdapter = new WeatherHourAdapter(forecastDataModels);
        weatherHourRecyclerView.setAdapter(weatherHourRecyclerViewAdapter);
    }

    //from ContractMVP.View interface
    @Override
    public void showWeather(CurrentWeather currentWeather, ArrayList<FutureWeather> hourlyWeather, ArrayList<FutureWeather> dailyWeather) {
        //Initializing data on daily recyclerView
        DayRecyclerView(dailyWeather);
        HourRecyclerView(hourlyWeather);
        //Initializing widgets
        mainWindowSetWidget(currentWeather);
    }

    //Getting name of City and country from lat and lon
    private String getLocationName(WeatherHttpModel httpModel){
        Geocoder geocoder = new Geocoder(contextWeather, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(httpModel.getLat(), httpModel.getLon(), 1);
            return addresses.get(0).getCountryName() + "/" + addresses.get(0).getLocality();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    //Getting Wind direction
    private String windDirection(int deg){
        if(deg >= 350 || deg <= 10)
            return "N";
        if(deg < 80)
            return "NE";
        if(deg <= 110)
            return "E";
        if(deg < 170)
            return "SE";
        if(deg <= 190)
            return "S";
        if(deg < 260)
            return "SW";
        if(deg <= 280)
            return "W";
        return "NW";
    }

    //Getting UV alert
    private String uvAlert(double uv){
        if(uv <= 2.0)
            return "no risk";
        if(uv <= 5.0)
            return "Medium risk";
        if(uv <= 7.0)
            return "High risk";
        return "Very high risk";
    }
}
