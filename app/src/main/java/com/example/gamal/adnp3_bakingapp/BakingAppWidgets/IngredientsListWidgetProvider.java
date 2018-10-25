package com.example.gamal.adnp3_bakingapp.BakingAppWidgets;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.gamal.adnp3_bakingapp.Handlers.JSONHandler;
import com.example.gamal.adnp3_bakingapp.Models.Ingredients;
import com.example.gamal.adnp3_bakingapp.R;

import java.util.ArrayList;
import java.util.List;

public class IngredientsListWidgetProvider implements RemoteViewsService.RemoteViewsFactory {
    List<String> mIngredientsList = null;
    List<Ingredients> ingredients;
    private Context mContext = null;
    private int Id;

    public IngredientsListWidgetProvider(Context context, Intent intent) {
        mContext = context;
        if (intent.hasExtra("RECIPE ID")) {
            Id = intent.getIntExtra("RECIPE ID", -1);
        }
    }

    @Override
    public void onCreate() {
        mIngredientsList = new ArrayList<>();
        ingredients = new JSONHandler().getIngredients(Id, mContext);
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
