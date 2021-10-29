package com.example.cookbook.viewmodel;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import com.example.cookbook.data.Recipe;
import com.example.cookbook.data.RecipeDao;
import com.example.cookbook.data.RecipeRepository;
import com.example.cookbook.listeners.RecipeRetrievedListener;
import com.example.cookbook.listeners.RecipeUpdateApprovedListener;

import java.util.ArrayList;
import java.util.List;

public class EditRecipeViewModel extends RecipeViewModel {

    private List<RecipeUpdateApprovedListener> mUpdateApprovedListeners;

    public EditRecipeViewModel(Application application){
        super(application);
        mUpdateApprovedListeners = new ArrayList<>();
    }

    public void addRecipeUpdateApprovedListener(RecipeUpdateApprovedListener listener){
        mUpdateApprovedListeners.add(listener);
    }

    public void approveRecipeUpdate(){
        for(RecipeUpdateApprovedListener listener : mUpdateApprovedListeners){
            listener.onRecipeUpdateApproved();
        }
    }

    public void updateRecipe(){
        Recipe recipe = mRecipe.getValue();
        if(recipe != null){
            mRepository.update(recipe);
        }
    }

    public void deleteRecipe(){
        Recipe recipe = mRecipe.getValue();
        if(recipe != null){
            mRepository.delete(recipe);
        }
    }

}
