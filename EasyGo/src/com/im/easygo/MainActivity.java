package com.im.easygo;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.location.*;
import android.os.Bundle;
import android.os.Handler;  
import android.os.Message;  
import android.os.Build;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.*;
import android.webkit.*;
import android.widget.*;
import android.content.DialogInterface;
import android.content.Intent;

import org.apache.http.HttpResponse;  
import org.apache.http.NameValuePair;  
import org.apache.http.client.ClientProtocolException;  
import org.apache.http.client.HttpClient;  
import org.apache.http.client.entity.UrlEncodedFormEntity;  
import org.apache.http.client.methods.HttpGet;  
import org.apache.http.client.methods.HttpPost;  
import org.apache.http.impl.client.DefaultHttpClient;  
import org.apache.http.message.BasicNameValuePair;  
import org.apache.http.util.EntityUtils;  
import org.json.JSONArray;
import org.json.JSONException;  
import org.json.JSONObject;  

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;  
import java.util.List; 
import java.io.IOException;  
import java.io.UnsupportedEncodingException;  



public class MainActivity extends Activity{

	private static final String url = "file:///android_asset/map.html";
	private WebView mWebView = null;
	private DrawerLayout layDrawer;
	private RelativeLayout lstDrawer;
	private ActionBarDrawerToggle drawerToggle;
	private Button startLBtn;
	private Button endLBtn;
	private Button findRoadPointBtn;
	private Button chooseRoadPointBtn;
	private Button rdmBtn;
	private LocationManager Lmgr;
	private String bestLocationProvider;
	private EditText startLocation;
	private EditText endLocation;
	private LocationListener locationListener;
	private String Lat;
	private String Lon;
	private String LatE;
	private String LonE;
	private static String center;
	private String rad;
	private String centerlt;
	private String centerlg;
	private String startAddress;
	private String endAddress;
	private int seFlag;
	private int findRoadPointBtnFlag=0;
	private List<String> roadPointLocation;
	private List<Integer> roadPointLocationChosen;
	private List<String> roadPointTitle;
	private AlertDialog.Builder builder;
	private int cancelflag=0;
	private int goflag=0;

	
	
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initActionBar();
        initDrawer();
        
        startLocation = (EditText)findViewById(R.id.editText1);
        endLocation = (EditText)findViewById(R.id.editText2);
    
        startLBtn=(Button)findViewById(R.id.button1);
        endLBtn=(Button)findViewById(R.id.button2);
        findRoadPointBtn=(Button)findViewById(R.id.button3);
        chooseRoadPointBtn=(Button)findViewById(R.id.button4);
        rdmBtn=(Button)findViewById(R.id.button5);
        
	    
       
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
				
