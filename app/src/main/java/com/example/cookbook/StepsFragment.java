package com.example.cookbook;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cookbook.adapters.StepListAdapter;
import com.example.cookbook.data.Recipe;
import com.example.cookbook.data.Step;
import com.example.cookbook.databinding.FragmentStepsBinding;
import com.example.cookbook.viewmodel.EditRecipeViewModel;
import com.example.cookbook.viewmodel.EditStepsViewModel;
import com.example.cookbook.viewmodel.RecipeViewModel;
import com.example.cookbook.viewmodel.StepsViewModel;

import java.util.List;

public class StepsFragment extends Fragment {

    private FragmentStepsBinding mBinding;

    private RecipeViewModel mRecipeViewModel;
    private StepsViewModel mStepsViewModel;

    private StepListAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentStepsBinding.inflate(inflater, container, false);

        mAdapter = new StepListAdapter(getActivity());

        mStepsViewModel = new ViewModelProvider(this).get(StepsViewModel.class);
        mStepsViewModel.getSteps().observe(getViewLifecycleOwner(), new Observer<List<Step>>() {
            @Override
            public void onChanged(List<Step> steps) {
                if(steps != null){
                    mAdapter.setSteps(steps);
                    mBinding.setEmpty(mStepsViewModel.isStepsEmpty());
                } else {
                    //TODO no steps found
                    mAdapter.setSteps(null);
                    mBinding.setEmpty(false);
                }
            }
        });

        //Get recipe view model to watch the recipe stuff
        mRecipeViewModel = new ViewModelProvider(getActivity()).get(RecipeViewModel.class);
        mRecipeViewModel.getRecipe().observe(getViewLifecycleOwner(), new Observer<Recipe>() {
            @Override
            public void onChanged(Recipe recipe) {
                if(recipe != null){
                    mStepsViewModel.setRecipe(recipe.getRecipeId());
                } else {
                    //TODO no recipe in parent
                    mAdapter.setSteps(null);
                }
            }
        });

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerview_steps);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
}