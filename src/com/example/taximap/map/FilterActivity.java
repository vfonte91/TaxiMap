package com.example.taximap.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.taximap.R;
import com.example.taximap.menu.Help;
import com.example.taximap.menu.Settings;

public class FilterActivity extends Activity implements OnItemSelectedListener, OnClickListener {
	public static Map<String, Map<String, String>> filters;
	private static String mapViewType;

	static {
		mapViewType= MapViewActivity.markerType;
		filters = new HashMap<String, Map<String, String>>();
		filters.put("driver", new HashMap<String, String>());
		filters.get("driver").put("company", "");
		filters.get("driver").put("rating", "");
		filters.get("driver").put("distance", "");
		filters.put("customer", new HashMap<String, String>());
		filters.get("customer").put("distance", "");
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.filter_layout);
		
		if (mapViewType == "driver") {
			findViewById(R.id.textView1).setVisibility(View.VISIBLE);
			findViewById(R.id.textView2).setVisibility(View.VISIBLE);
			findViewById(R.id.company_filter).setVisibility(View.VISIBLE);
			findViewById(R.id.rating_filter).setVisibility(View.VISIBLE);

			String[] keyArray = { "company", "rating", "distance" };
			for (String key : keyArray) {
				Spinner sp;
				int itemLst = 0, viewId = 0;
				if (key == "company") {
					itemLst = R.array.company_list;
					viewId = R.id.company_filter;
				} else if (key == "rating") {
					itemLst = R.array.rating_list;
					viewId = R.id.rating_filter;
				} else if (key == "distance") {
					itemLst = R.array.dist_list;
					viewId = R.id.dist_filter;
				}
				sp = (Spinner) findViewById(viewId);
				ArrayAdapter<CharSequence> adapter = ArrayAdapter
						.createFromResource(this, itemLst,
								android.R.layout.simple_spinner_item);
				sp.setAdapter(adapter);
				sp.setOnItemSelectedListener(this);
			}
		} else if (mapViewType == "customer") {
			findViewById(R.id.textView1).setVisibility(View.GONE);
			findViewById(R.id.textView2).setVisibility(View.GONE);
			findViewById(R.id.company_filter).setVisibility(View.GONE);
			findViewById(R.id.rating_filter).setVisibility(View.GONE);

			String[] keyArray = { "distance" };
			for (String key : keyArray) {
				Spinner sp;
				int itemLst = 0, viewId = 0;
				if (key == "distance") {
					itemLst = R.array.dist_list;
					viewId = R.id.dist_filter;
				}
				sp = (Spinner) findViewById(viewId);
				ArrayAdapter<CharSequence> adapter = ArrayAdapter
						.createFromResource(this, itemLst,
								android.R.layout.simple_spinner_item);
				sp.setAdapter(adapter);
				sp.setOnItemSelectedListener(this);
			}
		}
	}

	// fires on new selection
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		Toast.makeText(
				getBaseContext(),
				"you select : "
						+ parent.getItemAtPosition(pos), Toast.LENGTH_SHORT)
				.show();
		switch (parent.getId()) {
		case R.id.company_filter:
			updateFilter("company", parent.getItemAtPosition(pos).toString());
			break;
		case R.id.rating_filter:
			updateFilter("rating", parent.getItemAtPosition(pos).toString());
			break;
		case R.id.dist_filter:
			updateFilter("distance", parent.getItemAtPosition(pos).toString());
			break;
		}
	}

	public static void updateFilter(String key, String value) {
		filters.get(mapViewType).put(key, value);
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.button_apply_filter:
			finishActivity(0);		//take you back to the previous activity.
			/*startActivity(new Intent(this,MapViewActivity.class));*/
			MapViewActivity.loadMarkers();
			break;
		}
		
	}
}
