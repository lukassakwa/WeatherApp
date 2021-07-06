package com.olivier.weatherapp.api;

import com.olivier.weatherapp.model.weathermodels.current.CurrentWeatherModel;
import com.olivier.weatherapp.model.weathermodels.daily.DailyWeatherModel;
import com.olivier.weatherapp.model.weathermodels.find.FindCity;
import com.olivier.weatherapp.model.weathermodels.onecall.HourlyWeatherModel;
import retrofit2.http.GET;
import retrofit2.http.Query;
import io.reactivex.rxjava3.core.Observable;

public interface WeatherRestRepository {
    @GET("onecall")
    Observable<HourlyWeatherModel> getHourlyWeather(@Query("lat") Double lat,
                                                              @Query("lon") Double lon,
                                                              @Query("exclude") String exclude,
                                                              @Query("lang") String lang,
                                                              @Query("units") String units,
                                                              @Query("appid") String authorization);

    @GET("weather")
    Observable<CurrentWeatherModel> getCurrentWeather(@Query("lat") Double lat,
                                                @Query("lon") Double lon,
                                                @Query("lang") String lang,
                                                @Query("units") String units,
                                                @Query("appid") String authorization);

    @GET("forecast/daily")
    Observable<DailyWeatherModel> getDailyWeather(@Query("lat") Double lat,
                                            @Query("lon") Double lon,
                                            @Query("lang") String lang,
                                            @Query("units") String units,
                                            @Query("appid") String authorization);

    @GET("find")
    Observable<FindCity> getFindCity(@Query("q") String city,
                               @Query("lang") String lang,
                               @Query("units") String units,
                               @Query("appid") String authorization);

}
