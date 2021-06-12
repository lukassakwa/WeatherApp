package com.olivier.weatherapp.presenter.contract;

import android.os.Bundle;
import com.olivier.weatherapp.model.CurrentWeather;
import com.olivier.weatherapp.model.DailyWeather;
import com.olivier.weatherapp.model.HourlyWeather;

import java.util.ArrayList;

public interface LocationWeatherFragmentContract {

    interface View{
        void showHourlyWeather(ArrayList<HourlyWeather> hourlyWeathers);
        void showDailyWeather(ArrayList<DailyWeather> dailyWeathers);
        void showCurrentWeather(CurrentWeather currentWeather);

        void resultDataToParent(Bundle result);
        void turnOffSwipeOnRefresh();
    }

    interface Presenter{
        void getWeather();
        void updateWeather();
    }

}
