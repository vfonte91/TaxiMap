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

public class FilterActivity extends Activity implements OnItemSelectedListener  {
	private static Map<String, Map<String, String>> filterMap;
	/*private static Map<String, Map<String, String[]>> spinnerMap;
	static {
		filterMap = new HashMap<String, Map<String, String>>() {
			{
				put("driver", new HashMap<String, String>() {
					{
						put("company", "ANY");
						put("rating", "ANY");
						put("distance", "ANY");
					}
				});
				put("customer", new HashMap<String, String>() {
					{
						put("distance", "ANY");
					}
				});
			}
		};
		spinnerMap = new HashMap<String, Map<String, String[]>>() {
			{
				put("driver", new HashMap<String, String[]>() {
					{
						put("company", null);
						put("rating", null);
						put("distance", null);
					}
				});
				put("customer", new HashMap<String, String[]>() {
					{
						put("distance", null);
					}
				});
			}
		};
	}*/

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.content_map_layout);

		// load spinner values
		if (MapActivity.markerType=="driver"){
			// add a list of company for selection
			String[] keyArray={"company","rating","distance"};
			for (String key:keyArray){
				Spinner sp;
				int itemLst=0,viewId=0;
				if (key=="company"){
					itemLst=R.array.company_list;
					viewId=R.id.company_filter;
				}
				else if (key=="rating"){
					itemLst=R.array.rating_list;
					viewId=R.id.rating_filter;
				}
				else if (key=="distance"){
					itemLst=R.array.dist_list;
					viewId=R.id.dist_filter;
				}
				sp = (Spinner) findViewById(viewId);
				ArrayAdapter<CharSequence> adapter = ArrayAdapter
			            .createFromResource(this, itemLst, android.R.layout.simple_spinner_item);
				sp.setAdapter(adapter);
				sp.setOnItemSelectedListener(this);
			}
		}
	}

	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id){
		Toast.makeText(getBaseContext(), 
				"you select : " + parent.getItemAtPosition(pos), Toast.LENGTH_SHORT).show();
	}
	public static void updateFilter(String key, String value) {
		filterMap.get(MapActivity.markerType).put(key, value);
	}


	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
}
