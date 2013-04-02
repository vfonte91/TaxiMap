package com.example.taximap;

import com.example.taximap.R;
import com.example.taximap.db.QueryDatabaseLogin;

import android.accounts.AccountManager;
import android.accounts.Account;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Login extends Activity implements OnClickListener{
   private EditText userNameEditableField;
   private EditText passwordEditableField;
   private AccountManager mAccountManager;

   private static final String TAG = "Login Activity";
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        mAccountManager = AccountManager.get(this);
        
        //Check if account exists
        if (!(savedAccount())){
	    	setContentView(R.layout.login);
	    	//initialize instance variables
	        userNameEditableField=(EditText)findViewById(R.id.username_text);
	        passwordEditableField=(EditText)findViewById(R.id.password_text);
	
	        View btnLogin=(Button)findViewById(R.id.login_button);
	        btnLogin.setOnClickListener(this);		//Defined in the onclick function below
	
	        /*  Removed because home button will take care of cancel
	        View btnCancel=(Button)findViewById(R.id.cancel_button);
	        btnCancel.setOnClickListener(this);
	        */
	
	        View btnNewUser=(Button)findViewById(R.id.new_user_button);
	        btnNewUser.setOnClickListener(this);
        }
     }

    // The commented functions are for due 3
    /*public void onStart(){
    	super.onStart();
    	Log.i(TAG, "onStart()");
    }

    public void onResume(){
    	super.onResume();
    	Log.i(TAG, "onResume()");
    }

    public void onPause(){
    	super.onPause();
    	Log.i(TAG, "onPause()");
    }

    public void onStop(){
    	super.onStop();
    	Log.i(TAG, "onStop()");
    }

    public void onDestroy(){
    	super.onDestroy();
    	Log.i(TAG, "onDestroy()");
    }

    public void onRestart(){
    	super.onRestart();
    	Log.i(TAG, "onRestart()");
    }*/
    
    private boolean savedAccount() {
    	boolean result = false;
    	String username, password;
    	Account[] accounts = mAccountManager.getAccountsByType(Constants.ACCOUNT_TYPE);
    	if (accounts.length > 0) {
    		//There maybe more than one account, so the last one created is used
    		Account userAccount = accounts[accounts.length - 1];
    		username = mAccountManager.getUserData(userAccount, Constants.USER_DATA_KEY);
        	password = mAccountManager.getPassword(userAccount);
        	checkLogin(username, password);
        	result = true;
    	}
		return result;
    }
    
    private void checkLogin(String username, String password){
        new QueryDatabaseLogin(this).execute(username, password);
      }

    public void onClick(View v) {			//Login activity's click event
		switch (v.getId()) {
  		case R.id.login_button:
		    checkLogin(this.userNameEditableField.getText().toString(), this.passwordEditableField.getText().toString());
		    break;
		    /* Removed because home button will take care of cancel
  		case R.id.cancel_button:
	    	finish();
    		break;
		     */
    	case R.id.new_user_button:
    	    startActivity(new Intent(this, AccountActivity.class));		// start a new activity Direct invocation 
    	    break;
		}
    }
}
