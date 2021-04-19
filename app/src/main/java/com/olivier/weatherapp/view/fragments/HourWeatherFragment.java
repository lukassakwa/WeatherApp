package com.olivier.weatherapp.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.olivier.weatherapp.R;
import com.olivier.weatherapp.model.FutureWeather;
import com.olivier.weatherapp.view.recyclerviews.WeatherHourAdapter;

import java.util.ArrayList;

public class HourWeatherFragment extends Fragment {

    private RecyclerView weatherHourRecyclerView;
    private RecyclerView.Adapter weatherHourRecyclerViewAdapter;
    private RecyclerView.LayoutManager weatherHourRecyclerViewLayoutManager;

    private FragmentActivity fragmentContext;

    private ArrayList<FutureWeather> hourWeathers;

    public HourWeatherFragment(Context context){
        this.fragmentContext = (FragmentActivity) context;
    }

    //@Override
    //public void onAttach(@NonNull Context context) {
    //   super.onAttach(context);
    //    if(context instanceof Activity){
    //        this.fragmentContext = (FragmentActivity) context;
    //    }
    //}

    // This event fires 2nd, before views are created for the fragment
    // The onCreate method is called when the Fragment instance is being created, or re-created.
    // Use onCreate for any standard setup that does not require the activity to be fully created
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get data from Bundle
        hourWeathers = (ArrayList<FutureWeather>) this.getArguments().getSerializable("hourlyWeathers");
    }

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.hour_weather_fragment, container, false);
    }

    // This event is triggered soon after onCreateView().
    // onViewCreated() is only called if the view returned from onCreateView() is non-null.
    // Any dsadas setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        weatherHourRecyclerView = view.findViewById(R.id.weatherHourRecyclerView);
        weatherHourRecyclerViewLayoutManager = new LinearLayoutManager(fragmentContext, LinearLayoutManager.HORIZONTAL, false);

        //initializing data on hour recyclerView
        HourRecyclerView(hourWeathers);
    }

    // This method is called after the parent Activity's onCreate() method has completed.
    // Accessing the view hierarchy of the parent activity must be done in the onActivityCreated.
    // At this point, it is safe to search for activity View objects by their ID, for example.
    //@Override
    //public void onActivityCreated(Bundle savedInstanceState) {
    //    super.onActivityCreated(savedInstanceState);
    //}

    private void HourRecyclerView(ArrayList<FutureWeather> forecastDataModels){
        //Do clasy fragmentowej Recycler View
        weatherHourRecyclerView.setHasFixedSize(true);
        weatherHourRecyclerView.setLayoutManager(weatherHourRecyclerViewLayoutManager);

        weatherHourRecyclerViewAdapter = new WeatherHourAdapter(forecastDataModels);
        weatherHourRecyclerView.setAdapter(weatherHourRecyclerViewAdapter);
    }

}
