package com.example.gamal.adnp3_bakingapp;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.gamal.adnp3_bakingapp.Adapters.rv_RecipeAdapter;
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

import static com.example.gamal.adnp3_bakingapp.MainActivity.RECIPES_URL;

public class BakingStepsIngredients extends AppCompatActivity implements RecipeStepsFragment.onItemClickListener {
    private final String TAG = getClass().getSimpleName();
    public final static String ID = "RECIPE_ID";
    Intent intent;
    int Id;
    private boolean isTablet = false;
    private SimpleExoPlayerView exoPlayerView;
    private SimpleExoPlayer mExoPlayer;
    Bundle mSavedInstanceState;
    List<Steps> steps;
    RecipeDescriptionFragment recipeDescriptionFragment;
    long position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baking_steps__ingredients);

        if (savedInstanceState != null)
            intent = savedInstanceState.getParcelable("intent");
        else
            intent = getIntent();
        exoPlayerView = findViewById(R.id.exo_player);
        if (intent.getExtras() == null) {
            Log.e(TAG, "Activity Steps has No Extra Data");
            return;
        }

        Id = intent.getIntExtra(ID, -1);

        BakingAppWidget.recipeId = Id;
        int widgetIDs[] = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), BakingAppWidget.class));

        BakingAppWidget.updateListItems(this, AppWidgetManager.getInstance(this), widgetIDs);

        if (mSavedInstanceState != null)
            position = mSavedInstanceState.getLong("video-position", 0);
        else
            position = 0;

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
        StringRequest stringRequest = new StringRequest(Request.Method.GET, RECIPES_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                RecipeIngredientsFragment ingredientsFragment = new RecipeIngredientsFragment();
                RecipeStepsFragment recipeStepsFragment = new RecipeStepsFragment();
                recipeDescriptionFragment = new RecipeDescriptionFragment();
                recipeStepsFragment.setTablet(true);
                JSONHandler jsonHandler = new JSONHandler();
                List<Ingredients> ingredients = jsonHandler.getIngredients(Id, response);
                steps = jsonHandler.getSteps(Id, response);
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
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }

    private void handlePhone() {


        StringRequest stringRequest = new StringRequest(Request.Method.GET, RECIPES_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONHandler jsonHandler = new JSONHandler();
                List<Ingredients> ingredients = jsonHandler.getIngredients(Id, response);
                List<Steps> steps = jsonHandler.getSteps(Id, response);
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
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
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

        if (video_url.equals("")) {
            exoPlayerView.setVisibility(View.GONE);
            findViewById(R.id.video_not_available).setVisibility(View.VISIBLE);
            return;
        }
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
        outState.putParcelable("intent", getIntent());
        if (mExoPlayer != null)
            outState.putLong("video-position", mExoPlayer.getCurrentPosition());
        outState.putInt("RECIPE ID", Id);

    }


    private void releasePlayer() {

        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mExoPlayer != null) {
            mExoPlayer.setPlayWhenReady(false);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mExoPlayer != null) {
            mExoPlayer.stop();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mExoPlayer != null) {
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mExoPlayer != null) {
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }


}
