package com.olivier.weatherapp.presenter.activitypresenters;

import com.olivier.weatherapp.api.ClientApi;
import com.olivier.weatherapp.api.WeatherRestRepository;
import com.olivier.weatherapp.model.WeatherModel;
import com.olivier.weatherapp.model.weathermodels.find.FindCity;
import com.olivier.weatherapp.presenter.BasePresenter;
import com.olivier.weatherapp.presenter.contract.SearchActivityContract;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.util.ArrayList;

public class SearchActivityPresenter extends BasePresenter<SearchActivityContract.View> implements SearchActivityContract.Presenter{

    private ArrayList<WeatherModel> weatherModels;

    public SearchActivityPresenter(ArrayList<WeatherModel> weatherModels) {
        this.weatherModels = weatherModels;
    }

    @Override
    public void addLocationWeather(WeatherModel weatherModel){

        if(!weatherModels.isEmpty()){
            WeatherModel cityWeather = weatherModels.get(0);

            if(cityWeather.getCity().equals("current")){
                weatherModels.set(0, weatherModel);
            }else{
                weatherModels.add(0, weatherModel);
            }

        }else{
            weatherModels.add(0, weatherModel);
        }
    }

    @Override
    public void findCity(String city) {
        WeatherModel weatherModel = new WeatherModel(city);

        WeatherRestRepository findCity = ClientApi.getRetrofit(weatherModel).create(WeatherRestRepository.class);
        Observable<FindCity> findCityCall = findCity.getFindCity(weatherModel.getCity(),
                "en",
                weatherModel.getUnits(),
                weatherModel.getAuthorization());

        findCityCall.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::findCitySucessHandler);
    }

    private void findCitySucessHandler(FindCity findCityWeather){
        //TODO:: get list of city and throw this list to user
        Double lat = findCityWeather.getList().get(0).getCoord().getLat();
        Double lon = findCityWeather.getList().get(0).getCoord().getLon();

        WeatherModel cityWeatherModel = new WeatherModel(lon, lat);

        addWeather(cityWeatherModel);
        exit();
    }

    @Override
    public void addWeather(WeatherModel weatherModel) {
        final double THRESHOLD = .0001;
        boolean cityExsist = false;

        //check if element exist in list
        for(WeatherModel cityWeather : weatherModels){
            if(Math.abs(cityWeather.getLat() - weatherModel.getLat()) < THRESHOLD && Math.abs(cityWeather.getLon() - weatherModel.getLon()) < THRESHOLD){
                cityExsist = true;
                break;
            }
        }

        //if element doesnt exist add him to list
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
