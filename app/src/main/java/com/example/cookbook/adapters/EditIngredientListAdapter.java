package com.example.cookbook.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookbook.R;
import com.example.cookbook.data.Ingredient;
import com.example.cookbook.databinding.RecyclerviewEditIngredientsItemBinding;

import java.util.List;

public class EditIngredientListAdapter extends RecyclerView.Adapter {

    private final LayoutInflater mInflater;

    private List<Ingredient> mIngredients;

    public EditIngredientListAdapter(Context context){ mInflater = LayoutInflater.from(context); }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerviewEditIngredientsItemBinding binding = DataBindingUtil.inflate(mInflater, R.layout.recyclerview_edit_ingredients_item, parent, false);
        return new EditIngredientViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        EditIngredientViewHolder ingredientViewHolder = (EditIngredientViewHolder) holder;
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

    public int adapterPositionOf(Ingredient ingredient){
        int position = mIngredients.indexOf(ingredient);
        return position;
    }

    private class EditIngredientViewHolder extends RecyclerView.ViewHolder{
        private final RecyclerviewEditIngredientsItemBinding mBinding;

        private EditIngredientViewHolder(RecyclerviewEditIngredientsItemBinding binding){
            super(binding.getRoot());
            mBinding = binding;
        }
    }

}
