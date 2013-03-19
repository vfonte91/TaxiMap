package com.example.taximap.test;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;


import android.net.Uri;
import android.os.Bundle;

import android.view.KeyEvent;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;

/*
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
*/

import com.google.android.maps.Overlay;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.view.MotionEvent;
import android.widget.Toast;

import android.location.Address;
import android.location.Geocoder;

import java.util.Locale;
import java.io.IOException;

import com.example.taximap.MyItemizedOverlay;
import com.example.taximap.R;
import com.example.taximap.R.drawable;
import com.example.taximap.R.id;
import com.example.taximap.R.layout;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class CustomerMap extends MapActivity {
	MapView mapView;
	MapController mc;
	GeoPoint p;
	
    private LocationManager lm;
    private LocationListener locationListener;   
    
//    private SensorManager mySensorManager;
    
	class MapOverlay extends com.google.android.maps.Overlay
    {
        @Override
        public boolean draw(Canvas canvas, MapView mapView, 
        boolean shadow, long when) 
        {
            super.draw(canvas, mapView, shadow);                   
 
            //---translate the GeoPoint to screen pixels---
            Point screenPts = new Point();
            mapView.getProjection().toPixels(p, screenPts);
 
            //---add the marker---
            Bitmap bmp = BitmapFactory.decodeResource(
                getResources(), R.drawable.pushpin);            
            canvas.drawBitmap(bmp, screenPts.x, screenPts.y-50, null);         
            return true;
        }
        
       /* @Override
        public boolean onTouchEvent(MotionEvent event, MapView mapView) 
        {   
            //---when user lifts his finger---
            if (event.getAction() == 1) {                
                GeoPoint p = mapView.getProjection().fromPixels(
                    (int) event.getX(),
                    (int) event.getY());
                
                
                    Toast.makeText(getBaseContext(), 
                    	"Location: "+ 
                        p.getLatitudeE6() / 1E6 + "," + 
                        p.getLongitudeE6() /1E6 , 
                        Toast.LENGTH_SHORT).show();
                        
                
                Geocoder geoCoder = new Geocoder(
                        getBaseContext(), Locale.getDefault());
                    try {
                        List<Address> addresses = geoCoder.getFromLocation(
                            p.getLatitudeE6()  / 1E6, 
                            p.getLongitudeE6() / 1E6, 1);
     
                        String add = "";
                        if (addresses.size() > 0) 
                        {
                            for (int i=0; i<addresses.get(0).getMaxAddressLineIndex(); 
                                 i++)
                               add += addresses.get(0).getAddressLine(i) + "\n";
                        }
     
                        Toast.makeText(getBaseContext(), add, Toast.LENGTH_SHORT).show();
                    }
                    catch (IOException e) {                
                        e.printStackTrace();
                    }   
                    return true;
            }                            
            return false;
        }        	*/
    } 
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.customer_map);

        
        //---Get version code of app---
