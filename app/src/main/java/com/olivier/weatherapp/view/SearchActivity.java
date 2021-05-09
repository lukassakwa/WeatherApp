package com.olivier.weatherapp.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.olivier.weatherapp.R;
import com.olivier.weatherapp.model.WeatherModel;
import com.olivier.weatherapp.presenter.activitypresenters.SearchActivityPresenter;
import com.olivier.weatherapp.presenter.contract.SearchActivityContract;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity implements SearchActivityContract.View {

    //TODO:: move to inner class beacous this is in 2 clas
    private static final String PREFS_NAME = "PrefMain";
    private SearchActivityPresenter mSearchActivityPresenter;

    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LocationRequest mLocationRequest;

    //Shared Preferences
    private SharedPreferences mSharedPref;

    private Button currentButton;
    private Button krakowButton;
    private Button warsawButton;

    private Intent mMainAcivityIntent;

    public SearchActivity() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);

        //set Toolbar
        initToolbar();

        //intent to mainActivity
        mMainAcivityIntent = new Intent(this, MainActivity.class);

        //preference init to save weather array list and go back to activity
        mSharedPref = this.getSharedPreferences(PREFS_NAME, MainActivity.MODE_PRIVATE);

        //location init
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        //init Presenter
        Intent searchActivityIntent = getIntent();
        ArrayList<WeatherModel> weatherModels = (ArrayList<WeatherModel>) searchActivityIntent.getSerializableExtra("httpModels");
        mSearchActivityPresenter = new SearchActivityPresenter(weatherModels);
        mSearchActivityPresenter.attach(this);


        currentButton = findViewById(R.id.currentButton);
        krakowButton = findViewById(R.id.krakowButton);
        warsawButton = findViewById(R.id.warsawButton);



        currentButton.setOnClickListener((v) -> {
            getLocation();
        });

        krakowButton.setOnClickListener((v) -> {
            WeatherModel weatherModel = new WeatherModel(19.9167, 50.0833, "Krakow");

            mSearchActivityPresenter.addWeather(weatherModel);
            mSearchActivityPresenter.exit();
        });

        warsawButton.setOnClickListener((v) -> {
            WeatherModel weatherModel = new WeatherModel(21.012229, 52.229676, "Warsaw");

            mSearchActivityPresenter.addWeather(weatherModel);
            mSearchActivityPresenter.exit();
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initToolbar(){
        Toolbar toolbar = findViewById(R.id.search_toolbar);
        TextView toolbarTitle = findViewById(R.id.search_toolbar_title);
        setSupportActionBar(toolbar);
        toolbarTitle.setText(toolbar.getTitle());
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    //TODO::SET PREFERENCES
    @Override
    public void exitActivity() {
        mMainAcivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mMainAcivityIntent);
    }

    @SuppressLint("MissingPermission")
    private void getLocation(){
        mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location == null){
                    setLocationRequest();
                }

                double lat = location.getLatitude();
                double lon = location.getLongitude();

                WeatherModel weatherModel = new WeatherModel(lon, lat, "current");

                mSearchActivityPresenter.addLocationWeather(weatherModel);
                mSearchActivityPresenter.exit();
            }
        });
    }

    //Saving ArrayList
    @Override
    public void setPreferences(ArrayList<WeatherModel> weatherModels) {
        Gson gson = new Gson();
        String weatherArrayList = gson.toJson(weatherModels);
        //save in shared prefs
        mSharedPref.edit().putString("weatherArray", weatherArrayList).apply();

    }

    @Override
    public void listOfCities() {

    }

    private void setLocationRequest(){
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(6000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }


}
