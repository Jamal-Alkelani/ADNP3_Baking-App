package com.example.gamal.adnp3_bakingapp;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.gamal.adnp3_bakingapp.BakingAppWidgets.BakingAppWidget;
import com.example.gamal.adnp3_bakingapp.Fragments.RecipeDescriptionFragment;
import com.example.gamal.adnp3_bakingapp.Fragments.RecipeIngredientsFragment;
import com.example.gamal.adnp3_bakingapp.Fragments.RecipeStepsFragment;
import com.example.gamal.adnp3_bakingapp.Handlers.JSONHandler;
import com.example.gamal.adnp3_bakingapp.Models.Ingredients;
import com.example.gamal.adnp3_bakingapp.Models.Steps;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.List;

public class BakingSteps_Ingredients extends AppCompatActivity implements RecipeStepsFragment.onItemClickListener {
    private final String TAG = getClass().getName();
    public final static String ID = "RECIPE_ID";
    Intent intent;
    int Id;
    private boolean isTablet = false;
    private SimpleExoPlayerView exoPlayerView;
    private SimpleExoPlayer mExoPlayer;
    Bundle mSavedInstanceState;
    List<Steps> steps;
    RecipeDescriptionFragment recipeDescriptionFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baking_steps__ingredients);
        intent = getIntent();
        exoPlayerView = findViewById(R.id.exo_player);

        if (intent.getExtras() == null && savedInstanceState == null) {
            Log.e(TAG, "Activity Steps has No Extra Data");
            return;
        }


        mSavedInstanceState = savedInstanceState;
        if (mSavedInstanceState != null)
            Id = mSavedInstanceState.getInt("RECIPE ID", 0);
        else
            Id = intent.getIntExtra(ID, -1);

        BakingAppWidget.recipeId = Id;
        int widgetIDs[] = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), BakingAppWidget.class));

        BakingAppWidget.updateListItems(this, AppWidgetManager.getInstance(this), widgetIDs);

        if (findViewById(R.id.phone_layout) == null) {
            isTablet = true;

        } else {
            isTablet = false;
        }

        if (isTablet)
            handleTablet();
        else
            handlePhone();

    }

    private void handleTablet() {
        long position;
        if (mSavedInstanceState != null)
            position = mSavedInstanceState.getLong("video-position", 0);
        else
            position = 0;


        RecipeIngredientsFragment ingredientsFragment = new RecipeIngredientsFragment();
        RecipeStepsFragment recipeStepsFragment = new RecipeStepsFragment();
        recipeDescriptionFragment = new RecipeDescriptionFragment();
        recipeStepsFragment.setTablet(true);
        JSONHandler jsonHandler = new JSONHandler();
        List<Ingredients> ingredients = jsonHandler.getIngredients(Id, this);
        steps = jsonHandler.getSteps(Id, this);
        ingredientsFragment.setIngredients(ingredients);
        recipeStepsFragment.setSteps(steps);

        initializePlayer(steps.get(0).getVidepURL(), position);
        recipeDescriptionFragment.setDescription(steps.get(0).getDescription());

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.ingredients, ingredientsFragment)
                .add(R.id.steps, recipeStepsFragment)
                .add(R.id.description, recipeDescriptionFragment)
                .commit();
    }

    private void handlePhone() {
        JSONHandler jsonHandler = new JSONHandler();
        List<Ingredients> ingredients = jsonHandler.getIngredients(Id, this);
        List<Steps> steps = jsonHandler.getSteps(Id, this);
        RecipeIngredientsFragment ingredientsFragment = new RecipeIngredientsFragment();
        RecipeStepsFragment recipeStepsFragment = new RecipeStepsFragment();
        ingredientsFragment.setIngredients(ingredients);
        recipeStepsFragment.setSteps(steps);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.ingredients, ingredientsFragment)
                .add(R.id.steps, recipeStepsFragment)
                .commit();
    }

    @Override
    public void onStepClicked(int Id) {
        String v_url = "";
        String desc = "";
        for (int i = 0; i < steps.size(); i++) {
            if (steps.get(i).getId() == Id) {
                v_url = steps.get(i).getVidepURL();
                desc = steps.get(i).getDescription();
                break;
            }
        }
        recipeDescriptionFragment.setDescription(desc);
        setMediaSourceAndPrepareExoPlayer(v_url);
    }

    private void initializePlayer(String video_url, long position) {

        Bitmap icon = BitmapFactory.decodeResource(this.getResources(),
                R.drawable.ic_action_google_play);
        exoPlayerView.setDefaultArtwork(icon);

        if (mExoPlayer == null) {

            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();


            mExoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl);

            mExoPlayer.seekTo(position);
            exoPlayerView.setPlayer(mExoPlayer);
            setMediaSourceAndPrepareExoPlayer(video_url);

        }

    }

    private void setMediaSourceAndPrepareExoPlayer(String video_url) {
        String userAgent = Util.getUserAgent(this, "BakingApp");
        MediaSource mediaSource = new ExtractorMediaSource(
                Uri.parse(video_url),
                new DefaultDataSourceFactory(this, userAgent),
                new DefaultExtractorsFactory(), null, null);


        mExoPlayer.prepare(mediaSource);
        mExoPlayer.setPlayWhenReady(false);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mExoPlayer != null)
            outState.putLong("video-position", mExoPlayer.getCurrentPosition());
        outState.putInt("RECIPE ID", Id);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    private void releasePlayer() {

        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }

    }
}
