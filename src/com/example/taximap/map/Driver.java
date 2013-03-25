package com.example.taximap.map;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/*Driver info returned from the db: 
 * id, name, latlng, company, rating, distance*/
public class Driver {
	public LatLng latlng;
	public String name;
	public String company;
	public int rating;
	public double distance;		
	public MarkerOptions marker;
	public boolean isActive=true;
	
	public Driver(LatLng latlng,String name,String company, int rating) {
		this.latlng=latlng;
		this.name=name;
		this.company=company;
		this.rating=rating;
	}
	
	public String title(){
		return String.format("%s", this.name,this.rating);
	}
	public String snippet(){
		return String.format("%s, %d stars", this.company,this.rating);
	}
	


}
