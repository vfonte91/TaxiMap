package com.example.taximap;

import android.app.Application;

public class TaxiMapApp extends Application{
	private Integer[] state;
	public TaxiMapApp() {
		// TODO Auto-generated constructor stub
	}
	public void setState(Integer[] state){
		this.state=state;
	}
	public Integer[] getState(){
		return state;
	}
	public void onCreate(){
		super.onCreate();
	}
}

/*In an activity, use
MyApp appState=(MyApp)getApplicationContext();
Integer[] state=appState.getState();*/
