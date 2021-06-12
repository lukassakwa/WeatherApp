package com.olivier.weatherapp.api;

import com.olivier.weatherapp.model.weathermodels.current.CurrentWeatherModel;
import com.olivier.weatherapp.model.weathermodels.daily.DailyWeatherModel;
import com.olivier.weatherapp.model.weathermodels.find.FindCity;
import com.olivier.weatherapp.model.weathermodels.onecall.HourlyWeatherModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherRestRepository {
    @GET("onecall")
    Call<HourlyWeatherModel> getHourlyWeather(@Query("lat") Double lat,
                                              @Query("lon") Double lon,
                                              @Query("exclude") String exclude,
                                              @Query("lang") String lang,
                                              @Query("units") String units,
                                              @Query("appid") String authorization);

    @GET("weather")
    Call<CurrentWeatherModel> getCurrentWeather(@Query("lat") Double lat,
                                                @Query("lon") Double lon,
                                                @Query("lang") String lang,
                                                @Query("units") String units,
                                                @Query("appid") String authorization);

    @GET("forecast/daily")
    Call<DailyWeatherModel> getDailyWeather(@Query("lat") Double lat,
                                            @Query("lon") Double lon,
                                            @Query("lang") String lang,
                                            @Query("units") String units,
                                            @Query("appid") String authorization);

    @GET("find")
    Call<FindCity> getFindCity(@Query("q") String city,
                               @Query("lang") String lang,
                               @Query("units") String units,
                               @Query("appid") String authorization);

}
