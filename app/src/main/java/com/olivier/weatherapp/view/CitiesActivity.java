package com.olivier.weatherapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.olivier.weatherapp.R;
import com.olivier.weatherapp.model.WeatherModel;
import com.olivier.weatherapp.presenter.activitypresenters.CitiesActivityPresenter;
import com.olivier.weatherapp.presenter.contract.Contract;
import com.olivier.weatherapp.presenter.recyclerviewspresenters.CitiesRVPresenter;
import com.olivier.weatherapp.view.recyclerviews.CitiesAdapter;

import java.util.ArrayList;

public class CitiesActivity extends AppCompatActivity implements Contract.CitiesActivityView {

    //Presenter for City recycler view
    private CitiesActivityPresenter citiesActivityPresenter;

    private Intent intent;
    private Bundle bundle;

    private RecyclerView citiesRecyclerView;
    private RecyclerView.Adapter citiesAdapter;
    private RecyclerView.LayoutManager citiesLayoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cities_activity);

        //Setting toolbar elements
        initToolbar();

        bundle = new Bundle();
        intent = new Intent();

        //get weather list from main activity
        Intent mainIntent = getIntent();
        ArrayList<WeatherModel> cityLocationArray = (ArrayList<WeatherModel>) mainIntent.getSerializableExtra("httpModels");

        //Init presenter
        citiesActivityPresenter = new CitiesActivityPresenter(cityLocationArray);
        citiesActivityPresenter.attach(this);
        citiesActivityPresenter.getInitRecyclerView();
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
                citiesActivityPresenter.exitCityActivity();
                return true;
            case R.id.add_item:
                citiesActivityPresenter.getIntentSearchActivity();
                return true;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        citiesActivityPresenter.exitCityActivity();
    }

    //Exit from Intent
    @Override
    public void onExitActivity(ArrayList<WeatherModel> weatherModels) {
        bundle.putSerializable("httpModels", weatherModels);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void initRecyclerView(CitiesRVPresenter citiesRVPresenter) {
        citiesRecyclerView = findViewById(R.id.citiesRecyclerView);

        citiesLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        citiesRecyclerView.setLayoutManager(citiesLayoutManager);

        citiesAdapter = new CitiesAdapter(citiesRVPresenter);
        citiesRecyclerView.setAdapter(citiesAdapter);
    }

    @Override
    public void intentSearchActivity(ArrayList<WeatherModel> weatherModels) {
        Intent searchIntent = new Intent(this, SearchActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("httpModels", weatherModels);
        searchIntent.putExtras(bundle);
        startActivity(searchIntent);
    }

    private void initToolbar(){
        Toolbar toolbar = findViewById(R.id.cities_toolbar);
        setSupportActionBar(toolbar);

        TextView mToolbarTitle = findViewById(R.id.cities_toolbar_title);
        mToolbarTitle.setText(toolbar.getTitle());

        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

}
