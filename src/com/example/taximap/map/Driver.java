package com.example.taximap.map;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/*Driver info returned from the db: 
 * id, name, latlng, company, rating, distance*/
public class Driver {
	public LatLng latlng;
	private String name;
	public String company;
	public int rating;
	public double distance;		
	public MarkerOptions markerOptions;
	public Marker marker;
	public boolean isActive=true;
	
	public Driver(LatLng latlng,String name,String company, int rating, double distance) {
		this.latlng=latlng;
		this.name=name;
		this.company=company;
		this.rating=rating;
		this.distance=distance;
	}
	
	public String title(){
		return String.format("Driver %s", this.company,this.rating);
	}
	public String snippet(){
		return String.format("%s company, %d stars, %f distance", this.company,this.rating,this.distance);
	}
}
