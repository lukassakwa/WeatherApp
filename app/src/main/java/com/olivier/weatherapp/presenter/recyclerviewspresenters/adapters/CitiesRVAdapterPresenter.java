package com.olivier.weatherapp.presenter.recyclerviewspresenters.adapters;

import android.util.Log;
import com.olivier.weatherapp.api.ClientApi;
import com.olivier.weatherapp.api.WeatherRestRepository;
import com.olivier.weatherapp.model.WeatherModel;
import com.olivier.weatherapp.model.weathermodels.current.CurrentWeatherModel;
import com.olivier.weatherapp.presenter.BasePresenter;
import com.olivier.weatherapp.presenter.contract.Contract;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CitiesRVAdapterPresenter extends BasePresenter<Contract.CityRVAdapterView> implements Contract.CityRVAdapterPresenter{

    private final WeatherModel weatherModel;

    public CitiesRVAdapterPresenter(WeatherModel weatherModel) {
        this.weatherModel = weatherModel;
    }

    //getting Current Weather from Rest Api openWeather
    @Override
    public void getCurrentWeather(){
        WeatherRestRepository weatherRestRepository = ClientApi.getRetrofit(weatherModel).create(WeatherRestRepository.class);

        Call<CurrentWeatherModel> oneCall = weatherRestRepository.getCurrentWeather(weatherModel.getLat(),
                weatherModel.getLon(),
                weatherModel.getUnits(),
                weatherModel.getAuthorization());

        oneCall.enqueue(new Callback<CurrentWeatherModel>() {

            @Override
            public void onResponse(Call<CurrentWeatherModel> call, Response<CurrentWeatherModel> response) {
                CurrentWeatherModel currentWeatherModel = response.body();

                //set recycler view with data from rest api
                view.showName(currentWeatherModel.getName());
                view.showDescription(currentWeatherModel.getWeather().get(0).getDescription());
                view.showTemp((int) currentWeatherModel.getMain().getTemp());
            }

            @Override
            public void onFailure(Call<CurrentWeatherModel> call, Throwable t) {
                Log.d("GSON_EXCEPTION", t.toString());
            }
        });
    }

    //getActual Weather
    public WeatherModel getWeatherModel() {
        return weatherModel;
    }
}
