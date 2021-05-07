package com.olivier.weatherapp.presenter.activitypresenters;

import com.olivier.weatherapp.model.WeatherModel;
import com.olivier.weatherapp.presenter.BasePresenter;
import com.olivier.weatherapp.presenter.contract.ContractMVP;

import java.util.ArrayList;

public class SearchActivityPresenter extends BasePresenter<ContractMVP.SearchActivityView> implements ContractMVP.SearchActivityPresenter{

    private ArrayList<WeatherModel> weatherModels;

    public SearchActivityPresenter(ArrayList<WeatherModel> weatherModels) {
        this.weatherModels = weatherModels;
    }

    @Override
    public void addLocationWeather(WeatherModel weatherModel){
        weatherModels.add(0, weatherModel);
    }

    @Override
    public void addWeather(WeatherModel weatherModel) {
        weatherModels.add(weatherModel);
    }

    @Override
    public void exit() {
        saveArrayToPreferences();
        view.exitActivity();
    }

    @Override
    public void saveArrayToPreferences(){
        view.setPreferences(weatherModels);
    }

}
