package com.olivier.weatherapp;

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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.olivier.weatherapp.model.HttpModel;
import com.olivier.weatherapp.view.fragments.WeatherFragment;
import com.olivier.weatherapp.view.viewpager.ViewPagerFragmentAdapter;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    //Location cod
    private static final String PREFS_NAME = "PrefMain";
    private final int LOCATION_PERMISSION_CODE = 1000;
    private static final int REQUEST_CODE = 255;

    //Bundle
    Bundle intentBundle = new Bundle();

    //HttpModel
    private HashMap<String, HttpModel> cityLocationArray = new HashMap<>();

    //ViewPager
    private ViewPager2 mViewPager2;
    private ViewPagerFragmentAdapter mAdapter;
    private ArrayList<Fragment> arrayList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Toolbar setting
        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView mToolbarTitle = findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        mToolbarTitle.setText(toolbar.getTitle());
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //ViewPager
        mViewPager2 = findViewById(R.id.view_pager);

        //TODO: Do naprawienia rozpisania itp.
        //Permission check
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_PERMISSION_CODE);
        }

        cityLocationArray = getPreferences();
        SetViewPager();
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
                    //Todo: intent to window with city choice
                    Intent intentCity = new Intent(this, CitiesActivity.class);
                    intentBundle.putSerializable("httpModels", cityLocationArray);
                    intentCity.putExtras(intentBundle);
                    startActivityForResult(intentCity, REQUEST_CODE);
                } else {
                    Toast.makeText(this, "Location is needed to get weather", Toast.LENGTH_SHORT).show();
                }
                return;
        }
        // Other 'case' lines to check for other
        // permissions this app might request.
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            //setting cities
            this.cityLocationArray = (HashMap<String, HttpModel>) data.getExtras().getSerializable("httpModels");
            SetViewPager();
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
                Intent intentCity = new Intent(this, CitiesActivity.class);
                intentBundle.putSerializable("httpModels", cityLocationArray);
                intentCity.putExtras(intentBundle);
                startActivityForResult(intentCity, REQUEST_CODE);
                return true;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void SetViewPager() {
        arrayList.clear();

        //Init Fragments
        for(String i : cityLocationArray.keySet()){
            Bundle fragmentBundle = new Bundle();
            fragmentBundle.putSerializable("httpModel", (Serializable) cityLocationArray.get(i));
            WeatherFragment mainWeatherFragment = new WeatherFragment(MainActivity.this);
            mainWeatherFragment.setArguments(fragmentBundle);
            arrayList.add(mainWeatherFragment);
        }

        mAdapter = new ViewPagerFragmentAdapter(getSupportFragmentManager(), getLifecycle(), arrayList);
        // set Orientation in your ViewPager2
        mViewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        mViewPager2.setPageTransformer(new MarginPageTransformer(0));
        mViewPager2.setAdapter(mAdapter);
    }

    @Override
    protected void onStop() {
        super.onStop();

        setPreferences();
    }

    //Saving HashMap
    private void setPreferences(){
        SharedPreferences pSharedPref = this.getSharedPreferences(PREFS_NAME, this.MODE_PRIVATE);
        Gson gson = new Gson();
        String hashMapString = gson.toJson(cityLocationArray);
        //save in shared prefs
        pSharedPref.edit().putString("cityHashMap", hashMapString).apply();
    }

    private HashMap<String, HttpModel> getPreferences(){
        SharedPreferences pSharedPref = this.getSharedPreferences(PREFS_NAME, this.MODE_PRIVATE);
        try{
            //get from shared prefs
            String storedHashMapString = pSharedPref.getString("cityHashMap", (new JSONObject()).toString());
            java.lang.reflect.Type type = new TypeToken<HashMap<String, HttpModel>>(){}.getType();
            Gson gson = new Gson();
            return gson.fromJson(storedHashMapString, type);
        }catch(Exception e){
            e.printStackTrace();
        }

        return new HashMap<>();
    }
}