package com.example.cookbook.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.cookbook.data.Recipe;
import com.example.cookbook.data.RecipeRepository;
import com.example.cookbook.listeners.RecipeSelectedListener;

import java.util.List;

public class DrawerViewModel extends AndroidViewModel {

    private RecipeRepository mRepository;
    private LiveData<List<Recipe>> mRecipes;//Cache

    public DrawerViewModel(@NonNull Application application) {
        super(application);
        mRepository = new RecipeRepository(application);
        mRecipes = mRepository.getAll();
    }

    public void insertRecipe(Recipe recipe){ mRepository.insert(recipe); }
    public void insertRecipe(Recipe recipe, RecipeSelectedListener callback){ mRepository.insert(recipe, callback); }

    public LiveData<List<Recipe>> getRecipes(){ return mRecipes; }
}
