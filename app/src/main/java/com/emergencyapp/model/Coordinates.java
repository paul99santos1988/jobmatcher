package com.emergencyapp.model;

/**
 * Created by pauljosephdarsantos on 3/3/16.
 */
public final class Coordinates {
    double longitude, latitude;

    // Empty Constructor
    public Coordinates () {

    }

    // Constructor
    public  Coordinates(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
