package com.example.cookbook;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cookbook.adapters.EditStepListAdapter;
import com.example.cookbook.adapters.StepListAdapter;
import com.example.cookbook.data.Recipe;
import com.example.cookbook.data.Step;
import com.example.cookbook.databinding.FragmentEditStepsBinding;
import com.example.cookbook.databinding.FragmentStepsBinding;
import com.example.cookbook.listeners.RecipeUpdateApprovedListener;
import com.example.cookbook.listeners.StepsAlteredListener;
import com.example.cookbook.viewmodel.EditRecipeViewModel;
import com.example.cookbook.viewmodel.EditStepsViewModel;
import com.example.cookbook.viewmodel.RecipeViewModel;
import com.example.cookbook.viewmodel.StepsViewModel;

import java.util.List;

public class EditStepsFragment extends Fragment implements RecipeUpdateApprovedListener, StepsAlteredListener {

    private FragmentEditStepsBinding mBinding;

    private EditRecipeViewModel mRecipeViewModel;
    private EditStepsViewModel mStepsViewModel;

    private EditStepListAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentEditStepsBinding.inflate(inflater, container, false);

        mAdapter = new EditStepListAdapter(getActivity(), this);

        mStepsViewModel = new ViewModelProvider(this).get(EditStepsViewModel.class);
        mStepsViewModel.getSteps().observe(getViewLifecycleOwner(), new Observer<List<Step>>() {
            @Override
            public void onChanged(List<Step> steps) {
                if(steps != null){
                    mAdapter.setSteps(steps);
                    mBinding.setEmpty(mStepsViewModel.isStepsEmpty());
                } else {
                    mAdapter.setSteps(null);
                    mBinding.setEmpty(false);//Hide button if null
                }
            }
        });

        //Get recipe view model to watch the recipe stuff
        mRecipeViewModel = new ViewModelProvider(getActivity()).get(EditRecipeViewModel.class);
        mRecipeViewModel.addRecipeUpdateApprovedListener(this);
        mRecipeViewModel.getRecipe().observe(getViewLifecycleOwner(), new Observer<Recipe>() {
            @Override
            public void onChanged(Recipe recipe) {
                if(recipe != null){
                    mStepsViewModel.setRecipe(recipe.getRecipeId());
                } else {
                    //TODO no recipe in parent
                    mAdapter.setSteps(null);
                }
            }
        });

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = mBinding.recyclerviewEditSteps;
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        ItemTouchHelper helper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.START | ItemTouchHelper.END) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                        int from = viewHolder.getAdapterPosition();
                        int to = target.getAdapterPosition();
                        onMoveStep(from, to);
                        return true;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                        int position = viewHolder.getAdapterPosition();
                        onDeleteStep(position);
                    }
                }
        );
        helper.attachToRecyclerView(recyclerView);

        mBinding.buttonStepAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onInsertStep(mAdapter.getItemCount());
            }
        });

    }

    @Override
    public void onRecipeUpdateApproved() {
        mStepsViewModel.updateSteps();
    }

    @Override
    public void onInsertStep(int position) {
        mStepsViewModel.insertStep(position);
        mAdapter.notifyItemInserted(position);
        mBinding.setEmpty(mStepsViewModel.isStepsEmpty());
    }

    @Override
    public void onMoveStep(int from, int to) {
        mStepsViewModel.moveStep(from, to);
        mAdapter.notifyItemMoved(from, to);
    }

    @Override
    public void onDeleteStep(int position) {
        mStepsViewModel.removeStep(position);
        mAdapter.notifyItemRemoved(position);
        mBinding.setEmpty(mStepsViewModel.isStepsEmpty());
    }
}