package com.example.cookbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.ViewPager2;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.example.cookbook.adapters.RecipeViewPagerAdapter;
import com.example.cookbook.data.Recipe;
import com.example.cookbook.databinding.ActivityMainBinding;
import com.example.cookbook.listeners.RecipeSelectedListener;
import com.example.cookbook.viewmodel.DrawerViewModel;
import com.example.cookbook.viewmodel.RecipeViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.List;

public class MainActivity extends AppCompatActivity implements RecipeSelectedListener {

    private static final String SHARED_PREFERENCES_FILE = "com.example.cookbook.last_recipe";
    private static final String RECIPE_KEY = "currentRecipe";

    private SharedPreferences mPreferences;

    private ActivityMainBinding mBinding;

    private DrawerViewModel mDrawerViewModel;
    private RecipeViewModel mRecipeViewModel;

    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mDrawer;
    private SwipeRefreshLayout mSwipeRefresh;

    private Recipe mCachedRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View root = mBinding.getRoot();

        mPreferences = getSharedPreferences(SHARED_PREFERENCES_FILE, MODE_PRIVATE);

        mDrawerLayout = root.findViewById(R.id.drawer_layout);

        mDrawer = root.findViewById(R.id.nav_view);
        mDrawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                onRecipeSelected(item.getItemId());
                mDrawerLayout.closeDrawers();
                return true;
            }
        });

        View header = mBinding.navView.getHeaderView(0);
        FloatingActionButton addButton = header.findViewById(R.id.button_recipe_add);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Recipe recipe = new Recipe("New Recipe", "", false);
                mDrawerViewModel.insertRecipe(recipe, MainActivity.this);
                mDrawerLayout.closeDrawers();
            }
        });

        mDrawerViewModel = new ViewModelProvider(this).get(DrawerViewModel.class);
        mDrawerViewModel.getRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(List<Recipe> recipes) {
                buildTableOfContents(recipes);
            }
        });

        mRecipeViewModel = new ViewModelProvider(this).get(RecipeViewModel.class);
        mRecipeViewModel.getRecipe().observe(this, new Observer<Recipe>() {
            @Override
            public void onChanged(Recipe recipe) {
                mCachedRecipe = recipe;
                if(mCachedRecipe != null){
                    mBinding.contentMain.setRecipe(mCachedRecipe);
                    mToolbar.getMenu().setGroupVisible(R.id.group_recipe, true);
                    setBookmarked(recipe.getBookmarked());
                } else {
                    mBinding.contentMain.setRecipe(null);
                    mToolbar.getMenu().setGroupVisible(R.id.group_recipe, false);
                }
            }
        });

        mToolbar = mBinding.contentMain.toolbar;
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()){
                    case R.id.action_recipe_bookmark:{
                        mRecipeViewModel.setRecipeBookmarked(true);
                        setBookmarked(true);
                        return true;
                    }
                    case R.id.action_recipe_bookmark_remove:{
                        mRecipeViewModel.setRecipeBookmarked(false);
                        setBookmarked(false);
                        return true;
                    }
                    case R.id.action_recipe_duplicate:{
                        mRecipeViewModel.duplicateRecipe();
                        return true;
                    }
                }
                return false;
            }
        });

        //Sort tabs
        RecipeViewPagerAdapter adapter = new RecipeViewPagerAdapter(this);
        ViewPager2 viewPager = root.findViewById(R.id.viewpager_recipe);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = root.findViewById(R.id.tablayout_recipe);
        new TabLayoutMediator(
                tabLayout,
                viewPager,
                (tab, position) -> {
                    if(position == 0){
                        tab.setIcon(R.drawable.ic_numbered_list);
                    } else if(position == 1){
                        tab.setIcon(R.drawable.ic_shopping_cart);
                    }
                }
        ).attach();

        mSwipeRefresh = root.findViewById(R.id.swiperefresh_recipe);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                syncRecipe();
            }
        });

        FloatingActionButton fab = root.findViewById(R.id.fab_edit);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCachedRecipe != null){
                    Intent intent = new Intent(MainActivity.this, EditActivity.class);
                    intent.putExtra(EditActivity.ARG_RECIPE, mCachedRecipe.getRecipeId());
                    intent.putExtra(EditActivity.ARG_TAB, viewPager.getCurrentItem());
                    startActivity(intent);
                }
            }
        });

        setContentView(root);

        onRecipeSelected(mPreferences.getInt(RECIPE_KEY, -1));
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(RECIPE_KEY, mRecipeViewModel.getRecipeId());
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mRecipeViewModel.syncRecipe();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void buildTableOfContents(List<Recipe> recipes){
        Menu menu = mDrawer.getMenu();
        menu.clear();
        if(recipes != null){
            for(Recipe recipe : recipes){
                menu.add(0, recipe.getRecipeId(), 0, recipe.getTitle());
                if(recipe.getBookmarked()){
                    MenuItem item = menu.findItem(recipe.getRecipeId());
                    item.setIcon(R.drawable.ic_bookmark);
                    item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
                }
            }
        } else {
            //Alter header to show this
        }
    }

    private void syncRecipe(){
        mRecipeViewModel.syncRecipe();
        mSwipeRefresh.setRefreshing(false);
    }

    private void setBookmarked(boolean bookmarked){
        MenuItem bookmark = mToolbar.getMenu().findItem(R.id.action_recipe_bookmark);
        MenuItem unbookmark = mToolbar.getMenu().findItem(R.id.action_recipe_bookmark_remove);
        bookmark.setVisible(!bookmarked);
        unbookmark.setVisible(bookmarked);
    }

    @Override
    public void onRecipeSelected(int recipeId) {
        //This exists to catch the async insertRecipe stuff from the VMs
        mRecipeViewModel.setRecipe(recipeId);
    }

}