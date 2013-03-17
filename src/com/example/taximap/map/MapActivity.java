package com.example.taximap.map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import android.net.Uri;
import android.os.Bundle;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import com.example.taximap.R;

public class MapActivity extends FragmentActivity {
	private static GoogleMap gmap;
	public static String markerType = "driver";		// set upon login either "driver" or "customer"
	private static List<Driver> driverLst;
	private static LatLngBounds.Builder boundsBuilder;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.content_map_layout);

		// 下面一段是测试定位及做标记的
		gmap = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map)).getMap(); // 获取地图对象
		setupMapView();
		loadData();
		//loadMarkers();
		// Move the camera instantly to Sydney with a zoom of 15.
		/*
		 * gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(SYDNEY, 15));
		 * 
		 * // Zoom in, animating the camera.
		 * gmap.animateCamera(CameraUpdateFactory.zoomIn());
		 * 
		 * // Zoom out to zoom level 10, animating with a duration of 2 seconds.
		 * gmap.animateCamera(CameraUpdateFactory.zoomTo(10),2000, null );
		 * 
		 * // Construct a CameraPosition focusing on Mountain View and animate
		 * the camera to that position. CameraPosition cameraPosition = new
		 * CameraPosition.Builder() .target(MOUNTAIN_VIEW) // Sets the center of
		 * the map to Mountain View .zoom(17) // Sets the zoom .bearing(90) //
		 * Sets the orientation of the camera to east .tilt(30) // Sets the tilt
		 * of the camera to 30 degrees .build(); // Creates a CameraPosition
		 * from the builder
		 * gmap.animateCamera(CameraUpdateFactory.newCameraPosition
		 * (cameraPosition));
		 */
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

	private void loadData() {
		driverLst = new ArrayList<Driver>();
		List<LatLng> latlnglst = Arrays.asList(
				new LatLng(39.944537, -82.989767), new LatLng(40.066824,
						-83.097153), new LatLng(39.963682, -83.000395),
				new LatLng(39.985630, -83.023232), new LatLng(39.949662,
						-82.995415), new LatLng(40.144633, -82.981110),
				new LatLng(39.985499, -83.005106), new LatLng(39.843014,
						-82.805591), new LatLng(40.054200, -83.067550),
				new LatLng(40.016447, -83.011828), new LatLng(40.116523,
						-83.014359), new LatLng(40.014551, -83.011319),
				new LatLng(39.936106, -82.983422), new LatLng(39.976447,
						-83.003342));
		int count = 0;
		for (LatLng latlng : latlnglst) {
			count++;
			Driver driver = new Driver(latlng,
					"name" + Integer.toString(count), "company"
							+ Integer.toString(count), count);

			MarkerOptions marker;
			BitmapDescriptor icon = BitmapDescriptorFactory
					.fromResource(R.drawable.taxidefault);
			marker = new MarkerOptions().position(latlng).title(driver.title())
					.snippet(driver.snippet()).icon(icon);
			driver.marker = marker;
			driverLst.add(driver);

		}
	}

	public static void loadMarkers() {
		boundsBuilder = new LatLngBounds.Builder();
		if (markerType == "driver") {
			for (Driver d : driverLst) {
				gmap.addMarker(d.marker); // 在指定的 纬-经 度上做个标记（气泡）
				boundsBuilder.include(d.latlng);
			}
			LatLngBounds bounds = boundsBuilder.build();
			gmap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 3));
		} else if (markerType == "customer") {

		}
	}
}