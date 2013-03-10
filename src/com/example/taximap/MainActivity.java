package com.example.taximap;

import com.example.taximap.*;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

public class MainActivity extends Activity {
    protected boolean active = true;
    protected int splashTime = 1000;
    protected int timeIncrement = 100;
    protected int sleepTime = 100;
    private static final String TAG = "Splash Screen Activity";
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
    	// MICHAEL WAS HERE
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        
        //Action bar setup
        ActionBar actionbar = getActionBar();
        //set navigation to tabs
        actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        //Initiate tabs
        ActionBar.Tab EditProfileTab = actionbar.newTab().setText("Edit Profile");
        ActionBar.Tab FilterTab = actionbar.newTab().setText("Filter");
        
        //create two fragments to display content
        Fragment EditProfileFrag = new FragmentView(R.layout.profile);
        Fragment FilterFrag = new FragmentView(R.layout.filter);
        
        //set tab listener
        EditProfileTab.setTabListener(new TabListener(EditProfileFrag));
        FilterTab.setTabListener(new TabListener(FilterFrag));
        
        actionbar.addTab(EditProfileTab);
        actionbar.addTab(FilterTab);
        
        // thread for displaying the SplashScreen
        Thread splashThread = new Thread() {
            @Override
            public void run() {
                try {
                    int elapsedTime = 0;
                    while(active && (elapsedTime < splashTime)) {
                        sleep(sleepTime);
                        if(active) elapsedTime = elapsedTime + timeIncrement;
                    }
                } catch(InterruptedException e) {
                    // do nothing
                } finally {
                    finish();
                    //intent invocation - Matching component name 
                    startActivity(new Intent("com.example.taximap.CustomerMap"));
                    //startActivity(new Intent("com.example.taximap.Navigation"));		//Navigation.xml missing..
                }
            }
        };
        splashThread.start();
        Log.i(TAG, "onCreate()");
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
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            active = false;
        }
        return true;
    }
}