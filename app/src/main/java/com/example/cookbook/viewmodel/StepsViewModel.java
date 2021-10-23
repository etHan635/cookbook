package com.example.cookbook.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.cookbook.data.Step;
import com.example.cookbook.data.StepRepository;
import com.example.cookbook.listeners.StepsRetrievedListener;

import java.util.List;

public class StepsViewModel extends AndroidViewModel implements StepsRetrievedListener {
    protected StepRepository mRepository;

    protected int mRecipeId;

    protected MutableLiveData<List<Step>> mSteps;

    public StepsViewModel(Application application){
        super(application);
        mRepository = new StepRepository(application);
        mSteps = new MutableLiveData<>();
    }

    public void setRecipe(int recipeId){
        mRecipeId = recipeId;
        mRepository.getFor(mRecipeId, this);
    }

    public LiveData<List<Step>> getSteps(){ return this.mSteps; }

    public boolean isStepsEmpty(){
        List<Step> steps = getSteps().getValue();
        if(steps != null){
            return steps.isEmpty();
        }
        return true;
    }

    @Override
    public void onStepsRetrieved(List<Step> steps) {
        mSteps.setValue(steps);
    }
}
