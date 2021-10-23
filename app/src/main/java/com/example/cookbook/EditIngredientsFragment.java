package com.example.cookbook;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cookbook.adapters.EditIngredientListAdapter;
import com.example.cookbook.data.Ingredient;
import com.example.cookbook.data.Recipe;
import com.example.cookbook.databinding.FragmentEditIngredientsBinding;
import com.example.cookbook.listeners.IngredientsAlteredListener;
import com.example.cookbook.listeners.RecipeUpdateApprovedListener;
import com.example.cookbook.viewmodel.EditIngredientsViewModel;
import com.example.cookbook.viewmodel.EditRecipeViewModel;

import java.util.List;

public class EditIngredientsFragment extends Fragment implements RecipeUpdateApprovedListener, IngredientsAlteredListener {

    private FragmentEditIngredientsBinding mBinding;

    private EditRecipeViewModel mRecipeViewModel;
    private EditIngredientsViewModel mIngredientsViewModel;

    private EditIngredientListAdapter mAdapter;
    private RecyclerView mRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentEditIngredientsBinding.inflate(inflater, container, false);

        mAdapter = new EditIngredientListAdapter(getActivity());

        mIngredientsViewModel = new ViewModelProvider(this).get(EditIngredientsViewModel.class);
        mIngredientsViewModel.getIngredients().observe(getViewLifecycleOwner(), new Observer<List<Ingredient>>() {
            @Override
            public void onChanged(List<Ingredient> ingredients) {
                if(ingredients != null){
                    mAdapter.setIngredients(ingredients);
                    mBinding.setEmpty(mIngredientsViewModel.isIngredientsEmpty());
                } else {
                    //TODO no ingredients found
                    mAdapter.setIngredients(null);
                    mBinding.setEmpty(false);//Hide button if null
                }
            }
        });

        mRecipeViewModel = new ViewModelProvider(getActivity()).get(EditRecipeViewModel.class);
        mRecipeViewModel.addRecipeUpdateApprovedListener(this);
        mRecipeViewModel.getRecipe().observe(getViewLifecycleOwner(), new Observer<Recipe>() {
            @Override
            public void onChanged(Recipe recipe) {
                if(recipe != null){
                    mIngredientsViewModel.setRecipe(recipe.getRecipeId());
                } else {
                    //TODO no recipe in parent
                    mAdapter.setIngredients(null);
                }
            }
        });

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = mBinding.recyclerviewEditIngredients;
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        ItemTouchHelper helper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.START | ItemTouchHelper.END) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                        int position = viewHolder.getAdapterPosition();
                        onDeleteIngredient(position);
                    }
                }
        );
        helper.attachToRecyclerView(mRecyclerView);

        mBinding.buttonIngredientAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onInsertIngredient(-1);
            }
        });
    }


    @Override
    public void onInsertIngredient(int position) {
        mIngredientsViewModel.insertIngredient(position);

        if(position != -1){
            mAdapter.notifyItemInserted(position);
            mRecyclerView.scrollToPosition(position);
        } else {
            mAdapter.notifyDataSetChanged();
        }
        mBinding.setEmpty(mIngredientsViewModel.isIngredientsEmpty());
    }

    @Override
    public void onDeleteIngredient(int position) {
        mIngredientsViewModel.deleteIngredient(position);
        mAdapter.notifyItemRemoved(position);
        mBinding.setEmpty(mIngredientsViewModel.isIngredientsEmpty());
    }

    @Override
    public void onRecipeUpdateApproved() {
        mIngredientsViewModel.updateIngredients();
    }

}