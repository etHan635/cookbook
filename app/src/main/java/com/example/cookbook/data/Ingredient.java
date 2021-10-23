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
        tableName = "ingredient_table",
        foreignKeys = {
                @ForeignKey(
                        entity = Recipe.class,
                        parentColumns = "recipeId",
                        childColumns = "recipe",
                        onDelete = CASCADE
                )
        }
)
public class Ingredient {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "ingredientId")
    private int mIngredientId;

    @NonNull
    @ColumnInfo(name = "recipe")
    private int mRecipe;

    @NonNull
    @ColumnInfo(name = "name")
    private String mName;

    @NonNull
    @ColumnInfo(name = "quantity")
    private String mQuantity;

    @NonNull
    @ColumnInfo(name = "optional")
    private boolean mOptional;

    public Ingredient(@NonNull String name, @NonNull String quantity, @NonNull boolean optional, @NonNull int recipe){
        this.mName = name;
        this.mQuantity = quantity;
        this.mOptional = optional;
        this.mRecipe = recipe;
    }

    @Ignore
    public Ingredient(Ingredient other){
        this.mName = other.getName();
        this.mQuantity = other.getQuantity();
        this.mOptional = other.getOptional();
        this.mRecipe = other.getRecipe();
    }

    public void setIngredientId(int ingredientId){ this.mIngredientId = ingredientId; }
    public int getIngredientId(){ return this.mIngredientId; }

    public void setRecipe(int recipeId){ this.mRecipe = recipeId; }
    public int getRecipe(){ return this.mRecipe; }

    public void setName(String name){ this.mName = name; }
    public String getName(){ return this.mName; }

    public void setQuantity(String quantity){ this.mQuantity = quantity; }
    public String getQuantity(){ return this.mQuantity; }

    public void setOptional(boolean optional){ this.mOptional = optional; }
    public boolean getOptional(){ return this.mOptional; }

    @Ignore
    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj.getClass() == this.getClass()){
            Ingredient other = (Ingredient) obj;
            return other.mIngredientId == this.mIngredientId;
        } else {
            return false;
        }
    }
}
