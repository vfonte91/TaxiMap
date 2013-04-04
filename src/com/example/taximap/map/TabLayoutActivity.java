package com.example.taximap.map;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.TabHost.TabSpec;
import com.example.taximap.*;
import com.example.taximap.menu.Contact;
import com.example.taximap.menu.Help;
import com.example.taximap.menu.Settings;
 
public class TabLayoutActivity extends TabActivity {
	
	private AccountManager mAccountManager;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAccountManager = AccountManager.get(this);
        setContentView(R.layout.tablayout);
 
        TabHost tabHost = getTabHost();
 
        // Tab for MapView
        TabSpec mapspec = tabHost.newTabSpec("Map");
        // setting Title and Icon for the Tab
        mapspec.setIndicator("Map", getResources().getDrawable(R.layout.icon_map_tab));
        Intent mapIntent = new Intent(this, MapViewActivity.class);
        mapspec.setContent(mapIntent);
 
        // Tab for ListView
        TabSpec listspec = tabHost.newTabSpec("List");
        listspec.setIndicator("List", getResources().getDrawable(R.layout.icon_list_tab));
        Intent listIntent = new Intent(this, ListViewActivity.class);
        listspec.setContent(listIntent);
 
        // Tab for Profile
        TabSpec helpspec = tabHost.newTabSpec("Profile");
        helpspec.setIndicator("Profile", getResources().getDrawable(R.layout.icon_profile_tab));
        Intent helpIntent = new Intent(this, ProfileViewActivity.class);
        helpspec.setContent(helpIntent);
 
        // Adding all TabSpec to TabHost
        tabHost.addTab(mapspec); // Adding photos tab
        tabHost.addTab(listspec); // Adding songs tab
        tabHost.addTab(helpspec); // Adding videos tab
    }
    
    private void quitApplication() {
		new AlertDialog.Builder(this)
				.setTitle("Log Out")
				.setMessage("Log Out of Taxi Map?")
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								//Get all the accounts for this application on this device
						    	Account[] accounts = mAccountManager.getAccountsByType(Constants.ACCOUNT_TYPE);
					    		//There maybe more than one account, so the last one created is used
					    		Account userAccount = accounts[accounts.length - 1];
					    		//set LOGOUT key to null in users Account so it won't automatically log in
								mAccountManager.setUserData(userAccount, Constants.LOGOUT, "true");
								//Go back to log in screen
								startActivity(new Intent(TabLayoutActivity.this, Login.class));
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
					}
				}).show();
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {		// menu/action bar
		switch (item.getItemId()) {
		case R.id.menu_filter:
			// requestCode=1
			startActivityForResult(new Intent(this, FilterActivity.class), 1);
			break;
		case R.id.menu_help:
			startActivity(new Intent(this, Help.class));
			return true;
		case R.id.menu_contacts:
			startActivity(new Intent(this, Contact.class));
			return true;
		case R.id.menu_exit:
			quitApplication();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}


	// callback from filter activity
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1) {
			// do something
			if (resultCode == RESULT_OK) {
				MapViewActivity.loadMarkers();
			} else {
				Toast.makeText(this, "Filter Cancelled", Toast.LENGTH_SHORT)
						.show();
			}
		} else {
			Toast.makeText(this, "Request Code Error", Toast.LENGTH_SHORT)
					.show();
		}

	}
}