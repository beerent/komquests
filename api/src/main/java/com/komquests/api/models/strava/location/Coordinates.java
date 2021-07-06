package com.komquests.api.models.strava.location;

public class Coordinates {
    private String longitude;
    private String latitude;

    public Coordinates(String longitude, String latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getLongitude() {
        return this.longitude;
    }

    public String getLatitude() {
        return this.latitude;
    }
}
