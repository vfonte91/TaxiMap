package com.example.taximap.map;

import com.google.android.gms.maps.model.LatLng;

public class Customer {
	private LatLng latlng;
	private String name;
	private int numPassenger;
	private double distance;
	public Customer(LatLng latlng,String name,int numPassenger) {
		this.latlng=latlng;
		this.name=name;
		this.numPassenger=numPassenger;
	}
	public String snippet(){
		return String.format("%s,%d riders", this.name,this.numPassenger);
	}

}
