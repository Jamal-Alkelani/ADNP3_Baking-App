package com.example.gamal.adnp3_bakingapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.gamal.adnp3_bakingapp.Fragments.RecipeDescriptionFragment;
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

public class BakingVideo_Description extends AppCompatActivity {
    public final static String DESCRIPTION = "DESC";
    public final static String VIDEO_URL = "URL";
    private SimpleExoPlayerView exoPlayerView;
    private SimpleExoPlayer mExoPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baking_video__description);
        Intent intent = getIntent();
        if (intent == null) {
            Toast.makeText(this, "An error occured in Video_Desc Activity (Intent is null)", Toast.LENGTH_SHORT).show();
            return;
        }
        exoPlayerView = findViewById(R.id.exo_player);
        String description = intent.getStringExtra(DESCRIPTION);
        String video_url = intent.getStringExtra(VIDEO_URL);
        Bitmap icon = BitmapFactory.decodeResource(this.getResources(),
                R.drawable.ic_action_google_play);
        exoPlayerView.setDefaultArtwork(icon);
        RecipeDescriptionFragment recipeDescriptionFragment = new RecipeDescriptionFragment();
        recipeDescriptionFragment.setDescription(description);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.description, recipeDescriptionFragment)
                .commit();

        long position;
        if (savedInstanceState != null)
            position = savedInstanceState.getLong("video-position", 0);
        else
            position = 0;
        initializePlayer(video_url, position);


    }

    private void initializePlayer(String video_url, long position) {

        if (mExoPlayer == null) {

            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();


            mExoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl);

            mExoPlayer.seekTo(position);
            exoPlayerView.setPlayer(mExoPlayer);

            String userAgent = Util.getUserAgent(this, "BakingApp");
            MediaSource mediaSource = new ExtractorMediaSource(
                    Uri.parse(video_url),
                    new DefaultDataSourceFactory(this, userAgent),
                    new DefaultExtractorsFactory(), null, null);


            mExoPlayer.prepare(mediaSource);

            mExoPlayer.setPlayWhenReady(false);

        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putLong("video-position", mExoPlayer.getCurrentPosition());

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
