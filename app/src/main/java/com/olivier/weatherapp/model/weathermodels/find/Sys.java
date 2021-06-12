package com.olivier.weatherapp.model.weathermodels.find;

import com.google.gson.annotations.SerializedName;

public class Sys{

	@SerializedName("country")
	private String country;

	public String getCountry(){
		return country;
	}
}