package com.olivier.weatherapp.presenter;

import android.util.Log;
import com.olivier.weatherapp.model.CurrentWeather;
import com.olivier.weatherapp.model.FutureWeather;
import com.olivier.weatherapp.model.WeatherHttpModel;
import com.olivier.weatherapp.model.weathermodels.onecall.Current;
import com.olivier.weatherapp.model.weathermodels.onecall.DailyItem;
import com.olivier.weatherapp.model.weathermodels.onecall.HourlyItem;
import com.olivier.weatherapp.model.weathermodels.onecall.WeatherModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.ArrayList;
import java.util.List;

public class WeatherController extends BasePresenter<ContractMVP.WeatherView> implements ContractMVP.WeatherPresenter{

    private WeatherHttpModel weatherHttpModel;

    public WeatherController(WeatherHttpModel weatherHttpModel) {
        this.weatherHttpModel = weatherHttpModel;
    }

    @Override
    public void getWeather(WeatherHttpModel weatherHttpModel) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(weatherHttpModel.getHttpUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        WeatherRestRepository weatherRestRepository = retrofit.create(WeatherRestRepository.class);

        Call<WeatherModel> oneCall = weatherRestRepository.getWeather(weatherHttpModel.getLat(),
                weatherHttpModel.getLon(),
                weatherHttpModel.getExcludes(),
                weatherHttpModel.getUnits(),
                weatherHttpModel.getAuthorization());

        oneCall.enqueue(new Callback<WeatherModel>() {

            private ArrayList<FutureWeather> hourlyWeather;
            private ArrayList<FutureWeather> dailyWeather;
            private CurrentWeather currentWeather;

            @Override
            public void onResponse(Call<WeatherModel> call, Response<WeatherModel> response) {

                if(response.isSuccessful()){

                    WeatherModel weatherModel = response.body();

                    //TODO:: current weather Init to repair
                    this.currentWeather = currentWeatherInit(weatherModel.getCurrent());
                    this.dailyWeather = dailyWeatherInit(weatherModel.getDaily());
                    this.hourlyWeather = hourlyWeatherInit(weatherModel.getHourly());

                    view.showWeather(currentWeather, hourlyWeather, dailyWeather);
                }
            }

            @Override
            public void onFailure(Call<WeatherModel> call, Throwable t) {
                Log.d("GSON_EXCEPTION", t.toString());
            }

        });
    }

    //Initializing current model object
    private CurrentWeather currentWeatherInit(Current current){
        CurrentWeather currentWeather = new CurrentWeather();
        //Reading from Json Pojo
        currentWeather.setTemp(current.getTemp());
        currentWeather.setDescription(current.getWeather().get(0).getDescription());
        currentWeather.setFeels_temp(current.getFeelsLike());
        currentWeather.setVisibility(current.getVisibility());
        currentWeather.setPressure(current.getPressure());
        currentWeather.setSpeed(current.getWindSpeed());
        //windDirection
        currentWeather.setDegree(current.getWindDeg());
        //uvAlert
        currentWeather.setUv(current.getUvi());
        currentWeather.setHumidity(current.getHumidity());

        return currentWeather;
    }

    //Initializing Future Weather model for day
    private ArrayList<FutureWeather> dailyWeatherInit(List<DailyItem> dailyItem){
        ArrayList<FutureWeather> futureWeathers = new ArrayList<>();

        //DailyInit
        for(int i = 0; i < dailyItem.size()-1; i++){
            //Reading from Json Pojo
            FutureWeather futureWeather = new FutureWeather();
            futureWeather.setTemp(dailyItem.get(i).getTemp().getDay());
            futureWeather.setDescription(dailyItem.get(i).getWeather().get(0).getDescription());
            futureWeather.setIcon(dailyItem.get(i).getWeather().get(0).getIcon());
            futureWeather.setDt(dailyItem.get(i).getDt());
            futureWeathers.add(futureWeather);
        }

        return futureWeathers;
    }

    //Initializing Future Weather model for hour
    private ArrayList<FutureWeather> hourlyWeatherInit(List<HourlyItem> hourlyItem){
        ArrayList<FutureWeather> hourlyWeathers = new ArrayList<>();

        //HourlyInit
        for(int i = 1; i <= 24; i++){
            //Reading from Json Pojo
            FutureWeather hourlyWeather = new FutureWeather();
            hourlyWeather.setTemp(hourlyItem.get(i).getTemp());
            hourlyWeather.setDescription(hourlyItem.get(i).getWeather().get(0).getDescription());
            hourlyWeather.setIcon(hourlyItem.get(i).getWeather().get(0).getIcon());
            hourlyWeather.setDt(hourlyItem.get(i).getDt());
            hourlyWeathers.add(hourlyWeather);
        }

        return hourlyWeathers;
    }
}
