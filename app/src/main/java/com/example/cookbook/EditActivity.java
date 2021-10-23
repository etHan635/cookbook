package com.example.cookbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.ViewPager2;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.cookbook.adapters.EditRecipeViewPagerAdapter;
import com.example.cookbook.data.Recipe;
import com.example.cookbook.databinding.ActivityEditBinding;
import com.example.cookbook.listeners.RecipeUpdateApprovedListener;
import com.example.cookbook.viewmodel.EditRecipeViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class EditActivity extends AppCompatActivity implements RecipeUpdateApprovedListener {
    public static final String ARG_RECIPE = "recipeId";
    public static final String ARG_TAB = "currentTab";

    private ActivityEditBinding mBinding;

    private EditRecipeViewModel mEditRecipeViewModel;

    private SwipeRefreshLayout mSwipeRefresh;
    private EditText mTitleEditText;
    private Toolbar mToolbar;

    private Toolbar.OnMenuItemClickListener mToolbarMenuClickListener = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch(item.getItemId()){
                case R.id.action_recipe_delete:{
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(EditActivity.this).setTitle("Delete Recipe").setMessage("Are you sure that you want to permanently delete this recipe?");
                    alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mEditRecipeViewModel.deleteRecipe();
                            finish();
                        }
                    });
                    alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    alertDialog.show();
                    return true;
                }
            }
            return false;
        }
    };

    private View.OnClickListener mFabOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mEditRecipeViewModel.approveRecipeUpdate();
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = ActivityEditBinding.inflate(getLayoutInflater());

        mToolbar = mBinding.toolbarRecipeEdit;
        mToolbar.setOnMenuItemClickListener(mToolbarMenuClickListener);

        mSwipeRefresh = mBinding.swiperefreshEditRecipe;
        mSwipeRefresh.setEnabled(false);

        Intent intent = getIntent();
        int recipeId = intent.getIntExtra(ARG_RECIPE, -1);
        int currentTab = intent.getIntExtra(ARG_TAB, 0);

        mEditRecipeViewModel = new ViewModelProvider(this).get(EditRecipeViewModel.class);
        mEditRecipeViewModel.addRecipeUpdateApprovedListener(this);
        mEditRecipeViewModel.getRecipe().observe(this, new Observer<Recipe>() {
            @Override
            public void onChanged(Recipe recipe) {
                mSwipeRefresh.setRefreshing(false);
                mBinding.setRecipe(recipe);
            }
        });

        //Sort tabs
        EditRecipeViewPagerAdapter adapter = new EditRecipeViewPagerAdapter(this);
        ViewPager2 viewPager = mBinding.viewpagerEditRecipe;
        viewPager.setUserInputEnabled(false);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = mBinding.tablayoutEditRecipe;
        new TabLayoutMediator(
                tabLayout, viewPager,
                (tab, position) -> {
                    if(position == 0){
                        tab.setIcon(R.drawable.ic_numbered_list);
                    } else if(position == 1){
                        tab.setIcon(R.drawable.ic_shopping_cart);
                    }
                }
        ).attach();

        int tabCount = adapter.getItemCount();
        if(0 <= currentTab && currentTab < tabCount){
            viewPager.setCurrentItem(currentTab);
        }

        mTitleEditText = mBinding.edittextRecipeTitle;

        FloatingActionButton fab = mBinding.fabEditEnd;
        fab.setOnClickListener(mFabOnClickListener);

        mSwipeRefresh.setRefreshing(true);
        mEditRecipeViewModel.setRecipe(recipeId);

        setContentView(mBinding.getRoot());
    }

    @Override
    public void onRecipeUpdateApproved() {
        Recipe recipe = mEditRecipeViewModel.getRecipe().getValue();
        if(recipe != null){
            mEditRecipeViewModel.updateRecipe();
        }
    }
}