package com.olivier.weatherapp.model.weathermodels.onecall;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeatherModel{

	@SerializedName("current")
	private Current current;

	@SerializedName("timezone")
	private String timezone;

	@SerializedName("timezone_offset")
	private int timezoneOffset;

	@SerializedName("daily")
	private List<DailyItem> daily;

	@SerializedName("lon")
	private double lon;

	@SerializedName("hourly")
	private List<HourlyItem> hourly;

	@SerializedName("lat")
	private double lat;

	public void setCurrent(Current current){
		this.current = current;
	}

	public Current getCurrent(){
		return current;
	}

	public void setTimezone(String timezone){
		this.timezone = timezone;
	}

	public String getTimezone(){
		return timezone;
	}

	public void setTimezoneOffset(int timezoneOffset){
		this.timezoneOffset = timezoneOffset;
	}

	public int getTimezoneOffset(){
		return timezoneOffset;
	}

	public void setDaily(List<DailyItem> daily){
		this.daily = daily;
	}

	public List<DailyItem> getDaily(){
		return daily;
	}

	public void setLon(double lon){
		this.lon = lon;
	}

	public double getLon(){
		return lon;
	}

	public void setHourly(List<HourlyItem> hourly){
		this.hourly = hourly;
	}

	public List<HourlyItem> getHourly(){
		return hourly;
	}

	public void setLat(double lat){
		this.lat = lat;
	}

	public double getLat(){
		return lat;
	}
}