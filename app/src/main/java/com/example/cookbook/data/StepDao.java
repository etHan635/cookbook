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
public interface StepDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Step step);

    @Update(onConflict = OnConflictStrategy.ABORT)
    void update(Step step);

    @Delete
    void delete(Step step);

    @Query("SELECT * FROM step_table WHERE stepId = :stepId LIMIT 1")
    LiveData<Step> get(int stepId);

    @Query("SELECT * FROM step_table WHERE recipe = :recipe ORDER BY position ASC LIMIT 1 OFFSET :position")
    LiveData<Step> get(int recipe, int position);

    @Query("SELECT * FROM step_table WHERE recipe = :recipe ORDER BY position ASC")
    List<Step> getFor(int recipe);

}
