package com.example.gamal.adnp3_bakingapp.BakingAppWidgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.RemoteViews;

import com.example.gamal.adnp3_bakingapp.MainActivity;
import com.example.gamal.adnp3_bakingapp.R;

/**
 * Implementation of App Widget functionality.
 */
public class BakingAppWidget extends AppWidgetProvider {
    public static int recipeId =-1;
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId,int recipeID) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);

        Intent mainScreen=new Intent(context,MainActivity.class);
        PendingIntent mainPendingIntent=PendingIntent.getActivity(context,0,mainScreen,PendingIntent.FLAG_UPDATE_CURRENT);

        views.setOnClickPendingIntent(R.id.tv_no_recipe_selected_error,mainPendingIntent);

        if(recipeID==-1){
            views.setViewVisibility(R.id.recipe_widget_ingredients,View.GONE);
            views.setViewVisibility(R.id.tv_no_recipe_selected_error,View.VISIBLE);
        }else{
            views.setViewVisibility(R.id.recipe_widget_ingredients,View.VISIBLE);
            views.setViewVisibility(R.id.tv_no_recipe_selected_error,View.GONE);

            Intent intent=new Intent(context.getApplicationContext(),BakingAppWidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            intent.putExtra("random",Math.random());
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            intent.putExtra("RECIPE ID",recipeID);

            views.setRemoteAdapter(R.id.recipe_widget_ingredients,intent);
        }

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    public static void updateListItems(Context context,AppWidgetManager appWidgetManager,int []appWidgetIds){
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

