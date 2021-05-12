package com.olivier.weatherapp.api;

import com.olivier.weatherapp.model.WeatherModel;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

public class ClientApi {

    private static Retrofit retrofit;
    private static OkHttpClient okHttpClient;

    private static int REQUEST_TIMEOUT = 60;

    public static Retrofit getRetrofit(WeatherModel weatherModel){
        if(okHttpClient == null){
            initOkHttpClient();
        }

        if(retrofit == null){
            return new Retrofit.Builder()
                    .baseUrl(weatherModel.getHttpUrl())
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    private static void initOkHttpClient(){
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder()
                .connectTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS);

        okHttpClient = okHttpClientBuilder.build();
    }
}
