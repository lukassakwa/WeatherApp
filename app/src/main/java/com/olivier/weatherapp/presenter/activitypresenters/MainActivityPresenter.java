package com.olivier.weatherapp.presenter.activitypresenters;

import com.olivier.weatherapp.model.WeatherModel;
import com.olivier.weatherapp.presenter.BasePresenter;
import com.olivier.weatherapp.presenter.contract.Contract;

import java.util.ArrayList;

public class MainActivityPresenter extends BasePresenter<Contract.MainActivityView> implements Contract.MainActivityPresenter{

    private ArrayList<WeatherModel> cityLocationArray;

    public MainActivityPresenter() {
        cityLocationArray = new ArrayList<>();
    }

    //TODO:: operation on list from main activity

    public void getViewPager(){
        view.SetViewPager(cityLocationArray);
    }

    public void saveArrayToPreferences(){
        view.setPreferences(cityLocationArray);
    }

    public void getArrayFromPreferences(){
        view.getPreferences();
    }

    public void getIntentCity(){
        view.cityIntent(cityLocationArray);
    }

    public ArrayList<WeatherModel> getCityLocationArray() {
        return cityLocationArray;
    }

    public void setCityLocationArray(ArrayList<WeatherModel> cityLocationArray) {
        this.cityLocationArray = cityLocationArray;
    }

    public void setLocationWeather(WeatherModel locationWeather) {
        cityLocationArray.set(0, locationWeather);
    }
}
