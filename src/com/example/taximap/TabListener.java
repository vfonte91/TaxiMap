package com.example.taximap;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Fragment;
import android.app.FragmentTransaction;

public class TabListener implements ActionBar.TabListener {
	public Fragment fragment;
	
	public TabListener(Fragment fragment) {
		this.fragment = fragment;
	}
	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		//Same tab, so do nothing		
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		ft.replace(R.id.fragment_container, fragment);		
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		ft.remove(fragment);		
	}

}
