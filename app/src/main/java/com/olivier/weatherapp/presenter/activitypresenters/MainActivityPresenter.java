package com.olivier.weatherapp.presenter.activitypresenters;

import com.olivier.weatherapp.model.WeatherModel;
import com.olivier.weatherapp.presenter.BasePresenter;
import com.olivier.weatherapp.presenter.contract.MainActivityContract;

import java.util.ArrayList;

public class MainActivityPresenter extends BasePresenter<MainActivityContract.View> implements MainActivityContract.Presenter{

    private ArrayList<WeatherModel> cityLocationArray;

    public MainActivityPresenter() {
        cityLocationArray = new ArrayList<>();
    }

    //TODO:: operation on list from main activity

    @Override
    public void getViewPager(){
        view.SetViewPager(cityLocationArray);
    }

    @Override
    public void saveArrayToPreferences(){
        view.setPreferences(cityLocationArray);
    }

    @Override
    public void getArrayFromPreferences(){
        view.getPreferences();
    }

    @Override
    public void getIntentCity(){
        view.cityIntent(cityLocationArray);
    }

    @Override
    public void setLocationWeather(WeatherModel locationWeather) {
        cityLocationArray.set(0, locationWeather);
    }

    public ArrayList<WeatherModel> getCityLocationArray() {
        return cityLocationArray;
    }

    public void setCityLocationArray(ArrayList<WeatherModel> cityLocationArray) {
        this.cityLocationArray = cityLocationArray;
    }

}
