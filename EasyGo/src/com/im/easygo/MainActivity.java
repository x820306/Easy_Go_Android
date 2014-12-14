package com.im.easygo;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.location.*;
import android.os.AsyncTask;
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
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.content.DialogInterface;
import android.content.Intent;

import org.apache.http.HttpResponse;  
import org.apache.http.NameValuePair;  
import org.apache.http.client.ClientProtocolException;  
import org.apache.http.client.HttpClient;  
import org.apache.http.client.entity.UrlEncodedFormEntity;  
import org.apache.http.client.methods.HttpGet;  
import org.apache.http.client.methods.HttpPost;  
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;  
import org.apache.http.message.BasicNameValuePair;  
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;  
import org.json.JSONArray;
import org.json.JSONException;  
import org.json.JSONObject;  

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;  
import java.util.List; 
import java.util.Timer;
import java.util.TimerTask;
import java.io.IOException;  
import java.io.UnsupportedEncodingException;  
import java.net.SocketTimeoutException;
import java.net.URLDecoder;



public class MainActivity extends Activity{

	private static final String url = "file:///android_asset/map.html";
	private WebView mWebView = null;
	private DrawerLayout layDrawer;
	private ScrollView lstDrawer;
	private ActionBarDrawerToggle drawerToggle;
	private Button startLBtn;
	private Button endLBtn;
	private Button findRoadPointBtn;
	private Button chooseRoadPointBtn;
	private Button rdmBtn;
	private Button timerBtn;
	private Button saveRouteBtn;
	private Button rpCategoryBtn;
	private Switch IIIswitch;
	private LocationManager Lmgr;
	private LocationManager Lmgr2;
	private String bestLocationProvider;
	private String bestLocationProvider2;
	private EditText startLocation;
	private EditText endLocation;
	private EditText keyword;
	private TextView leftInfoText;
	private TextView rightInfoText;
	private LocationListener locationListener;
	private LocationListener locationListener2;
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
	private String startLatLng;
	private String endLatLng;
	private String tDistance;
	private String tDuration;
	private String iiiToken;
	private String keywordString;
	private int seFlag;
	private List<String> roadPointLocation;
	private List<Integer> roadPointLocationChosen;
	private List<String> roadPointTitle;
	private List<String> savedRoadPointLocation;
	private List<String> savedRoadPointTitle;
	private List<String> categoryName;
	private List<String> categoryValue;
	private List<String> categorySend;
	private List<Integer> categoryChosen;
	private AlertDialog.Builder builder;
	private AlertDialog.Builder builder2;
	private int cancelflag=0;
	private int goflag=0;
	private int timerBtnFlag=0;
	private int totalSec=0;
	private int limit;
	private int categoryIndex;
	private int leftTimes;
	private int repeatTimes=0;
	private boolean allFlag;
	private int HttpTimeoutFlag=0;
	private int serApiFlag=0;
	private int rpChosenNumber=0;
	private Timer timer1;
	private String cookie;
	private String uid;
	private String savedPath;
	private int flag=0;
	private int flag2=0;
	private Bundle bundle;
	private String[] num={"零","一","二","三","四","五","六"};

	
	
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initActionBar();
        initDrawer();
        
        startLocation = (EditText)findViewById(R.id.editText1);
        endLocation = (EditText)findViewById(R.id.editText2);
        keyword=(EditText)findViewById(R.id.editText3);
    
        startLBtn=(Button)findViewById(R.id.button1);
        endLBtn=(Button)findViewById(R.id.button2);
        findRoadPointBtn=(Button)findViewById(R.id.button3);
        chooseRoadPointBtn=(Button)findViewById(R.id.button4);
        rdmBtn=(Button)findViewById(R.id.button5);
        timerBtn=(Button)findViewById(R.id.button6);
        saveRouteBtn=(Button)findViewById(R.id.button7);
        rpCategoryBtn=(Button)findViewById(R.id.button8);
        leftInfoText=(TextView)findViewById(R.id.textView3);
        rightInfoText=(TextView)findViewById(R.id.textView4);
        IIIswitch=(Switch)findViewById(R.id.switch1);
       
