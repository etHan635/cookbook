package com.example.cookbook.viewmodel;

import android.app.Application;

import com.example.cookbook.data.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class EditIngredientsViewModel extends IngredientsViewModel {

    public List<Ingredient> mCachedIngredients;

    public EditIngredientsViewModel(Application application){ super(application); }

    @Override
    public void onIngredientsRetrieved(List<Ingredient> ingredients) {
        super.onIngredientsRetrieved(ingredients);
        mCachedIngredients = new ArrayList<>(ingredients);
    }

    public void insertIngredient(int position){
        List<Ingredient> ingredients = mIngredients.getValue();

        Ingredient ingredient = new Ingredient("", "", false, mRecipeId);

        if(position < 0 || position >= ingredients.size()){
            ingredients.add(ingredient);
        } else {
            ingredients.add(position, ingredient);
        }
    }

    public void deleteIngredient(int position){
        List<Ingredient> ingredients = mIngredients.getValue();
        ingredients.remove(position);
    }

    public void updateIngredients(){
        List<Ingredient> ingredients = mIngredients.getValue();
        if(ingredients != null){
            for(Ingredient ingredient : mCachedIngredients){
                if(ingredients.contains(ingredient)){
                    //update
                    mRepository.update(ingredient);
                } else {
                    //Delete
                    mRepository.delete(ingredient);
                }
            }

            for(Ingredient ingredient : ingredients){
                if(!mCachedIngredients.contains(ingredient)){
                    //Insert
                    mRepository.insert(ingredient);
                }
            }

        }


    }

}
