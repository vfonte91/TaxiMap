package com.example.taximap.map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.example.taximap.R;
import com.example.taximap.db.QueryDatabaseCustomerLoc;
import com.example.taximap.db.QueryDatabaseDriverLoc;
import com.example.taximap.db.QueryDatabaseLogin;
import com.example.taximap.db.QueryDatabaseUpdateLoc;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MapViewActivity extends FragmentActivity implements
		OnClickListener, LocationListener, LocationSource {

	private static GoogleMap gmap;
	public static String markerType = "driver"; // set upon login either
												// "driver" or
	public static List<Driver> driverLst;
	public static List<Customer> customerLst;
	
	
	private static LatLngBounds.Builder boundsBuilder;
	private static LatLngBounds currentBounds = null;
	private static TextView myLocationField = null;

	public static String uID = "";
	public static double myLastLat = 0;
	public static double myLastLng = 0;
	public static String myLastAddress = null;

	/* private static String bestProvider = null; */
	private static OnLocationChangedListener mListener;

	private static LocationManager locationManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.content_map_layout);
		
		setUpMapIfNeeded();

		((Button) findViewById(R.id.load)).setOnClickListener(this);
		((Button) findViewById(R.id.filters_setting)).setOnClickListener(this);
		((Button) findViewById(R.id.update_loc)).setOnClickListener(this);
		myLocationField = (TextView) findViewById(R.id.current_location);

	}

	private void enableLocationUpdate(){
		gmap.setMyLocationEnabled(true);
		locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		if (locationManager != null) { 
			boolean gpsIsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER); 
			boolean networkIsEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
			//update real time location every 5s
			if (gpsIsEnabled) { 
				locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000L, 10F, this); 
			} 
			else if(networkIsEnabled) { 
				locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000L, 10F, this); } 
			else { // Show an error dialog that GPS is disabled... 
				Toast.makeText(this,"GPS is disabled", Toast.LENGTH_SHORT);
			} 
		} else { // Show some generic error dialog because something must have gone wrong with
			Toast.makeText(this,"Location manager error", Toast.LENGTH_SHORT);
		}
	}
	
	private void disableLocationUpdate(){
		gmap.setMyLocationEnabled(false);
		locationManager.removeUpdates(this);
	}
	private void setUpMapIfNeeded() {
		// Do a null check to confirm that we have not already instantiated the
		// map.
		if (gmap == null) {
			// Try to obtain the map from the SupportMapFragment.
			gmap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();
			// Check if we were successful in obtaining the map.

			if (gmap != null) {
				setupMapView();
			}
			// This is how you register the LocationSource
			gmap.setLocationSource(this);
		}
	}

	private void setupMapView() {
		UiSettings settings = gmap.getUiSettings();
		settings.setAllGesturesEnabled(true);
		settings.setCompassEnabled(true);
		settings.setMyLocationButtonEnabled(true);
		settings.setRotateGesturesEnabled(true);
		settings.setScrollGesturesEnabled(true);
		settings.setTiltGesturesEnabled(true);
		settings.setZoomControlsEnabled(true);
		settings.setZoomGesturesEnabled(true);

		gmap.animateCamera(CameraUpdateFactory
				.newCameraPosition(new CameraPosition(new LatLng(39.983434,
						-83.003082), 13.5f, 30f, 112.5f))); // zoom, tilt,
															// bearing
		gmap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
		gmap.setTrafficEnabled(true);
	}


	private static void loadData() {
		Map<String, Map<String, String>> filters = FilterActivity.filters;
		if (markerType == "driver")
			for (Driver driver : driverLst) {
				MarkerOptions marker;
				BitmapDescriptor icon = BitmapDescriptorFactory
						.fromResource(R.drawable.taxidefault);
				marker = new MarkerOptions().position(driver.latlng)
						.title(driver.title()).snippet(driver.snippet())
						.icon(icon);
				driver.marker = marker;
			}
		else if (markerType == "customer") {
		}

	}

	public static void loadMarkers() {
		gmap.clear();
		loadData();
		boundsBuilder = new LatLngBounds.Builder();
		if (markerType == "driver") {
			for (Driver d : driverLst) {
				gmap.addMarker(d.marker);
				boundsBuilder.include(d.latlng);
			}
			currentBounds = boundsBuilder.build();
			gmap.moveCamera(CameraUpdateFactory.newLatLngBounds(currentBounds,
					30));
		} else if (markerType == "customer") {

		}
	}

	public static void callDB() {
		if (markerType == "driver") {
			(new QueryDatabaseDriverLoc()).execute("1"); // pass in uid. modify
		} else {
			(new QueryDatabaseCustomerLoc()).execute("1");
		}
	}

	public void onClick(View v) {
		Log.d("----", Integer.toString(v.getId()));
		switch (v.getId()) {
		case R.id.load:
			callDB();
			break;
		case R.id.filters_setting:
			startActivity(new Intent(this, FilterActivity.class));
			break;
		case R.id.update_loc:
			Button bt=(Button)findViewById(R.id.update_loc);
			Log.i("---", (String) bt.getText());
			
			if(bt.getText().equals("update")){
				enableLocationUpdate();
				bt.setText("stop");
			}else{
				disableLocationUpdate();
				bt.setText("update");
			}
			break;
		}
	}

	/*
	 * @Override public void onPause() { if (locationManager != null) {
	 * locationManager.removeUpdates(this); }
	 * 
	 * super.onPause(); }
	 * 
	 * @Override public void onResume() { super.onResume();
	 * 
	 * setUpMapIfNeeded();
	 * 
	 * if (locationManager != null) { gmap.setMyLocationEnabled(true); } }
	 */

	@Override
	public void activate(OnLocationChangedListener listener) {
		mListener = listener;
	}

	@Override
	public void deactivate() {
		mListener = null;
	}

	@Override
	public void onLocationChanged(Location location) {
		if (mListener != null) {
			mListener.onLocationChanged(location);
			double latitude = location.getLatitude();
			double longitude = location.getLongitude();
			LatLng point=new LatLng(latitude, longitude);
			GeolocationHelper.getAddressFromLocation(location, this,
					new GeocoderHandler());

			CameraPosition cp = new CameraPosition.Builder()
					.target(point).zoom(15).build();
			gmap.animateCamera(CameraUpdateFactory.newCameraPosition(cp));
			if(currentBounds==null){
				boundsBuilder = new LatLngBounds.Builder();
			}
			boundsBuilder.include(point);
			currentBounds=boundsBuilder.build();
			gmap.moveCamera(CameraUpdateFactory.newLatLngBounds(
						currentBounds, 30));
			
			myLastLat = latitude;
			myLastLng = longitude;
		}

	}

	private static class GeocoderHandler extends Handler {
		@Override
		public void handleMessage(Message message) {
			String result;
			switch (message.what) {
			case 1:
				Bundle bundle = message.getData();
				result = bundle.getString("address");
				break;
			default:
				result = null;
			}
			MapViewActivity.myLocationField.setText(result);
			myLastAddress = result;
			// Zach, we need implement following class and method
			// new
			// QueryDatabaseUpdateLoc().execute(uID,myLastLat,myLastLng,myLastAddress);
		}
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "provider disabled", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "provider enabled", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "status changed", Toast.LENGTH_SHORT).show();
	}
}