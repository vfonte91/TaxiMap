//Class connects to remote server to check if username/pass combo matches the database
//Connection is secure in that all passwords sent are strongly encrypted 
//If connection is succesful this class will start the MapViewActivity, if not the user will be prompted

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

import com.example.taximap.Constants;
import com.example.taximap.map.FragmentTabsActivity;
import com.example.taximap.map.MapViewActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;


public class QueryDatabaseLogin  extends AsyncTask<String, Void, Integer[]>{
	int puid=-1;		//Use -1 for the case of no user id returned from server
	Context context;
	
	public QueryDatabaseLogin(Context context) {
        this.context = context;
    }
	
	protected Integer[] doInBackground(String... username) {
		Integer[] return_result = new Integer[2];
		String line;
		
		//Prepare the data to send to server
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
		        
		        //Store data returned from server call
		        String  uid_string = json_login.getString("uid");		       
		        String type_string = json_login.getString("type");
		        return_result[0] = Integer.valueOf(uid_string);
		        return_result[1] = Integer.valueOf(type_string);
		}catch(Exception e){
		        Log.e("log_tag", "Error in http connection "+e.toString());
		        return_result[0] = -5;
		}		
		return return_result;
	}
	
	protected void onPostExecute(Integer[] result) {
		if(result[0]>0){ //If the database found the user/pass combo
			 MapViewActivity.uID=result[0].toString();		
			 Log.e("???",result[0].toString()+" "+result[1].toString());
			 if(result[1]==0){		//customer login
		        	MapViewActivity.markerType = Constants.DRIVER;
		     }else{					//driver login
		        	MapViewActivity.markerType = Constants.CUSTOMER;
		     }
	            context.startActivity(new Intent(context,FragmentTabsActivity.class));      
        } else if(result[0]==-5){ //If the database couldn't be connected to show error
        	new AlertDialog.Builder(context)
    		.setTitle("Error")
    		.setMessage("Trouble Connecting To Database, Check Network Connection")
    		.setNeutralButton("Try Again", new DialogInterface.OnClickListener() {
    			public void onClick(DialogInterface dialog, int which) {}
    		})
    		.show();
        } else {  //If couldnt find user/pass combo in database prompt user
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
