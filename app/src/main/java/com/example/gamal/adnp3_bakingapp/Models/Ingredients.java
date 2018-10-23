package com.example.gamal.adnp3_bakingapp.Models;

public class Ingredients {
    int quantity;
    String measure;
    String ingredient;

    public int getQuantity() {
        return quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public String getIngredient() {
        return ingredient;
    }

    public Ingredients(int quantity, String measure, String ingredient) {
        this.quantity = quantity;
        this.measure = measure;
        this.ingredient = ingredient;
    }
}
