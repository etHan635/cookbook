package com.example.cookbook.data;

import static androidx.room.ForeignKey.CASCADE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "step_table",
        foreignKeys = {
                @ForeignKey(
                        entity = Recipe.class,
                        parentColumns = "recipeId",
                        childColumns = "recipe",
                        onDelete = CASCADE
                )
        }
)
public class Step {
    @Ignore
    public static final int STEP_POSITION_DELTA = 1000;

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "stepId")
    private int mStepId;

    @NonNull
    @ColumnInfo(name = "recipe")
    private int mRecipe;

    @NonNull
    @ColumnInfo(name = "position")
    private int mPosition;

    @NonNull
    @ColumnInfo(name = "instruction")
    private String mInstruction;

    public Step(@NonNull String instruction, @NonNull int recipe, @NonNull int position){
        this.mInstruction = instruction;
        this.mRecipe = recipe;
        this.mPosition = position;
    }

    @Ignore
    public Step(Step other){
        this.mInstruction = other.getInstruction();
        this.mRecipe = other.getRecipe();
        this.mPosition = other.getPosition();
    }

    public void setStepId(int stepId){ this.mStepId = stepId; }
    public int getStepId(){ return this.mStepId; }

    public void setRecipe(int recipe){ this.mRecipe = recipe; }
    public int getRecipe(){ return this.mRecipe; }

    public void setPosition(int position){ this.mPosition = position; }
    public int getPosition(){ return this.mPosition; }

    public void setInstruction(String instruction){ this.mInstruction = instruction; }
    public String getInstruction(){ return this.mInstruction; }

    @Ignore
    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj.getClass() == this.getClass()){
            Step other = (Step)obj;
            return other.mStepId == this.mStepId;
        } else {
            return false;
        }
    }
}