/*        PackageManager pm = getPackageManager();
        try {
            //---get the package info---
            PackageInfo pi =  
                pm.getPackageInfo("com.example.taximap", 0);
            //---display the versioncode---
            Toast.makeText(getBaseContext(),
            		"VersionCode: " +Integer.toString(pi.versionCode),
            		Toast.LENGTH_SHORT).show();
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/
        //------------------------------        
        //get map view
        mapView = (MapView) findViewById(R.id.customer_map_view);
        mapView.setBuiltInZoomControls(true);
        
        mapView.setSatellite(false);
        mapView.setStreetView(true);
        mapView.setTraffic(true);
        
        mc = mapView.getController();
        mc.setZoom(10);    
        List<Overlay> mapOverlays = mapView.getOverlays();
        Drawable drawable = this.getResources().getDrawable(R.drawable.androidmarker);
        MyItemizedOverlay itemizedoverlay = new MyItemizedOverlay(drawable, this);
        
        // fake customer points
        List<List<Double>> customerLatLon=new ArrayList<List<Double>>();
        customerLatLon.add(Arrays.asList(39.988695, -83.051147));
        customerLatLon.add(Arrays.asList(39.888695, -83.051147));
        customerLatLon.add(Arrays.asList(39.788695, -83.051147));
        customerLatLon.add(Arrays.asList(39.988695, -82.851147));
        customerLatLon.add(Arrays.asList(39.888695, -82.851147));
        customerLatLon.add(Arrays.asList(39.788695, -82.851147));
        for(List<Double> latlon:customerLatLon){
        	GeoPoint point = new GeoPoint((int)(latlon.get(0)* 1E6),(int)(latlon.get(1)* 1E6));
        	OverlayItem overlayitem = new OverlayItem(point, "Hola, Mundo!", "I'm in Columbus!");
        	itemizedoverlay.addOverlay(overlayitem);
        	mapOverlays.add(itemizedoverlay);
        }

        //---navigate to a point first---
       /* String coordinates[] = {"39.888695", "-82.907202"};
        double lat = Double.parseDouble(coordinates[0]);
        double lng = Double.parseDouble(coordinates[1]); 
        p = new GeoPoint(
            (int) (lat * 1E6), 
            (int) (lng * 1E6)); 
        //mc.animateTo(p);     
        
        //---Add a location marker---
        MapOverlay mapOverlay = new MapOverlay();
        List<Overlay> listOfOverlays = mapView.getOverlays();
        listOfOverlays.clear();
        listOfOverlays.add(mapOverlay); 
        mapView.invalidate();*/
                
        /*       
        //---reverse geo-coding---
        Geocoder geoCoder = new Geocoder(this, Locale.getDefault());    
        try {
            List<Address> addresses = geoCoder.getFromLocationName(
                "empire state building", 5);
            String add = "";
            if (addresses.size() > 0) {
                p = new GeoPoint(
                        (int) (addresses.get(0).getLatitude() * 1E6), 
                        (int) (addresses.get(0).getLongitude() * 1E6));
                mc.animateTo(p);    
                mapView.invalidate();
            }    
        } catch (IOException e) {
            e.printStackTrace();
        }        
        */
        
        //---use the LocationManager class to obtain locations data---
        /*lm = (LocationManager) 
            getSystemService(Context.LOCATION_SERVICE);
        
        //---PendingIntent to launch activity if the user is within some locations---
        PendingIntent pendIntent = PendingIntent.getActivity(
            this, 0, new
            Intent(android.content.Intent.ACTION_VIEW,
              Uri.parse("http://www.amazon.com")), 0);
        
        //lm.addProximityAlert(1.364048, 103.850228, 5, -1, pendIntent);       
        
        locationListener = new MyLocationListener();   
                
        lm.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 
                0, 
                0, 
                locationListener);
        lm.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, 
                0, 
                0, 
                locationListener);*/
                
        /*        
        mySensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> mySensors = mySensorManager.getSensorList(Sensor.TYPE_MAGNETIC_FIELD);
        
        if(mySensors.size() > 0){
            mySensorManager.registerListener(mySensorEventListener, mySensors.get(0), SensorManager.SENSOR_DELAY_NORMAL);            
            Toast.makeText(this, "Start ORIENTATION Sensor", Toast.LENGTH_LONG).show();
          
           }
           else{
            Toast.makeText(this, "No ORIENTATION Sensor", Toast.LENGTH_LONG).show();
           }
           */
    }
    
    /*   
    private SensorEventListener mySensorEventListener = new SensorEventListener(){

    	@Override
    	public void onAccuracyChanged(Sensor sensor, int accuracy) {
    	 // TODO Auto-generated method stub
    	}

    	@Override
    	public void onSensorChanged(SensorEvent event) {
    	 // TODO Auto-generated method stub	 
    	 float degree = ((float)event.values[0]);
    	// Toast.makeText(getBaseContext(),new Float(degree).toString(), Toast.LENGTH_LONG).show();
    	 Log.d("", new Float(degree).toString());
    	}
    };    
    */
    
    private class MyLocationListener implements LocationListener 
    {
        @Override
        public void onLocationChanged(Location loc) {
            if (loc != null) {
                Toast.makeText(getBaseContext(), 
                    "Location changed : Lat: " + loc.getLatitude() + 
                    " Lng: " + loc.getLongitude(), 
                    Toast.LENGTH_SHORT).show();
            }            
            
            p = new GeoPoint(
                    (int) (loc.getLatitude() * 1E6), 
                    (int) (loc.getLongitude() * 1E6));
         
            mc.animateTo(p);
            mc.setZoom(18);
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, 
            Bundle extras) {
        }
    } 
    
    public boolean onKeyDown(int keyCode, KeyEvent event) 
    {
        MapController mc = mapView.getController(); 
        switch (keyCode) 
        {
            case KeyEvent.KEYCODE_3:
                mc.zoomIn();
                break;
            case KeyEvent.KEYCODE_1:
                mc.zoomOut();
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
    
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
}