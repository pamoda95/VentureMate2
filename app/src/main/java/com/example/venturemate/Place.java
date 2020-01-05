package com.example.venturemate;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;


public class Place implements Parcelable {
    private String  placeName,placeId;
    private String  category;
    private String  description;
    private Double latitude,longitude;
    private String  route;
    private String  weather;
    private String  specialNotice;
    private String  nearestTown;
    private String  difficultes;
    private String  image;
    private String likes;
    private String comments;

    protected Place(Parcel in) {
        placeName = in.readString();
        placeId = in.readString();
        category = in.readString();
        description = in.readString();
        if (in.readByte() == 0) {
            latitude = null;
        } else {
            latitude = in.readDouble();
        }
        if (in.readByte() == 0) {
            longitude = null;
        } else {
            longitude = in.readDouble();
        }
        route = in.readString();
        weather = in.readString();
        specialNotice = in.readString();
        nearestTown = in.readString();
        difficultes = in.readString();
        image = in.readString();
        likes = in.readString();
        comments = in.readString();
    }

    public static final Creator<Place> CREATOR = new Creator<Place>() {
        @Override
        public Place createFromParcel(Parcel in) {
            return new Place(in);
        }

        @Override
        public Place[] newArray(int size) {
            return new Place[size];
        }
    };

    public String getPlaceName() {
        return placeName;
    }

    public String getPlaceId() {
        return placeId;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public String getRoute() {
        return route;
    }

    public String getWeather() {
        return weather;
    }

    public String getSpecialNotice() {
        return specialNotice;
    }

    public String getNearestTown() {
        return nearestTown;
    }

    public String getDifficultes() {
        return difficultes;
    }

    public String getImage() {
        return image;
    }

    public String getLikes() {
        return likes;
    }

    public String getComments() {
        return comments;
    }



    public Place(String  placeName, String placeId,String  category, String  description, Double latitude,Double longitude,String  route,
    String  specialNotice, String  nearestTown, String  difficultes,String  image, String likes, String comments){
        this.placeName = placeName;
        this.category = category;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.route = route;
        this.specialNotice = specialNotice;
        this.nearestTown = nearestTown;
        this.difficultes = difficultes;
        this.image = image;
        this.likes = likes;
        this.comments =comments;
        this.placeId = placeId;

    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(placeName);
        parcel.writeString(placeId);
        parcel.writeString(category);
        parcel.writeString(description);
        if (latitude == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(latitude);
        }
        if (longitude == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(longitude);
        }
        parcel.writeString(route);
        parcel.writeString(weather);
        parcel.writeString(specialNotice);
        parcel.writeString(nearestTown);
        parcel.writeString(difficultes);
        parcel.writeString(image);
        parcel.writeString(likes);
        parcel.writeString(comments);
    }
}
