package com.olivier.weatherapp.presenter.recyclerviewspresenters;

import com.olivier.weatherapp.model.WeatherModel;
import com.olivier.weatherapp.presenter.BasePresenter;
import com.olivier.weatherapp.presenter.contract.CityRecyclerViewContract;
import com.olivier.weatherapp.presenter.recyclerviewspresenters.adapters.CitiesRVAdapterPresenter;
import com.olivier.weatherapp.view.recyclerviews.CitiesAdapter;

import java.util.ArrayList;

public class CitiesRVPresenter extends BasePresenter<CityRecyclerViewContract.View> implements CityRecyclerViewContract.Presenter {

    //Recycler View Adapter Presenter which is in city view presenter
    private final ArrayList<CitiesRVAdapterPresenter> citiesRVAdapterPresenters;

    public CitiesRVPresenter(ArrayList<CitiesRVAdapterPresenter> citiesRVAdapterPresenters) {
        this.citiesRVAdapterPresenters = citiesRVAdapterPresenters;
    }

    public void getLocationWeaher(CitiesAdapter.ViewHolder holder, int position){
        citiesRVAdapterPresenters.get(position).attach(holder);
        citiesRVAdapterPresenters.get(position).getCurrentWeather();
    }

    @Override
    public void updateView(int position) {
        citiesRVAdapterPresenters.remove(position);
        view.updateView(position);
    }

    public ArrayList<CitiesRVAdapterPresenter> getCitiesRVAdapterPresenters() {
        return citiesRVAdapterPresenters;
    }


    //getting actual size and value of weather array
    public ArrayList<WeatherModel> getWeatherModels(){
        ArrayList<WeatherModel> tempWeatherModels = new ArrayList<>();

        for(CitiesRVAdapterPresenter citiesRVPresenter : citiesRVAdapterPresenters)
            tempWeatherModels.add(citiesRVPresenter.getWeatherModel());

        return tempWeatherModels;
    }

    public int getSize(){
        return citiesRVAdapterPresenters.size();
    }
}
