package com.example.taximap;

import com.example.taximap.*;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;

public class Navigation extends Activity implements ActionBar.OnNavigationListener{

    SpinnerAdapter mSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.nav_list,
            android.R.layout.simple_spinner_dropdown_item);

	@Override
	public boolean onNavigationItemSelected(int arg0, long arg1) {
		// TODO Auto-generated method stub
		return false;
	}
}
