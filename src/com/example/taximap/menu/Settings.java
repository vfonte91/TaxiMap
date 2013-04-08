package com.example.taximap.menu;

import com.example.taximap.R;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

//this view is currently not active
public class Settings extends PreferenceActivity {
	
	private final static String 	OPT_LOCATION 		= "location";
	private final static String 	OPT_LOCATION_DEF 	= "Dreese Laboratories, 2015 Neil Avenue, Columbus, OH";
	private final static String 	OPT_ENABLE_UPDATE 		= "enable_location_update";
	private final static boolean 	OPT_ENABLE_UPDATE_DEF 	= true;
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.layout.menu_settings);
	}
	public static String getName(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
		.getString(OPT_LOCATION, OPT_LOCATION_DEF);
	}

	public static boolean doesHumanPlayFirst(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
		.getBoolean(OPT_ENABLE_UPDATE, OPT_ENABLE_UPDATE_DEF);
	}
	
}
