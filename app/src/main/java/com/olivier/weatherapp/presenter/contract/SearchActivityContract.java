package com.olivier.weatherapp.presenter.contract;

import com.olivier.weatherapp.model.WeatherModel;

import java.util.ArrayList;

public interface SearchActivityContract {

    interface View{
        void exitActivity();
        void setPreferences(ArrayList<WeatherModel> weatherModels);
    }

    interface Presenter{
        void addWeather(WeatherModel weatherModel, String cityName);
        void addLocationWeather(WeatherModel weatherModel);
        void saveArrayToPreferences();
        void exit();
    }
}
