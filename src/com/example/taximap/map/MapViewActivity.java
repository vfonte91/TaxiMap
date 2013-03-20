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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MapViewActivity extends FragmentActivity implements OnClickListener {

	private static GoogleMap gmap;
	public static String markerType = "driver";		// set upon login either "driver" or "customer"
	public static List<Driver> driverLst;
	public static List<Customer> customerLst;
	private static LatLngBounds.Builder boundsBuilder;
	
	static{
		driverLst = new ArrayList<Driver>();
		List<LatLng> latlnglst = Arrays.asList(
				new LatLng(40.066824,-83.097153), 
				new LatLng(39.963682, -83.000395),
				new LatLng(39.985630, -83.023232), 
				new LatLng(39.949662,-82.995415), 
				new LatLng(40.144633, -82.981110),
				new LatLng(39.985499, -83.005106), 
				new LatLng(39.843014,-82.805591), 
				new LatLng(40.054200, -83.067550),
				new LatLng(40.016447, -83.011828), 
				new LatLng(40.116523,-83.014359), 
				new LatLng(40.014551, -83.011319),
				new LatLng(39.936106, -82.983422), 
				new LatLng(39.976447,-83.003342));
		driverLst.add( new Driver(latlnglst.get(0),"Driver Name1","Blue Cab",5));
		driverLst.add( new Driver(latlnglst.get(1),"Driver Name2","Blue Cab",5));
		driverLst.add( new Driver(latlnglst.get(2),"Driver Name3","Blue Cab",5));
		driverLst.add( new Driver(latlnglst.get(3),"Driver Name4","Blue Cab",4));
		driverLst.add( new Driver(latlnglst.get(4),"Driver Name5","Yellow Cab",5));
		driverLst.add( new Driver(latlnglst.get(5),"Driver Name6","Yellow Cab",5));
		driverLst.add( new Driver(latlnglst.get(6),"Driver Name7","Yellow Cab",5));
		driverLst.add( new Driver(latlnglst.get(7),"Driver Name8","Yellow Cab",4));
		driverLst.add( new Driver(latlnglst.get(8),"Driver Name9","Yellow Cab",3));
		driverLst.add( new Driver(latlnglst.get(9),"Driver Name10","Green Cab",5));
		driverLst.add( new Driver(latlnglst.get(10),"Driver Name11","Green Cab",5));
		driverLst.add( new Driver(latlnglst.get(11),"Driver Name12","Green Cab",5));
		driverLst.add( new Driver(latlnglst.get(12),"Driver Name13","Green Cab",4));
	}
	
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
		if (markerType=="driver") new QueryDatabaseCustomerLoc().execute("1");
		else new QueryDatabaseDriverLoc().execute("1");
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