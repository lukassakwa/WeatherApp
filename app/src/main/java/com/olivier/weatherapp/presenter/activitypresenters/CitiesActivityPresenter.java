package com.olivier.weatherapp.presenter.activitypresenters;

import com.olivier.weatherapp.model.WeatherModel;
import com.olivier.weatherapp.presenter.BasePresenter;
import com.olivier.weatherapp.presenter.contract.Contract;
import com.olivier.weatherapp.presenter.recyclerviewspresenters.CitiesRVPresenter;
import com.olivier.weatherapp.presenter.recyclerviewspresenters.adapters.CitiesRVAdapterPresenter;

import java.util.ArrayList;

public class CitiesActivityPresenter extends BasePresenter<Contract.CitiesActivityView> implements Contract.CitiesActivityPresenter {

    //Recycler View Presenter which is in city view presenter
    private CitiesRVPresenter citiesRVPresenter;

    private ArrayList<WeatherModel> weatherModels;

    public CitiesActivityPresenter( ArrayList<WeatherModel> weatherModels) {
        this.weatherModels = weatherModels;
        citiesRVPresenter = initCitiesRVPresenter();
    }

    //give weather list to recycler view and recycler view will do something with it
    public void getInitRecyclerView(){
        view.initRecyclerView(citiesRVPresenter);
    }

    //get back weather list and exit city activity
    public void exitCityActivity(){
        weatherModels = citiesRVPresenter.getWeatherModels();
        view.onExitActivity(weatherModels);
    }

    public void getIntentSearchActivity(){
        weatherModels = citiesRVPresenter.getWeatherModels();
        view.intentSearchActivity(weatherModels);
    }

    //init adapters array and passing to recycler view objject
    private CitiesRVPresenter initCitiesRVPresenter(){
        ArrayList<CitiesRVAdapterPresenter> citiesRVAdapterPresenters = new ArrayList<>();

        for(WeatherModel weatherModel : weatherModels){
            citiesRVAdapterPresenters.add(new CitiesRVAdapterPresenter(weatherModel));
        }

        return new CitiesRVPresenter(citiesRVAdapterPresenters);
    }

    public void setCitiesRVPresenter(CitiesRVPresenter citiesRVPresenter) {
        this.citiesRVPresenter = citiesRVPresenter;
    }
}
