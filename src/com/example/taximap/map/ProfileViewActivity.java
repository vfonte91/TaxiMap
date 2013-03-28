package com.example.taximap.map;

import android.app.Activity;
import android.os.Bundle;
import com.example.taximap.*;
import com.google.android.gms.maps.model.LatLng;

import android.widget.TextView;

public class ProfileViewActivity extends Activity {
	private static Activity context;
	private static TextView name;
	private static TextView address;
	private static TextView location;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_profile_layout);
        name=((TextView) findViewById(R.id.name_text));
	     address=((TextView) findViewById(R.id.address_text));
	     location=((TextView) findViewById(R.id.location_text));
	     name.setText(String.format("Name: %s", MapViewActivity.uName));
	     address.setText(String.format("Last Address: %s", MapViewActivity.myLastAddress));
	     location.setText(
        		String.format("Last Location: %.6f,%.6f", MapViewActivity.myLastLatLng.latitude,MapViewActivity.myLastLatLng.longitude));
        context=this;
    }
    public static void updateLocation(String add, LatLng latlng){
    	if(address!=null)
    		address.setText(String.format("Last Address: %s", add));
    	if(location!=null)
    		location.setText(String.format("Last Location: %.6f,%.6f", latlng.latitude,latlng.longitude));
    	
        
    }
}