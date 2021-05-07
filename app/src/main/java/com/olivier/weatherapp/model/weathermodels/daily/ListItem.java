package com.olivier.weatherapp.model.weathermodels.daily;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListItem{

	@SerializedName("rain")
	private double rain;

	@SerializedName("sunrise")
	private int sunrise;

	@SerializedName("temp")
	private Temp temp;

	@SerializedName("deg")
	private int deg;

	@SerializedName("pressure")
	private int pressure;

	@SerializedName("clouds")
	private int clouds;

	@SerializedName("feels_like")
	private FeelsLike feelsLike;

	@SerializedName("speed")
	private double speed;

	@SerializedName("dt")
	private int dt;

	@SerializedName("pop")
	private double pop;

	@SerializedName("sunset")
	private int sunset;

	@SerializedName("weather")
	private List<WeatherItem> weather;

	@SerializedName("humidity")
	private int humidity;

	@SerializedName("gust")
	private double gust;

	public double getRain(){
		return rain;
	}

	public int getSunrise(){
		return sunrise;
	}

	public Temp getTemp(){
		return temp;
	}

	public int getDeg(){
		return deg;
	}

	public int getPressure(){
		return pressure;
	}

	public int getClouds(){
		return clouds;
	}

	public FeelsLike getFeelsLike(){
		return feelsLike;
	}

	public double getSpeed(){
		return speed;
	}

	public int getDt(){
		return dt;
	}

	public double getPop(){
		return pop;
	}

	public int getSunset(){
		return sunset;
	}

	public List<WeatherItem> getWeather(){
		return weather;
	}

	public int getHumidity(){
		return humidity;
	}

	public double getGust(){
		return gust;
	}
}