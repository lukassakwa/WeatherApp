package com.olivier.weatherapp;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import com.olivier.weatherapp.model.HttpModel;

import java.util.HashMap;

public class CitiesActivity extends AppCompatActivity {

    private HashMap<String, HttpModel> cityLocationArray;
    //Location
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LocationRequest locationResult;

    private final Intent intent = new Intent();
    private final Bundle bundle = new Bundle();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cities_activity);

        Toolbar toolbar = findViewById(R.id.cities_toolbar);
        setSupportActionBar(toolbar);
        TextView mToolbarTitle = findViewById(R.id.cities_toolbar_title);
        mToolbarTitle.setText(toolbar.getTitle());
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Intent mainIntent = getIntent();
        //Resolve back data from sharedpreferences to hashMap
        cityLocationArray = (HashMap<String, HttpModel>) mainIntent.getSerializableExtra("httpModels");

        //initialize Buttons
        Button currentButton = findViewById(R.id.currentButton);
        Button warsawButton = findViewById(R.id.warsawButton);
        Button krakowButton = findViewById(R.id.krakowButton);

        //location
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        //TODO:: background buttons
        //TODO:: add or remove city when button press
        currentButton.setOnClickListener((v)-> {
            if(cityLocationArray.containsKey("current")){
                cityLocationArray.remove("current");
            }else{
                getLocation();
            }
        });

        warsawButton.setOnClickListener((v) -> {
            if(cityLocationArray.containsKey("Warsaw")){
                cityLocationArray.remove("Warsaw");
            }else{
                sendData(21.017532, 52.237049, "Warsaw");
            }
        });

        krakowButton.setOnClickListener((v) -> {
            if(cityLocationArray.containsKey("Krakow")){
                cityLocationArray.remove("Krakow");
            }else{
                sendData(19.944544, 50.049683, "Krakow");
            }
        });
    }

    //Menu 3 kropki w prawym gornym rogu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);

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
                onExitActivity();
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

                sendData(location.getLongitude(), location.getLatitude(), "current");
            }
        });
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        onExitActivity();
    }

    private void sendData(Double lon, Double lat, String name){
        HttpModel httpModel = new HttpModel();
        httpModel.setLon(lon);
        httpModel.setLat(lat);

        cityLocationArray.put(name, httpModel);

        onExitActivity();
    }

    private void onExitActivity(){
        bundle.putSerializable("httpModels", cityLocationArray);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }
}
