package com.example.taximap.map;

import java.util.Locale;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/*Customer info returned from the db: id, latlng, name, 
 * numPassenger, distance from the center point.*/
public class Customer {		
	private LatLng latlng;
	private String name;
	private int numPassenger;
	public MarkerOptions markerOptions;
	public Marker marker;
	
	public Customer(LatLng latlng,String name) {
		this.latlng=latlng;
		this.name=name;
	}
	public Customer(LatLng latlng,String name,int numPassenger) {
		this.latlng=latlng;
		this.name=name;
		this.numPassenger=numPassenger;
	}
	public String snippet(){
		return String.format(Locale.US,"%s,%d riders", this.name,this.numPassenger);
	}

}
