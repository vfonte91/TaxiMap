package com.example.taximap.map;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;

import com.example.taximap.R;

public class FilterActivity extends Activity implements OnItemSelectedListener,
		OnClickListener {
	public static Map<String, Map<String, String>> filters; 
	public static Map<String, Map<String, Boolean>> classifications;	
	private static String markerType;
	private static CheckBox companyCheck,ratingCheck,distanceCheck;
	public static char[] classificationCode={'0','0'}; //company and rating 
	static {
		if (MapViewActivity.markerType != null) {
			markerType = MapViewActivity.markerType;
		} else {
			markerType = "driver";
		}
	}
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.filter_layout);
		resetFilter();
		classificationCode[0]='0';
		classificationCode[1]='0';
		initSpinner();
		((Button) findViewById(R.id.button_apply_filter)).setOnClickListener(this);
		companyCheck=((CheckBox) findViewById(R.id.by_company));
		companyCheck.setChecked(false);
		ratingCheck=((CheckBox) findViewById(R.id.by_rating));
		ratingCheck.setChecked(false);
	}
	protected void onResume(){
		super.onResume();
		resetFilter();
	}
	private static void resetFilter(){
		filters = new HashMap<String, Map<String, String>>();
		if (markerType.equals("driver")) {
			filters.put("driver", new HashMap<String, String>());
			filters.get("driver").put("company", "");
			filters.get("driver").put("rating", "");
			filters.get("driver").put("distance", "");
		} else if (markerType.equals("customer")) {
			filters.put("customer", new HashMap<String, String>());
			filters.get("customer").put("distance", "");
		}
		classifications = new HashMap<String, Map<String, Boolean>>();
		if (markerType.equals("driver")) {
			classifications.put("driver", new HashMap<String, Boolean>());
			classifications.get("driver").put("company", false);
			classifications.get("driver").put("rating", false);
			classifications.get("driver").put("distance", false);
		} else if (markerType.equals("customer")) {
			classifications.put("customer", new HashMap<String, Boolean>());
			classifications.get("customer").put("distance", false);
		}
	}
	
	private void initSpinner() {		//set filters to empty
		Spinner sp;
		int itemLst = 0, viewId = 0;
		if (markerType == "driver") {
			findViewById(R.id.textView1).setVisibility(View.VISIBLE);
			findViewById(R.id.textView2).setVisibility(View.VISIBLE);
			findViewById(R.id.company_filter).setVisibility(View.VISIBLE);
			findViewById(R.id.rating_filter).setVisibility(View.VISIBLE);
			String[] keyArray = { "company", "rating", "distance" };
			for (String key : keyArray) {
				if (key.equals("company")) {
					itemLst = R.array.company_list;
					viewId = R.id.company_filter;
				} else if (key.equals("rating")) {
					itemLst = R.array.rating_list;
					viewId = R.id.rating_filter;
				} else if (key.equals( "distance")) {
					itemLst = R.array.dist_list;
					viewId = R.id.dist_filter;
				}
				sp = (Spinner) findViewById(viewId);
				ArrayAdapter<CharSequence> adapter = ArrayAdapter
						.createFromResource(this, itemLst,
								android.R.layout.simple_spinner_item);
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				sp.setAdapter(adapter);
				sp.setOnItemSelectedListener(this);
			}
		} else if (markerType == "customer") { // not tested
			findViewById(R.id.textView1).setVisibility(View.GONE);
			findViewById(R.id.textView2).setVisibility(View.GONE);
			findViewById(R.id.company_filter).setVisibility(View.GONE);
			findViewById(R.id.rating_filter).setVisibility(View.GONE);
			String[] keyArray = {"distance"};
			for (String key : keyArray) {
				if (key == "distance") {
					itemLst = R.array.dist_list;
					viewId = R.id.dist_filter;
				}
			}
		}
		
	}

	// fires on new selection
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		String value="";
		switch (parent.getId()) {
		case R.id.company_filter:
			value=parent.getItemAtPosition(pos).toString();
			updateFilter("company", value);
			break;
		case R.id.rating_filter:
			value=parent.getItemAtPosition(pos).toString();
			updateFilter("rating", value);
			break;
		case R.id.dist_filter:
			value=parent.getItemAtPosition(pos).toString();
			updateFilter("distance", value);
			break;
		}
	}

	public void updateFilter(String key, String value) {
		filters.get(markerType).put(key, value);
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_apply_filter:
			Intent intent=new Intent();
			if(companyCheck.isChecked()){
				classificationCode[0]='1';
			}else{
				classificationCode[0]='0';
			}
			if(ratingCheck.isChecked()){
				classificationCode[1]='1';
			}else{
				classificationCode[1]='0';
			}
			this.setResult(RESULT_OK, intent);
            this.finish();
			break;
		}

	}
}
