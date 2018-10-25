package com.example.gamal.adnp3_bakingapp.Handlers;

import android.content.Context;
import android.util.Log;

import com.example.gamal.adnp3_bakingapp.Models.Ingredients;
import com.example.gamal.adnp3_bakingapp.Models.Recipe;
import com.example.gamal.adnp3_bakingapp.Models.Steps;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class JSONHandler {
    private final static String TAG = JSONHandler.class.getName();

    public static String loadJSONFromAsset(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("RecipeDataJSON");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }

    public static List<Recipe> getRecipes(String jsonData) {
        if (jsonData == null) {
            LogErrorMessage();
        } else {
            List<Recipe> recipes = new ArrayList<>();
            try {
                JSONArray objects = new JSONArray(jsonData);
                for (int i = 0; i < objects.length(); i++) {
                    JSONObject object = (JSONObject) objects.get(i);
                    String name = object.getString("name");
                    int Id = object.getInt("id");
                    String servings = object.getString("servings");
                    String image = object.getString("image");
                    recipes.add(new Recipe(Id, name, 0, (int) Math.random() * 5, "0"));
                }
                return recipes;

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
        return null;
    }

    public static void LogErrorMessage() {
        Log.e(TAG, "An Error Occured While Handling Json Data in Json Handler (Data=null)");
    }

    public static List<Ingredients> getIngredients(int Id, Context context) {
        String jsonData = loadJSONFromAsset(context);
        if (jsonData == null) {
            LogErrorMessage();
        } else {
            List<Ingredients> ingredients = new ArrayList<>();
            try {
                JSONArray objects = new JSONArray(jsonData);
                for (int i = 0; i < objects.length(); i++) {
                    JSONObject object = (JSONObject) objects.get(i);
                    if (object.getInt("id") == Id) {

                        JSONArray array = object.getJSONArray("ingredients");
                        for (int j = 0; j < array.length(); j++) {
                            JSONObject ingredientsObj = array.getJSONObject(j);
                            int quentity = ingredientsObj.getInt("quantity");
                            String mesure = ingredientsObj.getString("measure");
                            String ingredient = ingredientsObj.getString("ingredient");
                            ingredients.add(new Ingredients(quentity, mesure, ingredient));
                        }
                    }
                }
                return ingredients;

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static List<Steps> getSteps(int Id, Context context) {
        String jsonData = loadJSONFromAsset(context);
        if (jsonData == null) {
            LogErrorMessage();
        } else {
            List<Steps> steps = new ArrayList<>();
            try {
                JSONArray objects = new JSONArray(jsonData);
                for (int i = 0; i < objects.length(); i++) {
                    JSONObject object = (JSONObject) objects.get(i);
                    if (object.getInt("id") == Id) {
                        JSONArray array = object.getJSONArray("steps");
                        for (int j = 0; j < array.length(); j++) {
                            JSONObject stepsObj = array.getJSONObject(j);
                            int _Id = stepsObj.getInt("id");
                            String shortDescription = stepsObj.getString("shortDescription");
                            String description = stepsObj.getString("description");
                            String videoURL = stepsObj.getString("videoURL");
                            String thumbnailURL = stepsObj.getString("thumbnailURL");
                            steps.add(new Steps(_Id, shortDescription, description, videoURL, thumbnailURL));
                        }
                    }
                }
                return steps;

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
