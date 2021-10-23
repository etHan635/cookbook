package com.example.cookbook.data;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.cookbook.listeners.IngredientsRetrievedListener;

import java.util.List;

public class IngredientRepository {
    private IngredientDao mDao;

    public IngredientRepository(Application application){
        RecipeRoomDatabase db = RecipeRoomDatabase.getDatabase(application);
        mDao = db.ingredientDao();
    }

    public void insert(Ingredient ingredient){ new insertAsyncTask(mDao).execute(ingredient); }

    public void update(Ingredient ingredient){ new updateAsyncTask(mDao).execute(ingredient); }

    public void delete(Ingredient ingredient){ new deleteAsyncTask(mDao).execute(ingredient); }

    //public LiveData<Ingredient> get(int ingredientId){ return mDao.get(ingredientId); }

    public void getFor(int recipe, IngredientsRetrievedListener callback){
        new selectAsyncTask(mDao, callback).execute(recipe);
    }

    private static class insertAsyncTask extends AsyncTask<Ingredient, Void, Void> {
        private IngredientDao mAsyncTaskDAO;

        public insertAsyncTask(IngredientDao ingredientDao){
            mAsyncTaskDAO = ingredientDao;
        }

        @Override
        protected Void doInBackground(Ingredient... ingredients) {
            mAsyncTaskDAO.insert(ingredients[0]);
            return null;
        }
    }

    private static class updateAsyncTask extends AsyncTask<Ingredient, Void, Void>{
        private IngredientDao mAsyncTaskDAO;

        public updateAsyncTask(IngredientDao ingredientDao){
            mAsyncTaskDAO = ingredientDao;
        }

        @Override
        protected Void doInBackground(Ingredient... ingredients) {
            mAsyncTaskDAO.update(ingredients[0]);
            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<Ingredient, Void, Void>{
        private IngredientDao mAsyncTaskDAO;

        public deleteAsyncTask(IngredientDao ingredientDao){
            mAsyncTaskDAO = ingredientDao;
        }

        @Override
        protected Void doInBackground(Ingredient... ingredients) {
            mAsyncTaskDAO.delete(ingredients[0]);
            return null;
        }
    }

    private static class selectAsyncTask extends AsyncTask<Integer, Void, List<Ingredient>>{
        private IngredientDao mAsyncTaskDao;
        private IngredientsRetrievedListener mCallback;

        public selectAsyncTask(IngredientDao ingredientDao, IngredientsRetrievedListener callback){
            mAsyncTaskDao = ingredientDao;
            mCallback = callback;
        }

        @Override
        protected List<Ingredient> doInBackground(Integer... integers) {
            return mAsyncTaskDao.getFor(integers[0]);
        }

        @Override
        protected void onPostExecute(List<Ingredient> ingredients) {
            if(mCallback != null){
                mCallback.onIngredientsRetrieved(ingredients);
            }
        }
    }
}
