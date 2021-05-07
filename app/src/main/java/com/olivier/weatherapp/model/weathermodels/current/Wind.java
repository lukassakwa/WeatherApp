package com.olivier.weatherapp.model.weathermodels.current;

import com.google.gson.annotations.SerializedName;

public class Wind{

	@SerializedName("deg")
	private int deg;

	@SerializedName("speed")
	private double speed;

	@SerializedName("gust")
	private double gust;

	public int getDeg(){
		return deg;
	}

	public double getSpeed(){
		return speed;
	}

	public double getGust(){
		return gust;
	}
}