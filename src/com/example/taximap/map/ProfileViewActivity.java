package com.example.taximap.map;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.os.Bundle;
import com.example.taximap.*;
import com.google.android.gms.maps.model.LatLng;

import android.widget.ListView;
import android.widget.TextView;

public class ProfileViewActivity extends Activity {
	private static Activity context;
	private static TextView name;
	private static TextView address;
	private static TextView location;
	private static TextView hail;
	
	private static String username;
	private static AccountManager mAccountManager;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mAccountManager = AccountManager.get(this);
		getAccountUsername();
		
		setContentView(R.layout.content_profile_layout);
		name = ((TextView) findViewById(R.id.name_text));
		address = ((TextView) findViewById(R.id.address_text));
		location = ((TextView) findViewById(R.id.location_text));
		hail = ((TextView) findViewById(R.id.hail_status));
		
		name.setText(String.format(username));
		address.setText(String.format(MapViewActivity.myLastAddress));
		location.setText(String.format("%.6f,%.6f", MapViewActivity.myLastLatLng.latitude, MapViewActivity.myLastLatLng.longitude));
		hail.setText(String.format("Haven't requested pick-up service."));
		
		context = this;
	}

	public static void updateLocation(String add, LatLng latlng) {
		if (address != null)
			address.setText(String.format(add));
		if (location != null)
			location.setText(String.format("%.6f,%.6f", latlng.latitude, latlng.longitude));
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
	
	private static void getAccountUsername(){
		Account[] accounts = mAccountManager.getAccountsByType(Constants.ACCOUNT_TYPE);
    	if (accounts.length > 0) {
    		Account userAccount = accounts[accounts.length - 1];
    		username = mAccountManager.getUserData(userAccount, Constants.USER_DATA_KEY);
    	}
	}
}