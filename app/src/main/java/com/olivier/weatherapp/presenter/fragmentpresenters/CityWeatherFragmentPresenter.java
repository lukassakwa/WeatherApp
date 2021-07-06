package com.olivier.weatherapp.presenter.fragmentpresenters;

import com.olivier.weatherapp.api.ClientApi;
import com.olivier.weatherapp.api.WeatherRestRepository;
import com.olivier.weatherapp.model.CurrentWeather;
import com.olivier.weatherapp.model.DailyWeather;
import com.olivier.weatherapp.model.HourlyWeather;
import com.olivier.weatherapp.model.WeatherModel;
import com.olivier.weatherapp.model.weathermodels.current.CurrentWeatherModel;
import com.olivier.weatherapp.model.weathermodels.daily.DailyWeatherModel;
import com.olivier.weatherapp.model.weathermodels.daily.ListItem;
import com.olivier.weatherapp.model.weathermodels.onecall.HourlyItem;
import com.olivier.weatherapp.model.weathermodels.onecall.HourlyWeatherModel;
import com.olivier.weatherapp.presenter.BasePresenter;
import com.olivier.weatherapp.presenter.contract.CityWeatherFragmentContract;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.List;

public class CityWeatherFragmentPresenter extends BasePresenter<CityWeatherFragmentContract.View> implements CityWeatherFragmentContract.Presenter {

    private WeatherModel _weatherModel;

    private CurrentWeather _currentWeather;
    private ArrayList<HourlyWeather> _hourlyWeather;
    private ArrayList<DailyWeather> _dailyWeather;

    public CityWeatherFragmentPresenter(WeatherModel weatherModel) {
        this._weatherModel = weatherModel;
    }

    @Override
    public void getWeather() {
        WeatherRestRepository weatherRestRepository = ClientApi.getRetrofit(_weatherModel).create(WeatherRestRepository.class);

        Observable<CurrentWeatherModel> currentWeatherModelCall = weatherRestRepository.getCurrentWeather(_weatherModel.getLat(),
                _weatherModel.getLon(),
                "en",
                _weatherModel.getUnits(),
                _weatherModel.getAuthorization());

        Observable<HourlyWeatherModel> hourlyWeatherModelCall = weatherRestRepository.getHourlyWeather(_weatherModel.getLat(),
                _weatherModel.getLon(),
                _weatherModel.getExcludes(),
                "en",
                _weatherModel.getUnits(),
                _weatherModel.getAuthorization());

        Observable<DailyWeatherModel> dailyWeatherModelCall = weatherRestRepository.getDailyWeather(_weatherModel.getLat(),
                _weatherModel.getLon(),
                "en",
                _weatherModel.getUnits(),
                _weatherModel.getAuthorization());

        currentWeatherModelCall.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::currentWeatherSuccesHandle);

        hourlyWeatherModelCall.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::hourlyWeatherSuccesHandle);

        dailyWeatherModelCall.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::dailyWeatherSuccesHandle);

    }

    @Override
    public void getWeatherUpdate() {
        getWeather();
        view.viewUpdate();
    }

    private void currentWeatherSuccesHandle(CurrentWeatherModel currentWeatherModel){
        _currentWeather = currentWeatherInit(currentWeatherModel);
        view.showCurrentWeather(_currentWeather);
    }

    private void hourlyWeatherSuccesHandle(HourlyWeatherModel hourlyWeatherModel){
        _hourlyWeather = hourlyWeatherInit(hourlyWeatherModel.getHourly());
        view.showHourlyWeather(_hourlyWeather);
    }

    private void dailyWeatherSuccesHandle(DailyWeatherModel dailyWeatherModel){
        _dailyWeather = dailyWeatherInit(dailyWeatherModel.getList());
        view.showDailyWeather(_dailyWeather);
    }

    //Initializing current and future model object
    private CurrentWeather currentWeatherInit(CurrentWeatherModel current){
        CurrentWeather currentWeather = new CurrentWeather();
        //Reading from Json Pojo
        currentWeather.setName(current.getName());
        currentWeather.setTemp(current.getMain().getTemp());
        currentWeather.setDescription(current.getWeather().get(0).getDescription());
        currentWeather.setFeels_temp(current.getMain().getFeelsLike());
        currentWeather.setVisibility(current.getVisibility());
        currentWeather.setPressure(current.getMain().getPressure());
        currentWeather.setSpeed(current.getWind().getSpeed());
        //windDirection
        currentWeather.setDegree(windDirection(current.getWind().getDeg()));
        currentWeather.setHumidity(current.getMain().getHumidity());

        return currentWeather;
    }

    //Initializing hour Weather model for day
    private ArrayList<DailyWeather> dailyWeatherInit(List<ListItem> dailyItems){
        ArrayList<DailyWeather> futureWeathers = new ArrayList<>();

        //DailyInit
        for(ListItem dailyItem : dailyItems){
            //Reading from Json Pojo
            DailyWeather futureWeather = new DailyWeather();
            futureWeather.setMinTemp(dailyItem.getTemp().getMin());
            futureWeather.setMaxTemp(dailyItem.getTemp().getMax());
            futureWeather.setDescription(dailyItem.getWeather().get(0).getDescription());
            futureWeather.setIcon(dailyItem.getWeather().get(0).getIcon());
            futureWeather.setDt(dailyItem.getDt());
            futureWeathers.add(futureWeather);
        }

        return futureWeathers;
    }

    //Initializing Future Weather model for hour
    private ArrayList<HourlyWeather> hourlyWeatherInit(List<HourlyItem> hourlyItem){
        ArrayList<HourlyWeather> hourlyWeathers = new ArrayList<>();

        //HourlyInit
        for(int i = 1; i <= 24; i++){
            //Reading from Json Pojo
            HourlyWeather hourlyWeather = new HourlyWeather();
            hourlyWeather.setTemp(hourlyItem.get(i).getTemp());
            hourlyWeather.setDescription(hourlyItem.get(i).getWeather().get(0).getDescription());
            hourlyWeather.setIcon(hourlyItem.get(i).getWeather().get(0).getIcon());
            hourlyWeather.setDt(hourlyItem.get(i).getDt());
            hourlyWeathers.add(hourlyWeather);
        }

        return hourlyWeathers;
    }

    //Parse Wind direction degree to String value
    private String windDirection(int deg){
        if(deg >= 350 || deg <= 10)
            return "N";
        if(deg < 80)
            return "NE";
        if(deg <= 110)
            return "E";
        if(deg < 170)
            return "SE";
        if(deg <= 190)
            return "S";
        if(deg < 260)
            return "SW";
        if(deg <= 280)
            return "W";
        return "NW";
    }

}
