package com.example.cookbook.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.cookbook.EditIngredientsFragment;
import com.example.cookbook.EditStepsFragment;
import com.example.cookbook.StepsFragment;

public class EditRecipeViewPagerAdapter extends RecipeViewPagerAdapter {

    public EditRecipeViewPagerAdapter(FragmentActivity fragment){ super(fragment); }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch(position){
            case 0:
                return new EditStepsFragment();
            default:
                return new EditIngredientsFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
