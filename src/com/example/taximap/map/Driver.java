package com.example.taximap.map;

import android.view.View;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/*Driver info returned from the db: 
 * id, name, latlng, company, rating, distance*/
public class Driver {
	public LatLng latlng;
	public String name;
	public String company;
	public int rating;
	public double distance;	
	public int capacity;
	public String lastlocation;
	public String phone;
	public MarkerOptions markerOptions;
	public Marker marker;
	public boolean isActive=true;
	public View view;
	
	public Driver(LatLng latlng,String name,String company, int rating, double distance, int capacity, String lastlocation, String phone) {
		this.latlng=latlng;
		this.name=name;
		this.company=company;
		this.rating=rating;
		this.distance=distance;
		this.capacity=capacity;
		this.lastlocation=lastlocation;
		this.phone=phone;
	}
	
	public String title(){
		return String.format("Driver: %s", this.name);
	}
	public String snippet(){
		return String.format("%s company, %d star, %.2f miles", this.company,this.rating,this.distance);
	}
}
