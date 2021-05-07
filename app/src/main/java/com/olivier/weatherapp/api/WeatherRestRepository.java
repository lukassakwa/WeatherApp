package com.olivier.weatherapp.api;

import com.olivier.weatherapp.model.weathermodels.current.CurrentWeatherModel;
import com.olivier.weatherapp.model.weathermodels.daily.DailyWeatherModel;
import com.olivier.weatherapp.model.weathermodels.onecall.WeatherModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherRestRepository {
    @GET("onecall")
    Call<WeatherModel> getHourlyWeather(@Query("lat") Double lat,
                                        @Query("lon") Double lon,
                                        @Query("exclude") String exclude,
                                        @Query("units") String units,
                                        @Query("appid") String authorization);

    @GET("weather")
    Call<CurrentWeatherModel> getCurrentWeather(@Query("lat") Double lat,
                                                @Query("lon") Double lon,
                                                @Query("units") String units,
                                                @Query("appid") String authorization);

    @GET("forecast/daily")
    Call<DailyWeatherModel> getDailyWeather(@Query("lat") Double lat,
                                            @Query("lon") Double lon,
                                            @Query("units") String units,
                                            @Query("appid") String authorization);

}
