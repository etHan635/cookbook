package com.example.cookbook.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface RecipeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Recipe recipe);

    @Update(onConflict = OnConflictStrategy.ABORT)
    void update(Recipe recipe);

    @Delete
    void delete(Recipe recipe);

    @Query("SELECT * FROM recipe_table WHERE recipeId = :recipeId LIMIT 1")
    Recipe get(int recipeId);

    @Query("SELECT * FROM recipe_table ORDER BY bookmarked DESC, title ASC")
    LiveData<List<Recipe>> getAll();

    @Query("SELECT * FROM recipe_table")
    List<Recipe> getAllSync();
}
