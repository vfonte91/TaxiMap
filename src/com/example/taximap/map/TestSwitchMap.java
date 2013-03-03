/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.taximap.map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.taximap.R;
import com.example.taximap.R.id;
import com.example.taximap.R.layout;

public class TestSwitchMap extends Activity implements OnClickListener {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_switch_map);
		View btnCustomerMapView = (Button) findViewById(R.id.load_driver_map_button);
		btnCustomerMapView.setOnClickListener(this);
		View btnDriverMapView = (Button) findViewById(R.id.load_customer_map_button);
		btnDriverMapView.setOnClickListener(this);
		View btnExit= (Button) findViewById(R.id.exit_button);
		btnExit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.load_driver_map_button:
			startActivity(new Intent(this, CustomerMap.class));
			break;
		case R.id.load_customer_map_button:
			startActivity(new Intent(this, DriverMap.class));
			break;
		case R.id.exit_button:
			finish();
			break;
		}

	}

}
