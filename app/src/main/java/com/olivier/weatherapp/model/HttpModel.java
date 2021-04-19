package com.olivier.weatherapp.model;

import java.io.Serializable;

public class HttpModel implements Serializable{
    //Todo: Remove query,cnt add lon lat
    private String httpUrl = "https://api.openweathermap.org/data/2.5/";
    private Double lon = 0.0;
    private Double lat = 0.0;
    private String units = "metric";
    private String excludes = "minutely";
    private String authorization = "1ef216a9ed374bfb72c959ef31347a1f";
    private String query = "Warsaw";

    public HttpModel() {
    }

    public HttpModel(String query) {
        this.query = query;
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

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
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
}
