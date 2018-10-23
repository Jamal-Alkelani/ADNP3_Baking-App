package com.example.gamal.adnp3_bakingapp.Models;

public class Steps {
    int Id;
    String shortDescription;
    String description;
    String videpURL;
    String thumbnailURL;

    public int getId() {
        return Id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public String getVidepURL() {
        return videpURL;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public Steps(int id, String shortDescription, String description, String videpURL, String thumbnailURL) {

        Id = id;
        this.shortDescription = shortDescription;
        this.description = description;
        this.videpURL = videpURL;
        this.thumbnailURL = thumbnailURL;
    }
}
