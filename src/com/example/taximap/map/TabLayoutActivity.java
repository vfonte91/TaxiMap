package com.example.taximap.map;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import com.example.taximap.*;
import com.example.taximap.menu.ContactsView;
import com.example.taximap.menu.Help;
import com.example.taximap.menu.Settings;
 
public class TabLayoutActivity extends TabActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tablayout);
 
        TabHost tabHost = getTabHost();
 
        // Tab for Map
        TabSpec mapspec = tabHost.newTabSpec("Map");
        // setting Title and Icon for the Tab
        mapspec.setIndicator("Map", getResources().getDrawable(R.layout.icon_map_tab));
        Intent mapIntent = new Intent(this, MapViewActivity.class);
        mapspec.setContent(mapIntent);
 
        // Tab for Filters
        TabSpec listspec = tabHost.newTabSpec("List");
        listspec.setIndicator("List", getResources().getDrawable(R.layout.icon_list_tab));
        Intent listIntent = new Intent(this, ListActivity.class);
        listspec.setContent(listIntent);
 
        // Tab for Help
        TabSpec helpspec = tabHost.newTabSpec("Help");
        helpspec.setIndicator("Help", getResources().getDrawable(R.layout.icon_help_tab));
        Intent helpIntent = new Intent(this, HelpActivity.class);
        helpspec.setContent(helpIntent);
 
        // Adding all TabSpec to TabHost
        tabHost.addTab(mapspec); // Adding photos tab
        tabHost.addTab(listspec); // Adding songs tab
        tabHost.addTab(helpspec); // Adding videos tab
    }
    
    private void quitApplication() {
		new AlertDialog.Builder(this)
				.setTitle("Exit")
				.setMessage("Quit Taxi Map?")
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								System.exit(0);
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

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_help:
			startActivity(new Intent(this, Help.class));
			return true;
		case R.id.menu_exit:
			quitApplication();
			return true;
		case R.id.menu_contacts:
			startActivity(new Intent(this, ContactsView.class));
			return true;
		}
		return false;
	}
}