					mWebView.loadUrl("javascript:startLocation("+Lat+","+Lon+")");
				}else if(seFlag==1){
					LatE=String.valueOf(location.getLatitude());
					LonE=String.valueOf(location.getLongitude());
					Lmgr.removeUpdates(this);
					
					mWebView.loadUrl("javascript:endLocation("+LatE+","+LonE+")");
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
        				mWebView.loadUrl("javascript:frp('"+startAddress+"','"+endAddress+"')");
        			}
        		
        	}
        	});
        
        
        chooseRoadPointBtn.setOnClickListener(new Button.OnClickListener(){
        	@Override
        	public void onClick(View v) {
        		
        		int a;
        		String[] temp = new String[roadPointTitle.size()];
        		for(a=0;a<roadPointTitle.size();a++)
				{
        			temp[a]=roadPointTitle.get(a);
				}
        		boolean[] temp2 = new boolean[roadPointLocationChosen.size()];
        		for(a=0;a<roadPointLocationChosen.size();a++)
				{
        			if(roadPointLocationChosen.get(a)==0)
        				temp2[a]=false;
        			else
        				temp2[a]=true;
				}
        	
        		builder = new AlertDialog.Builder(MainActivity.this);
        		builder.setMultiChoiceItems(temp, temp2,
        		new DialogInterface.OnMultiChoiceClickListener() {
        			public void onClick(DialogInterface dialog, int whichButton,boolean isChecked) {
        					if(isChecked) {
        						int o;
        						int localCtr=0;
        						roadPointLocationChosen.set(whichButton,1);
        						
        						for(o=0;o<roadPointLocationChosen.size();o++)
        						{
        							if(roadPointLocationChosen.get(o)==1)
        							{
        								localCtr++;
        							}
        						}
        						
        						if(localCtr==6)
        						{
        							chooseRoadPointBtn.setEnabled(false);
        							Toast.makeText(MainActivity.this,"已達上限", 100).show();
        							dialog.cancel();
        						}
        						
        					}else {
        						roadPointLocationChosen.set(whichButton,0);
        					}
        					 
        			}
        		});
        		
        		builder.setPositiveButton("確定", new DialogInterface.OnClickListener() {
        			public void onClick(DialogInterface dialog, int whichButton) {
        				//Toast.makeText(MainActivity.this,"aaa", 100).show();  
        			}
        		});
        			
        		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
        			public void onClick(DialogInterface dialog, int whichButton) {
        			 
        			}
        		});
        			
        		builder.create().show();

        	}
        });
		
		rdmBtn.setOnClickListener(new Button.OnClickListener(){
        	@Override
        	public void onClick(View v) {
        		final int rngMax=roadPointTitle.size();
        		final int crr;
        		int tyu;
        		int flg;
        		int j;
        		final List<Integer> haha=new ArrayList<Integer>();

        		if(rngMax>0)
        		{
        			if(rngMax<5)
        			{
        				crr=(int)(Math.random()*rngMax+1);
        			}else
        			{
        				crr=(int)(Math.random()*5+1);
        			}

        			int hgh=0;

        			while(hgh<crr)
        			{
        				tyu=(int)(Math.random()*rngMax+1);
        				flg=0;
        				for(j=0;j<haha.size();j++)
        				{
        				   if(tyu==haha.get(j))
        				   {
        					   flg=1;
        				   }
        				}
        				if(flg!=1)
        				{

        				  haha.add(tyu);
        				  hgh++;
        				}
        			}

        			for(j=0;j<roadPointLocationChosen.size();j++)
        			{
        				roadPointLocationChosen.set(j,0);
        				
        		    }

        			for(j=0;j<haha.size();j++)
        			{
        				roadPointLocationChosen.set(haha.get(j)-1,1);
        				
        		    }
        			
        			chooseRoadPointBtn.setEnabled(true);

        		}
        		
        	}});
        
		chooseRoadPointBtn.setEnabled(false);
		rdmBtn.setEnabled(false);
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
	        }else {
	        	
	        	switch (item.getItemId()) {
	        		case R.id.cancel:
	        			cancelByflag();
	        			return true;
	        		case R.id.go:
	        			goByflag();
	        			return true;
	        		default:
	        			return super.onOptionsItemSelected(item);
	        	}
	        }
	        
	    }
	    
	    public boolean onCreateOptionsMenu(Menu menu) {  
	        MenuInflater inflater = getMenuInflater();  
	        inflater.inflate(R.menu.options_menu, menu);  
	        return super.onCreateOptionsMenu(menu);  
	    }  
	    
	    public void cancelByflag() {  
	        
	    }  
	    
	    public void goByflag() {  
	       
	    	/*runOnUiThread(new Runnable() {
	            public void run() {
	            	mWebView.loadUrl("javascript:alert('"+goflag+"')");
	           }
	       });*/
	    	
	    	
	    	if(goflag==1)
	    	{
	    		runOnUiThread(new Runnable() {
		            public void run() {
		            	mWebView.loadUrl("javascript:route("+roadPointLocation.size()+")");
		           }
		       });
	    		
	    		
	    	}
	    }  
	    

	    
	    public void httpPost(final String postUrl,final List<NameValuePair> params,final int postFlag){
	    	
	  
					HttpPost post=new HttpPost(postUrl);  
					 try {
						post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
						
						HttpClient client=new DefaultHttpClient();  
	                    HttpResponse response=client.execute(post);  
	                    if(response.getStatusLine().getStatusCode()==200){  
	                        String content=EntityUtils.toString(response.getEntity());     
	
	                        analysisJSON(content,postFlag);  
	                    } 
					} catch (ClientProtocolException e) {  
	                    e.printStackTrace();  
	                } catch (IOException e) {  
	                    e.printStackTrace();  
	                }  
	                    
				
	    }
	    
	    public void analysisJSON(String content,int postFlag){  
	    	if(postFlag==0){
	    		
	    		try {
	    			final JSONObject obj = new JSONObject(content);
	    			
	    			if(obj.getString("message").equals("yes")==true){
	    			   				
	    				
	    			    JSONArray dataAry=obj.getJSONArray("data");
	    				int ctr=dataAry.length();
	    				int a,b;
	    				
	    				
	    				roadPointLocation=new ArrayList<String>();
	    				roadPointTitle=new ArrayList<String>();
	    				roadPointLocationChosen=new ArrayList<Integer>();
	    				
	    				if(ctr>20)
	    				{
	    					a=20;
	    					b=ctr;
	    				}else{
	    					a=ctr;
	    					b=ctr;
	    				}
	    					int aryCtr=0;
							int rdr=0;
							int iu;
	    					
							while((roadPointLocation.size()<a)&&(aryCtr<b))
							{
							   int flagy=0;
							   String qwas=dataAry.getJSONObject(aryCtr).getString("latitude")+","+dataAry.getJSONObject(aryCtr).getString("longitude");
							   String qwas2=dataAry.getJSONObject(aryCtr).getString("title");
							   
							   if(roadPointLocation.size()==0)
							   {
								   roadPointLocation.add(qwas);
								   roadPointTitle.add(qwas2);
								   flagy=-1;
								  
							   }else{

								   for(iu=0;iu<roadPointLocation.size();iu++)
								   {

									   if(qwas.equals(roadPointLocation.get(iu)))
									   {
										   rdr=iu;
										   flagy=1;
									   }
								   }
							   }
							   
							   if(flagy==0)
							   {
								   roadPointLocation.add(qwas);
								   roadPointTitle.add(qwas2);
							   }else if(flagy==1)
							   {
								   roadPointTitle.set(rdr,roadPointTitle.get(rdr)+'&');
								   roadPointTitle.set(rdr,roadPointTitle.get(rdr)+qwas2);
							   }
							   aryCtr++;
							}	    				
	    				
							for(a=0;a<roadPointTitle.size();a++)
							{
								roadPointTitle.set(a,roadPointTitle.get(a)+"#推薦地點");
								roadPointLocationChosen.add(0);
							}
							
							
							
							
							runOnUiThread(new Runnable() {
					            public void run() {
					            	chooseRoadPointBtn.setEnabled(true);
									rdmBtn.setEnabled(true);
									mWebView.loadUrl("javascript:killRPmarkers()");
					            	mWebView.loadUrl("javascript:setMarkers("+roadPointLocation.size()+")");
					           }
					       });
							
								
	    			}else{
	    				
	    				runOnUiThread(new Runnable() {
				            public void run() {
				            	chooseRoadPointBtn.setEnabled(false);
								rdmBtn.setEnabled(false);
								mWebView.loadUrl("javascript:killRPmarkers()");
				           }
				       });
	    				
	    			}
	    		} catch (JSONException e) {
	    			e.printStackTrace();
	    		}  

	        }
	    }  
	    
	    
	    public void postData(final int postFlag){
	    	if(postFlag==0){
	    		String postUrl="http://easygo.ballchen.cc/check_roadpoint";
	    		List<NameValuePair> params=new ArrayList<NameValuePair>();  
                params.add(new BasicNameValuePair("lat_self", centerlt));  
                params.add(new BasicNameValuePair("lon_self", centerlg));  
                params.add(new BasicNameValuePair("rad", rad)); 
                httpPost(postUrl,params,postFlag);
	    	}
	    	
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
	    public void frpReturn(final String center0,final String rad0,final String centerlt0,final String centerlg0)
	    {
	    	
	    	center=center0;
	    	rad=rad0;
	    	centerlt=centerlt0;
	    	centerlg=centerlg0;
	    	postData(0);
	    	
	    	findRoadPointBtnFlag=0;
	    	goflag=1;
	     }
	    
	    @JavascriptInterface
	    public String getRPLocation(final String pos)
	    {
	    	int intValue=Integer.valueOf(pos);
	    	final String localS=roadPointLocation.get(intValue);
	    	
	    	return localS;
	    	
	    }
	    
	    @JavascriptInterface
	    public String getRPTitle(final String pos)
	    {
	    	
	    	int intValue=Integer.valueOf(pos);
	    	final String localS=roadPointTitle.get(intValue);
	    	
	    	return localS;
	    }
	    
	    @JavascriptInterface
	    public String getRPChosen(final String pos)
	    {
	    	
	    	int intValue=Integer.valueOf(pos);
	    	final String localS=Integer.toString(roadPointLocationChosen.get(intValue));
	    	
	    	return localS;
	    }
	    
	    @JavascriptInterface
	    public void chooseByRec(int Rec)
	    {
	    	 int flagGG=0;
	    	 int chosenCtr=0;
	    	 int j;
	    	
	    	if(roadPointLocationChosen.get(Rec)==1)
	    	{
	    		flagGG=1;
	    		Toast.makeText(MainActivity.this,"重複選擇囉", 100).show();
	    	}
	    	
	    	for(j=0;j<roadPointLocationChosen.size();j++)
			{
				if(roadPointLocationChosen.get(j)==1)
				{
					chosenCtr++;
				}
		    }
	    	
	    	if((flagGG==0)&&(chosenCtr<6)){
	    		roadPointLocationChosen.set(Rec,1);
	    		Toast.makeText(MainActivity.this,"已選擇", 100).show();
	    		chosenCtr++;
	    		   
	    		if(chosenCtr==6)
	    		{
	    			Toast.makeText(MainActivity.this,"已達上限", 100).show();
	    		}
	    	}
	    	else if((flagGG==0)&&(chosenCtr==6)){
	    		Toast.makeText(MainActivity.this,"已達上限", 100).show();
	    	}
	     }
	    
	    
	    

}





