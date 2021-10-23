package com.example.cookbook.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookbook.R;
import com.example.cookbook.data.Ingredient;
import com.example.cookbook.databinding.RecyclerviewIngredientsItemBinding;
import com.example.cookbook.databinding.RecyclerviewStepsItemBinding;

import java.util.ArrayList;
import java.util.List;
//https://www.tutorialspoint.com/how-to-filter-a-recyclerview-with-a-searchview-on-android
public class IngredientListAdapter extends RecyclerView.Adapter {
    private final LayoutInflater mInflater;

    private List<Ingredient> mIngredients;

    public IngredientListAdapter(Context context){ mInflater = LayoutInflater.from(context); }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerviewIngredientsItemBinding binding = DataBindingUtil.inflate(mInflater, R.layout.recyclerview_ingredients_item, parent, false);
        return new IngredientViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        IngredientViewHolder ingredientViewHolder = (IngredientViewHolder) holder;
        if(mIngredients != null){
            Ingredient current = mIngredients.get(position);
            ingredientViewHolder.mBinding.setIngredient(current);
        } else {
            ingredientViewHolder.mBinding.setIngredient(null);
        }
    }

    @Override
    public int getItemCount() {
        if(mIngredients != null){
            return mIngredients.size();
        } else {
            return 0;
        }
    }

    public void setIngredients(List<Ingredient> ingredients){
        mIngredients = ingredients;
        notifyDataSetChanged();
    }

    private class IngredientViewHolder extends RecyclerView.ViewHolder{
        private final RecyclerviewIngredientsItemBinding mBinding;

        private IngredientViewHolder(RecyclerviewIngredientsItemBinding binding){
            super(binding.getRoot());
            mBinding = binding;
        }
    }

}
