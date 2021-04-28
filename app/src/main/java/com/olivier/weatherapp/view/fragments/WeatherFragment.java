package com.olivier.weatherapp.view.fragments;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
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
import com.olivier.weatherapp.Repository.WeatherRestRepository;
import com.olivier.weatherapp.controller.ClientApi;
import com.olivier.weatherapp.model.CurrentWeather;
import com.olivier.weatherapp.model.FutureWeather;
import com.olivier.weatherapp.model.HttpModel;
import com.olivier.weatherapp.model.weathermodels.onecall.Current;
import com.olivier.weatherapp.model.weathermodels.onecall.DailyItem;
import com.olivier.weatherapp.model.weathermodels.onecall.HourlyItem;
import com.olivier.weatherapp.model.weathermodels.onecall.WeatherModel;
import com.olivier.weatherapp.view.recyclerviews.WeatherDayRaportAdapter;
import com.olivier.weatherapp.view.recyclerviews.WeatherHourAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class WeatherFragment extends Fragment {
    //Context
    private FragmentActivity contextWeather;

    //HttpModel
    private HttpModel weather;

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

        weather = (HttpModel) getArguments().getSerializable("httpModel");

        weatherHourRecyclerViewLayoutManager = new LinearLayoutManager(contextWeather, LinearLayoutManager.HORIZONTAL, false);
        weatherDayRaportLayoutManager = new LinearLayoutManager(contextWeather, LinearLayoutManager.VERTICAL, false);
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
        SetActivity(weather);

        //Refresh Swipe
        //Allow user to refresh weather data
        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                SetActivity(weather);
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
        cityNameTextView.setText(firstWeatherElement.getName());
        feels_tempTextView.setText((int) firstWeatherElement.getFeels_temp() + "\u2103");
        pressureTextView.setText(firstWeatherElement.getPressure() + "hPa");
        humidityTextView.setText(firstWeatherElement.getHumidity() + "%");
        visibilityTextView.setText(firstWeatherElement.getVisibility() + "km");
        speedTextView.setText((int) firstWeatherElement.getSpeed() + "km/h");
        uvTextView.setText(firstWeatherElement.getUv() + "");
        degreeTextView.setText(firstWeatherElement.getDegree() + " wind");
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

    public void SetActivity(HttpModel httpModel){

        WeatherRestRepository weatherRestRepository = ClientApi.getClient(httpModel).create(WeatherRestRepository.class);

        Call<WeatherModel> oneCall = weatherRestRepository.getWeather(httpModel.getLat(),
                httpModel.getLon(),
                httpModel.getExcludes(),
                httpModel.getUnits(),
                httpModel.getAuthorization());

        oneCall.enqueue(new Callback<WeatherModel>() {

            @Override
            public void onResponse(Call<WeatherModel> call, Response<WeatherModel> response) {

                if(response.isSuccessful()){

                    WeatherModel weatherModel = response.body();

                    CurrentWeather currentWeather = currentWeatherInit(weatherModel.getCurrent(), httpModel);
                    ArrayList<FutureWeather> futureWeather = dailyWeatherInit(weatherModel.getDaily());
                    ArrayList<FutureWeather> hourlyWeather = hourlyWeatherInit(weatherModel.getHourly());

                    //Initializing data on daily recyclerView
                    DayRecyclerView(futureWeather);
                    HourRecyclerView(hourlyWeather);
                    //Initializing widgets
                    mainWindowSetWidget(currentWeather);
                }
            }

            @Override
            public void onFailure(Call<WeatherModel> call, Throwable t) {
                Log.d("GSON_EXCEPTION", t.toString());
            }
        });
    }

    //Initializing current model object
    private CurrentWeather currentWeatherInit(Current current, HttpModel httpModel){
        CurrentWeather currentWeather = new CurrentWeather();
        //Reading from Json Pojo
        currentWeather.setTemp(current.getTemp());
        currentWeather.setDescription(current.getWeather().get(0).getDescription());
        //getLocation
        currentWeather.setName(getLocationName(httpModel));
        currentWeather.setFeels_temp(current.getFeelsLike());
        currentWeather.setVisibility(current.getVisibility());
        currentWeather.setPressure(current.getPressure());
        currentWeather.setSpeed(current.getWindSpeed());
        //windDirection
        currentWeather.setDegree(windDirection(current.getWindDeg()));
        //uvAlert
        currentWeather.setUv(uvAlert(current.getUvi()));
        currentWeather.setHumidity(current.getHumidity());

        return currentWeather;
    }

    //Initializing Future Weather model for day
    private ArrayList<FutureWeather> dailyWeatherInit(List<DailyItem> dailyItem){
        ArrayList<FutureWeather> futureWeathers = new ArrayList<>();

        //DailyInit
        for(int i = 0; i < dailyItem.size()-1; i++){
            //Reading from Json Pojo
            FutureWeather futureWeather = new FutureWeather();
            futureWeather.setTemp(dailyItem.get(i).getTemp().getDay());
            futureWeather.setDescription(dailyItem.get(i).getWeather().get(0).getDescription());
            futureWeather.setIcon(dailyItem.get(i).getWeather().get(0).getIcon());
            futureWeather.setDt(dailyItem.get(i).getDt());
            futureWeathers.add(futureWeather);
        }

        return futureWeathers;
    }

    //Initializing Future Weather model for hour
    private ArrayList<FutureWeather> hourlyWeatherInit(List<HourlyItem> hourlyItem){
        ArrayList<FutureWeather> hourlyWeathers = new ArrayList<>();

        //HourlyInit
        for(int i = 1; i <= 24; i++){
            //Reading from Json Pojo
            FutureWeather hourlyWeather = new FutureWeather();
            hourlyWeather.setTemp(hourlyItem.get(i).getTemp());
            hourlyWeather.setDescription(hourlyItem.get(i).getWeather().get(0).getDescription());
            hourlyWeather.setIcon(hourlyItem.get(i).getWeather().get(0).getIcon());
            hourlyWeather.setDt(hourlyItem.get(i).getDt());
            hourlyWeathers.add(hourlyWeather);
        }

        return hourlyWeathers;
    }

    //Getting name of City and country from lat and lon
    private String getLocationName(HttpModel httpModel){
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
