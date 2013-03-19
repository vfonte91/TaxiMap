package com.example.taximap;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentView extends Fragment{
	private int layout;
	
	public FragmentView(int resource) {
		this.layout = resource;		
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(this.layout, container, false);
    }
}
