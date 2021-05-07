package com.olivier.weatherapp.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.olivier.weatherapp.R;
import com.olivier.weatherapp.model.WeatherModel;
import com.olivier.weatherapp.presenter.activitypresenters.MainActivityPresenter;
import com.olivier.weatherapp.presenter.contract.ContractMVP;
import com.olivier.weatherapp.view.fragments.CityWeatherFragment;
import com.olivier.weatherapp.view.fragments.LocationWeatherFragment;
import com.olivier.weatherapp.view.viewpager.ViewPagerFragmentAdapter;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ContractMVP.MainActivityView {

    //TODO:: permission check to another class

    //Location cod
    private static final String PREFS_NAME = "PrefMain";
    private final int LOCATION_PERMISSION_CODE = 1000;
    private static final int REQUEST_CODE = 255;

    //Bundle
    Bundle intentBundle = new Bundle();

    //Shared Preferences
    private SharedPreferences pSharedPref;

    //presenter
    private MainActivityPresenter mMainActivityPresenter;

    //intent
    private Intent intentCity;

    //ViewPager
    private ViewPager2 mViewPager2;
    private ViewPagerFragmentAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Toolbar setting
        initToolbar();

        //ViewPager
        mViewPager2 = findViewById(R.id.view_pager);

        pSharedPref = this.getSharedPreferences(PREFS_NAME, MainActivity.MODE_PRIVATE);
        intentCity = new Intent(this, CitiesActivity.class);

        //Permission check
        permissionCheck();

        //update Weather
        //getting updated Location weatherModel from fragment
        getSupportFragmentManager().setFragmentResultListener("requestWeatherObject", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                WeatherModel weatherModel = (WeatherModel) result.getSerializable("weatherObject");
                mMainActivityPresenter.setLocationWeather(weatherModel);
            }
        });

        //get saved ArrayList from peferences
        mMainActivityPresenter = new MainActivityPresenter();
        mMainActivityPresenter.attach(this);
        mMainActivityPresenter.getArrayFromPreferences();
        mMainActivityPresenter.getViewPager();
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

                    mMainActivityPresenter.getIntentCity();

                } else {
                    Toast.makeText(this, "Location is needed to get weather", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            //setting cities
            mMainActivityPresenter.setCityLocationArray((ArrayList<WeatherModel>) data.getExtras().getSerializable("httpModels"));
            mMainActivityPresenter.getViewPager();
        }
    }

    //Menu 3 kropki w prawym gornym rogu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        //Left corner item
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icons8_city_buildings_30);
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
            case R.id.action_settings:
                Toast.makeText(this, "Work in progress", Toast.LENGTH_SHORT).show();
                return true;
            case android.R.id.home:
                //Todo: intent to window with city choice
                mMainActivityPresenter.getIntentCity();
                return true;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mMainActivityPresenter.saveArrayToPreferences();
    }

    /**
     * view methods
     */
    @Override
    public void cityIntent(ArrayList<WeatherModel> cityLocationArray){
        intentBundle.putSerializable("httpModels", cityLocationArray);
        intentCity.putExtras(intentBundle);
        startActivityForResult(intentCity, REQUEST_CODE);
    }

    @Override
    public void SetViewPager(ArrayList<WeatherModel> cityLocationArray) {
        ArrayList<Fragment> arrayList = new ArrayList<>();
        Bundle fragmentBundle;

        //init city fragments
        for(int i = 0; i < cityLocationArray.size(); i++){

            fragmentBundle = new Bundle();
            fragmentBundle.putSerializable("httpModel", (Serializable) cityLocationArray.get(i));

            String city = cityLocationArray.get(i).getCity();

            if(city.equals("current")){
                LocationWeatherFragment cityWeatherFragment = new LocationWeatherFragment(MainActivity.this);
                cityWeatherFragment.setArguments(fragmentBundle);
                arrayList.add(cityWeatherFragment);
            }else{
                CityWeatherFragment cityWeatherFragment = new CityWeatherFragment(MainActivity.this);
                cityWeatherFragment.setArguments(fragmentBundle);
                arrayList.add(cityWeatherFragment);
            }
        }

        mAdapter = new ViewPagerFragmentAdapter(getSupportFragmentManager(), getLifecycle(), arrayList);
        // set Orientation in your ViewPager2
        mViewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        mViewPager2.setPageTransformer(new MarginPageTransformer(0));
        mViewPager2.setAdapter(mAdapter);
    }

    @Override
    public void getPreferences(){
        try{
            //get from shared prefs
            String storedCityArrayString = pSharedPref.getString("weatherArray", (new JSONObject()).toString());
            java.lang.reflect.Type cityType = new TypeToken<ArrayList<WeatherModel>>(){}.getType();
            Gson gson = new Gson();

            ArrayList<WeatherModel> tempCityArray = gson.fromJson(storedCityArrayString, cityType);
            if(tempCityArray != null) {
                mMainActivityPresenter.setCityLocationArray(tempCityArray);
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    //Saving ArrayList
    @Override
    public void setPreferences(ArrayList<WeatherModel> cityLocationArray){
        Gson gson = new Gson();
        String hashMapString = gson.toJson(cityLocationArray);
        //save in shared prefs
        pSharedPref.edit().putString("weatherArray", hashMapString).apply();
    }

    /**
     * MainACtivity methoods
     */

    private void permissionCheck(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_PERMISSION_CODE);
        }
    }

    private void initToolbar(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView mToolbarTitle = findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        mToolbarTitle.setText(toolbar.getTitle());
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }


}