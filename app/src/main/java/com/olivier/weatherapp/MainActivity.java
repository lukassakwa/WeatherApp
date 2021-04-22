package com.olivier.weatherapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
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
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.olivier.weatherapp.model.HttpModel;
import com.olivier.weatherapp.view.fragments.LocationWeatherFragment;
import com.olivier.weatherapp.view.viewpager.ViewPagerFragmentAdapter;

import java.io.Serializable;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //Location cod
    private final int LOCATION_PERMISSION_CODE = 1000;

    //Bundle
    private ArrayList<Bundle> bundle;
    //HttpModel
    private ArrayList<HttpModel> weather;

    //Location
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LocationRequest locationResult;

    //ViewPager
    ViewPager2 mViewPager2;
    ViewPagerFragmentAdapter mAdapter;
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

        //Todo: view pager
        mViewPager2 = findViewById(R.id.view_pager);

        bundle = new ArrayList<>();
        weather = new ArrayList<>();

        //location
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        //TODO: Do naprawienia rozpisania itp.
        //Permission check
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_PERMISSION_CODE);
        }

        //Function which Set location and send to start activity
        getLocation();
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

                    getLocation();

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
        //TODO:: Title in center

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
                Toast.makeText(this, "Work in progress", Toast.LENGTH_SHORT).show();
                return true;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("MissingPermission")
    private void getLocation(){
        //Device location
        mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location == null) {
                    //Device Location
                    locationResult = LocationRequest.create();
                    locationResult.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                    locationResult.setInterval(20 * 1000);
                }
                HttpModel httpModel = new HttpModel();
                httpModel.setLat(location.getLatitude());
                httpModel.setLon(location.getLongitude());

                String msg = "Lat: " + httpModel.getLat() + "Lon" + httpModel.getLon();
                Log.d("LOCATION_PARAMETERS", msg);

                SetViewPager(httpModel);
            }
        });
    }

    private void SetViewPager(HttpModel httpModel) {
        //Init View pager
        HttpModel httpModel1 = new HttpModel();
        httpModel1.setLat(httpModel.getLat());
        httpModel1.setLon(httpModel.getLon());
        weather.add(httpModel1);
        HttpModel httpModel2 = new HttpModel();
        httpModel2.setLat(52.237049);
        httpModel2.setLon(21.017532);
        weather.add(httpModel2);
        HttpModel httpModel3 = new HttpModel();
        httpModel3.setLat(52.409538);
        httpModel3.setLon(16.931992);
        weather.add(httpModel3);


        Bundle bundle1 = new Bundle();
        bundle1.putSerializable("httpModel", (Serializable) weather.get(0));
        bundle.add(bundle1);

        Bundle bundle2 = new Bundle();
        bundle2.putSerializable("httpModel", (Serializable) weather.get(1));
        bundle.add(bundle2);

        Bundle bundle3 = new Bundle();
        bundle3.putSerializable("httpModel", (Serializable) weather.get(2));
        bundle.add(bundle3);

        //Init Fragments
        for(int i = 0; i < weather.size(); i++){
            LocationWeatherFragment mainWeatherFragment = new LocationWeatherFragment(MainActivity.this);
            mainWeatherFragment.setArguments(bundle.get(i));
            arrayList.add(mainWeatherFragment);
        }

        mAdapter = new ViewPagerFragmentAdapter(getSupportFragmentManager(), getLifecycle(), arrayList);
        // set Orientation in your ViewPager2
        mViewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        mViewPager2.setPageTransformer(new MarginPageTransformer(0));
        mViewPager2.setAdapter(mAdapter);
    }


}