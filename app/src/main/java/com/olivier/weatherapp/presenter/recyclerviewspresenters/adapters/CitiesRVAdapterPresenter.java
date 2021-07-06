package com.olivier.weatherapp.presenter.recyclerviewspresenters.adapters;

import com.olivier.weatherapp.api.ClientApi;
import com.olivier.weatherapp.api.WeatherRestRepository;
import com.olivier.weatherapp.model.WeatherModel;
import com.olivier.weatherapp.model.weathermodels.current.CurrentWeatherModel;
import com.olivier.weatherapp.presenter.BasePresenter;
import com.olivier.weatherapp.presenter.contract.CityRecyclerViewAdapterContract;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CitiesRVAdapterPresenter extends BasePresenter<CityRecyclerViewAdapterContract.View> implements CityRecyclerViewAdapterContract.Presenter{

    private final WeatherModel weatherModel;

    public CitiesRVAdapterPresenter(WeatherModel weatherModel) {
        this.weatherModel = weatherModel;
    }

    //getting Current Weather from Rest Api openWeather
    @Override
    public void getCurrentWeather(){
        WeatherRestRepository weatherRestRepository = ClientApi.getRetrofit(weatherModel).create(WeatherRestRepository.class);

        Observable<CurrentWeatherModel> currentWeatherModelCall = weatherRestRepository.getCurrentWeather(weatherModel.getLat(),
                weatherModel.getLon(),
                "en",
                weatherModel.getUnits(),
                weatherModel.getAuthorization());

        currentWeatherModelCall.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::currentWeatherSuccesHandle);
    }

    private void currentWeatherSuccesHandle(CurrentWeatherModel currentWeatherModel){
        view.showName(currentWeatherModel.getName());
        view.showDescription(currentWeatherModel.getWeather().get(0).getDescription());
        view.showTemp((int) currentWeatherModel.getMain().getTemp());
    }

    //getActual Weather
    public WeatherModel getWeatherModel() {
        return weatherModel;
    }
}
