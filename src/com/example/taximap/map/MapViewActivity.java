package com.example.taximap.map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.example.taximap.R;
import com.example.taximap.db.QueryDatabaseCustomerLoc;
import com.example.taximap.db.QueryDatabaseDriverLoc;
import com.example.taximap.db.QueryDatabaseLogin;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MapViewActivity extends FragmentActivity implements OnClickListener {

	private static GoogleMap gmap;
	public static String markerType = "";		// set upon login either "driver" or "customer"
	public static List<Driver> driverLst;
	public static List<Customer> customerLst;
	private static LatLngBounds.Builder boundsBuilder;
	

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.content_map_layout);
		View btnLoad = (Button)findViewById(R.id.load);
		btnLoad.setOnClickListener(this);
		
		View btnFilter = (Button)findViewById(R.id.filters_setting);
		btnFilter.setOnClickListener((android.view.View.OnClickListener) this);
		
		gmap = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map)).getMap();
		setupMapView();
	}
	

	
	private void setupMapView() {
		UiSettings settings = gmap.getUiSettings();
		gmap.animateCamera(CameraUpdateFactory
				.newCameraPosition(new CameraPosition(new LatLng(39.983434,
						-83.003082), 13.5f, 30f, 112.5f))); // zoom, tilt,
															// bearing
		gmap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
		gmap.setTrafficEnabled(true);
		settings.setAllGesturesEnabled(true);
		settings.setCompassEnabled(true);
		settings.setMyLocationButtonEnabled(true);
		settings.setRotateGesturesEnabled(true);
		settings.setScrollGesturesEnabled(true);
		settings.setTiltGesturesEnabled(true);
		settings.setZoomControlsEnabled(true);
		settings.setZoomGesturesEnabled(true);
	}

	private static void loadData() {
		Map <String,Map<String,String>> filters=FilterActivity.filters;
		if (markerType=="driver")
			for (Driver driver: driverLst) {
				MarkerOptions marker;
				BitmapDescriptor icon = BitmapDescriptorFactory
						.fromResource(R.drawable.taxidefault);
				marker = new MarkerOptions().position(driver.latlng).title(driver.title())
						.snippet(driver.snippet()).icon(icon);
				driver.marker = marker;
			}
		else if(markerType=="customer"){}
			
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
			LatLngBounds bounds = boundsBuilder.build();
			gmap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 30));
		} else if (markerType == "customer") {

		}
	}
	
	public static void callDB() {
		if (markerType=="driver") {
			new QueryDatabaseCustomerLoc().execute("1");		// pass in uid. modify
		}
		else {
			new QueryDatabaseDriverLoc().execute("1");
		}
	}

	public void onClick(View v) {
		System.out.print(v.getId());
		switch (v.getId()) {
		case R.id.load:
			callDB();			
			break;
		case R.id.filters_setting:
			startActivity(new Intent(this,FilterActivity.class));
		}
	}
}