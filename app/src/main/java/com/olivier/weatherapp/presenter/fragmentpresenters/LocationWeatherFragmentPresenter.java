package com.olivier.weatherapp.presenter.fragmentpresenters;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import androidx.annotation.NonNull;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.olivier.weatherapp.api.ClientApi;
import com.olivier.weatherapp.api.WeatherRestRepository;
import com.olivier.weatherapp.model.CurrentWeather;
import com.olivier.weatherapp.model.DailyWeather;
import com.olivier.weatherapp.model.HourlyWeather;
import com.olivier.weatherapp.model.WeatherModel;
import com.olivier.weatherapp.model.weathermodels.current.CurrentWeatherModel;
import com.olivier.weatherapp.model.weathermodels.daily.DailyWeatherModel;
import com.olivier.weatherapp.model.weathermodels.daily.ListItem;
import com.olivier.weatherapp.model.weathermodels.onecall.HourlyItem;
import com.olivier.weatherapp.model.weathermodels.onecall.HourlyWeatherModel;
import com.olivier.weatherapp.presenter.BasePresenter;
import com.olivier.weatherapp.presenter.contract.LocationWeatherFragmentContract;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.List;

public class LocationWeatherFragmentPresenter extends BasePresenter<LocationWeatherFragmentContract.View> implements LocationWeatherFragmentContract.Presenter {

    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LocationRequest mLocationResult;
    private LocationCallback locationCallback;

    private WeatherModel _weatherModel;

    private CurrentWeather _currentWeather;
    private ArrayList<HourlyWeather> _hourlyWeather;
    private ArrayList<DailyWeather> _dailyWeather;

    public LocationWeatherFragmentPresenter(WeatherModel weatherModel) {
        this._weatherModel = weatherModel;
    }

    public LocationWeatherFragmentPresenter(WeatherModel weatherModel, FusedLocationProviderClient fusedLocationProviderClient) {
        this._weatherModel = weatherModel;
        this.mFusedLocationProviderClient = fusedLocationProviderClient;
        initLocationCallback();
    }

    private void initLocationCallback(){
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

                    _weatherModel = new WeatherModel(lon, lat, "current");
                    getWeather();

                    //update location weatherModel in main activity
                    Bundle result = new Bundle();
                    result.putSerializable("weatherObject", _weatherModel);
                    view.resultDataToParent(result);
                }
                view.turnOffSwipeOnRefresh();
                //remove location Callback when update is done
                mFusedLocationProviderClient.removeLocationUpdates(locationCallback);
            }
        };
    }

    @SuppressLint("MissingPermission")
    @Override
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

    @Override
    public void getWeather() {
        WeatherRestRepository weatherRestRepository = ClientApi.getRetrofit(_weatherModel).create(WeatherRestRepository.class);

        Observable<CurrentWeatherModel> currentWeatherModelCall = weatherRestRepository.getCurrentWeather(_weatherModel.getLat(),
                _weatherModel.getLon(),
                "en",
                _weatherModel.getUnits(),
                _weatherModel.getAuthorization());

        Observable<HourlyWeatherModel> hourlyWeatherModelCall = weatherRestRepository.getHourlyWeather(_weatherModel.getLat(),
                _weatherModel.getLon(),
                _weatherModel.getExcludes(),
                "en",
                _weatherModel.getUnits(),
                _weatherModel.getAuthorization());

        Observable<DailyWeatherModel> dailyWeatherModelCall = weatherRestRepository.getDailyWeather(_weatherModel.getLat(),
                _weatherModel.getLon(),
                "en",
                _weatherModel.getUnits(),
                _weatherModel.getAuthorization());

        currentWeatherModelCall.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::currentWeatherSuccesHandle);

        hourlyWeatherModelCall.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::hourlyWeatherSuccesHandle);

        dailyWeatherModelCall.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::dailyWeatherSuccesHandle);

    }

    private void currentWeatherSuccesHandle(CurrentWeatherModel currentWeatherModel){
        _currentWeather = currentWeatherInit(currentWeatherModel);
        view.showCurrentWeather(_currentWeather);
    }

    private void hourlyWeatherSuccesHandle(HourlyWeatherModel hourlyWeatherModel){
        _hourlyWeather = hourlyWeatherInit(hourlyWeatherModel.getHourly());
        view.showHourlyWeather(_hourlyWeather);
    }

    private void dailyWeatherSuccesHandle(DailyWeatherModel dailyWeatherModel){
        _dailyWeather = dailyWeatherInit(dailyWeatherModel.getList());
        view.showDailyWeather(_dailyWeather);
    }

    //Initializing current and future model object
    private CurrentWeather currentWeatherInit(CurrentWeatherModel current){
        CurrentWeather currentWeather = new CurrentWeather();
        //Reading from Json Pojo
        currentWeather.setName(current.getName());
        currentWeather.setTemp(current.getMain().getTemp());
        currentWeather.setDescription(current.getWeather().get(0).getDescription());
        currentWeather.setFeels_temp(current.getMain().getFeelsLike());
        currentWeather.setVisibility(current.getVisibility());
        currentWeather.setPressure(current.getMain().getPressure());
        currentWeather.setSpeed(current.getWind().getSpeed());
        //windDirection
        currentWeather.setDegree(windDirection(current.getWind().getDeg()));
        currentWeather.setHumidity(current.getMain().getHumidity());

        return currentWeather;
    }

    //Initializing hour Weather model for day
    private ArrayList<DailyWeather> dailyWeatherInit(List<ListItem> dailyItems){
        ArrayList<DailyWeather> futureWeathers = new ArrayList<>();

        //DailyInit
        for(ListItem dailyItem : dailyItems){
            //Reading from Json Pojo
            DailyWeather futureWeather = new DailyWeather();
            futureWeather.setMinTemp(dailyItem.getTemp().getMin());
            futureWeather.setMaxTemp(dailyItem.getTemp().getMax());
            futureWeather.setDescription(dailyItem.getWeather().get(0).getDescription());
            futureWeather.setIcon(dailyItem.getWeather().get(0).getIcon());
            futureWeather.setDt(dailyItem.getDt());
            futureWeathers.add(futureWeather);
        }

        return futureWeathers;
    }

    //Initializing daily Weather model for hour
    private ArrayList<HourlyWeather> hourlyWeatherInit(List<HourlyItem> hourlyItem){
        ArrayList<HourlyWeather> hourlyWeathers = new ArrayList<>();

        //HourlyInit
        for(int i = 1; i <= 24; i++){
            //Reading from Json Pojo
            HourlyWeather hourlyWeather = new HourlyWeather();
            hourlyWeather.setTemp(hourlyItem.get(i).getTemp());
            hourlyWeather.setDescription(hourlyItem.get(i).getWeather().get(0).getDescription());
            hourlyWeather.setIcon(hourlyItem.get(i).getWeather().get(0).getIcon());
            hourlyWeather.setDt(hourlyItem.get(i).getDt());
            hourlyWeathers.add(hourlyWeather);
        }

        return hourlyWeathers;
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

    public void setWeatherModel(WeatherModel _weatherModel) {
        this._weatherModel = _weatherModel;
    }
}
