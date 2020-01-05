package com.example.venturemate;

import android.graphics.Bitmap;

import com.firebase.geofire.GeoFire;

public class Place {
    private String  placeName;
    private String  category;
    private String  description;
    private GeoFire coordinates;
    private String  route;
    private String  weather;
    private String  specialNotice;
    private String  nearestTown;
    private String  difficultes;
    private Bitmap  image;
    private int likes;

    /*public Place(String  placeName,String  category, String  description, GeoFire coordinates,String  route, String  weather,
    String  specialNotice, String  nearestTown, String  difficultes,Bitmap  image, int likes){
        this.placeName = placeName;
        this.category = category;
        this.description = description;
        this.coordinates = coordinates;
        this.route = route;
        this.weather = weather;
        this.specialNotice = specialNotice;
        this.nearestTown = nearestTown;
        this.difficultes = difficultes;
        this.image = image;
        this.likes = likes;

    }*/

    public Place(String  placeName,String  category){
        this.placeName = placeName;
        this.category = category;

    }

}
