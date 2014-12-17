package com.im.easygo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;


public class History extends Activity {
	
	 private String cookie;
	 private pathClass PC;
	 private LinearLayout ll;
	 private ListView mainListView;
	 private ArrayAdapter<String> listAdapter;
	 
	 public void init(){
		 if(PC.size()>0){
			 PC.init();
			 ll.removeView(mainListView);
			 mainListView=new ListView(History.this);
		 }
		 
		 if(!cookie.equals("null")){
	        	new getAu2().execute(cookie);
	        } 
	 }
	
	 protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.history_layout);
	        ll = (LinearLayout)findViewById(R.id.LinearLayout1);
	 
	        Bundle bundle=this.getIntent().getExtras();

	        cookie=bundle.getString("cookie");
	       
	        PC=new pathClass();
	        
	        mainListView=new ListView(History.this);
	        
	 }
	 
	 protected void onResume(){
		 super.onResume();
		 init();
		
	 }
	 
	 public void showHistoryPaths(String result){

			try{
				final JSONArray dataAry= new JSONArray(result);
			    int i;
			
				
				for(i=0;i<dataAry.length();i++){
					int k=i+1;
					final int g=i;
		
					PC.pathName.add("路線"+k);
					PC.pathContent.add(dataAry.getJSONObject(g).getString("path"));
					PC.pathID.add(dataAry.getJSONObject(g).getString("id"));
				}
				
				int a;
				String[] tempSarray = new String[PC.size()];
				for(a=0;a<PC.size();a++)
				{
					tempSarray[a]=PC.pathName.get(a);
				}
				
				listAdapter = new ArrayAdapter<String>(History.this,android.R.layout.simple_list_item_1,tempSarray);
				mainListView.setAdapter(listAdapter);
				mainListView.setOnItemClickListener(new OnItemClickListener(){
					@Override
					public void onItemClick(AdapterView<?> arg0,View arg1, int position, long arg3) {
						Intent intent = new Intent();
		        		intent.setClass(History.this, MainActivity.class);
		        		
		        		Bundle bundle = new Bundle();
		                bundle.putString("cookie", cookie);
		                bundle.putString("savedPath",PC.pathContent.get(position));
						
		                intent.putExtras(bundle);
		        	
		        		startActivity(intent); 
					}
				});
				
				mainListView.setOnItemLongClickListener(new OnItemLongClickListener(){

					@Override
					public boolean onItemLongClick(AdapterView<?> arg0,
							View arg1, final int position, long arg3) {
						new AlertDialog.Builder(History.this)
			            
			            .setMessage("您確定要刪除"+PC.pathName.get(position)+"嗎?")
			            .setPositiveButton("是", new DialogInterface.OnClickListener() {
			                   
			                @Override
			                public void onClick(DialogInterface dialog, int which) {
			                    // TODO Auto-generated method stub
			                	new deletePath().execute(PC.pathID.get(position));
			                }
			            })
			            .setNegativeButton("否", new DialogInterface.OnClickListener() {
			                  
			                @Override
			                public void onClick(DialogInterface dialog, int which) {
			                    // TODO Auto-generated method stub
			                      
			                }
			            })
			            .show();
						return true;
					}
				});
				
				ll.addView(mainListView);
				
				Toast.makeText(History.this, "找到"+String.valueOf(dataAry.length())+"條路線",100).show();
			
			}catch (JSONException e) {
 			e.printStackTrace();
			}  
	 		
		}
	 
	 class getAu2 extends AsyncTask<String,Void, String>
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
	                 flag=true;
	             } else{
	             	content=String.valueOf(response.getStatusLine().getStatusCode());
	             	flag=false;
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
	 		if(!flag){
	 			Toast.makeText(History.this, result,100).show();	
	 		}else{
	 			
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
	 	
	 	private boolean flag=false;
	 }

	 class getRoute extends AsyncTask<String,Void, String>
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
	                 flag=true;
	             } else{
	             	content=String.valueOf(response.getStatusLine().getStatusCode());
	             	flag=false;
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
	 		if(!flag){
	 			Toast.makeText(History.this, result,100).show();	
	 		}else if(flag){
	 			showHistoryPaths(result);
	 		}
	     }
	 	
	 	private boolean flag=false;
	 }

	 class deletePath extends AsyncTask<String,Void, Integer>
	 {
	 	
	 	@Override
	 	protected Integer doInBackground(String... arg0) {
	 		
	 		String getUrl="http://easygo.ballchen.cc/history/destroy/"+arg0[0];
	 		int Scode;
	 		int lFlag=0;
	 		 
	         HttpGet get=new HttpGet(getUrl);  
	 		 try {
	 			
	 			HttpClient client=new DefaultHttpClient();  
	             HttpResponse response=client.execute(get);  
	             Scode=response.getStatusLine().getStatusCode();
	             if((Scode==200)||(Scode==204)||(Scode==202)){   
	             	lFlag=1;
	             } else{
	             	lFlag=0;
	             }
	 		} catch (ClientProtocolException e) {  
	             e.printStackTrace();  
	        } catch (IOException e) {  
	             e.printStackTrace();  
	        }
	 		return lFlag;  
	 	}
	 	
	 	@Override
	 	protected void onPostExecute(Integer result)
	     {
	 		if(result==0){
	 			Toast.makeText(History.this,"刪除失敗",100).show();	
	 		}else if(result==1){
	 			Toast.makeText(History.this,"刪除成功",100).show();
	 			init();
	 		}
	     }
	 	
	 }

	 class pathClass{
	 	pathClass(){
	 		init();
	 	}
	 	
	 	public void init(){
	 		pathName=new ArrayList<String>();
	         pathContent=new ArrayList<String>();
	         pathID=new ArrayList<String>();
	 	}
	 	
	 	public int size(){
	 		return pathName.size();
	 	}
	 	
	 	public List<String> pathName;
	 	public List<String> pathContent;
	 	public List<String> pathID;
	 }
}