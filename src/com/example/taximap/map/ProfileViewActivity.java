package com.example.taximap.map;

import android.app.Activity;
import android.os.Bundle;
import com.example.taximap.*;
import android.widget.TextView;

public class ProfileViewActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_profile_layout);
        ((TextView) findViewById(R.id.name_text)).setText(String.format("Name: %s", MapViewActivity.uName));
        ((TextView) findViewById(R.id.location_text)).setText(String.format("Name: %s", MapViewActivity.myLastAddress));
    }
}