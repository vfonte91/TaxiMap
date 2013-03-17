package com.example.taximap.menu;

import com.example.taximap.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;

public class HelpWithWebView extends Activity implements OnClickListener {
	protected void onCreate(Bundle savedInstanceState) {
		String URL=null;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu_helpwithwebview);
		WebView helpInWebView=null;
		helpInWebView = (WebView) findViewById(R.id.helpwithwebview);

		View buttonExit = findViewById(R.id.button_exit);
		buttonExit.setOnClickListener(this);
		Bundle extras = getIntent().getExtras();
		if(extras !=null){
			URL = extras.getString("URL");
		}
		
		helpInWebView.loadUrl(URL);
	}
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.button_exit:
			finishFromChild(this);
			break;
		}
	}	

}
