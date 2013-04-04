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

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;


public class QueryDatabaseUpdateLoc  extends AsyncTask<Object, Void, Integer>{
	public QueryDatabaseUpdateLoc() {
    }
	
	//following method to be implemented
	protected Integer doInBackground(Object... param) {
		Integer uID=(Integer)param[0];
		Double lat=(Double)param[1];
		Double lng=(Double)param[2];
		String address=(String)param[3];
		
		/*Integer status=0;
		String line;
		//the data to send
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("username",username[0]));
		nameValuePairs.add(new BasicNameValuePair("password",username[1]));
		 
		//http post
		try{
		        HttpClient httpclient = new DefaultHttpClient();
		        HttpPost httppost = new HttpPost("http://ec2-23-22-121-122.compute-1.amazonaws.com/create_user.php");
		        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		        HttpResponse response = httpclient.execute(httppost);		        	        
		        HttpEntity entity = response.getEntity();
		        InputStream is = entity.getContent();
		        BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
		        line = reader.readLine();
		        status = Integer.parseInt(line);
		}catch(Exception e){
		        Log.e("log_tag", "Error in http connection "+e.toString());
		}
		
		return status;*/
		return 0;
	}
	
	//following method to be implemented
	protected void onPostExecute(Integer result) {
		/*if(result==1){           
            new AlertDialog.Builder(context)
    		.setTitle("Message")
    		.setMessage("Update success!")			//suppress message when completed
    		.setNeutralButton("OK", new DialogInterface.OnClickListener() {
    			public void onClick(DialogInterface dialog, int which) {}
    		})
    		.show();
            
        } *//*else if(result==2){ 
        	new AlertDialog.Builder(context)
    		.setTitle("Error")
    		.setMessage("Username Taken Already, Try Other Name")
    		.setNeutralButton("Try Again", new DialogInterface.OnClickListener() {
    			public void onClick(DialogInterface dialog, int which) {}
    		})
    		.show();
        } else { 
        	new AlertDialog.Builder(context)
    		.setTitle("Error")
    		.setMessage("Trouble Connecting To Database")
    		.setNeutralButton("Try Again", new DialogInterface.OnClickListener() {
    			public void onClick(DialogInterface dialog, int which) {}
    		})
    		.show();
        }*/
    }

}
