package com.example.cookbook.listeners;

import com.example.cookbook.data.Step;

import java.util.List;

public interface StepsRetrievedListener {
    void onStepsRetrieved(List<Step> steps);
}
