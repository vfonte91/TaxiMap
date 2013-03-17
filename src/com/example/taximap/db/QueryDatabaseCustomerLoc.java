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

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


//Pass in the driver id, as well as their lat and long cords as a string array
public class QueryDatabaseCustomerLoc  extends AsyncTask<String, Void, String[][]>{
	Context context;
	
	public QueryDatabaseCustomerLoc(Context context) {
        this.context = context;
    }
	
	protected String[][] doInBackground(String... driver_info) {
		String[][] return_result = new String[20][5];
		String line;
		//the data to send
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("id",String.valueOf(driver_info[0])));
		nameValuePairs.add(new BasicNameValuePair("lat",String.valueOf(driver_info[0])));
		nameValuePairs.add(new BasicNameValuePair("lon",String.valueOf(driver_info[1])));
		 
		//try connecting to the server
		try{
		        HttpClient httpclient = new DefaultHttpClient();
		        HttpPost httppost = new HttpPost("http://ec2-23-22-121-122.compute-1.amazonaws.com/customer_locations.php");
		        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		        HttpResponse response = httpclient.execute(httppost);		  
		        HttpEntity entity = response.getEntity();
		        InputStream is = entity.getContent();
		        BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
		        int i=0;
		        while((line = reader.readLine()) != null){
			        JSONObject json_convert = new JSONObject(line);
			        return_result[i][0] = json_convert.getString("cname");
			        return_result[i][1] = json_convert.getString("lat");
			        return_result[i][2] = json_convert.getString("lon");
			        return_result[i][3] = json_convert.getString("rating");
			        return_result[i][4] = json_convert.getString("phone");
			        i++;
		        }
		}catch(Exception e){
		        Log.e("log_tag", "Error in http connection "+e.toString());
		}		
		return return_result;
	}
	
	protected void onPostExecute(String[][] result) {
		//call to CustomerMap functions to erase old markers and draw new ones
    }

}
