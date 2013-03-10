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
   private EditText userNameEditableField;
   private EditText passwordEditableField;

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
