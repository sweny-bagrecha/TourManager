package com.example.user.groupexpensetracker.bean;

/**
 * Created by user on 3/11/2017.
 */

public class LatiLongi {


    private String location;
    private Double latitude;
    private Double longitude;

    public LatiLongi(String location, Double latitude, Double longitude) {
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public LatiLongi(Double latitude, Double longitude) {
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
}
