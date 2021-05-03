package com.olivier.weatherapp.presenter;

import androidx.fragment.app.FragmentActivity;
import com.olivier.weatherapp.model.CurrentWeather;
import com.olivier.weatherapp.model.FutureWeather;
import com.olivier.weatherapp.model.weathermodels.onecall.WeatherModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import java.util.ArrayList;

public interface ContractMVP {

    /**
     * Represents the View (Activity, Fragment, View subclass) in MVP
     */
    interface WeatherView{
        void showWeather(CurrentWeather currentWeather, ArrayList<FutureWeather> hourlyWeather, ArrayList<FutureWeather> dailyWeather);
        void showName(String cityName);
    }

    /**
     * Represents the Presenter in MVP
     */
    interface WeatherPresenter{
        void getWeather();
        void getLocationName(FragmentActivity fragmentActivity);
    }

    /**
     * Represents the Model
     * Function download data from api
     */
    interface WeatherRestRepository {
        @GET("onecall")
        Call<WeatherModel> getWeather(@Query("lat") Double lat,
                                      @Query("lon") Double lon,
                                      @Query("exclude") String exclude,
                                      @Query("units") String units,
                                      @Query("appid") String authorization);

    }
    
}
