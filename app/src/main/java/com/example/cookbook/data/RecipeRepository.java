package com.example.cookbook.data;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.cookbook.listeners.RecipeRetrievedListener;
import com.example.cookbook.listeners.RecipeSelectedListener;

import java.util.List;

public class RecipeRepository {
    private RecipeDao mDao;
    private StepDao mStepDao;
    private IngredientDao mIngredientDao;

    private LiveData<List<Recipe>> mRecipes;

    public RecipeRepository(Application application){
        RecipeRoomDatabase db = RecipeRoomDatabase.getDatabase(application);
        mDao = db.recipeDao();
        mStepDao = db.stepDao();
        mIngredientDao = db.ingredientDao();
        mRecipes = mDao.getAll();
    }

    public void insert(Recipe recipe){ new insertAsyncTask(mDao).execute(recipe); }
    public void insert(Recipe recipe, RecipeSelectedListener callback){ new insertAsyncTask(mDao, callback).execute(recipe); }

    public void update(Recipe recipe){ new updateAsyncTask(mDao).execute(recipe); }

    public void delete(Recipe recipe){ new deleteAsyncTask(mDao).execute(recipe); }

    public void duplicate(Recipe recipe){ new duplicateAsyncTask(mDao, mStepDao, mIngredientDao).execute(recipe); }

    public void get(int recipeId, RecipeRetrievedListener callback){ new selectAsyncTask(mDao, callback).execute(recipeId); }

    public LiveData<List<Recipe>> getAll(){ return mRecipes; }


    private static class insertAsyncTask extends AsyncTask<Recipe, Void, Long> {
        private RecipeDao mAsyncTaskDao;
        private RecipeSelectedListener mCallback;

        public insertAsyncTask(RecipeDao recipeDao){
            mAsyncTaskDao = recipeDao;
        }

        public insertAsyncTask(RecipeDao recipeDao, RecipeSelectedListener callback){
            mAsyncTaskDao = recipeDao;
            mCallback = callback;
        }

        @Override
        protected Long doInBackground(Recipe... recipes) {
            Long newRecipeId = mAsyncTaskDao.insert(recipes[0]);
            return newRecipeId;
        }

        @Override
        protected void onPostExecute(Long newRecipeId) {
            if(mCallback != null){
                mCallback.onRecipeSelected(newRecipeId.intValue());
            }
        }
    }

    private static class updateAsyncTask extends AsyncTask<Recipe, Void, Void>{
        private RecipeDao mAsyncTaskDAO;

        public updateAsyncTask(RecipeDao recipeDao){
            mAsyncTaskDAO = recipeDao;
        }

        @Override
        protected Void doInBackground(Recipe... recipes) {
            mAsyncTaskDAO.update(recipes[0]);
            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<Recipe, Void, Void>{
        private RecipeDao mAsyncTaskDAO;

        public deleteAsyncTask(RecipeDao recipeDao){ mAsyncTaskDAO = recipeDao; }

        @Override
        protected Void doInBackground(Recipe... recipes) {
            mAsyncTaskDAO.delete(recipes[0]);
            return null;
        }
    }

    private static class duplicateAsyncTask extends AsyncTask<Recipe, Void, Void>{
        private RecipeDao mRecipeDao;
        private StepDao mStepDao;
        private IngredientDao mIngredientDao;

        public duplicateAsyncTask(RecipeDao recipeDao, StepDao stepDao, IngredientDao ingredientDao){
            mRecipeDao = recipeDao;
            mStepDao = stepDao;
            mIngredientDao = ingredientDao;
        }

        @Override
        protected Void doInBackground(Recipe... recipes) {
            Recipe recipe = recipes[0];
            int recipeId = recipe.getRecipeId();
            List<Step> steps = mStepDao.getFor(recipeId);
            List<Ingredient> ingredients = mIngredientDao.getFor(recipeId);

            Recipe newRecipe = new Recipe(recipe);
            Long newId = mRecipeDao.insert(newRecipe);

            for(Step step : steps){
                Step newStep = new Step(step);
                newStep.setRecipe(newId.intValue());
                mStepDao.insert(newStep);
            }

            for(Ingredient ingredient : ingredients){
                Ingredient newIngredient = new Ingredient(ingredient);
                newIngredient.setRecipe(newId.intValue());
                mIngredientDao.insert(newIngredient);
            }

            return null;
        }
    }

    private static class selectAsyncTask extends AsyncTask<Integer, Void, Recipe>{
        private RecipeDao mAsyncTaskDao;
        private RecipeRetrievedListener mCallback;

        public selectAsyncTask(RecipeDao recipeDao, RecipeRetrievedListener callback){
            mAsyncTaskDao = recipeDao;
            mCallback = callback;
        }

        @Override
        protected Recipe doInBackground(Integer... integers) {
            return mAsyncTaskDao.get(integers[0]);
        }

        @Override
        protected void onPostExecute(Recipe recipe) {
            if(mCallback != null){
                mCallback.onRecipeRetrieved(recipe);
            }

        }
    }
}
