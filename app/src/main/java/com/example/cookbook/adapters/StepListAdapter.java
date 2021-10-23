package com.example.cookbook.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookbook.R;
import com.example.cookbook.data.Step;
import com.example.cookbook.databinding.RecyclerviewStepsItemBinding;

import java.util.List;

public class StepListAdapter extends RecyclerView.Adapter {
    private final LayoutInflater mInflater;

    private List<Step> mSteps;

    public StepListAdapter(Context context){
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerviewStepsItemBinding binding = DataBindingUtil.inflate(mInflater, R.layout.recyclerview_steps_item, parent, false );
        return new StepViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        StepViewHolder stepHolder = (StepViewHolder) holder;
        if(mSteps != null){
            Step current = mSteps.get(position);
            stepHolder.mBinding.setInstruction(current.getInstruction());
        } else {
            stepHolder.mBinding.setInstruction(null);
        }
    }

    @Override
    public int getItemCount() {
        if(mSteps != null){
            return mSteps.size();
        } else {
            return 0;
        }
    }

    public void setSteps(List<Step> steps){
        mSteps = steps;
        notifyDataSetChanged();
    }


    private class StepViewHolder extends RecyclerView.ViewHolder{
        private final RecyclerviewStepsItemBinding mBinding;

        private StepViewHolder(RecyclerviewStepsItemBinding binding){
            super(binding.getRoot());
            mBinding = binding;
        }
    }

}
