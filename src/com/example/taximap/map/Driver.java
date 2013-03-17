package com.example.taximap.map;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Driver {
	public LatLng latlng;
	private String name;
	private String company;
	private int rating;
	public MarkerOptions marker;
	
	public Driver(LatLng latlng,String name,String company, int rating) {
		this.latlng=latlng;
		this.name=name;
		this.company=company;
		this.rating=rating;
	}
	
	public String title(){
		return String.format("Driver %s", this.company,this.rating);
	}
	public String snippet(){
		return String.format("%s company, %d stars", this.company,this.rating);
	}
	


}
