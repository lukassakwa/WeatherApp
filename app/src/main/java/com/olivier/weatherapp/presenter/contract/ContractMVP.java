package com.olivier.weatherapp.presenter.contract;

import com.olivier.weatherapp.model.CurrentWeather;
import com.olivier.weatherapp.model.FutureWeather;
import com.olivier.weatherapp.model.WeatherModel;
import com.olivier.weatherapp.presenter.recyclerviewspresenters.CitiesRVPresenter;

import java.util.ArrayList;

public interface ContractMVP {

    /**
     * Represents the View (Activity, Fragment, View subclass) in MVP
     */

    interface MainActivityView{
        void cityIntent(ArrayList<WeatherModel> weatherModels);
        void SetViewPager(ArrayList<WeatherModel> weatherModels);
        void getPreferences();
        void setPreferences(ArrayList<WeatherModel> weatherModels);
    }

    interface CitiesActivityView{
        void onExitActivity(ArrayList<WeatherModel> weatherModels);
        void initRecyclerView(CitiesRVPresenter citiesRVPresenter);
        void intentSearchActivity(ArrayList<WeatherModel> weatherModels);
    }

    interface SearchActivityView{
        void exitActivity();
        void setPreferences(ArrayList<WeatherModel> weatherModels);
    }

    interface LocationWeatherFragmentView{
        void showWeather(ArrayList<FutureWeather> hourlyWeathers, ArrayList<FutureWeather> dailyWeathers);
        void showCurrentWeather(CurrentWeather currentWeather);
    }

    interface CityWeatherFragmentView{
        void showWeather(ArrayList<FutureWeather> hourlyWeathers, ArrayList<FutureWeather> dailyWeathers);
        void showCurrentWeather(CurrentWeather currentWeather);
    }

    interface CityRVView {
        void updateView(int position);
    }

    interface CityRVAdapterView{
        void showTemp(int temp);
        void showName(String name);
        void showDescription(String description);
    }

    /**
     * Represents the Presenter in MVP
     */
    interface MainActivityPresenter{

    }

    interface CitiesActivityPresenter{

    }

    interface SearchActivityPresenter{
        void addWeather(WeatherModel weatherModel);
        void addLocationWeather(WeatherModel weatherModel);
        void saveArrayToPreferences();
        void exit();
    }

    interface WeatherFragmentPresenter{
        void getWeather();
    }

    interface CityRVPresenter {
        void updateView(int position);
    }

    interface CityRVAdapterPresenter{
        void getCurrentWeather();
    }
}
