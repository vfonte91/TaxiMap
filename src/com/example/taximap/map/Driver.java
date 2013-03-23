package com.example.taximap.map;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/*Driver info returned from the db: 
 * id, name, latlng, company, rating, distance*/
public class Driver {
	public LatLng latlng;
	private String name;
	private String company;
	private int rating;
	private double distance;		
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
