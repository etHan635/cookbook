
package com.example.cookbook.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookbook.R;
import com.example.cookbook.data.Step;
import com.example.cookbook.databinding.RecyclerviewEditStepsItemBinding;
import com.example.cookbook.databinding.RecyclerviewStepsItemBinding;
import com.example.cookbook.listeners.StepsAlteredListener;

import java.util.Collections;
import java.util.List;

public class EditStepListAdapter extends RecyclerView.Adapter {
    private final LayoutInflater mInflater;

    private StepsAlteredListener mListener;

    private List<Step> mSteps;

    public EditStepListAdapter(Context context, StepsAlteredListener listener){
        mInflater = LayoutInflater.from(context);
        mListener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerviewEditStepsItemBinding binding = DataBindingUtil.inflate(mInflater, R.layout.recyclerview_edit_steps_item, parent, false );
        return new StepViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        StepViewHolder stepHolder = (StepViewHolder) holder;
        if(mSteps != null){
            Step current = mSteps.get(position);
            stepHolder.mBinding.setStep(current);
        } else {
            stepHolder.mBinding.setStep(null);
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

    public List<Step> getSteps(){
        return mSteps;
    }

    private class StepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
        private final RecyclerviewEditStepsItemBinding mBinding;

        private StepViewHolder(RecyclerviewEditStepsItemBinding binding){
            super(binding.getRoot());
            mBinding = binding;
            mBinding.buttonEditStepMenu.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            PopupMenu popup = new PopupMenu(v.getContext(), v);
            popup.getMenuInflater().inflate(R.menu.menu_step_edit, popup.getMenu());
            popup.setOnMenuItemClickListener(this);
            popup.show();
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            int position = getAdapterPosition();
            switch(item.getItemId()){
                case R.id.action_step_add_above:
                    mListener.onInsertStep(position);
                    return true;
                case R.id.action_step_add_below:
                    mListener.onInsertStep(position + 1);
                    return true;
            }
            return false;
        }
    }

}
