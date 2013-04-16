package com.example.taximap.map;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
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
import android.widget.Toast;

import com.example.taximap.Constants;
import com.example.taximap.R;

public class FilterActivity extends Activity implements OnItemSelectedListener, OnClickListener {
	public static SparseArray<Map<Integer,String>> filters; 
	public static SparseArray<Map<Integer, Boolean>> classifications;	
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
	
	//Called every time Filters menu is selected 
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.filter_layout);
		((Button) findViewById(R.id.button_apply_filter)).setOnClickListener(this);
		//Check if filters and classifications were previously setup
		if (filters == null) {
			//Setup filters and classifications, plus view
			createFilter();
			//Set checkboxes to unchecked
		}
		// set listener
		companyCheck=((CheckBox) findViewById(R.id.by_company));
		companyCheck.setChecked(false);
		ratingCheck=((CheckBox) findViewById(R.id.by_rating));
		ratingCheck.setChecked(false);
		//Set classification to none
		classificationCode[0]='0';
		classificationCode[1]='0';
		//Setup spinners using filter values
		initSpinner();
	}
	protected void onResume(){
		super.onResume();
	}
	private static void createFilter(){
		filters = new SparseArray<Map<Integer, String>>();
		classifications = new SparseArray<Map<Integer, Boolean>>();
		if (markerType == Constants.DRIVER) {
			filters.put(Constants.DRIVER, new HashMap<Integer, String>());
			filters.get(Constants.DRIVER).put(Constants.COMPANY, "");
			filters.get(Constants.DRIVER).put(Constants.RATING, "");
			filters.get(Constants.DRIVER).put(Constants.DISTANCE, "");
		} else if (markerType == Constants.CUSTOMER) {
			filters.put(Constants.CUSTOMER, new HashMap<Integer, String>());
			filters.get(Constants.CUSTOMER).put(Constants.DISTANCE, "");
		}
		if (markerType == Constants.DRIVER) {
			classifications.put(Constants.DRIVER, new HashMap<Integer, Boolean>());
			classifications.get(Constants.DRIVER).put(Constants.COMPANY, false);
			classifications.get(Constants.DRIVER).put(Constants.RATING, false);
			classifications.get(Constants.DRIVER).put(Constants.DISTANCE, false);
		} else if (markerType == Constants.CUSTOMER) {
			classifications.put(Constants.CUSTOMER, new HashMap<Integer, Boolean>());
			classifications.get(Constants.CUSTOMER).put(Constants.DISTANCE, false);
		}
	}
	
	//Set spinners to values
	private void initSpinner() {
		Spinner sp;
		int itemLst = 0, viewId = 0;
		if (markerType == Constants.DRIVER) {
			findViewById(R.id.textView1).setVisibility(View.VISIBLE);
			findViewById(R.id.textView2).setVisibility(View.VISIBLE);
			findViewById(R.id.company_filter).setVisibility(View.VISIBLE);
			findViewById(R.id.rating_filter).setVisibility(View.VISIBLE);
			int[] keyArray = { Constants.COMPANY, Constants.RATING, Constants.DISTANCE};
			for (int key : keyArray) {
				if (key == Constants.COMPANY) {
					itemLst = R.array.company_list;
					viewId = R.id.company_filter;
				} else if (key == Constants.RATING) {
					itemLst = R.array.rating_list;
					viewId = R.id.rating_filter;
				} else if (key == Constants.DISTANCE) {
					itemLst = R.array.dist_list;
					viewId = R.id.dist_filter;
				}
				sp = (Spinner) findViewById(viewId);
				ArrayAdapter<CharSequence> adapter = ArrayAdapter
						.createFromResource(this, itemLst,
								android.R.layout.simple_spinner_item);
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				sp.setAdapter(adapter);
				String tmp = filters.get(Constants.DRIVER).get(key);
				if (!tmp.equals("") && !tmp.equals("Any")) {
				    for (int position = 0; position < adapter.getCount(); position++)
				    {
				    	//If filter was previously set
				        if(adapter.getItem(position) == filters.get(Constants.DRIVER).get(key))
				        {
				        	//set spinner to previously selected value
				            sp.setSelection(position);
				        }
				    }
				}
				sp.setOnItemSelectedListener(this);
			}
		}		
	}

	// fires on new selection
	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		String value="";
		switch (parent.getId()) {
		case R.id.company_filter:
			value=parent.getItemAtPosition(pos).toString();
			updateFilter(Constants.COMPANY, value);
			break;
		case R.id.rating_filter:
			value=parent.getItemAtPosition(pos).toString();
			updateFilter(Constants.RATING, value);
			break;
		case R.id.dist_filter:
			value=parent.getItemAtPosition(pos).toString();
			updateFilter(Constants.DISTANCE, value);
			break;
		}
	}

	public void updateFilter(int key, String value) {
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
			
			//this.setResult(RESULT_OK, this.getIntent());
			Toast.makeText(this, "Loading new markers..", Toast.LENGTH_SHORT).show();
			FragmentTabsActivity.tabHost.setCurrentTab(0);
			this.finish();
			break;
		}
	}
}
