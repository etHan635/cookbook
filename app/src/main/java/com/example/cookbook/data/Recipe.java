package com.example.cookbook.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "recipe_table")
public class Recipe {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "recipeId")
    private int mRecipeId;

    @NonNull
    @ColumnInfo(name = "title")
    private String mTitle;

    @NonNull
    @ColumnInfo(name = "subtitle")
    private String mSubtitle;

    @ColumnInfo(name = "bookmarked")
    private boolean mBookmarked;

    public Recipe(@NonNull String title, @NonNull String subtitle, boolean bookmarked){ mTitle = title; mSubtitle = subtitle; mBookmarked = bookmarked; }

    @Ignore
    public Recipe(Recipe other){
        this.mTitle = other.getTitle();
        this.mSubtitle = other.getSubtitle();
        this.mBookmarked = other.getBookmarked();
    }

    public void setRecipeId(int recipeId){this.mRecipeId = recipeId; }

    public int getRecipeId(){ return mRecipeId; }

    public void setTitle(String title){ this.mTitle = title; }

    public String getTitle(){ return this.mTitle; }

    public void setSubtitle(String subtitle){ this.mSubtitle = subtitle; }

    public String getSubtitle(){ return this.mSubtitle; }

    public void setBookmarked(boolean bookmarked){ this.mBookmarked = bookmarked; }

    public boolean getBookmarked(){ return this.mBookmarked; }

    @Ignore
    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj.getClass() == this.getClass()){
            Recipe other = (Recipe)obj;
            return other.mRecipeId == this.mRecipeId;
        } else {
            return false;
        }
    }
}
