package com.olivier.weatherapp.presenter;

import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import androidx.fragment.app.FragmentActivity;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class WeatherController extends BasePresenter<ContractMVP.WeatherView> implements ContractMVP.WeatherPresenter{

    private WeatherHttpModel weatherHttpModel;

    private CurrentWeather currentWeather;
    private ArrayList<FutureWeather> hourlyWeather;
    private ArrayList<FutureWeather> dailyWeather;

    public WeatherController(WeatherHttpModel weatherHttpModel) {
        this.weatherHttpModel = weatherHttpModel;
    }

    //Getting name of City and country from lat and lon
    @Override
    public void getLocationName(FragmentActivity fragmentActivity){
        Geocoder geocoder = new Geocoder(fragmentActivity, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(weatherHttpModel.getLat(), weatherHttpModel.getLon(), 1);

            view.showName(addresses.get(0).getCountryName() + "/" + addresses.get(0).getLocality());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getWeather() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(weatherHttpModel.getHttpUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ContractMVP.WeatherRestRepository weatherRestRepository = retrofit.create(ContractMVP.WeatherRestRepository.class);

        Call<WeatherModel> oneCall = weatherRestRepository.getWeather(weatherHttpModel.getLat(),
                weatherHttpModel.getLon(),
                weatherHttpModel.getExcludes(),
                weatherHttpModel.getUnits(),
                weatherHttpModel.getAuthorization());

        oneCall.enqueue(new Callback<WeatherModel>() {

            @Override
            public void onResponse(Call<WeatherModel> call, Response<WeatherModel> response) {

                if(response.isSuccessful()){

                    WeatherModel weatherModel = response.body();

                    currentWeather = currentWeatherInit(weatherModel.getCurrent());
                    dailyWeather = dailyWeatherInit(weatherModel.getDaily());
                    hourlyWeather = hourlyWeatherInit(weatherModel.getHourly());

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
        currentWeather.setDegree(windDirection(current.getWindDeg()));
        //uvAlert
        currentWeather.setUv(uvAlert(current.getUvi()));
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

    //Getting Wind direction
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

    //Getting UV alert
    private String uvAlert(double uv){
        if(uv <= 2.0)
            return "no risk";
        if(uv <= 5.0)
            return "Medium risk";
        if(uv <= 7.0)
            return "High risk";
        return "Very high risk";
    }
}
