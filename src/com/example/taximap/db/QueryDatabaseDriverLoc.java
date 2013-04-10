package com.example.taximap.db;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import com.example.taximap.map.Driver;
import com.example.taximap.map.MapViewActivity;
import com.google.android.gms.maps.model.LatLng;

import android.os.AsyncTask;
import android.util.Log;


//Pass in the driver id, as well as their lat and long cords as a string array
public class QueryDatabaseDriverLoc  extends AsyncTask<String, Void, Integer>{
	public QueryDatabaseDriverLoc() {
    }
	protected Integer doInBackground(String... driver_info) {
		Integer return_count = 0;
		String line;
		
		//Prepare the data to send
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("id",String.valueOf(driver_info[0])));
		nameValuePairs.add(new BasicNameValuePair("lat",String.valueOf(driver_info[1])));
		nameValuePairs.add(new BasicNameValuePair("lon",String.valueOf(driver_info[2])));
		 
		//Try connecting to the server
		try{
		        HttpClient httpclient = new DefaultHttpClient();
		        HttpPost httppost = new HttpPost("http://ec2-23-22-121-122.compute-1.amazonaws.com/driver_locations.php");
		        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		        HttpResponse response = httpclient.execute(httppost);		  
		        HttpEntity entity = response.getEntity();
		        InputStream is = entity.getContent();
		        BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);	
	        	MapViewActivity.driverLst = new ArrayList<Driver>();
	        	
	        	//Populate driver list with values returned from server
		        while((line = reader.readLine()) != null){
		        	return_count++;
				    JSONObject json_convert = new JSONObject(line);
				    LatLng latlon = new LatLng(json_convert.getDouble("lat"), json_convert.getDouble("lon"));
				    MapViewActivity.driverLst.add(new Driver(latlon,json_convert.getString("dname"),json_convert.getString("cname"),json_convert.getInt("rating"),json_convert.getDouble("distance"),json_convert.getInt("capacity"),json_convert.getString("lastlocation"),json_convert.getString("phone")));
		        }
		        
		}catch(Exception e){
		        Log.e("log_tag", "Error in http connection "+e.toString());
		}		
		return return_count;
	}
	
	protected void onPostExecute(Integer result) {
		if(result!=0) {
			//rebuild map based on new data
			MapViewActivity.loadMarkers();
		}
				
    }

}

