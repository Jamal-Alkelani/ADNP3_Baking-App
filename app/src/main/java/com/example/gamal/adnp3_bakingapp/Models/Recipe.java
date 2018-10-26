package com.example.gamal.adnp3_bakingapp.Models;

public class Recipe {
    private int Id;
    private String name;
    private String duration;
    private float rating;
    private int image;


    public Recipe(int Id, String name, int image, int rating, String duration) {
        this.Id=Id;
        this.name = name;
        this.image = image;
        this.rating = rating;
        this.duration = duration;
    }


    public String getName() {
        return name;
    }

    public int getId() {
        return Id;
    }

    public String getDuration() {
        return duration;
    }

    public float getRating() {
        return rating;
    }

    public int getImage() {
        return image;
    }
}
