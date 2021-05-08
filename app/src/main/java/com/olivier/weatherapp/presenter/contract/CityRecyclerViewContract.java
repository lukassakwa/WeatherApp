package com.olivier.weatherapp.presenter.contract;

public interface CityRecyclerViewContract {

    interface View {
        void updateView(int position);
    }

    interface Presenter {
        void updateView(int position);
    }
}
