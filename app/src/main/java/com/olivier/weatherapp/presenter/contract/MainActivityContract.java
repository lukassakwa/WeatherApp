package com.olivier.weatherapp.presenter.contract;

import com.olivier.weatherapp.model.WeatherModel;

import java.util.ArrayList;

public interface MainActivityContract {

    interface View{
        void cityIntent(ArrayList<WeatherModel> weatherModels);
        void SetViewPager(ArrayList<WeatherModel> weatherModels);
        void getPreferences();
        void setPreferences(ArrayList<WeatherModel> weatherModels);
    }

    interface Presenter{

    }

}
