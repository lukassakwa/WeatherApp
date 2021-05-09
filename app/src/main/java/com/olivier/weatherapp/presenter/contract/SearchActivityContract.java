package com.olivier.weatherapp.presenter.contract;

import com.olivier.weatherapp.model.WeatherModel;

import java.util.ArrayList;

public interface SearchActivityContract {

    interface View{
        void exitActivity();
        void setPreferences(ArrayList<WeatherModel> weatherModels);
        void listOfCities();
    }

    interface Presenter{
        void findCity(String city);
        void addWeather(WeatherModel weatherModel);
        void addLocationWeather(WeatherModel weatherModel);
        void saveArrayToPreferences();
        void exit();
    }
}