package com.olivier.weatherapp.api;

import com.olivier.weatherapp.model.WeatherModel;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ClientApi {

    private static Retrofit retrofit;

    public static Retrofit getRetrofit(WeatherModel weatherModel){
        if(retrofit == null){
            return new Retrofit.Builder()
                    .baseUrl(weatherModel.getHttpUrl())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
