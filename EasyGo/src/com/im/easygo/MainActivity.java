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
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import org.apache.http.HttpResponse;  
import org.apache.http.NameValuePair;  
import org.apache.http.ParseException;
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



interface TimeWatchCallback{
	public void setTimeWatchText(final String input);
}

interface GetLocationFromPhoneCallback{
	public void getLatLonONCE(String lat,String lon,int number);
	public void getLatLonLOOP(String lat,String lon);
}

interface GetAuCallback{
	public void getAuHandler(boolean flag,String result);
}

interface PostSaveRouteCallback{
	public void postSaveRouteHandler(boolean flag,String result);
}

interface PostDatasCallback{
	public void frpEnding(boolean flag,String result);
	public void toaster(String toToast);
}

interface PostData0Callback extends PostDatasCallback{
	public void analysisJSON0(String result);
	public void analysisJSON0(String result,String center,String radius);
}


interface PostData1Callback extends PostDatasCallback{
	public String analysisJSON1(String result);
	public void analysisJSON1(String result,String center,String radius);
}

interface PostData2Callback extends PostDatasCallback{
	public int analysisJSON2(String result);
	
}

class HttpWork extends AsyncTask<String,Void, String>{

	HttpWork(){
		HttpFlag=false;
	}
	
	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		return "null";
	}

	protected String HttpHandler(HttpGet get,int timeout,int statusCode){
		return HttpProcessAndError(setHttpParams(timeout),get,statusCode);
	}
	
	protected String HttpHandler(HttpPost post,int timeout,int statusCode){
    	return HttpProcessAndError(setHttpParams(timeout),post,statusCode);
	}
    
    protected HttpClient setHttpParams(int timeout){
    	HttpParams httpParameters=new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters, timeout);
		HttpConnectionParams.setSoTimeout(httpParameters, timeout);
		HttpClient client=new DefaultHttpClient(httpParameters);  
    	
		return client;
    }
    
    protected String HttpProcessAndError(HttpClient client, HttpGet get,int statusCode){
    	 
        String content;
        
        try{
        	HttpResponse response=client.execute(get);
    	
        	if(response.getStatusLine().getStatusCode()==statusCode){  
        		content=responseToContent(response);   
            	HttpFlag=true;
        	} else{
        		content=String.valueOf(response.getStatusLine().getStatusCode());
        	}
        } catch (ConnectTimeoutException e) {
        	content="No Response From Server";
        	e.printStackTrace();  
        }catch (SocketTimeoutException e) { 
        	content="No Response From Server";
        	e.printStackTrace();  
        }catch (ClientProtocolException e) {
        	content="Network Problem";
        	e.printStackTrace();  
        } catch (IOException e) { 
        	content="Network Problem";
        	e.printStackTrace();  
        }  
    	return content;
    }
    
    protected String HttpProcessAndError(HttpClient client, HttpPost post,int statusCode){
   	 
    	String content;
        
        try{
        	HttpResponse response=client.execute(post);
    	
        	if(response.getStatusLine().getStatusCode()==statusCode){  
        		content=responseToContent(response);   
            	HttpFlag=true;
        	} else{
        		content=String.valueOf(response.getStatusLine().getStatusCode());
        	}
        } catch (ConnectTimeoutException e) {
        	content="No Response From Server";
        	e.printStackTrace();  
        }catch (SocketTimeoutException e) { 
        	content="No Response From Server";
        	e.printStackTrace();  
        }catch (ClientProtocolException e) {
        	content="Network Problem";
        	e.printStackTrace();  
        } catch (IOException e) { 
        	content="Network Problem";
        	e.printStackTrace();  
        }  
    	return content;
    }
	
	protected String responseToContent(HttpResponse response){
		
		String content = "null";
		try {
			content=EntityUtils.toString(response.getEntity());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			content="Network Problem";
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			content="Network Problem";
			e.printStackTrace();
		}
		
		return content;
	}
	
	protected boolean HttpFlag=false;
}

class GetAu extends HttpWork
{
	GetAu(GetAuCallback caller){
		caller_c=caller;
	}
	
	@Override
	protected String doInBackground(String... arg0) {
		
		String getUrl="http://easygo.ballchen.cc/isAuthenticated";
		String content = "null";
		 
        HttpGet get=new HttpGet(getUrl);  
	
		get.setHeader("cookie",arg0[0]);
		
		content=HttpHandler(get,15000,200);
		
		return content;
	}
	
	@Override
	protected void onPostExecute(String result)
    {
		caller_c.getAuHandler(HttpFlag, result);
    }
	
	
	private GetAuCallback caller_c;
}


class PostSaveRoute extends HttpWork
{
	PostSaveRoute(PostSaveRouteCallback caller){
		caller_c=caller;
	}
	
	
	@Override
	protected String responseToContent(HttpResponse response){
		
		return "null";
	}
	
	
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
			
			content=HttpHandler(post,15000,201);
			
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			content="Network Problem";
			e1.printStackTrace();
		}
		
		return content;
	}
	
	@Override
	protected void onPostExecute(String result)
    {
		caller_c.postSaveRouteHandler(HttpFlag, result);
    }
	
	private PostSaveRouteCallback caller_c;
}

class PostData0 extends HttpWork
{
	
