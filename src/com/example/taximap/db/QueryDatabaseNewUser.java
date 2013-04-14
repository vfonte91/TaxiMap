//Class connects to remote server to create a new user, on success user will be sent back to login page

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

import com.example.taximap.Constants;
import com.example.taximap.Hash;

import android.accounts.AccountManager;
import android.accounts.Account;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;


public class QueryDatabaseNewUser  extends AsyncTask<String, Void, Integer>{
	int puid=-1; //Use -1 as the default error case later
	Context context;
	private AccountManager mAccountManager;
	private String username;
	private String password;
	
	public QueryDatabaseNewUser(Context context) {
        this.context = context;
        mAccountManager = AccountManager.get(context);
    }
	
	protected Integer doInBackground(String... credentials) {
		Integer status=0;
		String line;
		//Prepare the data to send to server
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		username = credentials[0];
		password = Hash.hashString(credentials[1]);
		nameValuePairs.add(new BasicNameValuePair("username",username));
		nameValuePairs.add(new BasicNameValuePair("password",password));
		 
		//Try connecting to server
		try{
		        HttpClient httpclient = new DefaultHttpClient();
		        HttpPost httppost = new HttpPost("http://ec2-23-22-121-122.compute-1.amazonaws.com/create_user.php");
		        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		        HttpResponse response = httpclient.execute(httppost);		        	        
		        HttpEntity entity = response.getEntity();
		        InputStream is = entity.getContent();
		        BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
		        line = reader.readLine(); //Get returned value from server
		        status = Integer.parseInt(line);
		}catch(Exception e){
		        Log.e("log_tag", "Error in http connection "+e.toString());
		}
		
		return status;
	}
	
	protected void onPostExecute(Integer result) {
		
		if(result==1){ //Success
			//Create new account based on username and account type
			Account account = new Account(username, Constants.ACCOUNT_TYPE);
			//Bundle is used to save user data. username is saved
			Bundle userData = new Bundle();
			userData.putString(Constants.USER_DATA_KEY, username);
			//Add account and password to AccountManager
			mAccountManager.addAccountExplicitly(account, password, userData);
			
            new AlertDialog.Builder(context)
    		.setTitle("Message")
    		.setMessage("New User Created!")
    		.setNeutralButton("OK", new DialogInterface.OnClickListener() {
    			public void onClick(DialogInterface dialog, int which) {((Activity)context).finish();}
    		})
    		.show();
        } else if(result==2){ //Failure, show error (username taken)
        	new AlertDialog.Builder(context)
    		.setTitle("Error")
    		.setMessage("Username Taken Already, Try Other Name")
    		.setNeutralButton("Try Again", new DialogInterface.OnClickListener() {
    			public void onClick(DialogInterface dialog, int which) {}
    		})
    		.show();
        } else { //Failure, show error (couldnt connect to DB)
        	new AlertDialog.Builder(context)
    		.setTitle("Error")
    		.setMessage("Trouble Connecting To Database")
    		.setNeutralButton("Try Again", new DialogInterface.OnClickListener() {
    			public void onClick(DialogInterface dialog, int which) {}
    		})
    		.show();
        }
    }

}
