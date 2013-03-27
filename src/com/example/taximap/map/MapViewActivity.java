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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
//import com.google.android.maps.GeoPoint;
//import com.google.android.maps.MapActivity;

import android.app.Activity;
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
import android.sax.StartElementListener;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

public class MapViewActivity extends FragmentActivity implements
		LocationListener, LocationSource {
	private static GoogleMap gmap;
	public static String markerType = "driver";
	public static List<Driver> driverLst;
	public static List<Customer> customerLst;
	private static Driver currentDriver;
	private static Customer currentCustomer;
	private static LatLngBounds.Builder boundsBuilder;
	private static LatLngBounds currentBounds = null;
	private static TextView myLocationField = null;
	private static Handler loadMarkerHandler;
	private static Runnable loadMarkerRunnable;
	public static String uID = "18";
	public static LatLng myLastLatLng = null;
	public static String myLastAddress = null;
	private static boolean firstMap=true;
	// private static OnLocationChangedListener mListener;
	private static LocationManager locationManager;
	private static final String TAG = "-------------";
	private static MapViewActivity context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.content_map_layout);
		setUpMapIfNeeded();
		enableLocationUpdate();
		loadMarkerHandler= new Handler();
		loadMarkerRunnable=new Runnable(){
			public void run(){
				callDB();
				long delayTime=10000000;
				loadMarkerHandler.postDelayed(this, delayTime);
			}
		};
		context=this;
	}

	private void setUpMapIfNeeded() {
		if (gmap == null) {
			gmap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();
			if (gmap != null) {
				setupMapView();
			}
		}
	}

	private void enableLocationUpdate() {
		gmap.setLocationSource(this);
		gmap.setMyLocationEnabled(true);
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		if (locationManager != null) {
			boolean gpsIsEnabled = locationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);
			boolean networkIsEnabled = locationManager
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
			// update real time location every 5s
			long timeInterval=100000000;
			float distDifference=50;
			if (gpsIsEnabled) {
				// public void requestLocationUpdates (String provider, long
				// minTime, float minDistance, LocationListener listener)
				// min time interval 5s, min difference meters 10m.
				locationManager.requestLocationUpdates(
						LocationManager.GPS_PROVIDER, timeInterval, distDifference, this);
			} else if (networkIsEnabled) {
				locationManager.requestLocationUpdates(
						LocationManager.NETWORK_PROVIDER, timeInterval, distDifference, this);
			} else { // Show an error dialog that GPS is disabled...
				Toast.makeText(this, "Both GPS and Network are disabled",
						Toast.LENGTH_SHORT).show();
			}
		} else { // Show some generic error dialog because something must have
					// gone wrong with
			Toast.makeText(this, "Location manager error", Toast.LENGTH_SHORT)
					.show();
		}
	}

	// As Vince said, this func is not needed.
	/*
	 * private void disableLocationUpdate(){ gmap.setMyLocationEnabled(false);
	 * locationManager.removeUpdates(this); }
	 */

	private void setupMapView() {
		UiSettings settings = gmap.getUiSettings();
		settings.setAllGesturesEnabled(true);
		settings.setCompassEnabled(true);
		settings.setMyLocationButtonEnabled(true);
		settings.setRotateGesturesEnabled(true);
		settings.setScrollGesturesEnabled(true);
		settings.setTiltGesturesEnabled(true);
		settings.setZoomControlsEnabled(false);
		settings.setZoomGesturesEnabled(true);
		gmap.animateCamera(CameraUpdateFactory
				.newCameraPosition(new CameraPosition(new LatLng(39.983434,
						-83.003082), 8f, 0, 0)));
		gmap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
		gmap.setTrafficEnabled(true);
	}

	@Override
	public void onLocationChanged(Location location) {	
		// Create current user marker if not exists.
		// move current user marker if exists.
		double latitude = location.getLatitude();
		double longitude = location.getLongitude();
		myLastLatLng=new LatLng(latitude, longitude);
		String username="User Name";
		// get physical address from that lat lon location
		GeolocationHelper.getAddressFromLocation(location, this, new GeocoderHandler());
		// add a marker to show current user location.
		if(markerType.equals("driver")){
			if(currentDriver==null){
				BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.customerdefault);
				MarkerOptions markerOptions = new MarkerOptions().position(myLastLatLng)
						.title(username).icon(icon);
				currentCustomer=new Customer(myLastLatLng,username);
				currentCustomer.markerOptions=markerOptions;
				currentCustomer.marker=gmap.addMarker(markerOptions);
				currentCustomer.marker.showInfoWindow();
				gmap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(myLastLatLng,10f,0,0)));
				if (currentBounds == null) {
					boundsBuilder = new LatLngBounds.Builder();
				}
				boundsBuilder.include(myLastLatLng);
				currentBounds = boundsBuilder.build();
				//gmap.moveCamera(CameraUpdateFactory.newLatLngBounds(currentBounds, 50));	//50px
			}else{
				currentCustomer.marker.setPosition(myLastLatLng);
			}
		}
		new Thread(loadMarkerRunnable).run();
