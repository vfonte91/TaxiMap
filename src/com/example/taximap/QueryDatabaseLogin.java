package com.example.taximap;

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

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;


public class QueryDatabaseLogin  extends AsyncTask<String, Void, Integer[]>{
	int puid=-1;
	Context context;
	
	public QueryDatabaseLogin(Context context) {
        this.context = context;
    }
	
	protected Integer[] doInBackground(String... username) {
		Integer uid=0, type=0;
		String line;
		//the data to send
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("username",username[0]));
		nameValuePairs.add(new BasicNameValuePair("password",username[1]));
		 
		//try connecting to the server
		try{
		        HttpClient httpclient = new DefaultHttpClient();
		        HttpPost httppost = new HttpPost("http://ec2-23-22-121-122.compute-1.amazonaws.com/login.php");
		        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		        HttpResponse response = httpclient.execute(httppost);		  
		        HttpEntity entity = response.getEntity();
		        InputStream is = entity.getContent();
		        BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
		        line = reader.readLine();
		        JSONObject json_login = new JSONObject(line);
		        String  uid_string = json_login.getString("uid");
		        String type_string = json_login.getString("type");
		        uid = Integer.valueOf(uid_string);
		        type = Integer.valueOf(type_string);		        
		}catch(Exception e){
		        Log.e("log_tag", "Error in http connection "+e.toString());
		        uid = -5;
		}
		Integer[] return_result = new Integer[2];
		return_result[0] = uid;
		return_result[1] = type;
		return return_result;
	}
	
	protected void onPostExecute(Integer[] result) {
		if(result[0]>0){ 
			if(result[1]==1){
				//to test this case you can user username: 'a' password: 'a'
	            new AlertDialog.Builder(context)
	    		.setTitle("Message")
	    		.setMessage("Logged in as a driver!")
	    		.setNeutralButton("Go back to login", new DialogInterface.OnClickListener() {
	    			public void onClick(DialogInterface dialog, int which) {
	    				//I think you would put the call to the driver map activity here?
	    			}
	    		})
	    		.show();
			} else {
				//to test this case create a new user or use username: 'test' password: 'test'
				new AlertDialog.Builder(context)
	    		.setTitle("Message")
	    		.setMessage("Logged in as a standard user!")
	    		.setNeutralButton("Go back to login", new DialogInterface.OnClickListener() {
	    			public void onClick(DialogInterface dialog, int which) {
	    				//I think you would put the call to the user map activity here?
	    			}
	    		})
	    		.show();
			}
            
        } else if(result[0]==-5){ 
        	new AlertDialog.Builder(context)
    		.setTitle("Error")
    		.setMessage("Trouble Connecting To Database, Check Network Connection")
    		.setNeutralButton("Try Again", new DialogInterface.OnClickListener() {
    			public void onClick(DialogInterface dialog, int which) {}
    		})
    		.show();
        } else { 
        	new AlertDialog.Builder(context)
    		.setTitle("Error")
    		.setMessage("Login failed")
    		.setNeutralButton("Try Again", new DialogInterface.OnClickListener() {
    			public void onClick(DialogInterface dialog, int which) {}
    		})
    		.show();
        }
    }

}
