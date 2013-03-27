package com.example.taximap.map;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.taximap.*;

public class ListViewActivity extends Activity{
	public void onCreate(Bundle savedInstanceState) {  
	    super.onCreate(savedInstanceState);  
	    setContentView(R.layout.content_list_layout); 
	    
	}  
	public void onResume(){
		super.onResume();
		//bind ListView and use it as the container for listitem
	    ListView list = (ListView) findViewById(R.id.listview);  
	    ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();  
	    //sort DriverList by name default
	    if(MapViewActivity.markerType.equals("driver")){
	    	if(MapViewActivity.driverLst==null){return;}
		    Collections.sort(MapViewActivity.driverLst, new Comparator<Driver>(){
		        public int compare(Driver o1, Driver o2){
		            int compResult=o1.name.compareTo(o2.name);
		    		return compResult;
		        }
		    });
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
}