        mWebView = (WebView)findViewById(R.id.webView1);
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.getSettings().setJavaScriptEnabled(true);       
        mWebView.loadUrl(url); 
        mWebView.addJavascriptInterface(MainActivity.this, "android");
        Lmgr=(LocationManager)getSystemService(LOCATION_SERVICE);
        Lmgr2=(LocationManager)getSystemService(LOCATION_SERVICE);
       
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
				if(provider.equals( bestLocationProvider)==true)
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
			}
        };
        
        locationListener2 = new LocationListener(){

			@Override
			public void onLocationChanged(Location location) {
				// TODO Auto-generated method stub
				String LatX=String.valueOf(location.getLatitude());
				String LonX=String.valueOf(location.getLongitude());
			
				mWebView.loadUrl("javascript:currentLocation("+LatX+","+LonX+")");
			
			}

			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub
				if(provider.equals( bestLocationProvider2)==true)
					Lmgr2.removeUpdates(this);
			}

			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {
				// TODO Auto-generated method stub
			}
        };
        
        
        IIIswitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {  
        	  
            @Override  
            public void onCheckedChanged(CompoundButton buttonView,  
                    boolean isChecked) {    
                if (isChecked) {  
                	serApiFlag=1;  
                } else {  
                	serApiFlag=0;  
                }  
            }  
        });  
         
        timerBtn.setOnClickListener(new Button.OnClickListener(){
        	@Override
        	public void onClick(View v) {
        		if(timerBtnFlag==0)
        		{
        			timer1=new Timer();
        			timer1.schedule(new myTask(), 0,1000);
        			
        			Criteria c=new Criteria();
            	    bestLocationProvider2=Lmgr2.getBestProvider(c, true);
            		Lmgr2.requestLocationUpdates(bestLocationProvider2,0,0,locationListener2);
        			
        			timerBtn.setText("Stop");
        			timerBtnFlag=1;
        		}else{
        			timer1.cancel();
        			timer1.purge();
        			
        			Lmgr2.removeUpdates(locationListener2);
        			mWebView.loadUrl("javascript:killCurrentLocation()");
        			
        			timerBtn.setText("Start");
        			timerBtnFlag=0;
        		}
        	}
        });
        
        startLBtn.setOnClickListener(new Button.OnClickListener(){
        	@Override
        	public void onClick(View v) {
        		seFlag=0;
        		Criteria c=new Criteria();
        	    bestLocationProvider=Lmgr.getBestProvider(c, true);
        		Lmgr.requestLocationUpdates(bestLocationProvider,0,0,locationListener);
        		
        	}
        });
        
        endLBtn.setOnClickListener(new Button.OnClickListener(){
        	@Override
        	public void onClick(View v) {
        		seFlag=1;
        		Criteria c=new Criteria();
        	    bestLocationProvider=Lmgr.getBestProvider(c, true);
        		Lmgr.requestLocationUpdates(bestLocationProvider,0,0,locationListener);
        		
        	}
        });
        
        saveRouteBtn.setOnClickListener(new Button.OnClickListener(){
        	@Override
        	public void onClick(View v) {
        		if(!uid.equals("null")){
        			int y;
        			String path="";
        			path=path+"{\"land\":\""+startLatLng+"\",\"name\":\""+startAddress+"\"},";
        			
        			for(y=0;y<roadPointLocationChosen.size();y++){
        				if(roadPointLocationChosen.get(y)==1){
        					path=path+"{\"land\":\""+roadPointLocation.get(y)+"\",\"name\":\""+roadPointTitle.get(y)+"\"},";
        				}
        			}
        			
        			path=path+"{\"land\":\""+endLatLng+"\",\"name\":\""+endAddress+"\"}";
        			
        			flag2=0;
        			new postSaveRoute().execute(path,uid);
        		}
        	}
        });
        
        findRoadPointBtn.setOnClickListener(new Button.OnClickListener(){
        	@Override
        	public void onClick(View v) {
        	
        			if((!startLocation.getText().toString().trim().equals(""))&&(!endLocation.getText().toString().trim().equals("")))
        			{
        				findRoadPointBtn.setEnabled(false);
        				chooseRoadPointBtn.setEnabled(false);
						rdmBtn.setEnabled(false);
						IIIswitch.setEnabled(false);
        				
        				startAddress=startLocation.getText().toString();
        				endAddress=endLocation.getText().toString();
        				
        				routeKiller();
        				goflag=0;
        				cancelflag=0;
        				
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
        						rpChosenNumber++;
        						mWebView.loadUrl("javascript:setChosenIcon("+String.valueOf(whichButton)+")");
        						
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
        						rpChosenNumber--;
        						mWebView.loadUrl("javascript:setOrgIcon("+String.valueOf(whichButton)+")");
        					}
        					
        					if(rpChosenNumber>0){
        						chooseRoadPointBtn.setText("已選"+num[rpChosenNumber]+"點");
        					}else if(rpChosenNumber==0){
        						chooseRoadPointBtn.setText("選擇路點");
        					}
        					 
        			}
        		});
        		
        		/*builder.setPositiveButton("確定", new DialogInterface.OnClickListener() {
        			public void onClick(DialogInterface dialog, int whichButton) {
        				//Toast.makeText(MainActivity.this,"aaa", 100).show();  
        			}
        		});
        			
        		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
        			public void onClick(DialogInterface dialog, int whichButton) {
        			 
        			}
        		});*/
        			
        		builder.create().show();

        	}
        });
        
        rpCategoryBtn.setOnClickListener(new Button.OnClickListener(){
        	@Override
        	public void onClick(View v) {
        		
        		int a;
        		String[] temp = new String[categoryName.size()];
        		for(a=0;a<categoryName.size();a++)
				{
        			temp[a]=categoryName.get(a);
				}
        		boolean[] temp2 = new boolean[categoryChosen.size()];
        		for(a=0;a<categoryChosen.size();a++)
				{
        			if(categoryChosen.get(a)==0)
        				temp2[a]=false;
        			else
        				temp2[a]=true;
				}
        	
        		builder2 = new AlertDialog.Builder(MainActivity.this);
        		builder2.setMultiChoiceItems(temp, temp2,
        		new DialogInterface.OnMultiChoiceClickListener() {
        			public void onClick(DialogInterface dialog, int whichButton,boolean isChecked) {
        					if(isChecked) {
        						categoryChosen.set(whichButton,1);
        						
        					}else {
        						categoryChosen.set(whichButton,0);
        					}
        					 
        			}
        		});
	
        		builder2.create().show();

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
        			rpChosenNumber=0;
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
        				mWebView.loadUrl("javascript:setOrgIcon("+String.valueOf(j)+")");
        				
        		    }

        			for(j=0;j<haha.size();j++)
        			{
        				roadPointLocationChosen.set(haha.get(j)-1,1);
        				mWebView.loadUrl("javascript:setChosenIcon("+String.valueOf(haha.get(j)-1)+")");
        				rpChosenNumber++;
	
        		    }
        			
        			chooseRoadPointBtn.setText("已選"+num[rpChosenNumber]+"點");
        			chooseRoadPointBtn.setEnabled(true);

        		}
        		
        	}});
		
		bundle=this.getIntent().getExtras();

        cookie=bundle.getString("cookie");
        uid="null";
        
        categoryName=new ArrayList<String>();
    	categoryChosen=new ArrayList<Integer>();
    	categoryValue=new ArrayList<String>();
    	
    	categoryName.add("商務"); categoryValue.add("Local business"); categoryChosen.add(0);
    	categoryName.add("餐廳"); categoryValue.add("Restaurant/cafe"); categoryChosen.add(0);
    	categoryName.add("旅館"); categoryValue.add("Hotel"); categoryChosen.add(0);
    	categoryName.add("休閒"); categoryValue.add("Travel/leisure"); categoryChosen.add(0);
    	categoryName.add("學校"); categoryValue.add("School"); categoryChosen.add(0);
    	categoryName.add("地標"); categoryValue.add("Landmark"); categoryChosen.add(0);
    	categoryName.add("觀光"); categoryValue.add("Tours/sightseeing"); categoryChosen.add(0);
    	categoryName.add("娛樂"); categoryValue.add("Arts/entertainment/nightlife"); categoryChosen.add(0);
    	categoryName.add("購物"); categoryValue.add("Shopping/retail"); categoryChosen.add(0);
    	categoryName.add("美容"); categoryValue.add("Health/beauty"); categoryChosen.add(0);
    	categoryName.add("食品"); categoryValue.add("Food/grocery"); categoryChosen.add(0);
    	categoryName.add("飲料"); categoryValue.add("Food/beverages"); categoryChosen.add(0);
    	categoryName.add("服飾"); categoryValue.add("Clothing"); categoryChosen.add(0);
    	categoryName.add("宗教"); categoryValue.add("Church/religious organization"); categoryChosen.add(0);
    	categoryName.add("博物館"); categoryValue.add("Museum/art gallery"); categoryChosen.add(0);
    	categoryName.add("體育館"); categoryValue.add("Sports venue"); categoryChosen.add(0);
    	categoryName.add("酒吧"); categoryValue.add("Bar"); categoryChosen.add(0);
    	categoryName.add("俱樂部"); categoryValue.add("Club"); categoryChosen.add(0);
    	categoryName.add("圖書館"); categoryValue.add("Library"); categoryChosen.add(0);
    	categoryName.add("零售店"); categoryValue.add("Retail and consumer merchandise"); categoryChosen.add(0);
    	categoryName.add("書店"); categoryValue.add("Book store"); categoryChosen.add(0);
    	categoryName.add("政府"); categoryValue.add("Government organization"); categoryChosen.add(0);
    	categoryName.add("電影院"); categoryValue.add("Movie theater"); categoryChosen.add(0);
    	categoryName.add("珠寶"); categoryValue.add("Jewelry/watches"); categoryChosen.add(0);
    	categoryName.add("院所"); categoryValue.add("Hospital/clinic"); categoryChosen.add(0);
    	
		chooseRoadPointBtn.setEnabled(false);
		rdmBtn.setEnabled(false);
		timerBtn.setEnabled(false);
		saveRouteBtn.setEnabled(false);
		
		 if(!cookie.equals("null")){
			 	flag=0;
	        	new getAu().execute(cookie);
	     }
		
		
    }
    
	
	 private void initActionBar(){
	        getActionBar().setDisplayHomeAsUpEnabled(true);
	        getActionBar().setHomeButtonEnabled(true);
	    }

	    private void initDrawer(){

	        layDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
	        lstDrawer = (ScrollView) findViewById(R.id.scrollView1);

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
	        	
	        	int itemId = item.getItemId();
				if (itemId == R.id.cancel) {
					cancelByflag();
					return true;
				} else if (itemId == R.id.go) {
					goByflag();
					return true;
				} else {
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
	    	if(cancelflag==1)
	    	{
	    		if(roadPointTitle.size()>0){
	    			int a;
	    			for(a=0;a<roadPointLocationChosen.size();a++)
	    			{
	    				roadPointLocationChosen.set(a,0);
	    				mWebView.loadUrl("javascript:setOrgIcon("+String.valueOf(a)+")");
	    			}
	    		
	    			runOnUiThread(new Runnable() {
	    				public void run() {
	    					rpChosenNumber=0;
	    					chooseRoadPointBtn.setText("選擇路點");
	    					chooseRoadPointBtn.setEnabled(true);
	    				}
	    			});
	    		}
	    	}else if(cancelflag==2)
	    	{
	    		routeKiller();
	    
	    		if(roadPointTitle.size()>0){
	    			int a;
	    			for(a=0;a<roadPointLocationChosen.size();a++)
					{
						roadPointLocationChosen.set(a,0);
						mWebView.loadUrl("javascript:setOrgIcon("+String.valueOf(a)+")");
					}
	    		
	    			runOnUiThread(new Runnable() {
		            	public void run() {
		            		chooseRoadPointBtn.setEnabled(true);
		            	}
		        	});
	    		}
	    		
	    		mWebView.loadUrl("javascript:showRPmarkers()");
	    		cancelflag=1;
	    		
	    	}  else if(cancelflag==3)
	    	{
	    		routeKiller();
	    		
	    		cancelflag=0;
	    		
	    	}  
	    }  
	    
	    public void goByflag() {  
	       
	    	if(goflag==1)
	    	{
	    		runOnUiThread(new Runnable() {
		            public void run() {
		            	if(timerBtnFlag==1){
		            		timer1.cancel();
		        			timer1.purge();
		        			Lmgr2.removeUpdates(locationListener2);
		        			
		        			timerBtn.setText("Start");
		        	    	
		        	    	timerBtnFlag=0;
		        	    	
		        	    	mWebView.loadUrl("javascript:killCurrentLocation()");
		            	}
		            	totalSec=0;
		            	mWebView.loadUrl("javascript:route("+roadPointLocation.size()+")");
		            	
		           }
		       });
	    		
	    		
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
	    			}
	    		} catch (JSONException e) {
	    			e.printStackTrace();
	    		}  

	        }else if(postFlag==2){
	        	
	        	try {
	    			final JSONObject obj = new JSONObject(content);
	    			
	    			if(obj.getString("message").equals("success")==true){

	    			    JSONArray dataAry=obj.getJSONArray("result");
	    				int ctr=dataAry.length();
	    				int a;
	    				
	    				limit-=ctr;
	    				
							for(a=0;a<ctr;a++)
							{
								String tempp=dataAry.getJSONObject(a).getString("name").replace("\\","%");
								String tempp2 = "null";
								try {
									tempp2=URLDecoder.decode(tempp, "UTF-8");
								} catch (UnsupportedEncodingException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
								roadPointTitle.add(tempp2+"#人氣度:"+dataAry.getJSONObject(a).getString("checkins"));
								roadPointLocation.add(dataAry.getJSONObject(a).getString("latitude")+","+dataAry.getJSONObject(a).getString("longitude"));
								roadPointLocationChosen.add(0);
							}	
							
	    			}
	    		} catch (JSONException e) {
	    			e.printStackTrace();
	    		}  
	        }else if(postFlag==1){
	        	
	        	try {
	    			final JSONObject obj = new JSONObject(content);
	    			
	    			if(obj.getString("message").equals("success")==true){
	    				iiiToken=obj.getJSONObject("result").getString("token");
	    				iiiRPsetting();
	    			}
	    			
	    		} catch (JSONException e) {
	    			e.printStackTrace();
	    		}  
	        }
	    }  
	    
	    
	    public void iiiRPsetting(){
	    	int i;
	    	categorySend=new ArrayList<String>();
	    	for(i=0;i<categoryChosen.size();i++)
	    	{
	    		if(categoryChosen.get(i)==1)
	    		{
	    			categorySend.add(categoryValue.get(i));
	    		}
	    	}
	    	
	    	limit=20;
	    	categoryIndex=0;
	    	
	    	if(categorySend.size()==0)
	    	{
	    		leftTimes=1;
	    		allFlag=true;
	    	}else if(categorySend.size()>0){
	    		leftTimes=categorySend.size();
	    		allFlag=false;
	    	}
	    	
	    	if(!keyword.getText().toString().trim().equals("")){
	    		keywordString=keyword.getText().toString();
   		 	}else{
   		 		keywordString="null";
   		 	}
	    	
	    	//repeatTimes=0;
	    	new postData2().execute();
	    }
	    
	    public void routeKiller(){
	    	runOnUiThread(new Runnable() {
	            public void run() {
	            	if(timerBtnFlag==1){
	            		timer1.cancel();
	        			timer1.purge();
	        			Lmgr2.removeUpdates(locationListener2);
	        			
	        			timerBtn.setText("Start");
	        	    	timerBtnFlag=0;
	        	    	
	        	    	mWebView.loadUrl("javascript:killCurrentLocation()");
	            	}
	            	totalSec=0;
	            	timerBtn.setEnabled(false);
	            	
	            	if(!uid.equals("null")){
	            		saveRouteBtn.setEnabled(false);
	            	}
	            	
	            	rpChosenNumber=0;
	            	chooseRoadPointBtn.setText("選擇路點");
	            	
	            	leftInfoText.setText("");
	            	rightInfoText.setText("");
	            	mWebView.loadUrl("javascript:killRoute()");
	           }
	       });
	    }
	    
	    public void frpEnding(){
	    	if(roadPointTitle.size()>0){
		            	chooseRoadPointBtn.setEnabled(true);
						rdmBtn.setEnabled(true);
		            	mWebView.loadUrl("javascript:setMarkers("+roadPointTitle.size()+")");
	    	}
	    	
	    	findRoadPointBtn.setEnabled(true);
	    	IIIswitch.setEnabled(true);
	    	goflag=1;
	    	cancelflag=1;
	    	
	    	if(HttpTimeoutFlag==1){
	    		Toast.makeText(MainActivity.this,"HTTP連線錯誤，路點可能因此短缺\n找到"+roadPointTitle.size()+"個路點", 100).show();
			}else{
				Toast.makeText(MainActivity.this,"找到"+roadPointTitle.size()+"個路點", 100).show();
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
	    public void frpReturn(final String center0,final String rad0,final String centerlt0,final String centerlg0,final String startLoc,final String endLoc)
	    {
	    	
	    	center=center0;
	    	rad=rad0;
	    	centerlt=centerlt0;
	    	centerlg=centerlg0;
	    	startLatLng=startLoc;
	    	endLatLng=endLoc;
	    	
	    	roadPointLocation=new ArrayList<String>();
			roadPointTitle=new ArrayList<String>();
			roadPointLocationChosen=new ArrayList<Integer>();
			HttpTimeoutFlag=0;
			
			runOnUiThread(new Runnable() {
	            public void run() {
	            	mWebView.loadUrl("javascript:killRPmarkers()");
	            	repeatTimes=0;
	            	new postData0().execute();
	           }
	       });
	    	
	    	
	    	
	     }
	    
	    @JavascriptInterface
	    public void routeReturn(final String distance,final String duration)
	    {
	    	tDistance=distance;
	    	tDuration=duration;
	    	cancelflag=2;
	    	
	    	runOnUiThread(new Runnable() {
            public void run() {
            	leftInfoText.setText(distance+"公里\n"+duration+"分鐘");
            	rightInfoText.setText("00:00:00");
            	timerBtn.setEnabled(true);
            	
            	if(!uid.equals("null")){
            		saveRouteBtn.setEnabled(true);
            	}
           }
           });
	     }
	    
	    @JavascriptInterface
	    public void routeHisReturn(final String distance,final String duration)
	    {
	    	cancelflag=3;
	    	
	    	runOnUiThread(new Runnable() {
	    		public void run() {
            		leftInfoText.setText(distance+"公里\n"+duration+"分鐘");
            		rightInfoText.setText("00:00:00");
            		timerBtn.setEnabled(true);
            	}
           });
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
	    public String getRPLocationHis(final String pos)
	    {
	    	int intValue=Integer.valueOf(pos);
	    	final String localS=savedRoadPointLocation.get(intValue);
	    	
	    	return localS;
	    	
	    }
	    
	    @JavascriptInterface
	    public String getRPTitleHis(final String pos)
	    {
	    	
	    	int intValue=Integer.valueOf(pos);
	    	final String localS=savedRoadPointTitle.get(intValue);
	    	
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
	    public void cancelByRec(final int Rec){
	    	int flagGG=0;
	    	
	    	if(roadPointLocationChosen.get(Rec)==0)
	    	{
	    		flagGG=1;
	    		Toast.makeText(MainActivity.this,"未選擇過", 100).show();
	    	}
	    	
	    	if(flagGG==0){
	    		roadPointLocationChosen.set(Rec,0);
	    		Toast.makeText(MainActivity.this,"已取消", 100).show();
	    		rpChosenNumber--;
	    		
	    		runOnUiThread(new Runnable() {
		            public void run() {
		            	if(rpChosenNumber>0){
    						chooseRoadPointBtn.setText("已選"+num[rpChosenNumber]+"點");
    					}else if(rpChosenNumber==0){
    						chooseRoadPointBtn.setText("選擇路點");
    					}
		            	mWebView.loadUrl("javascript:setOrgIcon("+String.valueOf(Rec)+")");
		            	chooseRoadPointBtn.setEnabled(true);
		           }
		       });
	    		
	    	}
	    
	    
	    }
	    
	    
	    @JavascriptInterface
	    public void chooseByRec(final int Rec)
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
	    		rpChosenNumber++;
	    		
	    		 runOnUiThread(new Runnable() {
			            public void run() {
			            	chooseRoadPointBtn.setText("已選"+num[rpChosenNumber]+"點");
			            	mWebView.loadUrl("javascript:setChosenIcon("+String.valueOf(Rec)+")");
			           }
			       });
	    		
	    		chosenCtr++;
	    		   
	    		if(chosenCtr==6)
	    		{
	    			  runOnUiThread(new Runnable() {
				            public void run() {
				            	chooseRoadPointBtn.setEnabled(false);
				           }
				       });
	    		}
	    	}
	    	else if((flagGG==0)&&(chosenCtr==6)){
	    		Toast.makeText(MainActivity.this,"已達上限", 100).show();
	    	}
	     }
	    
	    public class myTask extends TimerTask {
		    
		     @ Override
		      public void run() {
		    	 totalSec+=1;
				 int ss=totalSec;
			     int hh=ss/3600;
				 ss-=(hh*3600);
				 int mm=ss/60;
				 ss-=(mm*60);
				 
				 String sString=String.valueOf(ss);
				 String mString=String.valueOf(mm);
				 String hString=String.valueOf(hh);
				 final String sString2;
				 final String mString2;
				 final String hString2;
			
				 if(hh<10)
				 {
					 hString2="0"+hString+":";
				 }else
				 {
					 hString2=hString+":";
				 }

				 if(mm<10)
				 {
					 mString2="0"+mString+":";
				 }else
				 {
					 mString2=mString+":";
				 }

				  if(ss<10)
				 {
					  sString2="0"+sString;
				 }else
				 {
					 sString2=sString;
				 }
				  
				  runOnUiThread(new Runnable() {
			            public void run() {
			            	rightInfoText.setText(hString2+mString2+sString2);
			           }
			       });
		     }
	}
	    
	    private class getAu extends AsyncTask<String,Void, String>
	    {
			@Override
			protected String doInBackground(String... arg0) {
				
				String getUrl="http://easygo.ballchen.cc/isAuthenticated";
				String content = "null";
	    		 
	            HttpGet get=new HttpGet(getUrl);  
				 try {
					
					get.setHeader("cookie",arg0[0]);
					
					HttpClient client=new DefaultHttpClient();  
	                HttpResponse response=client.execute(get);  
	                if(response.getStatusLine().getStatusCode()==200){  
	                    content=EntityUtils.toString(response.getEntity());   
	                    flag=1;
	                } else{
	                	content=String.valueOf(response.getStatusLine().getStatusCode());
	                	flag=0;
	                }
				} catch (ClientProtocolException e) {  
	                e.printStackTrace();  
	           } catch (IOException e) {  
	                e.printStackTrace();  
	           }  
				
				
				return content;
			}
			
			@Override
			protected void onPostExecute(String result)
	        {
				if(flag==0){
					Toast.makeText(MainActivity.this, result,100).show();	
				}else if(flag==1){
					
					try{
						JSONObject obj = new JSONObject(result);
	    			
						if(obj.getString("message").equals("yes")==true){
							uid=obj.getString("uid");
							
							savedPath=bundle.getString("savedPath");
							
							if(!savedPath.equals("null")){
								//Toast.makeText(MainActivity.this, savedPath,100).show();
								savedRoadPointLocation=new ArrayList<String>();
								savedRoadPointTitle=new ArrayList<String>();
								savedPath="["+savedPath+"]";
								JSONArray dataAry= new JSONArray(savedPath);
								int i;
								
								for(i=0;i<dataAry.length();i++){
									savedRoadPointLocation.add(dataAry.getJSONObject(i).getString("land"));
									savedRoadPointTitle.add(dataAry.getJSONObject(i).getString("name"));
								}
								
								mWebView.loadUrl("javascript:routeHis("+savedRoadPointLocation.size()+")");
								
							}
						}
	    			
					}catch (JSONException e) {
		    			e.printStackTrace();
		    		}  
					
				}
	        }
	    }
	    
	    private class postSaveRoute extends AsyncTask<String,Void, String>
	    {
			@Override
			protected String doInBackground(String... arg0) {
				
				String postUrl="http://easygo.ballchen.cc/history/create";
				String content = "null";
	    		List<NameValuePair> params=new ArrayList<NameValuePair>();  
	            params.add(new BasicNameValuePair("path", arg0[0])); 
	            params.add(new BasicNameValuePair("userid", arg0[1]));
	    		 
	       
	            HttpPost post=new HttpPost(postUrl);  
				 try {
					post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
					
					HttpClient client=new DefaultHttpClient();  
	                HttpResponse response=client.execute(post);  
	                
	                if(response.getStatusLine().getStatusCode()==201){  
	                   flag2=1;
	                }else{
	                   flag2=0;
	                	
	                }
				} catch (ClientProtocolException e) {  
	                e.printStackTrace();  
	           } catch (IOException e) {  
	                e.printStackTrace();  
	           }  
				
				
				return content;
			}
			
			@Override
			protected void onPostExecute(String result)
	        {
				if(flag2==0){
					Toast.makeText(MainActivity.this,"收藏失敗",100).show();
				}else{
					Toast.makeText(MainActivity.this,"收藏成功",100).show();
				}
	        }
	    }
	    
	    private class postData0 extends AsyncTask<Void,Void,String>
	    {
			@Override
			protected String doInBackground(Void... arg0) {
				String content = "null";
				
		    	String postUrl="http://easygo.ballchen.cc/check_roadpoint";
		    	List<NameValuePair> params=new ArrayList<NameValuePair>();  
	            params.add(new BasicNameValuePair("lat_self", centerlt));  
	            params.add(new BasicNameValuePair("lon_self", centerlg));  
	            params.add(new BasicNameValuePair("rad", rad)); 
	            
	            HttpPost post=new HttpPost(postUrl);  
				 try {
					post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
					
					HttpParams httpParameters=new BasicHttpParams();
					HttpConnectionParams.setConnectionTimeout(httpParameters, 30000);
					HttpConnectionParams.setSoTimeout(httpParameters, 30000);
					HttpClient client=new DefaultHttpClient(httpParameters); 
                    HttpResponse response=client.execute(post);  
                   if(response.getStatusLine().getStatusCode()==200){  
                       content=EntityUtils.toString(response.getEntity());     
                       
                       return content;
                   }else{
                   
                   	return "null2";
                   }
				}catch (ConnectTimeoutException e) {  
					
					e.printStackTrace();  
					return "null";
               }catch (SocketTimeoutException e) {  
					
					e.printStackTrace();  
					return "null";
               }catch (ClientProtocolException e) { 
               	
                   e.printStackTrace();  
                   return "null2";
               } catch (IOException e) {
               	
                   e.printStackTrace(); 
                   return "null2";
               }
			}
			
			@Override
			protected void onPostExecute(String result)
	        {
	
				//Toast.makeText(MainActivity.this,result,100).show();
				
				if(result.equals("null")){
					if(repeatTimes<3){
						repeatTimes++;
						Toast.makeText(MainActivity.this,"連線逾時重新發起連線中",100).show();
						new postData0().execute();
					}else{
						repeatTimes=0;
						HttpTimeoutFlag=1;
						frpEnding();
					}
					
				}else if(result.equals("null2")){
					HttpTimeoutFlag=1;
					frpEnding();
				}else{
					 analysisJSON(result,0);  
					 
					 if(serApiFlag==0){
							frpEnding();
					 }else if(serApiFlag==1){
						    repeatTimes=0;
							new postData1().execute();
					 }
				}
	        }
	    }
	    
	    private class postData1 extends AsyncTask<Void,Void, String>
	    {
			@Override
			protected String doInBackground(Void... arg0) {
				String content = "null";
				
	            String postUrl="http://api.ser.ideas.iii.org.tw/api/user/get_token";
	    		List<NameValuePair> params=new ArrayList<NameValuePair>();  
                params.add(new BasicNameValuePair("id", "277b49909b1d1400b8a139f0d575cad5"));  
                params.add(new BasicNameValuePair("secret_key", "2681a844c37d538bbd53d5ac101a3f43"));  
                
                HttpPost post=new HttpPost(postUrl);  
				 try {
					post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
					
					HttpParams httpParameters=new BasicHttpParams();
					HttpConnectionParams.setConnectionTimeout(httpParameters, 15000);
					HttpConnectionParams.setSoTimeout(httpParameters, 15000);
					HttpClient client=new DefaultHttpClient(httpParameters); 
                   HttpResponse response=client.execute(post);  
                  if(response.getStatusLine().getStatusCode()==200){  
                      content=EntityUtils.toString(response.getEntity());     
                      
                      return content;
                  }else{
   
                  	return "null2";
                  }
				}catch (ConnectTimeoutException e) {  
					
					e.printStackTrace();  
					return "null";
              }catch (SocketTimeoutException e) {  
					
					e.printStackTrace();  
					return "null";
              }catch (ClientProtocolException e) { 
              	
                  e.printStackTrace();  
                  return "null2";
              } catch (IOException e) {
              	
                  e.printStackTrace(); 
                  return "null2";
              }
			}
			
			@Override
			protected void onPostExecute(String result)
	        {	
				if(result.equals("null")){
					if(repeatTimes<3){
						repeatTimes++;
						Toast.makeText(MainActivity.this,"連線逾時重新發起連線中",100).show();
						new postData1().execute();
					}else{
						repeatTimes=0;
						HttpTimeoutFlag=1;
						frpEnding();
					}
				}else if(result.equals("null2")){
					HttpTimeoutFlag=1;
					frpEnding();
				}else{
					analysisJSON(result,1); 
				}
	        }
	    }
	    
	    private class postData2 extends AsyncTask<Void,Void, String>
	    {
			@Override
			protected String doInBackground(Void... arg0) {
				String content = "null";
				
				String postUrl="http://api.ser.ideas.iii.org.tw/api/fb_checkin_search";
		    	List<NameValuePair> params=new ArrayList<NameValuePair>();  
		
		    	 params.add(new BasicNameValuePair("coordinates", center));
	    		 params.add(new BasicNameValuePair("radius", rad));
	    		 params.add(new BasicNameValuePair("limit", String.valueOf(limit)));
	    		 params.add(new BasicNameValuePair("token", iiiToken));
	    		 
	    		 if(!keywordString.equals("null")){
	    			 params.add(new BasicNameValuePair("keyword",keywordString));
	    		 }
		    	
		    	if(!allFlag){
		    		params.add(new BasicNameValuePair("category",categorySend.get(categoryIndex)));
		    	}
		    	
		    	  HttpPost post=new HttpPost(postUrl);  
					 try {
						post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
						
						HttpParams httpParameters=new BasicHttpParams();
						HttpConnectionParams.setConnectionTimeout(httpParameters, 15000);
						HttpConnectionParams.setSoTimeout(httpParameters, 15000);
						HttpClient client=new DefaultHttpClient(httpParameters); 
	                   HttpResponse response=client.execute(post);  
	                  if(response.getStatusLine().getStatusCode()==200){  
	                      content=EntityUtils.toString(response.getEntity());     
	                      
	                      return content;
	                  }else{
	                  	HttpTimeoutFlag=1;
	                  	return "null";
	                  }
					}catch (ConnectTimeoutException e) {  
						HttpTimeoutFlag=1;
						e.printStackTrace();  
						return "null";
	              }catch (SocketTimeoutException e) {  
						HttpTimeoutFlag=1;
						e.printStackTrace();  
						return "null";
	              }catch (ClientProtocolException e) { 
	              	HttpTimeoutFlag=1;
	                  e.printStackTrace();  
	                  return "null";
	              } catch (IOException e) {
	              	HttpTimeoutFlag=1;
	                  e.printStackTrace(); 
	                  return "null";
	              }
			}
			
			@Override
			protected void onPostExecute(String result)
	        {
				if(!result.equals("null")){
					 analysisJSON(result,2);  
					 
					 leftTimes--;
				    	
				     if((leftTimes!=0)&&(limit>0))
					 {
				    	categoryIndex++;
				    	new postData2().execute();
					 }else{
						frpEnding();
					 }
				}else{
					frpEnding();
				}
				
				
		    	
				/*
				   if(result.equals("null")){
				   		if(repeatTimes<3){
							repeatTimes++;
							Toast.makeText(MainActivity.this,"連線逾時重新發起連線中",100).show();
							new postData2().execute();
						}else{
							repeatTimes=0;
							HttpTimeoutFlag=1;
							frpEnding();
						}
				   }else if(result.equals("null2")){
						HttpTimeoutFlag=1;
						frpEnding();
				   }else{
				   		analysisJSON(result,2);  
					 	leftTimes--;
				    	
				     	if((leftTimes!=0)&&(limit>0))
					 	{
				    		categoryIndex++;
				    		repeatTimes=0;
				    		new postData2().execute();
					 	}else{
							frpEnding();
					 	}
				   }*/
				   //keep sending the same http request when not get data in time
				  //DO NOT use these codes while there is still usage limit on SER(III) API   
	        }
	    }
	    

}









