package com.example.taximap.db;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
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

import com.example.taximap.map.Customer;
import com.example.taximap.map.MapViewActivity;
import com.google.android.gms.maps.model.LatLng;

import android.os.AsyncTask;
import android.util.Log;


//Pass in the driver id, as well as their lat and long cords as a string array
public class QueryDatabaseDriverLoc  extends AsyncTask<String, Void, String[][]>{
	private WeakReference<MapViewActivity> mParentActivity = null;
	public QueryDatabaseDriverLoc() {

    }
	
	protected String[][] doInBackground(String... driver_info) {
		String[][] return_result = new String[20][5];
		String line;
		//the data to send
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("id",String.valueOf(driver_info[0])));
		 
		//try connecting to the server
		try{
		        HttpClient httpclient = new DefaultHttpClient();
		        HttpPost httppost = new HttpPost("http://ec2-23-22-121-122.compute-1.amazonaws.com/driver_locations.php");
		        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		        HttpResponse response = httpclient.execute(httppost);		  
		        HttpEntity entity = response.getEntity();
		        InputStream is = entity.getContent();
		        BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
		        if (mParentActivity.get() != null) {
	        		MapViewActivity.customerLst = new ArrayList<Customer>();
		        }
		        while((line = reader.readLine()) != null){
		        	if (mParentActivity.get() != null) {
				        JSONObject json_convert = new JSONObject(line);
				        LatLng latlon = new LatLng(json_convert.getLong("lat"), json_convert.getLong("lon"));
				        MapViewActivity.customerLst.add(new Customer(latlon,json_convert.getString("cname"),json_convert.getInt("numpass")));
		        	}
		        }
		}catch(Exception e){
		        Log.e("log_tag", "Error in http connection "+e.toString());
		}		
		return return_result;
	}
	
	protected void onPostExecute(String[][] result) {
		//call to CustomerMap functions to erase old markers and draw new ones
		if (mParentActivity.get() != null) {
			MapViewActivity.loadMarkers();
		}
    }

}
