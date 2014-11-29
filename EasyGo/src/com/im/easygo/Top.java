package com.im.easygo;

import com.facebook.AppEventsLogger;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class Top extends FragmentActivity {
	
	private MainFragment mainFragment;
	
	 protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);

	        
	        if (savedInstanceState == null) {
	            // Add the fragment on initial activity setup
	            mainFragment = new MainFragment();
	            getSupportFragmentManager()
	            .beginTransaction()
	            .add(android.R.id.content, mainFragment)
	            .commit();
	        } else {
	            // Or set the fragment from restored state info
	            mainFragment = (MainFragment) getSupportFragmentManager()
	            .findFragmentById(android.R.id.content);
	        }
	        
 
	 }
	 
	 @Override
	 protected void onResume() {
	   super.onResume();

	   // Logs 'install' and 'app activate' App Events.
	   AppEventsLogger.activateApp(this);
	 }
	 
	 @Override
	 protected void onPause() {
	   super.onPause();

	   // Logs 'app deactivate' App Event.
	   AppEventsLogger.deactivateApp(this);
	 }
}
