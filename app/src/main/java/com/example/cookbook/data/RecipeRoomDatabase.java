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
            //new PopulateDbAsync(INSTANCE).execute();
        }
    };

    public static RecipeRoomDatabase getDatabase(final Context context){
        if(INSTANCE == null){
            //context.getApplicationContext().deleteDatabase("recipe_database");
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

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {
        private final RecipeDao mRecipeDao;
        private final StepDao mStepDao;
        private final IngredientDao mIngredientDao;

        String[] recipeTitles = {"Alpha", "Beta", "Gamma", "Delta", "Epsilon", "Zeta", "Eta", "Theta", "Iota", "Kappa", "Lambda", "Mu", "Nu", "Xi", "Omicron", "Pi", "Rho", "Sigma", "Tau", "Upsilon", "Phi", "Chi", "Psi", "Omega"};

        public PopulateDbAsync(RecipeRoomDatabase db){
            mRecipeDao = db.recipeDao();
            mStepDao = db.stepDao();
            mIngredientDao = db.ingredientDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            for(int i = 0; i < recipeTitles.length; i++){
                Recipe recipe = new Recipe(recipeTitles[i], "", false);
                mRecipeDao.insert(recipe);
            }

            List<Recipe> recipes = mRecipeDao.getAllSync();
            for(Recipe recipe : recipes){
                for(int j = 1; j < 20; j++){
                    String text = String.format("This is the instruction which was initially positioned %s place(s) into the recipe for %s", j, recipe.getTitle());
                    Step step = new Step(text, recipe.getRecipeId(), (j * Step.STEP_POSITION_DELTA));
                    mStepDao.insert(step);
                }

                for(int k = 1; k < 7; k++){
                    String name = String.format("Ingr. %s of %s", k, recipe.getTitle());
                    Ingredient ingredient = new Ingredient(name, "100g", false, recipe.getRecipeId());
                    mIngredientDao.insert(ingredient);
                }
            }

            return null;
        }
    }
}
