package com.example.taximap.map;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Button;
import com.example.taximap.*;

public class ListViewActivity extends Activity implements android.view.View.OnClickListener{
	public void onCreate(Bundle savedInstanceState) {  
	    super.onCreate(savedInstanceState);  
	    setContentView(R.layout.content_list_layout); 
	    ((Button)findViewById(R.id.sort_by_name)).setOnClickListener(this);
	    ((Button)findViewById(R.id.sort_by_company)).setOnClickListener(this);
	    ((Button)findViewById(R.id.sort_by_rating)).setOnClickListener(this);
	    ((Button)findViewById(R.id.sort_by_distance)).setOnClickListener(this);
	}  
	public void onResume(){
		super.onResume();
		createList("name");
	}
	
	private void createList(String sortField){

		//bind ListView and use it as the container for listitem
	    ListView list = (ListView) findViewById(R.id.listview);  
	    list.setAdapter(null);
	    ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();  
	    //sort DriverList by name default
	    if(MapViewActivity.markerType.equals("driver")){
	    	if(MapViewActivity.driverLst==null){return;}
	    	if(sortField.equals("name")){
			    Collections.sort(MapViewActivity.driverLst, new Comparator<Driver>(){
			        public int compare(Driver o1, Driver o2){
			            int compResult=o1.name.compareTo(o2.name);
			    		return compResult;
			        }
			    });
	    	}else if(sortField.equals("company")){
			    Collections.sort(MapViewActivity.driverLst, new Comparator<Driver>(){
			        public int compare(Driver o1, Driver o2){
			            int compResult=o1.company.compareTo(o2.company);
			    		return compResult;
			        }
			    });
	    	}
	    	if(sortField.equals("rating")){
			    Collections.sort(MapViewActivity.driverLst, new Comparator<Driver>(){
			        public int compare(Driver o1, Driver o2){
			            return o1.rating<o2.rating?1:-1;
			        }
			    });
	    	}
	    	if(sortField.equals("distance")){
			    Collections.sort(MapViewActivity.driverLst, new Comparator<Driver>(){
			        public int compare(Driver o1, Driver o2){
			            return o1.distance>o2.distance?1:-1;
			        }
			    });
	    	}
		    for(Driver d:MapViewActivity.driverLst)  
		    {  
		        HashMap<String, String> map = new HashMap<String, String>();  
		        map.put("itemtitle", d.name);  
		        map.put("itemtext", d.snippet());  
		        mylist.add(map);  
		    }  
		    SimpleAdapter adapter = new SimpleAdapter(this, 
		                                                mylist,	//data source
		                                                R.layout.listviewitem,	//ListItem XML
		                                                // key correspondence
		                                                new String[] {"itemtitle", "itemtext"},   
		                                                // ListItem的XML文件里面的两个TextView ID  
		                                                new int[] {R.id.itemtitle,R.id.itemtext});  
		    // add and display
		    list.setAdapter(adapter); 
	    }
	}
	@Override
	public void onClick(View arg0) {
		switch(arg0.getId()){
		case R.id.sort_by_name:
			createList("name");
			break;
		case R.id.sort_by_company:
			createList("company");
			break;
		case R.id.sort_by_rating:
			createList("rating");
			break;
		case R.id.sort_by_distance:
			createList("distance");
			break;
		}
		
	}
}
