package com.olivier.weatherapp.model.weathermodels.current;

import com.google.gson.annotations.SerializedName;

public class Rain{

	@SerializedName("1h")
	private double jsonMember1h;

	public double getJsonMember1h(){
		return jsonMember1h;
	}
}