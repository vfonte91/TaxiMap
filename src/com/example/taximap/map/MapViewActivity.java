package com.example.taximap.map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
import android.media.audiofx.Equalizer;
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
	public static LatLng myLastLatLng=null;
	public static String myLastAddress = null;

	/* private static String bestProvider = null; */
	private static OnLocationChangedListener mListener;
	private static LocationManager locationManager;
	private static final String TAG="-------------";

	
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

	public static void loadMarkers() {
		// clear screen
		gmap.clear();
		// load data if necessary
		if (markerType.equals("driver")){
			if(driverLst==null){
				callDB();
			}
		}else if(markerType.equals("customer")){
			if(customerLst==null){
				callDB();
			}
		}
		for(Driver driver:driverLst){
			driver.isActive=true;
		}
		
		// apply filters first
		if (markerType == "driver"){
			Map<String,String> companies=new HashMap<String,String>();
			companies.put("Blue Cab", "Blue Cab");
			companies.put("Yellow Cab", "Yellow Cab");
			companies.put("Green Cab", "Green Cab");
			Map<String,Integer> ratings=new HashMap<String,Integer>();
			ratings.put("5 Stars", 5);
			ratings.put("4 Stars and Above", 4);
			ratings.put("3 Stars and Above", 3);
			ratings.put("2 Stars and Above", 2);
			ratings.put("1 Star and Above", 1);
			Map<String,Integer> distance=new HashMap<String,Integer>();
			distance.put("Within 30 mins", 15);		//15 miles, 30 miles/hour speed
			distance.put("Within 20 mins", 10);
			distance.put("Within 10 mins", 5);
			if(FilterActivity.filters!=null){	// filter is previously set
				for(String key:FilterActivity.filters.get("driver").keySet()){
					String value=FilterActivity.filters.get("driver").get(key);
					if(!value.equals("Any")){	// is not "Any"
						if(key.equals("company")){
							for(Driver driver:driverLst){
								if(!driver.company.equals(companies.get(value))){	// for those drivers not from selected company
									driver.isActive=false;
								}
							}
							FilterActivity.classificationCode[0]='1';
						}
						if(key.equals("rating")){
							for(Driver driver:driverLst){
								if(driver.rating<ratings.get(value)){	// for those drivers not from selected company
									Log.e(TAG, String.format("%s<%s", driver.rating,ratings.get(value)));
									driver.isActive=false;
								}
							}
							FilterActivity.classificationCode[1]='1';
						}
						if(key.equals("distance")){
							for(Driver driver:driverLst){
								if(driver.distance<ratings.get(value)){	// for those drivers not from selected company
									driver.isActive=false;
								}
							}
						}
					}
				}
				// load active markers using classification scheme and add marker to the map
				boundsBuilder = new LatLngBounds.Builder();
				if(myLastLatLng!=null){
					boundsBuilder.include(myLastLatLng);
				}
				for (Driver driver : driverLst) {
					if(driver.isActive){
						MarkerOptions marker;
						BitmapDescriptor icon=findIcon(driver);
						marker = new MarkerOptions().position(driver.latlng)
								.title(driver.title()).snippet(driver.snippet())
								.icon(icon);
						driver.marker = marker;
						boundsBuilder.include(driver.latlng);
						gmap.addMarker(marker);
					}
				}
				try{
					currentBounds = boundsBuilder.build();
					gmap.moveCamera(CameraUpdateFactory.newLatLngBounds(currentBounds,30));	// padding 30
				}catch(Exception e){}
				
			}else{
				boundsBuilder = new LatLngBounds.Builder();
				if(myLastLatLng!=null){
					boundsBuilder.include(myLastLatLng);
				}
				for (Driver driver : driverLst) {
					if(driver.isActive){
						MarkerOptions marker;
						BitmapDescriptor icon=BitmapDescriptorFactory
								.fromResource(R.drawable.taxidefault);
						marker = new MarkerOptions().position(driver.latlng)
								.title(driver.title()).snippet(driver.snippet())
								.icon(icon);
						gmap.addMarker(marker);
						driver.marker = marker;
						boundsBuilder.include(driver.latlng);
					}
				}
				try{
					currentBounds = boundsBuilder.build();
					gmap.moveCamera(CameraUpdateFactory.newLatLngBounds(currentBounds,30));	// padding 30
				}catch(Exception e){}
			}
		}
		else if (markerType == "customer") {
		}
	}

	private static BitmapDescriptor findIcon(Driver driver){
		BitmapDescriptor icon=null;
		// company, rating
		// classificationScheme={"00","10","01","11"};
		// 10 classify by company
		String s=new String(FilterActivity.classificationCode);
		if(s.equals("10")){
			Map<String,Integer> resource=new HashMap<String,Integer>();
			resource.put("Blue Cab", R.drawable.taxibluedefault);
			resource.put("Yellow Cab", R.drawable.taxiyellowdefault);
			resource.put("Green Cab", R.drawable.taxigreendefault);
			try{
				icon = BitmapDescriptorFactory
						.fromResource(resource.get(driver.company));
			}catch(Exception e){
				icon = BitmapDescriptorFactory
						.fromResource(R.drawable.taxidefault);
			}

		}
		if(s.equals("01")){
			Map<Integer,Integer> resource=new HashMap<Integer,Integer>();
			resource.put(5, R.drawable.taxi5);
			resource.put(4, R.drawable.taxi4);
			resource.put(3, R.drawable.taxi3);
			resource.put(2, R.drawable.taxi2);
			resource.put(1, R.drawable.taxi1);
			try{
			icon = BitmapDescriptorFactory
						.fromResource(resource.get(driver.rating));
			}catch(Exception e){
				icon = BitmapDescriptorFactory
						.fromResource(R.drawable.taxidefault);
			}
		}
		if(s.equals("11")){
			Map<String,Integer> resource=new HashMap<String,Integer>();
			resource.put("Blue Cab5", R.drawable.taxiblue5);
			resource.put("Blue Cab4", R.drawable.taxiblue4);
			resource.put("Blue Cab3", R.drawable.taxiblue3);
			resource.put("Blue Cab2", R.drawable.taxiblue2);
			resource.put("Blue Cab1", R.drawable.taxiblue1);
			resource.put("Yellow Cab5", R.drawable.taxiblue5);
			resource.put("Yellow Cab4", R.drawable.taxiblue4);
			resource.put("Yellow Cab3", R.drawable.taxiblue3);
			resource.put("Yellow Cab2", R.drawable.taxiblue2);
			resource.put("Yellow Cab1", R.drawable.taxiblue1);
			resource.put("Green Cab5", R.drawable.taxigreen5);
			resource.put("Green Cab4", R.drawable.taxigreen4);
			resource.put("Green Cab3", R.drawable.taxigreen3);
			resource.put("Green Cab2", R.drawable.taxigreen2);
			resource.put("Green Cab1", R.drawable.taxigreen1);
			try{
				icon = BitmapDescriptorFactory.fromResource(resource.get(driver.company+Integer.toString(driver.rating)));
			}catch(Exception e){
				icon = BitmapDescriptorFactory
						.fromResource(R.drawable.taxidefault);
			}
						
		}
		return icon;
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
			startActivityForResult(new Intent(this, FilterActivity.class), 1);
			break;
		case R.id.update_loc:
			Button bt=(Button)findViewById(R.id.update_loc);			
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
	
	// callback from filter activity
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1) {  
            //do something  
            if (resultCode == RESULT_OK) { 
        		loadMarkers();
            }
            else {  
             Toast.makeText(this, "Filter Cancelled", Toast.LENGTH_SHORT);
          }  
		} else {  
            Toast.makeText(this, "Request Code Error", Toast.LENGTH_SHORT);
		}

    }

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
			myLastLatLng=new LatLng(latitude, longitude);
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