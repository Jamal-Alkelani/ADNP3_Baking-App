package com.example.gamal.adnp3_bakingapp.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Steps implements Parcelable {
    int Id;
    String shortDescription;
    String description;
    String videpURL;
    String thumbnailURL;

    protected Steps(Parcel in) {
        Id = in.readInt();
        shortDescription = in.readString();
        description = in.readString();
        videpURL = in.readString();
        thumbnailURL = in.readString();
    }

    public static final Creator<Steps> CREATOR = new Creator<Steps>() {
        @Override
        public Steps createFromParcel(Parcel in) {
            return new Steps(in);
        }

        @Override
        public Steps[] newArray(int size) {
            return new Steps[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(Id);
        parcel.writeString(shortDescription);
        parcel.writeString(description);
        parcel.writeString(videpURL);
        parcel.writeString(thumbnailURL);
    }
}
