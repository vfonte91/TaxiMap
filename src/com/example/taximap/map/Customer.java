package com.example.taximap.map;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/*Customer info returned from the db: id, latlng, name, 
 * numPassenger, distance from the center point.*/
public class Customer {		
	private LatLng latlng;
	private String name;
	private int numPassenger;
	private double distance;
	public MarkerOptions marker;
	
	public Customer(LatLng latlng,String name,int numPassenger) {
		this.latlng=latlng;
		this.name=name;
		this.numPassenger=numPassenger;
	}
	public String snippet(){
		return String.format("%s,%d riders", this.name,this.numPassenger);
	}

}
