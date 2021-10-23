package com.example.cookbook.viewmodel;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.cookbook.data.Ingredient;
import com.example.cookbook.data.IngredientRepository;
import com.example.cookbook.data.Recipe;
import com.example.cookbook.data.RecipeDao;
import com.example.cookbook.data.RecipeRepository;
import com.example.cookbook.data.Step;
import com.example.cookbook.data.StepRepository;
import com.example.cookbook.listeners.RecipeRetrievedListener;
import com.example.cookbook.listeners.RecipeSelectedListener;

import java.util.List;

public class RecipeViewModel extends AndroidViewModel implements RecipeRetrievedListener {
    protected RecipeRepository mRepository;
    protected MutableLiveData<Recipe> mRecipe;

    private int mRecipeId;

    public RecipeViewModel(Application application){
        super(application);
        mRepository = new RecipeRepository(application);
        mRecipe = new MutableLiveData<>();
        mRecipeId = -1;
    }

    public void setRecipe(int recipeId){ mRecipeId = recipeId; syncRecipe(); }

    public void syncRecipe(){
        mRecipe.setValue(null);
        mRepository.get(mRecipeId, this);
    }

    public void duplicateRecipe(){
        Recipe recipe = mRecipe.getValue();
        if(recipe != null){
            mRepository.duplicate(recipe);
        }
    }

    public void setRecipeBookmarked(boolean bookmarked){
        Recipe recipe = mRecipe.getValue();
        if(recipe != null){
            recipe.setBookmarked(bookmarked);
            mRepository.update(recipe);
        }
    }

    public LiveData<Recipe> getRecipe(){ return mRecipe; }

    public int getRecipeId(){ return mRecipeId; }

    @Override
    public void onRecipeRetrieved(Recipe recipe) {
        mRecipe.setValue(recipe);
    }

}
