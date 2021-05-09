package com.olivier.weatherapp.presenter.activitypresenters;

import android.util.Log;
import com.olivier.weatherapp.api.ClientApi;
import com.olivier.weatherapp.api.WeatherRestRepository;
import com.olivier.weatherapp.model.WeatherModel;
import com.olivier.weatherapp.model.weathermodels.find.FindCity;
import com.olivier.weatherapp.presenter.BasePresenter;
import com.olivier.weatherapp.presenter.contract.SearchActivityContract;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        Call<FindCity> findCityCall = findCity.getFindCity(weatherModel.getCity(),
                weatherModel.getUnits(),
                weatherModel.getAuthorization());

        findCityCall.enqueue(new Callback<FindCity>() {
            @Override
            public void onResponse(Call<FindCity> call, Response<FindCity> response) {
                FindCity findCityWeather = response.body();

                //TODO:: get list of city and throw this list to user
                Double lat = findCityWeather.getList().get(0).getCoord().getLat();
                Double lon = findCityWeather.getList().get(0).getCoord().getLon();

                WeatherModel cityWeatherModel = new WeatherModel(lon, lat);

                addWeather(cityWeatherModel);
                exit();
            }

            @Override
            public void onFailure(Call<FindCity> call, Throwable t) {
                Log.d("GSON_EXCEPTION", t.toString());
            }
        });
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
