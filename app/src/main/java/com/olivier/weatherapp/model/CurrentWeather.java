package com.olivier.weatherapp.model;

import java.io.Serializable;

public class CurrentWeather implements Serializable {
    private double temp;
    private String description;
    private double feels_temp;
    private int pressure;
    private int humidity;
    private int visibility;
    private double uv;
    private double speed;
    private int degree;

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getFeels_temp() {
        return feels_temp;
    }

    public void setFeels_temp(double feels_temp) {
        this.feels_temp = feels_temp;
    }

    public int getPressure() {
        return pressure;
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public double getUv() {
        return uv;
    }

    public void setUv(double uv) {
        this.uv = uv;
    }

    public int getDegree() {
        return degree;
    }

    public void setDegree(int degree) {
        this.degree = degree;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }
}