	PostData0(PostData0Callback caller,String lat,String lon,String rad,int repeatTimes,Boolean keepGoingFlag){
		lat_c=lat;
		lon_c=lon;
		rad_c=rad;
		repeatTimes_c=repeatTimes;
		keepGoingFlag_c=new Boolean(keepGoingFlag);
		caller_c=caller;
	}
	
	@Override
	protected String doInBackground(String... arg0) {
		String postUrl="http://easygo.ballchen.cc/check_roadpoint";
		String content="null";
		
    	List<NameValuePair> params=new ArrayList<NameValuePair>();  
        params.add(new BasicNameValuePair("lat_self", lat_c));  
        params.add(new BasicNameValuePair("lon_self", lon_c));  
        params.add(new BasicNameValuePair("rad", rad_c)); 
        
        HttpPost post=new HttpPost(postUrl);  
		try {
			post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			
			content=HttpHandler(post,15000,200);
			
		} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				content="Network Problem";
				e1.printStackTrace();
		}
			
	   return content;
	}
	
	@Override
	protected void onPostExecute(String result)
    {	
		if(!HttpFlag){
			if(result.equals("No Response From Server")){
				if(repeatTimes_c<2){
					repeatTimes_c++;

					caller_c.toaster("HTTP連線逾時重新發起連線中");
					
					new PostData0(caller_c,lat_c,lon_c,rad_c,repeatTimes_c,keepGoingFlag_c).execute();
				}else{
					caller_c.frpEnding(HttpFlag,result);
				}	
			}else{
				caller_c.frpEnding(HttpFlag,result);
			}
		}else{
			
			if(!keepGoingFlag_c){
				caller_c.analysisJSON0(result);
				caller_c.frpEnding(HttpFlag,"null");
			}else{
				String center=lat_c+","+lon_c;
				
				caller_c.analysisJSON0(result, center, rad_c);
			}
			
		}
    }
	
	private String lat_c;
	private String lon_c;
	private String rad_c;
	private int repeatTimes_c;
	private Boolean keepGoingFlag_c=false;
	private PostData0Callback caller_c;

}

class PostData1 extends HttpWork
{
	PostData1(PostData1Callback caller,String center,String rad,int repeatTimes){
		caller_c=caller;
		center_c=center;
		rad_c=rad;
		repeatTimes_c=repeatTimes;
		keepGoingFlag_c=true;
		
	}
	
	PostData1(PostData1Callback caller,int repeatTimes){
		caller_c=caller;
		repeatTimes_c=repeatTimes;
		keepGoingFlag_c=false;
	}
	
	@Override
	protected String doInBackground(String... arg0) {
		
		
		
		
		String postUrl="http://api.ser.ideas.iii.org.tw/api/user/get_token";
		String content="null";
		
		List<NameValuePair> params=new ArrayList<NameValuePair>();  
        params.add(new BasicNameValuePair("id", "277b49909b1d1400b8a139f0d575cad5"));  
        params.add(new BasicNameValuePair("secret_key", "2681a844c37d538bbd53d5ac101a3f43")); 
        
        HttpPost post=new HttpPost(postUrl);  
		try {
			post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			
			content=HttpHandler(post,15000,200);
			
		} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				content="Network Problem";
				e1.printStackTrace();
		}
		 
		 return content;
	}
	
	@Override
	protected void onPostExecute(String result)
    {	
		if(!HttpFlag){
			if(result.equals("No Response From Server")){
				if(repeatTimes_c<2){
					repeatTimes_c++;
					caller_c.toaster("HTTP連線逾時重新發起連線中");
					if(keepGoingFlag_c){
						new PostData1(caller_c,center_c,rad_c,repeatTimes_c).execute();
					}else{
						new PostData1(caller_c,repeatTimes_c).execute();
					}
				}else{
					caller_c.frpEnding(HttpFlag,result);
				}	
			}else{
				caller_c.frpEnding(HttpFlag,result);
			}
		}else{
			if(!keepGoingFlag_c){
				caller_c.analysisJSON1(result);
				caller_c.frpEnding(HttpFlag,"null");
			}else{
				caller_c.analysisJSON1(result, center_c, rad_c);
			}
			
		}
    }
	
	private String center_c="null";
	private String rad_c="null";
	private int repeatTimes_c;
	private boolean keepGoingFlag_c=false;
	private PostData1Callback caller_c;
}

class PostData2 extends HttpWork
{
	PostData2(PostData2Callback caller,List<String> categorySend,String keywordString,int limit,String iiiToken,String radius,String center,int categoryIndex,int leftTimes,boolean allFlag,int repeatTimes){
		caller_c=caller;
		categorySend_c=categorySend;
    	keywordString_c=keywordString;
    	limit_c=limit;
    	iiiToken_c=iiiToken;
    	radius_c=radius;
    	center_c=center;
    	categoryIndex_c=categoryIndex;
    	leftTimes_c=leftTimes;
    	allFlag_c=allFlag;
    	repeatTimes_c=repeatTimes;
	}
	
