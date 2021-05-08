package com.olivier.weatherapp.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import com.olivier.weatherapp.R;
import com.olivier.weatherapp.model.WeatherModel;
import com.olivier.weatherapp.presenter.activitypresenters.CitiesActivityPresenter;
import com.olivier.weatherapp.presenter.contract.CitiesActivityContract;
import com.olivier.weatherapp.presenter.recyclerviewspresenters.CitiesRVPresenter;
import com.olivier.weatherapp.view.recyclerviews.CitiesAdapter;

import java.util.ArrayList;

public class CitiesActivity extends AppCompatActivity implements CitiesActivityContract.View {

    private static final String PREFS_NAME = "PrefMain";

    //Presenter for City recycler view
    private CitiesActivityPresenter mCitiesActivityPresenter;

    private SharedPreferences mSharedPreferences;

    private Intent mIntent;
    private Bundle mBundle;

    private RecyclerView mCitiesRecyclerView;
    private RecyclerView.Adapter mCitiesAdapter;
    private RecyclerView.LayoutManager mCitiesLayoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cities_activity);

        //Setting toolbar elements
        initToolbar();

        mSharedPreferences = this.getSharedPreferences(PREFS_NAME, CitiesActivity.MODE_PRIVATE);
        mBundle = new Bundle();
        mIntent = new Intent();

        //get weather list from main activity
        Intent mainIntent = getIntent();
        ArrayList<WeatherModel> cityLocationArray = (ArrayList<WeatherModel>) mainIntent.getSerializableExtra("httpModels");

        //Init presenter
        mCitiesActivityPresenter = new CitiesActivityPresenter(cityLocationArray);
        mCitiesActivityPresenter.attach(this);
        mCitiesActivityPresenter.getInitRecyclerView();
    }

    //Menu 3 kropki w prawym gornym rogu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.cities_menu, menu);
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
                mCitiesActivityPresenter.exitCityActivity();
                return true;
            case R.id.add_item:
                mCitiesActivityPresenter.getIntentSearchActivity();
                return true;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();

        mCitiesActivityPresenter.saveArrayToPreferences();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        mCitiesActivityPresenter.exitCityActivity();
    }

    //Exit from Intent
    @Override
    public void onExitActivity(ArrayList<WeatherModel> weatherModels) {
        mBundle.putSerializable("httpModels", weatherModels);
        mIntent.putExtras(mBundle);
        setResult(RESULT_OK, mIntent);
        finish();
    }

    @Override
    public void initRecyclerView(CitiesRVPresenter citiesRVPresenter) {
        mCitiesRecyclerView = findViewById(R.id.citiesRecyclerView);

        mCitiesLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mCitiesRecyclerView.setLayoutManager(mCitiesLayoutManager);

        mCitiesAdapter = new CitiesAdapter(citiesRVPresenter);
        mCitiesRecyclerView.setAdapter(mCitiesAdapter);
    }

    @Override
    public void intentSearchActivity(ArrayList<WeatherModel> weatherModels) {
        Intent searchIntent = new Intent(this, SearchActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("httpModels", weatherModels);
        searchIntent.putExtras(bundle);
        startActivity(searchIntent);
    }

    @Override
    public void setPreferences(ArrayList<WeatherModel> weatherModels) {
        Gson gson = new Gson();
        String weatherArray = gson.toJson(weatherModels);
        mSharedPreferences.edit().putString("weatherArray", weatherArray).apply();
    }

    private void initToolbar(){
        Toolbar toolbar = findViewById(R.id.cities_toolbar);
        setSupportActionBar(toolbar);

        TextView mToolbarTitle = findViewById(R.id.cities_toolbar_title);
        mToolbarTitle.setText(toolbar.getTitle());

        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

}
