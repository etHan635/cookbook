package com.example.cookbook.data;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.cookbook.listeners.StepsRetrievedListener;

import java.util.List;

public class StepRepository {
    private StepDao mDao;

    public StepRepository(Application application){
        RecipeRoomDatabase db = RecipeRoomDatabase.getDatabase(application);
        mDao = db.stepDao();
    }

    public void insert(Step step){ new insertAsyncTask(mDao).execute(step); }

    public void update(Step step){ new updateAsyncTask(mDao).execute(step); }

    public void delete(Step step){ new deleteAsyncTask(mDao).execute(step); }

    //public LiveData<Step> get(int stepId){ return mDao.get(stepId); }

    public void getFor(int recipe, StepsRetrievedListener callback){
        new selectAsyncTask(mDao, callback).execute(recipe);
    }

    private static class insertAsyncTask extends AsyncTask<Step, Void, Void> {
        private StepDao mAsyncTaskDAO;

        public insertAsyncTask(StepDao stepDao){
            mAsyncTaskDAO = stepDao;
        }

        @Override
        protected Void doInBackground(Step... steps) {
            mAsyncTaskDAO.insert(steps[0]);
            return null;
        }
    }

    private static class updateAsyncTask extends AsyncTask<Step, Void, Void>{
        private StepDao mAsyncTaskDAO;

        public updateAsyncTask(StepDao stepDao){
            mAsyncTaskDAO = stepDao;
        }

        @Override
        protected Void doInBackground(Step... steps) {
            mAsyncTaskDAO.update(steps[0]);
            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<Step, Void, Void>{
        private StepDao mAsyncTaskDAO;

        public deleteAsyncTask(StepDao stepDao){
            mAsyncTaskDAO = stepDao;
        }

        @Override
        protected Void doInBackground(Step... steps) {
            mAsyncTaskDAO.delete(steps[0]);
            return null;
        }
    }

    private static class selectAsyncTask extends AsyncTask<Integer, Void, List<Step>>{
        private StepDao mAsyncTaskDao;
        private StepsRetrievedListener mCallback;

        public selectAsyncTask(StepDao stepDao, StepsRetrievedListener callback){
            mAsyncTaskDao = stepDao;
            mCallback = callback;
        }

        @Override
        protected List<Step> doInBackground(Integer... integers) {
            return mAsyncTaskDao.getFor(integers[0]);
        }

        @Override
        protected void onPostExecute(List<Step> steps) {
            if(mCallback != null){
                mCallback.onStepsRetrieved(steps);
            }
        }
    }
}