	@Override
	protected String doInBackground(String... arg0) {
		           
        String content="null";
		
		String postUrl="http://api.ser.ideas.iii.org.tw/api/fb_checkin_search";
    	List<NameValuePair> params=new ArrayList<NameValuePair>();  

    	 params.add(new BasicNameValuePair("coordinates",center_c));
		 params.add(new BasicNameValuePair("radius", radius_c));
		 params.add(new BasicNameValuePair("limit", String.valueOf(limit_c)));
		 params.add(new BasicNameValuePair("token", iiiToken_c));
		 
		 if(!keywordString_c.equals("null")){
			 params.add(new BasicNameValuePair("keyword",keywordString_c));
		 }
    	
    	if(!allFlag_c){
    		params.add(new BasicNameValuePair("category",categorySend_c.get(categoryIndex_c)));
    	}
    	
    	HttpPost post=new HttpPost(postUrl);  
		try {
			post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			
			content=HttpHandler(post,15000,200);
			
		} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				content="Network Problem";
				e1.printStackTrace();
		}
		
		return content;
	}
	
	@Override
	protected void onPostExecute(String result)
    {
		if(!HttpFlag){
			caller_c.frpEnding(HttpFlag,result);
		}else{
			int fnd=caller_c.analysisJSON2(result);  
			limit_c-=fnd;
			leftTimes_c--;
		    	
		     if((leftTimes_c>0)&&(limit_c>0))
			 {
		    	 categoryIndex_c++;
		    	 repeatTimes_c=0;
		    	 new PostData2(caller_c,categorySend_c,keywordString_c,limit_c,iiiToken_c,radius_c,center_c,categoryIndex_c,leftTimes_c,allFlag_c,repeatTimes_c).execute();
			 }else{
				 caller_c.frpEnding(HttpFlag,result);
			 }
		}
		
		/*if(!HttpFlag){
			if(result.equals("No Response From Server")){
				if(repeatTimes_c<1){
					repeatTimes_c++;
					caller_c.toaster("HTTP連線逾時重新發起連線中");
					new PostData2(caller_c,categorySend_c,keywordString_c,limit_c,iiiToken_c,radius_c,center_c,categoryIndex_c,leftTimes_c,allFlag_c,repeatTimes_c).execute();
					
				}else{
					caller_c.frpEnding(HttpFlag,result);
				}	
			}else{
				caller_c.frpEnding(HttpFlag,result);
			}
		}else{
			int fnd=caller_c.analysisJSON2(result);  
			limit_c-=fnd;
			leftTimes_c--;
		    	
		     if((leftTimes_c>0)&&(limit_c>0))
			 {
		    	 categoryIndex_c++;
		    	 repeatTimes_c=0;
		    	 new PostData2(caller_c,categorySend_c,keywordString_c,limit_c,iiiToken_c,radius_c,center_c,categoryIndex_c,leftTimes_c,allFlag_c,repeatTimes_c).execute();
			 }else{
				 caller_c.frpEnding(HttpFlag,result);
			 }
		}*/
		   //keep sending the same http request when not get data in time
		  //DO NOT use these codes while there is still usage limit on SER(III) API   
    }
	
	private List<String> categorySend_c;
	private String keywordString_c;
	private int limit_c;
	private String iiiToken_c;
	private String radius_c;
	private String center_c;
	private int categoryIndex_c;
	private int leftTimes_c;
	private boolean allFlag_c;
	private int repeatTimes_c;
	private PostData2Callback caller_c;
}

class DataGroupChosenWithText extends DataGroupChosen{
	
	public String[] num={"零","一","二","三","四","五","六"};
}

class DataGroupOriginal{
	
	DataGroupOriginal(){
		init();
	}
	
	public void init(){
		name=new ArrayList<String>();
		value=new ArrayList<String>();
	} 
	
	public int size(){
		return name.size();
	}

	public List<String> value;
	public List<String> name;

}

class DataGroupChosen extends DataGroupOriginal{
	
	DataGroupChosen(){
		super();
		initChosen();
	}
	
	public void init(){
		super.init();
		initChosen();
	} 
	
	public void initChosen(){
		chosen=new ArrayList<Integer>();
		chosenNumber=0;
	}

	public List<Integer> chosen;
	public int chosenNumber;
}

class Category extends DataGroupChosen{

	Category(){
		super();
    	
    	name.add("商務"); value.add("Local business"); chosen.add(0);
    	name.add("餐廳"); value.add("Restaurant/cafe"); chosen.add(0);
    	name.add("旅館"); value.add("Hotel"); chosen.add(0);
    	name.add("休閒"); value.add("Travel/leisure"); chosen.add(0);
    	name.add("學校"); value.add("School"); chosen.add(0);
    	name.add("地標"); value.add("Landmark"); chosen.add(0);
    	name.add("觀光"); value.add("Tours/sightseeing"); chosen.add(0);
    	name.add("娛樂"); value.add("Arts/entertainment/nightlife"); chosen.add(0);
    	name.add("購物"); value.add("Shopping/retail"); chosen.add(0);
    	name.add("美容"); value.add("Health/beauty"); chosen.add(0);
    	name.add("食品"); value.add("Food/grocery"); chosen.add(0);
    	name.add("飲料"); value.add("Food/beverages"); chosen.add(0);
    	name.add("服飾"); value.add("Clothing"); chosen.add(0);
    	name.add("宗教"); value.add("Church/religious organization"); chosen.add(0);
    	name.add("博物館"); value.add("Museum/art gallery"); chosen.add(0);
    	name.add("體育館"); value.add("Sports venue"); chosen.add(0);
    	name.add("酒吧"); value.add("Bar"); chosen.add(0);
    	name.add("俱樂部"); value.add("Club"); chosen.add(0);
    	name.add("圖書館"); value.add("Library"); chosen.add(0);
    	name.add("零售店"); value.add("Retail and consumer merchandise"); chosen.add(0);
    	name.add("書店"); value.add("Book store"); chosen.add(0);
    	name.add("政府"); value.add("Government organization"); chosen.add(0);
    	name.add("電影院"); value.add("Movie theater"); chosen.add(0);
    	name.add("珠寶"); value.add("Jewelry/watches"); chosen.add(0);
    	name.add("院所"); value.add("Hospital/clinic"); chosen.add(0);
	}
}




