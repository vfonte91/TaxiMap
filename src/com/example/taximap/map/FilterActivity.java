package com.example.taximap.map;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;

import com.example.taximap.Constants;
import com.example.taximap.R;

public class FilterActivity extends Activity implements OnItemSelectedListener,
		OnClickListener {
	public static SparseArray<Map<String, String>> filters; 
	public static SparseArray<Map<String, Boolean>> classifications;	
	private static int markerType;
	private static CheckBox companyCheck,ratingCheck;
	public static char[] classificationCode={'0','0'}; //company and rating 
	static {
		if (MapViewActivity.markerType != 0) {
			markerType = MapViewActivity.markerType;
		} else {
			markerType = Constants.DRIVER;
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
		filters = new SparseArray<Map<String, String>>();
		if (markerType == Constants.DRIVER) {
			filters.put(Constants.DRIVER, new HashMap<String, String>());
			filters.get(Constants.DRIVER).put("company", "");
			filters.get(Constants.DRIVER).put("rating", "");
			filters.get(Constants.DRIVER).put("distance", "");
		} else if (markerType == Constants.CUSTOMER) {
			filters.put(Constants.CUSTOMER, new HashMap<String, String>());
			filters.get(Constants.CUSTOMER).put("distance", "");
		}
		classifications = new SparseArray<Map<String, Boolean>>();
		if (markerType == Constants.DRIVER) {
			classifications.put(Constants.DRIVER, new HashMap<String, Boolean>());
			classifications.get(Constants.DRIVER).put("company", false);
			classifications.get(Constants.DRIVER).put("rating", false);
			classifications.get(Constants.DRIVER).put("distance", false);
		} else if (markerType == Constants.CUSTOMER) {
			classifications.put(Constants.CUSTOMER, new HashMap<String, Boolean>());
			classifications.get(Constants.CUSTOMER).put("distance", false);
		}
	}
	
	private void initSpinner() {		//set filters to empty
		Spinner sp;
		int itemLst = 0, viewId = 0;
		if (markerType == Constants.DRIVER) {
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
		} else if (markerType == Constants.CUSTOMER) { // not tested
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
