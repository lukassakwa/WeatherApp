package com.olivier.weatherapp.presenter.contract;

import com.olivier.weatherapp.model.WeatherModel;
import com.olivier.weatherapp.presenter.recyclerviewspresenters.CitiesRVPresenter;

import java.util.ArrayList;

public interface CitiesActivityContract {

    interface View{
        void onExitActivity(ArrayList<WeatherModel> weatherModels);
        void initRecyclerView(CitiesRVPresenter citiesRVPresenter);
        void intentSearchActivity(ArrayList<WeatherModel> weatherModels);
    }

    interface Presenter{

    }

}
