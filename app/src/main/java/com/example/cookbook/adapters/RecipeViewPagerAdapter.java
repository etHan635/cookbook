package com.example.cookbook.adapters;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.cookbook.IngredientsFragment;
import com.example.cookbook.StepsFragment;

public class RecipeViewPagerAdapter extends FragmentStateAdapter {

    public RecipeViewPagerAdapter(FragmentActivity fragment){
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch(position){
            case 0:
                return new StepsFragment();
            case 1:
                return new IngredientsFragment();
            default:
                return null;
        }

    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
