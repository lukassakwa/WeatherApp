package com.olivier.weatherapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.olivier.weatherapp.Repository.WeatherRestRepository;
import com.olivier.weatherapp.model.CurrentWeather;
import com.olivier.weatherapp.model.FutureWeather;
import com.olivier.weatherapp.model.HttpModel;
import com.olivier.weatherapp.model.weathermodels.onecall.WeatherModel;
import com.olivier.weatherapp.view.fragments.DailyWeatherFragment;
import com.olivier.weatherapp.view.fragments.HourWeatherFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private final int LOCATION_PERMISSION_CODE = 1000;

    //Widgets
    private TextView mainTemperatureTextView;
    private TextView weatherDescriptionTextView;
    private TextView feels_tempTextView;
    private TextView pressureTextView;
    private TextView humidityTextView;
    private TextView visibilityTextView;
    private TextView speedTextView;
    private TextView degreeTextView;
    private TextView uvTextView;
    private TextView cityNameTextView;

    //Bundle
    private ArrayList<Bundle> bundle;

    //Location
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LocationRequest locationResult;
    private LocationCallback locationCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bundle = new ArrayList<>();
        bundle.add(new Bundle());
        bundle.add(new Bundle());

        //Init Main Activity Widgets
        InitWidgets();

        HttpModel weather = new HttpModel();
        //location
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        //TODO: Do naprawienia rozpisania itp.
        //Permission check
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_PERMISSION_CODE);
        }

        //Device Location
        locationResult = LocationRequest.create();
        locationResult.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationResult.setInterval(20 * 1000);
        //Function which Set location and send to start activity
        getLocation(weather);

        //Init Refresh
        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getLocation(weather);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    //When user decide to give app permission or not
    //This function handle Request code and do something
    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case LOCATION_PERMISSION_CODE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if(location != null){
                                HttpModel httpModel = new HttpModel();
                                httpModel.setLon(location.getLongitude());
                                httpModel.setLat(location.getLatitude());

                                //Set Activity Widgets
                                SetActivity(httpModel);
                            }
                        }
                    });
                } else {
                    Toast.makeText(this, "Location is needed to get weather", Toast.LENGTH_SHORT).show();
                }
                return;
        }
        // Other 'case' lines to check for other
        // permissions this app might request.
    }

    //Menu 3 kropki w prawym gornym rogu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void InitWidgets() {
        mainTemperatureTextView = findViewById(R.id.mainTemperatureTextView);
        weatherDescriptionTextView = findViewById(R.id.weatherDescriptionTextView);
        feels_tempTextView = findViewById(R.id.feels_tempTextView);
        pressureTextView = findViewById(R.id.pressureTextView);
        humidityTextView = findViewById(R.id.humidityTextView);
        visibilityTextView = findViewById(R.id.visibilityTextView);
        speedTextView = findViewById(R.id.windSpeedTextView);
        uvTextView = findViewById(R.id.uvTextView);
        degreeTextView = findViewById(R.id.windDirectionTextView);
        cityNameTextView = findViewById(R.id.cityTextView);
    }

    public void mainWindowSetWidget(CurrentWeather firstWeatherElement) {
        //Initialize widget from main Window
        mainTemperatureTextView.setText((int) firstWeatherElement.getTemp() + "");
        weatherDescriptionTextView.setText(firstWeatherElement.getDescription());
        cityNameTextView.setText(firstWeatherElement.getName());
        feels_tempTextView.setText((int) firstWeatherElement.getFeels_temp() + "\u2103");
        pressureTextView.setText(firstWeatherElement.getPressure() + "hPa");
        humidityTextView.setText(firstWeatherElement.getHumidity() + "%");
        visibilityTextView.setText(firstWeatherElement.getVisibility() + "km");
        speedTextView.setText((int) firstWeatherElement.getSpeed() + "km/h");
        uvTextView.setText(firstWeatherElement.getUv() + "");
        degreeTextView.setText(firstWeatherElement.getDegree() + " wind");

    }

    @SuppressLint("MissingPermission")
    private void getLocation(HttpModel httpModel){
        //Device location
        mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location == null) {
                    return;
                }
                httpModel.setLat(location.getLatitude());
                httpModel.setLon(location.getLongitude());

                String msg = "Lat: " + httpModel.getLat() + "Lon" + httpModel.getLon();
                Log.d("LOCATION_PARAMETERS", msg);
                SetActivity(httpModel);
            }
        });
    }

    //Getting name of City and country from lat and lon
    private String getLocationName(HttpModel httpModel){
        Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(httpModel.getLat(), httpModel.getLon(), 1);
            return addresses.get(0).getCountryName() + "/" + addresses.get(0).getLocality();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
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

    public void SetActivity(HttpModel httpModel){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(httpModel.getHttpUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        WeatherRestRepository weatherRestRepository = retrofit.create(WeatherRestRepository.class);

        Call<WeatherModel> oneCall = weatherRestRepository.getWeather(httpModel.getLat(),
                                                                        httpModel.getLon(),
                                                                        httpModel.getExcludes(),
                                                                        httpModel.getUnits(),
                                                                        httpModel.getAuthorization());

        oneCall.enqueue(new Callback<WeatherModel>() {

            @Override
            public void onResponse(Call<WeatherModel> call, Response<WeatherModel> response) {

                WeatherModel weatherModel = response.body();

                if(response.isSuccessful()){
                    CurrentWeather currentWeather = new CurrentWeather();
                    ArrayList<FutureWeather> dailyWeathers = new ArrayList<>();
                    ArrayList<FutureWeather> hourlyWeathers = new ArrayList<>();

                    //Reading from Json Pojo
                    currentWeather.setTemp(weatherModel.getCurrent().getTemp());
                    currentWeather.setDescription(weatherModel.getCurrent().getWeather().get(0).getDescription());
                    //getLocation
                    currentWeather.setName(getLocationName(httpModel));
                    currentWeather.setFeels_temp(weatherModel.getCurrent().getFeelsLike());
                    currentWeather.setVisibility(weatherModel.getCurrent().getVisibility());
                    currentWeather.setPressure(weatherModel.getCurrent().getPressure());
                    currentWeather.setSpeed(weatherModel.getCurrent().getWindSpeed());
                    //windDirection
                    currentWeather.setDegree(windDirection(weatherModel.getCurrent().getWindDeg()));
                    //uvAlert
                    currentWeather.setUv(uvAlert(weatherModel.getCurrent().getUvi()));
                    currentWeather.setHumidity(weatherModel.getCurrent().getHumidity());

                    //DailyInit
                    for(int i = 0; i < weatherModel.getDaily().size()-1; i++){
                        //Reading from Json Pojo
                        FutureWeather dailyWeather = new FutureWeather();
                        dailyWeather.setTemp(weatherModel.getDaily().get(i).getTemp().getDay());
                        dailyWeather.setDescription(weatherModel.getDaily().get(i).getWeather().get(0).getDescription());
                        dailyWeather.setIcon(weatherModel.getDaily().get(i).getWeather().get(0).getIcon());
                        dailyWeather.setDt(weatherModel.getDaily().get(i).getDt());
                        dailyWeathers.add(dailyWeather);
                    }

                    //HourlyInit
                    for(int i = 1; i <= 24; i++){
                        //Reading from Json Pojo
                        FutureWeather hourlyWeather = new FutureWeather();
                        hourlyWeather.setTemp(weatherModel.getHourly().get(i).getTemp());
                        hourlyWeather.setDescription(weatherModel.getHourly().get(i).getWeather().get(0).getDescription());
                        hourlyWeather.setIcon(weatherModel.getHourly().get(i).getWeather().get(0).getIcon());
                        hourlyWeather.setDt(weatherModel.getHourly().get(i).getDt());
                        hourlyWeathers.add(hourlyWeather);
                    }

                    mainWindowSetWidget(currentWeather);

                    //put dailyWeathers and hourlyWeathers to bundle
                    //Fragments are going to read some data from bundle
                    bundle.get(0).putSerializable("dailyWeathers", (Serializable) dailyWeathers);
                    bundle.get(1).putSerializable("hourlyWeathers", (Serializable) hourlyWeathers);

                    //Init Fragments
                    DailyWeatherFragment dailyWeatherFragment = new DailyWeatherFragment(MainActivity.this);
                    dailyWeatherFragment.setArguments(bundle.get(0));

                    HourWeatherFragment hourWeatherFragment = new HourWeatherFragment(MainActivity.this);
                    hourWeatherFragment.setArguments(bundle.get(1));

                    //Doing some staff with fragments
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.dailyFragmentInActivity, dailyWeatherFragment);
                    fragmentTransaction.replace(R.id.hourDayFragmentInActivity, hourWeatherFragment);
                    fragmentTransaction.commit();
                }
            }

            @Override
            public void onFailure(Call<WeatherModel> call, Throwable t) {
                Log.d("GSON_EXCEPTION", t.toString());
            }
        });
    }



}