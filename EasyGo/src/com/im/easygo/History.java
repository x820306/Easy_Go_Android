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

interface GetRoutesCallback{
	public void getRoutesHandler(boolean flag,String result);
}


interface DeleteRoutesCallback{
	public void deleteRoutesHandler(boolean flag,String result);
}


class GetRoutes extends HttpWork
{
	GetRoutes(GetRoutesCallback caller){
		caller_c=caller;
	}
	
	@Override
	protected String doInBackground(String... arg0) {
		
		String getUrl="http://easygo.ballchen.cc/history/find/user/"+arg0[0];
		String content = "null";
		 
        HttpGet get=new HttpGet(getUrl);  
        content=HttpHandler(get,15000,200);
		
		return content;
	}
	
	@Override
	protected void onPostExecute(String result)
    {
		caller_c.getRoutesHandler(HttpFlag, result);
    }
	
	private GetRoutesCallback caller_c;
}

class DeleteRoutes extends HttpWork
{
	DeleteRoutes(DeleteRoutesCallback caller){
		caller_c=caller;
	}
	
	@Override
	protected String responseToContent(HttpResponse response){
		
		return "null";
	}
	
	@Override
	protected String doInBackground(String... arg0) {
		
		String getUrl="http://easygo.ballchen.cc/history/destroy/"+arg0[0];
		String content="null";
		 
		HttpGet get=new HttpGet(getUrl);  
        content=HttpHandler(get,15000,200);
        
        if((content.equals("204"))||(content.equals("202"))){
        	HttpFlag=true;
        }
 
		return content;  
	}
	
	@Override
	protected void onPostExecute(String result)
    {
		caller_c.deleteRoutesHandler(HttpFlag, result);
    }
	
	private DeleteRoutesCallback caller_c;
}


public class History extends Activity implements GetAuCallback,DeleteRoutesCallback,GetRoutesCallback{
	
	 private String cookie;
	 private DataGroupStringListX3 savedRoutes;
	 private LinearLayout ll;
	 private ListView mainListView;
	 private ArrayAdapter<String> listAdapter;
	 
	 public void init(){
		 if(savedRoutes.size()>0){
			 savedRoutes.init();
			 ll.removeView(mainListView);
			 mainListView=new ListView(History.this);
		 }
		 
		 if(!cookie.equals("null")){
	        	new GetAu(History.this).execute(cookie);
	        } 
	 }
	
	 protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.history_layout);
	        ll = (LinearLayout)findViewById(R.id.LinearLayout1);
	 
	        Bundle bundle=this.getIntent().getExtras();

	        cookie=bundle.getString("cookie");
	       
	        savedRoutes=new DataGroupStringListX3();
	        
	        mainListView=new ListView(History.this);
	        
	 }
	 
	 protected void onResume(){
		 super.onResume();
		 init();
		
	 }
	 
	 public void showHistoryRoutes(String result){
	
		 try{
				final JSONArray dataAry= new JSONArray(result);
			    int i;
			
				
				for(i=0;i<dataAry.length();i++){
					int k=i+1;
					final int g=i;
		
					savedRoutes.add("路線"+k,dataAry.getJSONObject(g).getString("path"),dataAry.getJSONObject(g).getString("id"));
				}
				
				int a;
				String[] tempSarray = new String[savedRoutes.size()];
				for(a=0;a<savedRoutes.size();a++)
				{
					tempSarray[a]=savedRoutes.nameGet(a);
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
		                bundle.putString("savedPath",savedRoutes.valueGet(position));
						
		                intent.putExtras(bundle);
		        	
		        		startActivity(intent); 
					}
				});
				
				mainListView.setOnItemLongClickListener(new OnItemLongClickListener(){

					@Override
					public boolean onItemLongClick(AdapterView<?> arg0,
							View arg1, final int position, long arg3) {
						new AlertDialog.Builder(History.this)
			            
			            .setMessage("您確定要刪除"+savedRoutes.nameGet(position)+"嗎?")
			            .setPositiveButton("是", new DialogInterface.OnClickListener() {
			                   
			                @Override
			                public void onClick(DialogInterface dialog, int which) {
			                    // TODO Auto-generated method stub
			                	new DeleteRoutes(History.this).execute(savedRoutes.ID.get(position));
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
	 
	@Override
	public void getAuHandler(boolean flag, String result) {
		// TODO Auto-generated method stub
		if(!flag){
 			Toast.makeText(History.this,"身份認證失敗\n"+result,100).show();	
 		}else{
 			
 			try{
 				JSONObject obj = new JSONObject(result);
 			
 				if(obj.getString("message").equals("yes")==true){
 					new GetRoutes(History.this).execute(obj.getString("uid"));
 				}
 			
 			}catch (JSONException e) {
     			e.printStackTrace();
     		}  
 			
 		}
	}

	@Override
	public void getRoutesHandler(boolean flag, String result) {
		// TODO Auto-generated method stub
		if(!flag){
			Toast.makeText(History.this,"取得路線失敗\n"+result,100).show();	
		}else if(flag){
			showHistoryRoutes(result);
		}
	}

	@Override
	public void deleteRoutesHandler(boolean flag, String result) {
		// TODO Auto-generated method stub
		if(!flag){
			Toast.makeText(History.this,"刪除失敗"+result,100).show();	
		}else if(flag){
			Toast.makeText(History.this,"刪除成功",100).show();
			init();
		}
		
	}
	
	private class DataGroupStringListX3 extends DataGroupOriginal{
		
		DataGroupStringListX3(){
			super();
			init3rd();
		}
		
		public void init(){
			super.init();
			init3rd();
		} 
		
		public void init3rd(){
			ID=new ArrayList<String>();
		}
		
		public void IDset(int index,String input){
			ID.set(index,input);
		}
		
		public String IDget(int index){
			return ID.get(index);
		}
		
		public void add(String nameInput,String valueInput,String IDinput){
			super.add(nameInput,valueInput);
			ID.add(IDinput);
		}

		private List<String> ID;
	}
}