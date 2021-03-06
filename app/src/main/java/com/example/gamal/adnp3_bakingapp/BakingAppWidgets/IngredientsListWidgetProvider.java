package com.example.gamal.adnp3_bakingapp.BakingAppWidgets;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.gamal.adnp3_bakingapp.Adapters.rv_RecipeAdapter;
import com.example.gamal.adnp3_bakingapp.Handlers.JSONHandler;
import com.example.gamal.adnp3_bakingapp.Models.Ingredients;
import com.example.gamal.adnp3_bakingapp.R;

import java.util.ArrayList;
import java.util.List;

import static com.example.gamal.adnp3_bakingapp.MainActivity.RECIPES_URL;

public class IngredientsListWidgetProvider implements RemoteViewsService.RemoteViewsFactory {
    List<String> mIngredientsList = null;
    ArrayList<Ingredients> ingredients;
    private Context mContext = null;
    private int Id;
    Intent intent;
    public IngredientsListWidgetProvider(Context context, Intent intent) {
        mContext = context;
        this.intent=intent;

    }

    @Override
    public void onCreate() {
        mIngredientsList = new ArrayList<>();
        ingredients=intent.getParcelableArrayListExtra("ingred");
        updateIngredientsList();
    }

    private void updateIngredientsList() {
        String[] values = new String[ingredients.size()];
        for (int i = 0; i < ingredients.size(); i++) {
            values[i] = "*" + ingredients.get(i).getQuantity() + " " + ingredients.get(i).getMeasure() + " " + ingredients.get(i).getIngredient();
            mIngredientsList.add(values[i]);
        }

    }

    @Override
    public void onDataSetChanged() {
        Log.e("cc","datac chaned");
        if(ingredients!=null)
        updateIngredientsList();
    }

    @Override
    public void onDestroy() {
        mIngredientsList.clear();
    }

    @Override
    public int getCount() {
        if (mIngredientsList == null)
            return 0;

        return mIngredientsList.size();
    }

    //similar to getView in the adapter, but in thi case we return the RemoteView
    @Override
    public RemoteViews getViewAt(int i) {
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
        remoteViews.setTextViewText(R.id.single_text_view, mIngredientsList.get(i));

        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return mIngredientsList.size();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
