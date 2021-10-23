package com.example.cookbook.listeners;

public interface StepsAlteredListener {
    void onInsertStep(int position);

    void onMoveStep(int from, int to);

    void onDeleteStep(int position);
}
