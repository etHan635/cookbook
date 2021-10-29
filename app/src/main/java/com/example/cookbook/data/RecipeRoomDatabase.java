package com.example.cookbook.data;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.List;

@Database(
        entities = {Recipe.class, Step.class, Ingredient.class},
        version = 1,
        exportSchema = false
)
public abstract class RecipeRoomDatabase extends RoomDatabase {
    public abstract RecipeDao recipeDao();
    public abstract StepDao stepDao();
    public abstract IngredientDao ingredientDao();

    private static RecipeRoomDatabase INSTANCE;

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback(){
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
        }
    };

    public static RecipeRoomDatabase getDatabase(final Context context){
        if(INSTANCE == null){
            synchronized (RecipeRoomDatabase.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            RecipeRoomDatabase.class,
                            "recipe_database"
                    ).fallbackToDestructiveMigration()
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
