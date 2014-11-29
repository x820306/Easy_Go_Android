package com.im.easygo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class History extends Activity {
	
	 private String token;
	
	 protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.history_layout);
	 
	 
	        Bundle bundle=this.getIntent().getExtras();

	        token=bundle.getString("token");
	        
	        //Toast.makeText(History.this, token,100).show();
	        
	 }
}