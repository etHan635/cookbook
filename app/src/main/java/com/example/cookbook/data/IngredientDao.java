package com.example.cookbook.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface IngredientDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Ingredient ingredient);

    @Update(onConflict = OnConflictStrategy.ABORT)
    void update(Ingredient ingredient);

    @Delete
    void delete(Ingredient ingredient);

    @Query("SELECT * FROM ingredient_table WHERE ingredientId = :ingredientId LIMIT 1")
    LiveData<Ingredient> get(int ingredientId);

    @Query("SELECT * FROM ingredient_table WHERE recipe = :recipe ORDER BY optional ASC, name ASC")
    List<Ingredient> getFor(int recipe);

}
