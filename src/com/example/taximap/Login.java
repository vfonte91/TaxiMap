package com.example.taximap;

import com.example.taximap.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Login extends Activity implements OnClickListener{
   //private DatabaseHelper dh;					//from old login system
   private EditText userNameEditableField;
   private EditText passwordEditableField;
   //private final static String OPT_NAME="name";	//from old login system
   private static final String TAG = "Login Activity";
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);		
    	setContentView(R.layout.login);
    	//initialize instance variables
        userNameEditableField=(EditText)findViewById(R.id.username_text);
        passwordEditableField=(EditText)findViewById(R.id.password_text);
        
        View btnLogin=(Button)findViewById(R.id.login_button);
        btnLogin.setOnClickListener(this);		//Defined in the onclick function below
        
        View btnCancel=(Button)findViewById(R.id.cancel_button);
        btnCancel.setOnClickListener(this);
        
        View btnNewUser=(Button)findViewById(R.id.new_user_button);
        btnNewUser.setOnClickListener(this);
     }
    
    public void onStart(){
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
    }
    private void checkLogin(){
    	String username=this.userNameEditableField.getText().toString();
        String password=this.passwordEditableField.getText().toString();
        new QueryDatabaseLogin(this).execute(username, password);        
       
        /* OLD LOGIN METHOD - Can it be deleted?
         * Yeah, Zach. Please Do. Wei.
        this.dh=new DatabaseHelper(this);
        List<String> names=this.dh.selectAll(username,password);
        if(names.size() >0){ // Login successful
        	// Save username as the name of the player
        	SharedPreferences settings=PreferenceManager.getDefaultSharedPreferences(this);
        	SharedPreferences.Editor editor=settings.edit();
            editor.putString(OPT_NAME, username);
            editor.commit();
        	// Bring up the GameOptions screen
        	//startActivity(new Intent(this, GameOptions.class));
            
            new AlertDialog.Builder(this)
    		.setTitle("Message")
    		.setMessage("Login succeeded!")
    		.setNeutralButton("Go back to login", new DialogInterface.OnClickListener() {
    			public void onClick(DialogInterface dialog, int which) {}
    		})
    		.show();
            
        	//finish();
        } else {
            // Try again? 
        	new AlertDialog.Builder(this)
    		.setTitle("Error")
    		.setMessage("Login failed")
    		.setNeutralButton("Try Again", new DialogInterface.OnClickListener() {
    			public void onClick(DialogInterface dialog, int which) {}
    		})
    		.show();
        }
        */
      }
    
    public void onClick(View v) {			//Login activity's click event
		switch (v.getId()) {
  		case R.id.login_button:
		    checkLogin();
		    break;
  		case R.id.cancel_button:
	    	finish();
    		break;
    	case R.id.new_user_button:
    	    startActivity(new Intent(this, Account.class));		// start a new activity Direct invocation 
    	    break;
		}
    }
}
