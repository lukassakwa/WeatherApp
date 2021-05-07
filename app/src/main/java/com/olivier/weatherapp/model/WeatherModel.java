package com.olivier.weatherapp.model;

import java.io.Serializable;

public class WeatherModel implements Serializable{
    private String httpUrl = "https://api.openweathermap.org/data/2.5/";
    private Double lon = 0.0;
    private Double lat = 0.0;
    private String units = "metric";
    private String excludes = "current,minutely";
    private String authorization = "your api";
    private String city = "";

    public WeatherModel() {
    }

    public WeatherModel(Double lon, Double lat) {
        this.lon = lon;
        this.lat = lat;
    }

    public WeatherModel(Double lon, Double lat, String city) {
        this.lon = lon;
        this.lat = lat;
        this.city = city;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public String getHttpUrl() {
        return httpUrl;
    }

    public String getExcludes() {
        return excludes;
    }

    public void setExcludes(String excludes) {
        this.excludes = excludes;
    }

    public String getUnits() {
        return units;
    }

    public String getAuthorization() {
        return authorization;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
