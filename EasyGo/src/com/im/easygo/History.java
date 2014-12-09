package com.im.easygo;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.location.Criteria;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class History extends Activity {
	
	 private String cookie;
	 private int flag;
	 private int flag2;
	
	 protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.history_layout);
	 
	 
	        Bundle bundle=this.getIntent().getExtras();

	        cookie=bundle.getString("cookie");
	        
	        if(!cookie.equals("null")){
	        	new getAu().execute(cookie);
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
					Toast.makeText(History.this, result,100).show();	
				}else if(flag==1){
					
					try{
						JSONObject obj = new JSONObject(result);
	    			
						if(obj.getString("message").equals("yes")==true){
							new getRoute().execute(obj.getString("uid"));
						}
	    			
					}catch (JSONException e) {
		    			e.printStackTrace();
		    		}  
					
				}
	        }
	    }
	 
	 private class getRoute extends AsyncTask<String,Void, String>
	    {
			@Override
			protected String doInBackground(String... arg0) {
				
				String getUrl="http://easygo.ballchen.cc/history/find/user/"+arg0[0];
				String content = "null";
	    		 
	            HttpGet get=new HttpGet(getUrl);  
				 try {
					
					HttpClient client=new DefaultHttpClient();  
	                HttpResponse response=client.execute(get);  
	                if(response.getStatusLine().getStatusCode()==200){  
	                    content=EntityUtils.toString(response.getEntity());   
	                    flag2=1;
	                } else{
	                	content=String.valueOf(response.getStatusLine().getStatusCode());
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
					Toast.makeText(History.this, result,100).show();	
				}else if(flag2==1){
					
					try{
						final JSONArray dataAry= new JSONArray(result);
					    int i;
	    			
						
						LinearLayout ll = (LinearLayout)findViewById(R.id.LinearLayout1);
						
						for(i=0;i<dataAry.length();i++){
							int k=i+1;
							final int g=i;
							Button tempBtn = new Button(History.this);
					    	tempBtn.setText("路線"+k);
					    	ll.addView( tempBtn );
					    	
					    	tempBtn.setOnClickListener(new Button.OnClickListener(){
					         	@Override
					         	public void onClick(View v) {
					         		Intent intent = new Intent();
					        		intent.setClass(History.this, MainActivity.class);
					        		
					        		Bundle bundle = new Bundle();
					                bundle.putString("cookie", cookie);
					                try {
										bundle.putString("savedPath",dataAry.getJSONObject(g).getString("path"));
									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
					                intent.putExtras(bundle);
					        	
					        		startActivity(intent); 
					         		
					         	}
					         });
						}
						Toast.makeText(History.this, "找到"+String.valueOf(dataAry.length())+"條路線",100).show();
	    			
					}catch (JSONException e) {
		    			e.printStackTrace();
		    		}  
					
				}
	        }
	    }
}