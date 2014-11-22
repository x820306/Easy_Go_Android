package com.im.easygo;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.location.*;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.*;
import android.webkit.*;
import android.widget.*;
import android.os.Build;
import android.content.Intent;


public class MainActivity extends Activity{

	private static final String url = "file:///android_asset/map.html";
	private WebView mWebView = null;
	private DrawerLayout layDrawer;
	private RelativeLayout lstDrawer;
	private ActionBarDrawerToggle drawerToggle;
	private Button startLBtn;
	private Button endLBtn;
	private Button findRoadPointBtn;
	private LocationManager Lmgr;
	private String bestLocationProvider;
	private EditText startLocation;
	private EditText endLocation;
	private LocationListener locationListener;
	private String Lat;
	private String Lon;
	private String LatE;
	private String LonE;
	private String startAddress;
	private String endAddress;
	private int seFlag;
	private int findRoadPointBtnFlag=0;

	
	
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initActionBar();
        initDrawer();
        
        startLocation = (EditText)findViewById(R.id.editText1);
        endLocation = (EditText)findViewById(R.id.editText2);
    
        startLBtn=(Button)findViewById(R.id.button1);
        endLBtn=(Button)findViewById(R.id.button2);
        findRoadPointBtn=(Button)findViewById(R.id.button3);
        
	    
       
        mWebView = (WebView)findViewById(R.id.webView1);
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.getSettings().setJavaScriptEnabled(true);       
        mWebView.loadUrl(url); 
        mWebView.addJavascriptInterface(MainActivity.this, "android");
        Lmgr=(LocationManager)getSystemService(LOCATION_SERVICE);
        Criteria c=new Criteria();
        bestLocationProvider=Lmgr.getBestProvider(c, true);
        locationListener = new LocationListener(){

			@Override
			public void onLocationChanged(Location location) {
				// TODO Auto-generated method stub
				
				if(seFlag==0){
					Lat=String.valueOf(location.getLatitude());
					Lon=String.valueOf(location.getLongitude());
					Lmgr.removeUpdates(this);
				
					mWebView.loadUrl("javascript:startLocation()");
				}else if(seFlag==1){
					LatE=String.valueOf(location.getLatitude());
					LonE=String.valueOf(location.getLongitude());
					Lmgr.removeUpdates(this);
					
					mWebView.loadUrl("javascript:endLocation()");
				}
			}

			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub
				Lmgr.removeUpdates(this);
			}

			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {
				// TODO Auto-generated method stub
				Criteria c=new Criteria();
			    bestLocationProvider=Lmgr.getBestProvider(c, true);
			}
        };
        
        
        
        startLBtn.setOnClickListener(new Button.OnClickListener(){
        	@Override
        	public void onClick(View v) {
        		seFlag=0;
        		Lmgr.requestLocationUpdates(bestLocationProvider,0,0,locationListener);
        		
        	}
        	});
        
        endLBtn.setOnClickListener(new Button.OnClickListener(){
        	@Override
        	public void onClick(View v) {
        		seFlag=1;
        		Lmgr.requestLocationUpdates(bestLocationProvider,0,0,locationListener);
        		
        	}
        	});
        
        findRoadPointBtn.setOnClickListener(new Button.OnClickListener(){
        	@Override
        	public void onClick(View v) {
        	
        			if((startLocation.getText().toString() != null)&&(endLocation.getText().toString() != null)&&(findRoadPointBtnFlag!=1))
        			{
        				findRoadPointBtnFlag=1;
        				startAddress=startLocation.getText().toString();
        				endAddress=endLocation.getText().toString();
        				mWebView.loadUrl("javascript:frp()");
        				
        				
        				
        			}
        		
        	}
        	});
        
        
        
    }
    
	
	 private void initActionBar(){
	        getActionBar().setDisplayHomeAsUpEnabled(true);
	        getActionBar().setHomeButtonEnabled(true);
	    }

	    private void initDrawer(){
	        setContentView(R.layout.activity_main);

	        layDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
	        lstDrawer = (RelativeLayout) findViewById(R.id.left_drawer);

	        layDrawer.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

	        drawerToggle = new ActionBarDrawerToggle(
	                this, 
	                layDrawer,
	                R.drawable.ic_drawer, 
	                R.string.open_left_drawer,
	                R.string.close_left_drawer) {

	            @Override
	            public void onDrawerClosed(View view) {
	                super.onDrawerClosed(view);
	                getActionBar().setTitle(R.string.close_left_drawer);
	            }

	            @Override
	            public void onDrawerOpened(View drawerView) {
	                super.onDrawerOpened(drawerView);
	                getActionBar().setTitle(R.string.open_left_drawer);
	            }
	        };
	        drawerToggle.syncState();

	        layDrawer.setDrawerListener(drawerToggle);
	    }
	    
	    public boolean onOptionsItemSelected(MenuItem item) {
	       
	        if (drawerToggle.onOptionsItemSelected(item)) {
	          return true;
	        }
	        return super.onOptionsItemSelected(item);
	    }

	    
	    @JavascriptInterface
	    public String GetLat()
	    {
	    	return Lat;
	     }
	    
	         
	    @JavascriptInterface
	    public String GetLon()
	    {
	         return Lon;
	    }
	    
	    @JavascriptInterface
	    public String GetLatE()
	    {
	    	return LatE;
	     }
	    
	         
	    @JavascriptInterface
	    public String GetLonE()
	    {
	         return LonE;
	    }
	    
	    @JavascriptInterface
	    public String GetsAddr()
	    {
	         return startAddress;
	    }
	    
	    @JavascriptInterface
	    public String GeteAddr()
	    {
	         return endAddress;
	    }
	    
	    @JavascriptInterface
	    public void setStartLocation(final String rtn)
	    {
	    	 runOnUiThread(new Runnable() {
		            public void run() {
		            	startLocation.setText(rtn, TextView.BufferType.EDITABLE);
		           }
		       });
	     }
	    
	    @JavascriptInterface
	    public void setEndLocation(final String rtn)
	    {
	    	 runOnUiThread(new Runnable() {
		            public void run() {
		            	endLocation.setText(rtn, TextView.BufferType.EDITABLE);
		           }
		       });
	     }
	    
	    @JavascriptInterface
	    public void frpReturn()
	    {
	    	findRoadPointBtnFlag=0;
	     }
	    
	   

	    

}