/*		callDB();*/
	}

	private static class GeocoderHandler extends Handler {
		@Override
		public void handleMessage(Message message) {
			String result;
			switch (message.what) {
			case 1:
				Bundle bundle = message.getData();
				result = bundle.getString("address");
				myLastAddress = result;
				if(markerType.equals("driver")){
					currentCustomer.marker.setSnippet(result);
					 new Thread() {
					        @Override public void run() {
					        	while(MapViewActivity.currentCustomer.marker==null){
									//loop and wait for currentCustomer constructor to complete
									Log.i(TAG, "currentCustomer.marker==null");
								}
								context.runOnUiThread(new Runnable() {
								    public void run() {
								    	MapViewActivity.currentCustomer.marker.showInfoWindow();
										try {
											Thread.sleep(2000);
										} catch (InterruptedException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										MapViewActivity.currentCustomer.marker.hideInfoWindow();
								    }
								});
					        }
					 }.start();
				}
				break;
			default:
				result = null;
			}
		}
	}
	
	public static void callDB() {
		if (markerType == "driver") {
			Log.d("----", uID);
			(new QueryDatabaseDriverLoc()).execute(uID,Double.toString(myLastLatLng.latitude),Double.toString(myLastLatLng.longitude)); // pass in uid. modify
		} else {
			Log.d("----", uID);
			(new QueryDatabaseCustomerLoc()).execute(uID);
		}
	}	
	
	// load markers to the map every 5 seconds based on filter and classification setting.
	// first query db based on myLastLatLng
	// render markers on the map using classification and filter settings.
	public static void loadMarkers() {
		// clear all markers except for the current user
		if (markerType.equals("driver")) {
			if (driverLst != null) {
				for(Driver d:driverLst){
					if(d.marker!=null){
						d.marker.remove();
					}else{
						break;
					}
				}
			}
			//callDB();
			/*for (Driver driver : driverLst) {
				driver.isActive = true;
			}*/
			// set up filter mapping
			Map<String, String> companies = new HashMap<String, String>();
			companies.put("Blue Cab", "Blue Cab");
			companies.put("Yellow Cab", "Yellow Cab");
			companies.put("Green Cab", "Green Cab");
			Map<String, Integer> ratings = new HashMap<String, Integer>();
			ratings.put("5 Stars", 5);
			ratings.put("4 Stars and Above", 4);
			ratings.put("3 Stars and Above", 3);
			ratings.put("2 Stars and Above", 2);
			ratings.put("1 Star and Above", 1);
			Map<String, Integer> distance = new HashMap<String, Integer>();
			distance.put("Within 30 mins", 15); // 15 miles, 30 miles/hour speed
			distance.put("Within 20 mins", 10);
			distance.put("Within 10 mins", 5);
			if (FilterActivity.filters != null) { // filter is previously set
				for (String key : FilterActivity.filters.get("driver").keySet()) {
					String value = FilterActivity.filters.get("driver")
							.get(key);
					if (!value.equals("Any")) { // is not "Any"
						if (key.equals("company")) {
							for (Driver driver : driverLst) {
								if (!driver.company.equals(companies.get(value))) { 
									driver.isActive = false;
								}
							}
							FilterActivity.classificationCode[0] = '1';
						}
						if (key.equals("rating")) {
							for (Driver driver : driverLst) {
								if (driver.rating < ratings.get(value)) { 
									Log.e(TAG, String.format("%s<%s",
											driver.rating, ratings.get(value)));
									driver.isActive = false;
								}
							}
							FilterActivity.classificationCode[1] = '1';
						}
						if (key.equals("distance")) {
							for (Driver driver : driverLst) {
								if (driver.distance < ratings.get(value)) {
									driver.isActive = false;
								}
							}
						}
					}
				}
			}
			// map out active markers and classify by assigning different icons
			boundsBuilder = new LatLngBounds.Builder();
			if (myLastLatLng != null) {
				boundsBuilder.include(myLastLatLng);
			}
			for (Driver driver : driverLst) {
				if (driver.isActive) {
					BitmapDescriptor icon = findIcon(driver);
					MarkerOptions markerOptions = new MarkerOptions().position(driver.latlng)
							.title(driver.title())
							.snippet(driver.snippet()).icon(icon);
					driver.markerOptions = markerOptions;
					driver.marker=gmap.addMarker(markerOptions);
					boundsBuilder.include(driver.latlng);
				}
			}
			currentBounds = boundsBuilder.build();
			if(firstMap){
				gmap.moveCamera(CameraUpdateFactory.newLatLngBounds(
					currentBounds, 50)); // padding 50
				firstMap=false;
			}
			//showMessages(this, "Drivers Updated");
			//updateListView();
			Toast.makeText(context, "Drivers Updated", Toast.LENGTH_SHORT).show();
		} else if (markerType.equals("customer")) {
		
		}
		
	}
	

	private static void updateListView(){
		context.startActivity(new Intent(context,ListViewActivity.class));
	}
	
	private static BitmapDescriptor findIcon(Driver driver) {
		BitmapDescriptor icon = null;
		// company, rating
		// classificationScheme={"00","10","01","11"};
		// 10 classify by company
		String s = new String(FilterActivity.classificationCode);
		if (s.equals("10")) {
			Map<String, Integer> resource = new HashMap<String, Integer>();
			resource.put("Blue Cab", R.drawable.taxibluedefault);
			resource.put("Yellow Cab", R.drawable.taxiyellowdefault);
			resource.put("Green Cab", R.drawable.taxigreendefault);
			try {
				icon = BitmapDescriptorFactory.fromResource(resource
						.get(driver.company));
			} catch (Exception e) {
				icon = BitmapDescriptorFactory
						.fromResource(R.drawable.taxidefault);
			}

		}
		else if (s.equals("01")) {
			Map<Integer, Integer> resource = new HashMap<Integer, Integer>();
			resource.put(5, R.drawable.taxi5);
			resource.put(4, R.drawable.taxi4);
			resource.put(3, R.drawable.taxi3);
			resource.put(2, R.drawable.taxi2);
			resource.put(1, R.drawable.taxi1);
			try {
				icon = BitmapDescriptorFactory.fromResource(resource
						.get(driver.rating));
			} catch (Exception e) {
				icon = BitmapDescriptorFactory
						.fromResource(R.drawable.taxidefault);
			}
		}
		else if (s.equals("11")) {
			Map<String, Integer> resource = new HashMap<String, Integer>();
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
			try {
				icon = BitmapDescriptorFactory.fromResource(resource
						.get(driver.company + Integer.toString(driver.rating)));
			} catch (Exception e) {
				icon = BitmapDescriptorFactory
						.fromResource(R.drawable.taxidefault);
			}
		}else{
			// default driver icon
			icon = BitmapDescriptorFactory
					.fromResource(R.drawable.taxidefault);
		}
		return icon;
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
		Toast.makeText(this, "Location Updated!", Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.id.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.menu_filter:
			// requestCode=1
			startActivityForResult(new Intent(this, FilterActivity.class), 1);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	// callback from filter activity
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1) {
			// do something
			if (resultCode == RESULT_OK) {
				loadMarkers();
			} else {
				Toast.makeText(this, "Filter Cancelled", Toast.LENGTH_SHORT)
						.show();
			}
		} else {
			Toast.makeText(this, "Request Code Error", Toast.LENGTH_SHORT)
					.show();
		}

	}

	@Override
	public void activate(OnLocationChangedListener arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deactivate() {
		// TODO Auto-generated method stub
		
	}
}