class GetLocationFromPhone{
	
	
	public GetLocationFromPhone(Context context,GetLocationFromPhoneCallback caller,boolean OnceLoopFlag){
		 
		 caller_c=caller;
		 OnceLoopFlag_c=OnceLoopFlag;
		
		 Lmgr=(LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
		 locationListener = new LocationListener(){

				@Override
				public void onLocationChanged(Location location) {
					// TODO Auto-generated method stub
					
					String Lat=String.valueOf(location.getLatitude());
					String Lon=String.valueOf(location.getLongitude());
					
					
					if(!OnceLoopFlag_c){
						
						caller_c.getLatLonONCE(Lat,Lon,onceRunningNumber);
						endListenerSE();
					}else{
						
						  caller_c.getLatLonLOOP(Lat,Lon);
					}
				}

				@Override
				public void onProviderDisabled(String provider) {
					// TODO Auto-generated method stub
					if(provider.equals( bestLocationProvider)==true){
						endListenerSE();
					}
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
		 
	}
	
	public void startListenerSE(int inputFlag){
		if(onceRunningNumber==0){
			onceRunningNumber=inputFlag;
			startListener();
		}
	}
	
	public void endListenerSE(){
		onceRunningNumber=0;
	    endListener();
	}
	
	public void startListener(){
		Criteria c=new Criteria();
		bestLocationProvider=Lmgr.getBestProvider(c, true);
		Lmgr.requestLocationUpdates(bestLocationProvider,0,0,locationListener);
	}
	
    public void endListener(){
    	Lmgr.removeUpdates(locationListener);
	}
	
	private LocationManager Lmgr;
	private String bestLocationProvider;
	private LocationListener locationListener;
	private int onceRunningNumber=0;
	private boolean OnceLoopFlag_c;
	private GetLocationFromPhoneCallback caller_c;
	
}

class TimeWatch{
	
	TimeWatch(TimeWatchCallback caller){
		caller_c=caller;
		timerRunningFlag=false;
		setTotalSec0();
	}
	
	public void start(){
		
		timer=new Timer();
		timer.schedule(new myTask(), 0,1000);
	}
	
	public void stop(){
		timer.cancel();
		timer.purge();
	}
	
	public void setTotalSec0(){
		totalSec=0;
	}
	
	private class myTask extends TimerTask {
	    
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
			  
			  
			  caller_c.setTimeWatchText(hString2+mString2+sString2);
		       
	     }
	}
	
	private int totalSec;
	private Timer timer;
	public boolean timerRunningFlag;
	private TimeWatchCallback caller_c; 
}

class SaveRouteManager{
	SaveRouteManager(){
		startAddress="null";
    	endAddress="null";
    	startLatLng="null";
    	endLatLng="null";
	}
	
	public String getRouteToSave(DataGroupChosenWithText input){
		int y;
		String route="";
		route=route+"{\"land\":\""+startLatLng+"\",\"name\":\""+startAddress+"\"},";
		
		for(y=0;y<input.size();y++){
			if(input.chosen.get(y)==1){
				route=route+"{\"land\":\""+input.value.get(y)+"\",\"name\":\""+input.name.get(y)+"\"},";
			}
		}
		
		route=route+"{\"land\":\""+endLatLng+"\",\"name\":\""+endAddress+"\"}";
		
		return route;
	}
	
	public String startAddress;
	public String endAddress;
	public String startLatLng;
	public String endLatLng;
	
}


public class MainActivity extends Activity implements TimeWatchCallback,GetLocationFromPhoneCallback,GetAuCallback,PostSaveRouteCallback,PostData0Callback,PostData1Callback,PostData2Callback{

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
	private EditText startLocation;
	private EditText endLocation;
	private EditText keyword;
	private TextView leftInfoText;
	private TextView rightInfoText;
	
	private Boolean serApiFlag=false;

	private Bundle bundle;
	private String cookie;
	private String uid;
	
	private GetLocationFromPhone once,loop;
	private DataGroupChosenWithText findedRoadPoint;
	private DataGroupOriginal savedRoadPoint;
	private Category categoryList;
	private TimeWatch timewatch;
	private SaveRouteManager saverouteManager;
	private CancelGoManager CGM;
	
	
	
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
       
       
        once=new GetLocationFromPhone(MainActivity.this,MainActivity.this,false);
        loop=new GetLocationFromPhone(MainActivity.this,MainActivity.this,true);
        categoryList=new Category();
        findedRoadPoint=new DataGroupChosenWithText();
        savedRoadPoint=new DataGroupOriginal();
        timewatch=new TimeWatch(MainActivity.this);
        saverouteManager=new SaveRouteManager();
        CGM=new CancelGoManager();
        
        //Button init
        
        IIIswitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {  
        	  
            @Override  
            public void onCheckedChanged(CompoundButton buttonView,  
                    boolean isChecked) {    
                if (isChecked) {  
                	serApiFlag=true;  
                } else {  
                	serApiFlag=false;  
                }  
            }  
        });  
         
        timerBtn.setOnClickListener(new Button.OnClickListener(){
        	@Override
        	public void onClick(View v) {
        		if(!timewatch.timerRunningFlag)
        		{
        			timewatch.start();
            		loop.startListener();
        			
        			timerBtn.setText("Stop");
        			timewatch.timerRunningFlag=true;
        		}else{
        			timewatch.stop();
        			
        			loop.endListener();
        			
        			mWebView.loadUrl("javascript:killCurrentLocation()");
        			
        			timerBtn.setText("Start");
        			timewatch.timerRunningFlag=false;
        		}
        	}
        });
        
        startLBtn.setOnClickListener(new Button.OnClickListener(){
        	@Override
        	public void onClick(View v) {
        		once.startListenerSE(1);	
        	}
        });
        
        endLBtn.setOnClickListener(new Button.OnClickListener(){
        	@Override
        	public void onClick(View v) {
        		once.startListenerSE(2);
        	}
        });
        
        saveRouteBtn.setOnClickListener(new Button.OnClickListener(){
        	@Override
        	public void onClick(View v) {
        		if(!uid.equals("null")){
        			
        			String route=saverouteManager.getRouteToSave(findedRoadPoint);
 
        			new PostSaveRoute(MainActivity.this).execute(route,uid);
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
        				
						saverouteManager.startAddress=startLocation.getText().toString();
						saverouteManager.endAddress=endLocation.getText().toString();
        				
        				findedRoadPoint=new DataGroupChosenWithText();
        				routeKiller();
        				CGM.goflag=0;
        				CGM.cancelflag=0;
        				
        				mWebView.loadUrl("javascript:frp('"+saverouteManager.startAddress+"','"+saverouteManager.endAddress+"')");
        			}
        		
        	}
        });
        
        
        chooseRoadPointBtn.setOnClickListener(new Button.OnClickListener(){
        	@Override
        	public void onClick(View v) {
        		
        		int a;
        		String[] temp = new String[findedRoadPoint.size()];
        		for(a=0;a<findedRoadPoint.size();a++)
				{
        			temp[a]=findedRoadPoint.name.get(a);
				}
        		boolean[] temp2 = new boolean[findedRoadPoint.size()];
        		for(a=0;a<findedRoadPoint.size();a++)
				{
        			if(findedRoadPoint.chosen.get(a)==0)
        				temp2[a]=false;
        			else
        				temp2[a]=true;
				}
        	
        		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        		builder.setMultiChoiceItems(temp, temp2,
        		new DialogInterface.OnMultiChoiceClickListener() {
        			public void onClick(DialogInterface dialog, int whichButton,boolean isChecked) {
        					if(isChecked) {
        						int o;
        						int localCtr=0;
        						findedRoadPoint.chosen.set(whichButton,1);
        						findedRoadPoint.chosenNumber++;
        						mWebView.loadUrl("javascript:setChosenIcon("+String.valueOf(whichButton)+")");
        						
        						for(o=0;o<findedRoadPoint.size();o++)
        						{
        							if(findedRoadPoint.chosen.get(o)==1)
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
        						findedRoadPoint.chosen.set(whichButton,0);
        						findedRoadPoint.chosenNumber--;
        						mWebView.loadUrl("javascript:setOrgIcon("+String.valueOf(whichButton)+")");
        					}
        					
        					if(findedRoadPoint.chosenNumber>0){
        						chooseRoadPointBtn.setText("已選"+findedRoadPoint.num[findedRoadPoint.chosenNumber]+"點");
        					}else if(findedRoadPoint.chosenNumber==0){
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
        		String[] temp = new String[categoryList.size()];
        		for(a=0;a<categoryList.size();a++)
				{
        			temp[a]=categoryList.name.get(a);
				}
        		boolean[] temp2 = new boolean[categoryList.size()];
        		for(a=0;a<categoryList.size();a++)
				{
        			if(categoryList.chosen.get(a)==0)
        				temp2[a]=false;
        			else
        				temp2[a]=true;
				}
        	
        		AlertDialog.Builder builder2 = new AlertDialog.Builder(MainActivity.this);
        		builder2.setMultiChoiceItems(temp, temp2,
        		new DialogInterface.OnMultiChoiceClickListener() {
        			public void onClick(DialogInterface dialog, int whichButton,boolean isChecked) {
        					if(isChecked) {
        						categoryList.chosen.set(whichButton,1);
        						
        					}else {
        						categoryList.chosen.set(whichButton,0);
        					}
        					 
        			}
        		});
	
        		builder2.create().show();

        	}
        });
		
		rdmBtn.setOnClickListener(new Button.OnClickListener(){
        	@Override
        	public void onClick(View v) {
        		final int rngMax=findedRoadPoint.size();
        		final int crr;
        		int tyu;
        		int flg;
        		int j;
        		final List<Integer> haha=new ArrayList<Integer>();

        		if(rngMax>0)
        		{
        			findedRoadPoint.chosenNumber=0;
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

        			for(j=0;j<findedRoadPoint.size();j++)
        			{
        				findedRoadPoint.chosen.set(j,0);
        				mWebView.loadUrl("javascript:setOrgIcon("+String.valueOf(j)+")");
        				
        		    }

        			for(j=0;j<haha.size();j++)
        			{
        				findedRoadPoint.chosen.set(haha.get(j)-1,1);
        				mWebView.loadUrl("javascript:setChosenIcon("+String.valueOf(haha.get(j)-1)+")");
        				findedRoadPoint.chosenNumber++;
	
        		    }
        			
        			chooseRoadPointBtn.setText("已選"+findedRoadPoint.num[findedRoadPoint.chosenNumber]+"點");
        			chooseRoadPointBtn.setEnabled(true);

        		}
        		
        	}});
		
		bundle=this.getIntent().getExtras();

        cookie=bundle.getString("cookie");
        uid="null";
    	
		chooseRoadPointBtn.setEnabled(false);
		rdmBtn.setEnabled(false);
		timerBtn.setEnabled(false);
		saveRouteBtn.setEnabled(false);
		
		 if(!cookie.equals("null")){
	        	new GetAu(MainActivity.this).execute(cookie);
	     }
		
    }
    
	//Bar and Drawer Init
    
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
					CGM.cancelByflag();
					return true;
				} else if (itemId == R.id.go) {
					CGM.goByflag();
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
	    
	    
	    //Callback Functions
	    
	    @Override
		public void getLatLonONCE(String lat, String lon,int number) {
	    	if(number==1){
				mWebView.loadUrl("javascript:startLocation("+lat+","+lon+")");
			}else if(number==2){
				mWebView.loadUrl("javascript:endLocation("+lat+","+lon+")");
			}
		}


		@Override
		public void getLatLonLOOP(String lat, String lon) {
			mWebView.loadUrl("javascript:currentLocation("+lat+","+lon+")");
		}
		
		@Override
	    public void setTimeWatchText(final String input){
	    	runOnUiThread(new Runnable() {
            	public void run() {
            		rightInfoText.setText(input);
            }
	    	});
	    }
	    
	    @Override
	    public void analysisJSON0(String content){
	    	
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
    					
						while((findedRoadPoint.size()<a)&&(aryCtr<b))
						{
						   int flagy=0;
						   String qwas=dataAry.getJSONObject(aryCtr).getString("latitude")+","+dataAry.getJSONObject(aryCtr).getString("longitude");
						   String qwas2=dataAry.getJSONObject(aryCtr).getString("title");
						   
						   if(findedRoadPoint.size()==0)
						   {
							   findedRoadPoint.value.add(qwas);
							   findedRoadPoint.name.add(qwas2);
							   flagy=-1;
							  
						   }else{

							   for(iu=0;iu<findedRoadPoint.size();iu++)
							   {

								   if(qwas.equals(findedRoadPoint.value.get(iu)))
								   {
									   rdr=iu;
									   flagy=1;
								   }
							   }
						   }
						   
						   if(flagy==0)
						   {
							   findedRoadPoint.value.add(qwas);
							   findedRoadPoint.name.add(qwas2);
						   }else if(flagy==1)
						   {
							   findedRoadPoint.name.set(rdr,findedRoadPoint.name.get(rdr)+'&');
							   findedRoadPoint.name.set(rdr,findedRoadPoint.name.get(rdr)+qwas2);
						   }
						   aryCtr++;
						}	    				
    				
						for(a=0;a<findedRoadPoint.size();a++)
						{
							findedRoadPoint.name.set(a,findedRoadPoint.name.get(a)+"#推薦地點");
							findedRoadPoint.chosen.add(0);
						}		
    			}
    		} catch (JSONException e) {
    			e.printStackTrace();
    		}  
	    }
	    
	    @Override
	    public void analysisJSON0(String content, String center, String radius){
	    	analysisJSON0(content);
	    	
	    	int repeatTimes=0;
	    	new PostData1(MainActivity.this,center,radius,repeatTimes).execute();
	    }
	     
	    @Override
	    public String analysisJSON1(String content){
	    	String iiiToken="null";
	    	try {
    			final JSONObject obj = new JSONObject(content);
    			
    			if(obj.getString("message").equals("success")==true){
    				iiiToken=obj.getJSONObject("result").getString("token");
    			}
    			
    		} catch (JSONException e) {
    			e.printStackTrace();
    		}  
        	
        	return iiiToken;
	    }
	    
	    @Override
	    public void analysisJSON1(String content, String center, String radius){
	    	String iiiToken=analysisJSON1(content);
	    	
	    	if(!iiiToken.equals("null")){
	    	
	    		iiiRPsetting(iiiToken,center,radius);
	    	}
	    }
        
	    @Override
        public int analysisJSON2(String content){
        	int ctr=0;
        	try {
    			final JSONObject obj = new JSONObject(content);
    			
    			if(obj.getString("message").equals("success")==true){

    			    JSONArray dataAry=obj.getJSONArray("result");
    				ctr=dataAry.length();
    				int a;
    				
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
							
							findedRoadPoint.name.add(tempp2+"#人氣度:"+dataAry.getJSONObject(a).getString("checkins"));
							findedRoadPoint.value.add(dataAry.getJSONObject(a).getString("latitude")+","+dataAry.getJSONObject(a).getString("longitude"));
							findedRoadPoint.chosen.add(0);
						}	
						
    			}
    		} catch (JSONException e) {
    			e.printStackTrace();
    		} 
        	return ctr;
	    }
        
        @Override
        public void frpEnding(boolean HttpFlag,String result){
	    	if(findedRoadPoint.size()>0){
		            	chooseRoadPointBtn.setEnabled(true);
						rdmBtn.setEnabled(true);
		            	mWebView.loadUrl("javascript:setMarkers("+findedRoadPoint.size()+")");
	    	}
	    	
	    	findRoadPointBtn.setEnabled(true);
	    	IIIswitch.setEnabled(true);
	    	CGM.goflag=1;
	    	CGM.cancelflag=1;
	    	
	    	if(!HttpFlag){
	    		Toast.makeText(MainActivity.this,result+"，路點可能因此短缺\n找到"+findedRoadPoint.size()+"個路點", 100).show();
			}else{
				Toast.makeText(MainActivity.this,"找到"+findedRoadPoint.size()+"個路點", 100).show();
			}
	    	
	    }

        @Override
        public void toaster(String toToast) {
        	// TODO Auto-generated method stub
        	Toast.makeText(MainActivity.this,toToast,100).show();
        }
        
        @Override
		public void getAuHandler(boolean flag, String result) {
			if(!flag){
				Toast.makeText(MainActivity.this,"身份認證失敗\n"+result,100).show();	
			}else if(flag){
				
				try{
					JSONObject obj = new JSONObject(result);
				
					if(obj.getString("message").equals("yes")==true){
						setUidAndHisRoute(obj.getString("uid"));
					}
				
				}catch (JSONException e) {
	    			e.printStackTrace();
	    		}  
			}
			
		}


		@Override
		public void postSaveRouteHandler(boolean flag, String result) {
			// TODO Auto-generated method stub
			if(!flag){
				Toast.makeText(MainActivity.this,"收藏失敗\n"+result,100).show();
			}else{
				Toast.makeText(MainActivity.this,"收藏成功",100).show();
			}
		}
	     
	    public void iiiRPsetting(String iiiToken,String center,String radius){
	    	
    		List<String> categorySend=new ArrayList<String>();
    		for(int i=0;i<categoryList.size();i++)
        	{
        		if(categoryList.chosen.get(i)==1)
        		{
        			categorySend.add(categoryList.value.get(i));
        		}
        	}	    	
	    	
	    	int limit=20;
	    	int categoryIndex=0;
	    	int leftTimes=1;
	    	boolean allFlag=true;
	    	String keywordString="null";
	    	int repeatTimes=0;
	    	
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
	    	
	    	new PostData2(MainActivity.this,categorySend,keywordString,limit,iiiToken,radius,center,categoryIndex,leftTimes,allFlag,repeatTimes).execute();
	    }
	    
	    public void routeKiller(){
	    	runOnUiThread(new Runnable() {
	            public void run() {
	            	if(timewatch.timerRunningFlag){
	            		timewatch.stop();
	        			
	        			loop.endListener();
	        			
	        			timerBtn.setText("Start");
	        			timewatch.timerRunningFlag=false;
	        	    	
	        	    	mWebView.loadUrl("javascript:killCurrentLocation()");
	            	}
	            	timewatch.setTotalSec0();
	            	timerBtn.setEnabled(false);
	            	
	            	if(!uid.equals("null")){
	            		saveRouteBtn.setEnabled(false);
	            	}
	            	
	            	findedRoadPoint.chosenNumber=0;
	            	chooseRoadPointBtn.setText("選擇路點");
	            	
	            	leftInfoText.setText("");
	            	rightInfoText.setText("");
	            	mWebView.loadUrl("javascript:killRoute()");
	           }
	       });
	    }
	    
	    public void setUidAndHisRoute(String uid_input){
	    	uid=uid_input;
	
			String savedPath=bundle.getString("savedPath");
			
			if(!savedPath.equals("null")){
				//Toast.makeText(MainActivity.this, savedPath,100).show();
				savedRoadPoint=new DataGroupOriginal();
				
				savedPath="["+savedPath+"]";
				JSONArray dataAry;
				try {
					dataAry = new JSONArray(savedPath);
				
					int i;
				
					for(i=0;i<dataAry.length();i++){
						savedRoadPoint.value.add(dataAry.getJSONObject(i).getString("land"));
						savedRoadPoint.name.add(dataAry.getJSONObject(i).getString("name"));
					}
					
					mWebView.loadUrl("javascript:routeHis("+savedRoadPoint.size()+")");
				
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	    	
	    }
	    
	    
	    //JavaInterface
	    
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
	    public void frpReturn(final String rad0,final String centerlt0,final String centerlg0,final String startLoc,final String endLoc)
	    {
	    	
	    	saverouteManager.startLatLng=startLoc;
	    	saverouteManager.endLatLng=endLoc;
	    	
	    	final int repeatTimes=0;
			
			runOnUiThread(new Runnable() {
	            public void run() {
	            	mWebView.loadUrl("javascript:killRPmarkers()");
	            	new PostData0(MainActivity.this,centerlt0,centerlg0,rad0,repeatTimes,serApiFlag).execute();
	           }
	       });
	    	
	    	
	    	
	     }
	    
	    @JavascriptInterface
	    public void routeReturn(final String distance,final String duration)
	    {
	    	CGM.cancelflag=2;
	    	
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
	    	CGM.cancelflag=3;
	    	
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
	    	final String localS=findedRoadPoint.value.get(intValue);
	    	
	    	return localS;
	    	
	    }
	    
	    @JavascriptInterface
	    public String getRPTitle(final String pos)
	    {
	    	
	    	int intValue=Integer.valueOf(pos);
	    	final String localS=findedRoadPoint.name.get(intValue);
	    	
	    	return localS;
	    }
	    
	    @JavascriptInterface
	    public String getRPLocationHis(final String pos)
	    {
	    	int intValue=Integer.valueOf(pos);
	    	final String localS=savedRoadPoint.value.get(intValue);
	    	
	    	return localS;
	    	
	    }
	    
	    @JavascriptInterface
	    public String getRPTitleHis(final String pos)
	    {
	    	
	    	int intValue=Integer.valueOf(pos);
	    	final String localS=savedRoadPoint.name.get(intValue);
	    	
	    	return localS;
	    }
	    
	    @JavascriptInterface
	    public String getRPChosen(final String pos)
	    {
	    	
	    	int intValue=Integer.valueOf(pos);
	    	final String localS=Integer.toString(findedRoadPoint.chosen.get(intValue));
	    	
	    	return localS;
	    }
	    
	    @JavascriptInterface
	    public void cancelByRec(final int Rec){
	    	int flagGG=0;
	    	
	    	if(findedRoadPoint.chosen.get(Rec)==0)
	    	{
	    		flagGG=1;
	    		Toast.makeText(MainActivity.this,"未選擇過", 100).show();
	    	}
	    	
	    	if(flagGG==0){
	    		findedRoadPoint.chosen.set(Rec,0);
	    		Toast.makeText(MainActivity.this,"已取消", 100).show();
	    		findedRoadPoint.chosenNumber--;
	    		
	    		runOnUiThread(new Runnable() {
		            public void run() {
		            	if(findedRoadPoint.chosenNumber>0){
    						chooseRoadPointBtn.setText("已選"+findedRoadPoint.num[findedRoadPoint.chosenNumber]+"點");
    					}else if(findedRoadPoint.chosenNumber==0){
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
	    	
	    	if(findedRoadPoint.chosen.get(Rec)==1)
	    	{
	    		flagGG=1;
	    		Toast.makeText(MainActivity.this,"重複選擇囉", 100).show();
	    	}
	    	
	    	for(j=0;j<findedRoadPoint.size();j++)
			{
				if(findedRoadPoint.chosen.get(j)==1)
				{
					chosenCtr++;
				}
		    }
	    	
	    	if((flagGG==0)&&(chosenCtr<6)){
	    		findedRoadPoint.chosen.set(Rec,1);
	    		Toast.makeText(MainActivity.this,"已選擇", 100).show();
	    		findedRoadPoint.chosenNumber++;
	    		
	    		 runOnUiThread(new Runnable() {
			            public void run() {
			            	chooseRoadPointBtn.setText("已選"+findedRoadPoint.num[findedRoadPoint.chosenNumber]+"點");
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
	    
	    //InnerClass
	    
	    class CancelGoManager{
	    	CancelGoManager(){
	    		cancelflag=0;
		    	goflag=0;
	    	}
	    	
	    	 public void cancelByflag() {  
	 	    	if(cancelflag==1)
	 	    	{
	 	    		if(findedRoadPoint.size()>0){
	 	    			int a;
	 	    			for(a=0;a<findedRoadPoint.size();a++)
	 	    			{
	 	    				findedRoadPoint.chosen.set(a,0);
	 	    				mWebView.loadUrl("javascript:setOrgIcon("+String.valueOf(a)+")");
	 	    			}
	 	    		
	 	    			runOnUiThread(new Runnable() {
	 	    				public void run() {
	 	    					findedRoadPoint.chosenNumber=0;
	 	    					chooseRoadPointBtn.setText("選擇路點");
	 	    					chooseRoadPointBtn.setEnabled(true);
	 	    				}
	 	    			});
	 	    		}
	 	    	}else if(cancelflag==2)
	 	    	{
	 	    		routeKiller();
	 	    
	 	    		if(findedRoadPoint.size()>0){
	 	    			int a;
	 	    			for(a=0;a<findedRoadPoint.size();a++)
	 					{
	 	    				findedRoadPoint.chosen.set(a,0);
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
	 		            	if(timewatch.timerRunningFlag){
	 		            		timewatch.stop();
	 		        			
	 		        			loop.endListener();
	 		        			
	 		        			timerBtn.setText("Start");
	 		        	    	
	 		        			timewatch.timerRunningFlag=false;
	 		        	    	
	 		        	    	mWebView.loadUrl("javascript:killCurrentLocation()");
	 		            	}
	 		            	timewatch.setTotalSec0();
	 		            	mWebView.loadUrl("javascript:route("+findedRoadPoint.size()+")");
	 		            	
	 		           }
	 		       });
	 	    		
	 	    	}
	 	    }  
	 	    
	    	public int cancelflag;
	    	public int goflag;
	    }
		
}









