package com.example.gamal.adnp3_bakingapp.BakingAppWidgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.RemoteViews;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.gamal.adnp3_bakingapp.Handlers.JSONHandler;
import com.example.gamal.adnp3_bakingapp.MainActivity;
import com.example.gamal.adnp3_bakingapp.Models.Ingredients;
import com.example.gamal.adnp3_bakingapp.R;

import java.util.ArrayList;
import java.util.List;

import static com.example.gamal.adnp3_bakingapp.MainActivity.RECIPES_URL;

/**
 * Implementation of App Widget functionality.
 */
public class BakingAppWidget extends AppWidgetProvider {
    public static int recipeId = -1;

    static void updateAppWidget(final Context context, AppWidgetManager appWidgetManager,
                                final int appWidgetId, final int recipeID) {

        // Construct the RemoteViews object
        final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);

        Intent mainScreen = new Intent(context, MainActivity.class);
        PendingIntent mainPendingIntent = PendingIntent.getActivity(context, 0, mainScreen, PendingIntent.FLAG_UPDATE_CURRENT);

        views.setOnClickPendingIntent(R.id.tv_no_recipe_selected_error, mainPendingIntent);

        if (recipeID == -1) {
            views.setViewVisibility(R.id.recipe_widget_ingredients, View.GONE);
            views.setViewVisibility(R.id.tv_no_recipe_selected_error, View.VISIBLE);
        } else {
            views.setViewVisibility(R.id.recipe_widget_ingredients, View.VISIBLE);
            views.setViewVisibility(R.id.tv_no_recipe_selected_error, View.GONE);

            final int appWidgetIdFinal=appWidgetId;
            final Context mContext=context;
            ///
            StringRequest stringRequest = new StringRequest(Request.Method.GET, RECIPES_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    ArrayList<Ingredients> ingredients=new ArrayList<>(new JSONHandler().getIngredients(recipeID,response));
                    Intent intent = new Intent(mContext.getApplicationContext(), BakingAppWidgetService.class);
                    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIdFinal);
                    intent.putExtra("random", Math.random());
                    intent.putParcelableArrayListExtra("ingred",ingredients);
                    intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
                    views.setRemoteAdapter(R.id.recipe_widget_ingredients, intent);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            RequestQueue queue = Volley.newRequestQueue(context);
            queue.add(stringRequest);


        }

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    public static void updateListItems(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, recipeId);
        }

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, recipeId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        //handles broadcast messages to the receiver.

    }
}

