package com.olivier.weatherapp.view.recyclerviews;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.olivier.weatherapp.R;
import com.olivier.weatherapp.presenter.contract.CityRecyclerViewAdapterContract;
import com.olivier.weatherapp.presenter.contract.CityRecyclerViewContract;
import com.olivier.weatherapp.presenter.recyclerviewspresenters.CitiesRVPresenter;

public class CitiesAdapter extends RecyclerView.Adapter<CitiesAdapter.ViewHolder> implements CityRecyclerViewContract.View{

    private CitiesRVPresenter mCitiesRVPresenter;

    public CitiesAdapter(CitiesRVPresenter citiesRVPresenter) {
        this.mCitiesRVPresenter = citiesRVPresenter;
        citiesRVPresenter.attach(this);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements CityRecyclerViewAdapterContract.View {

        private ImageButton delete;

        private TextView cityName;
        private TextView description;
        private TextView temp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            delete = itemView.findViewById(R.id.deleteImageButton);

            cityName = itemView.findViewById(R.id.cityNameTextView);
            description = itemView.findViewById(R.id.descriptionItem);
            temp = itemView.findViewById(R.id.tempItem);
        }

        //Methods which are callback in presenter
        @Override
        public void showTemp(int temp) {
            this.temp.setText(temp + "");
        }

        @Override
        public void showName(String name) {
            this.cityName.setText(name);
        }

        @Override
        public void showDescription(String description) {
            this.description.setText(description);
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.city_item, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Presenter for this Recycler View
        mCitiesRVPresenter.getLocationWeaher(holder, position);

        holder.delete.setOnClickListener((v) -> {
            mCitiesRVPresenter.updateView(holder.getAdapterPosition());
        });
    }

    @Override
    public int getItemCount() {
        return mCitiesRVPresenter.getSize();
    }

    @Override
    public void updateView(int position) {
        notifyItemRemoved(position);
    }
}
