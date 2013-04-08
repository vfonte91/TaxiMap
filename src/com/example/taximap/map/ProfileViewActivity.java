package com.example.taximap.map;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

import com.example.taximap.*;
import com.google.android.gms.maps.model.LatLng;

import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileViewActivity extends Activity {
	private static Activity context;
	private static TextView name;
	private static TextView address;
	private static TextView location;
	private static TextView hail;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.content_profile_layout);
		name = ((TextView) findViewById(R.id.name_text));
		address = ((TextView) findViewById(R.id.address_text));
		location = ((TextView) findViewById(R.id.location_text));
		hail = ((TextView) findViewById(R.id.hail_status));

		name.setText(String.format("Name: %s", MapViewActivity.uName));
		address.setText(String.format("Last Address: %s",
				MapViewActivity.myLastAddress));
		location.setText(String.format("Last Location: %.6f,%.6f",
				MapViewActivity.myLastLatLng.latitude,
				MapViewActivity.myLastLatLng.longitude));
		hail.setText(String.format("Haven't requested pick-up service."));
		context = this;
	}

	public static void updateLocation(String add, LatLng latlng) {
		if (address != null)
			address.setText(String.format("Last Address: %s", add));
		if (location != null)
			location.setText(String.format("Last Location: %.6f,%.6f",
					latlng.latitude, latlng.longitude));
	}
	
	public static void updateHail(String hailTime, String waitTime) {
		if (hail != null)
			hail.setText(String.format("Hailed at %s, driver arrives in about %s minutes",
				hailTime, waitTime));
	}
	public static void cancelHail(){
		if (hail != null)
			hail.setText(String.format("Pick-up service cancelled."));
	}
	
	private boolean doubleBackToExitPressedOnce = false;
	@Override
	public void onBackPressed() {		//this handler helps to reset the variable after 2 second.
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            Login.exitStatus=true;
            return;
        }
        //super.onBackPressed();
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
             doubleBackToExitPressedOnce=false;   
            }
        }, 2000);
    } 
	
	private static final String TAG = "-------------";
	@Override
	public void onStop(){
		super.onStop();
		Log.e(TAG, "profile onStop()");
		MapViewActivity.diableLocationUpdate();		// remove location updates after app exits 
	}
}