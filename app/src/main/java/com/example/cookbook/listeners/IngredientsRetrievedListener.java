package com.example.cookbook.listeners;

import com.example.cookbook.data.Ingredient;

import java.util.List;

public interface IngredientsRetrievedListener {
    void onIngredientsRetrieved(List<Ingredient> ingredients);
}
