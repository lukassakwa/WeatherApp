package com.olivier.weatherapp.view.viewpager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class ViewPagerFragmentAdapter extends FragmentStateAdapter {

    private final ArrayList<Fragment> mWeatherFragments;

    public ViewPagerFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, ArrayList<Fragment> weatherFragments) {
        super(fragmentManager, lifecycle);
        this.mWeatherFragments = weatherFragments;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if(mWeatherFragments.get(position) != null)
            return mWeatherFragments.get(position);
        return null;
    }

    @Override
    public int getItemCount() {
        return mWeatherFragments.size();
    }
}
