package com.olivier.weatherapp.presenter.activitypresenters;

import com.olivier.weatherapp.model.WeatherModel;
import com.olivier.weatherapp.presenter.BasePresenter;
import com.olivier.weatherapp.presenter.contract.Contract;

import java.util.ArrayList;

public class SearchActivityPresenter extends BasePresenter<Contract.SearchActivityView> implements Contract.SearchActivityPresenter{

    private ArrayList<WeatherModel> weatherModels;

    public SearchActivityPresenter(ArrayList<WeatherModel> weatherModels) {
        this.weatherModels = weatherModels;
    }

    @Override
    public void addLocationWeather(WeatherModel weatherModel){
        boolean cityExsist = false;

        for(WeatherModel cityWeather : weatherModels){
            if(cityWeather.getCity().equals("current")){
                cityExsist = true;
                break;
            }
        }

        if(cityExsist == true){
            weatherModels.set(0, weatherModel);
        }else{
            weatherModels.add(0, weatherModel);
        }
    }

    @Override
    public void addWeather(WeatherModel weatherModel, String name) {
        boolean cityExsist = false;

        for(WeatherModel cityWeather : weatherModels){
            if(cityWeather.getCity().equals(name)){
                cityExsist = true;
                break;
            }
        }

        if(cityExsist == false){
            weatherModels.add(weatherModel);
        }

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
