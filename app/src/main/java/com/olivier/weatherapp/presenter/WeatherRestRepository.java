package com.olivier.weatherapp.presenter;

import com.olivier.weatherapp.model.weathermodels.onecall.WeatherModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherRestRepository {
    @GET("onecall")
    Call<WeatherModel> getWeather(@Query("lat") Double lat,
                                  @Query("lon") Double lon,
                                  @Query("exclude") String exclude,
                                  @Query("units") String units,
                                  @Query("appid") String authorization);

}
