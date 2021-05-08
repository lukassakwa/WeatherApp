package com.olivier.weatherapp.presenter.contract;

public interface CityRecyclerViewAdapterContract {

    /**
     * Represents the View (Activity, Fragment, View subclass) in MVP
     */
    interface View{
        void showTemp(int temp);
        void showName(String name);
        void showDescription(String description);
    }

    /**
     * Represents the Presenter in MVP
     */

    interface Presenter{
        void getCurrentWeather();
    }
}
