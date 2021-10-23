package com.example.cookbook.viewmodel;

import android.app.Application;
import android.util.Log;

import com.example.cookbook.data.Step;
import com.example.cookbook.listeners.StepsAlteredListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EditStepsViewModel extends StepsViewModel {

    public List<Step> mCachedSteps;

    public EditStepsViewModel(Application application){
        super(application);
    }

    @Override
    public void onStepsRetrieved(List<Step> steps) {
        super.onStepsRetrieved(steps);
        mCachedSteps = new ArrayList<Step>(steps);
    }

    public void insertStep(int position){
        List<Step> steps = mSteps.getValue();
        Integer before = null;
        Integer after = null;
        if(position > 0){
            before = steps.get(position - 1).getPosition();
        }
        if(position < steps.size()){
            after = steps.get(position).getPosition();
        }

        int scaledPos = 0;

        if( (before != null) && (after != null) ){
            scaledPos = (before + after)/2;
        } else if(before != null){
            scaledPos = before + Step.STEP_POSITION_DELTA;
        } else if(after != null){
            scaledPos = after - Step.STEP_POSITION_DELTA;
        } else {
            scaledPos = 0;
        }

        Step step = new Step("", mRecipeId, scaledPos);
        steps.add(position, step);
    }

    public void moveStep(int from, int to){
        List<Step> steps = mSteps.getValue();
        Step stepFrom = steps.get(from);
        Step stepTo = steps.get(to);

        int fromPosition = stepFrom.getPosition();
        stepFrom.setPosition(stepTo.getPosition());
        stepTo.setPosition(fromPosition);

        Collections.swap(steps, from, to);
    }

    public void removeStep(int position){
        List<Step> steps = mSteps.getValue();
        steps.remove(position);
    }

    public void updateSteps(){
        List<Step> steps = mSteps.getValue();
        if(steps != null){
            for(Step step : mCachedSteps){
                if(steps.contains(step)){
                    //Update
                    Log.d("Present", Integer.toString(step.getStepId()));
                    mRepository.update(step);
                } else {
                    //Delete
                    mRepository.delete(step);
                    Log.d("Missing", Integer.toString(step.getStepId()));
                }
            }

            for(Step step: steps){
                if(!mCachedSteps.contains(step)){
                    //Insert
                    Log.d("New", Integer.toString(step.getStepId()));
                    mRepository.insert(step);
                }
            }

        }
    }
}
