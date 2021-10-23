package com.example.cookbook;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cookbook.adapters.IngredientListAdapter;
import com.example.cookbook.data.Ingredient;
import com.example.cookbook.data.Recipe;
import com.example.cookbook.databinding.FragmentIngredientsBinding;
import com.example.cookbook.viewmodel.IngredientsViewModel;
import com.example.cookbook.viewmodel.RecipeViewModel;

import java.util.List;

public class IngredientsFragment extends Fragment {

    private FragmentIngredientsBinding mBinding;

    private RecipeViewModel mRecipeViewModel;
    private IngredientsViewModel mIngredientsViewModel;

    private IngredientListAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentIngredientsBinding.inflate(inflater, container, false);

        mAdapter = new IngredientListAdapter(getActivity());

        mIngredientsViewModel = new ViewModelProvider(this).get(IngredientsViewModel.class);
        mIngredientsViewModel.getIngredients().observe(getViewLifecycleOwner(), new Observer<List<Ingredient>>() {
            @Override
            public void onChanged(List<Ingredient> ingredients) {
                if(!mIngredientsViewModel.isIngredientsEmpty()){
                    mAdapter.setIngredients(ingredients);
                    mBinding.setEmpty(mIngredientsViewModel.isIngredientsEmpty());
                } else {
                    //TODO no ingredients found
                    mAdapter.setIngredients(null);
                    mBinding.setEmpty(true);
                }
            }
        });

        mRecipeViewModel = new ViewModelProvider(getActivity()).get(RecipeViewModel.class);
        mRecipeViewModel.getRecipe().observe(getViewLifecycleOwner(), new Observer<Recipe>() {
            @Override
            public void onChanged(Recipe recipe) {
                if(recipe != null){
                    mIngredientsViewModel.setRecipe(recipe.getRecipeId());
                } else {
                    //TODO no recipe in parent
                    mAdapter.setIngredients(null);
                }
            }
        });

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = mBinding.recyclerviewIngredients;
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
}