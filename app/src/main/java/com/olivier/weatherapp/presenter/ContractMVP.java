package com.olivier.weatherapp.presenter;

import com.olivier.weatherapp.model.CurrentWeather;
import com.olivier.weatherapp.model.FutureWeather;
import com.olivier.weatherapp.model.WeatherHttpModel;

import java.util.ArrayList;

public interface ContractMVP {

    /**
     * Represents the View (Activity, Fragment, View subclass) in MVP
     */
    interface WeatherView{
        void showWeather(CurrentWeather currentWeather, ArrayList<FutureWeather> hourlyWeather, ArrayList<FutureWeather> dailyWeather);
    }

    /**
     * Represents the Presenter in MVP
     */
    interface WeatherPresenter{
        void getWeather(WeatherHttpModel weatherHttpModel);
    }
    
}
