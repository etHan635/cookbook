package com.example.cookbook.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.cookbook.data.Ingredient;
import com.example.cookbook.data.IngredientRepository;
import com.example.cookbook.listeners.IngredientsRetrievedListener;

import java.util.List;

public class IngredientsViewModel extends AndroidViewModel implements IngredientsRetrievedListener {
    protected IngredientRepository mRepository;

    protected int mRecipeId;

    protected MutableLiveData<List<Ingredient>> mIngredients;

    public IngredientsViewModel(Application application){
        super(application);
        mRepository = new IngredientRepository(application);
        mIngredients = new MutableLiveData<>();
    }

    public void setRecipe(int recipeId){
        mRecipeId = recipeId;
        mRepository.getFor(mRecipeId, this);
    }

    public LiveData<List<Ingredient>> getIngredients(){ return this.mIngredients; }

    public boolean isIngredientsEmpty(){
        List<Ingredient> ingredients = getIngredients().getValue();
        if(ingredients != null){
            return ingredients.isEmpty();
        }
        return true;
    }

    @Override
    public void onIngredientsRetrieved(List<Ingredient> ingredients){ mIngredients.setValue(ingredients); }
}
