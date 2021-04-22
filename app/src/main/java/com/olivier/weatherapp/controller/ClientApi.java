package com.olivier.weatherapp.controller;

import com.olivier.weatherapp.model.HttpModel;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ClientApi {
    private static Retrofit retrofit;

    public static Retrofit getClient(HttpModel httpModel) {
        if(retrofit == null){
            return retrofit = new Retrofit.Builder()
                    .baseUrl(httpModel.getHttpUrl())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
