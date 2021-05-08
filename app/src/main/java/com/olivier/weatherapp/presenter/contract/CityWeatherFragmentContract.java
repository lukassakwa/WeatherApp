package com.olivier.weatherapp.presenter.contract;

import com.olivier.weatherapp.model.CurrentWeather;
import com.olivier.weatherapp.model.DailyWeather;
import com.olivier.weatherapp.model.HourlyWeather;

import java.util.ArrayList;

public interface CityWeatherFragmentContract {

    interface View{
        void showHourlyWeather(ArrayList<HourlyWeather> hourlyWeathers);
        void showDailyWeather(ArrayList<DailyWeather> dailyWeathers);
        void showCurrentWeather(CurrentWeather currentWeather);
    }

    interface Presenter{
        void getWeather();
    }

}
