package com.example.gamal.adnp3_bakingapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.gamal.adnp3_bakingapp.Adapters.rv_RecipeAdapter;
import com.example.gamal.adnp3_bakingapp.Handlers.JSONHandler;
import com.example.gamal.adnp3_bakingapp.Models.Recipe;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements rv_RecipeAdapter.OnItemClickListener {

    private RecyclerView rv;
    private final String RECIPES_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    private List<Recipe> recipes;
    private ProgressBar bar;
    private Context mContext;
    rv_RecipeAdapter.OnItemClickListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rv = findViewById(R.id.rv);
        mContext = this;
        bar = findViewById(R.id.progress_bar);
        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(2, 1);
        rv.setLayoutManager(gridLayoutManager);
        rv.setHasFixedSize(true);
        recipes = new ArrayList<>();
        listener = this;
        getData();


    }

    @Override
    public void onItemClick(int Id) {
        Intent intent = new Intent(this, BakingSteps_Ingredients.class);
        intent.putExtra(BakingSteps_Ingredients.ID, Id);
        startActivity(intent);
    }

    public void getData() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, RECIPES_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                recipes = JSONHandler.getRecipes(response);
                rv.setAdapter(new rv_RecipeAdapter(mContext, recipes, listener));
                bar.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue queue = Volley.newRequestQueue(mContext);
        queue.add(stringRequest);
    }
}
