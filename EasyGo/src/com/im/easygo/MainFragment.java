package com.im.easygo;  

  


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

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
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;  
import android.os.AsyncTask;
import android.os.Bundle;  
import android.support.v4.app.Fragment;  
import android.util.Log;  
import android.view.LayoutInflater;  
import android.view.View;  
import android.view.ViewGroup;  
  
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.facebook.Session;  
import com.facebook.SessionState;  
import com.facebook.UiLifecycleHelper;  
import com.facebook.widget.LoginButton;  


interface AndroidLoginCallback{
	public void AndroidLoginHandler(boolean flag,String result);
}

interface LogoutCallback{
	public void LogoutHandler(boolean flag,String result);
}

class AndroidLogin extends HttpWork
{
	AndroidLogin(AndroidLoginCallback caller){
		caller_c=caller;
	}
	
	@Override
	protected String responseToContent(HttpResponse response){
		String content=response.getFirstHeader("Set-Cookie").getValue();
		return content;
	}
	
	@Override
	protected String doInBackground(String... arg0) {
		
		String postUrl="http://easygo.ballchen.cc/android_login";
		String content = "null";
		List<NameValuePair> params=new ArrayList<NameValuePair>();  
        params.add(new BasicNameValuePair("access_token", arg0[0]));  
        
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
		caller_c.AndroidLoginHandler(HttpFlag,result);
    }
	
	private AndroidLoginCallback caller_c;
}


class Logout extends HttpWork
{
	Logout(LogoutCallback caller){
		caller_c=caller;
	}
	
	@Override
	protected String responseToContent(HttpResponse response){
		
		return "null";
	}
	
	@Override
	protected String doInBackground(String... arg0) {
		
		String getUrl="http://easygo.ballchen.cc/logout";
		String content = "null";
		 
		HttpGet get=new HttpGet(getUrl);	
		get.setHeader("cookie",arg0[0]);
			
	    content=HttpHandler(get,15000,200);
		
	    return content;
	}
	
	@Override
	protected void onPostExecute(String result)
    {
		caller_c.LogoutHandler(HttpFlag,result);
    }
	
	private LogoutCallback caller_c;
}



public class MainFragment extends Fragment implements AndroidLoginCallback,LogoutCallback{  
    private static final String TAG = "MainFragment";  
    private UiLifecycleHelper uiHelper;  
	private ImageButton goImgBtn;
	private ImageButton historyImgBtn;
	private String cookie;
	private Session mSession;
  
    private Session.StatusCallback callback = new Session.StatusCallback() {  
        @Override  
        public void call(Session session, SessionState state,  
                Exception exception) {  
            onSessionStateChange(session, state, exception);  
        }  
    };  
  
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        uiHelper = new UiLifecycleHelper(getActivity(), callback);  
        uiHelper.onCreate(savedInstanceState);  
    }  
  
    @Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  
            Bundle savedInstanceState) {  
        View view = inflater.inflate(R.layout.top_layout, container, false);  
  
        LoginButton authButton = (LoginButton) view.findViewById(R.id.authButton);  
        authButton.setFragment(this);  
        
        goImgBtn=(ImageButton)view.findViewById(R.id.imageButton1);
        historyImgBtn=(ImageButton)view.findViewById(R.id.imageButton3);
        
        goImgBtn.setOnClickListener(new Button.OnClickListener(){
        	@Override
        	public void onClick(View v) {
        		Intent intent = new Intent();
        		intent.setClass(getActivity(), MainActivity.class);
        		
        		Bundle bundle = new Bundle();
                bundle.putString("cookie", cookie);
                bundle.putString("savedPath","null");
                intent.putExtras(bundle);
        	
        		startActivity(intent); 
        	}
        });
        
        historyImgBtn.setOnClickListener(new Button.OnClickListener(){
        	@Override
        	public void onClick(View v) {
        		Intent intent = new Intent();
        		intent.setClass(getActivity(), History.class);
        		
        		Bundle bundle = new Bundle();
                bundle.putString("cookie", cookie);
                intent.putExtras(bundle);
                
        		startActivity(intent); 
        	}
        });
        
        historyImgBtn.setEnabled(false);
        cookie="null";
        mSession=null;
       
        return view;  
    }  
  
    private void onSessionStateChange(Session session, SessionState state,  
            Exception exception) {  
        if (state.isOpened()) {  
        	if (mSession == null || isSessionChanged(session)) {
                mSession = session;
                Log.i(TAG, "Logged in..."); 
                //Toast.makeText(getActivity(), session.getAccessToken(),100).show();
                goImgBtn.setEnabled(false);
                String token=session.getAccessToken();
                new AndroidLogin(MainFragment.this).execute(token);
        	}
        } else if (state.isClosed()) {  
            Log.i(TAG, "Logged out..."); 
            
            if(!cookie.equals("null")){
            	goImgBtn.setEnabled(false);
            	historyImgBtn.setEnabled(false);
            	new Logout(MainFragment.this).execute(cookie);
            }
            
        }  
    }  
      
    @Override  
    public void onResume() {  
        super.onResume();  
  
        // For scenarios where the main activity is launched and user  
        // session is not null, the session state change notification  
        // may not be triggered. Trigger it if it's open/closed.  
        Session session = Session.getActiveSession();  
        if (session != null && (session.isOpened() || session.isClosed())) {  
            onSessionStateChange(session, session.getState(), null);  
        }  
  
        uiHelper.onResume();  
    }  
  
    @Override  
    public void onActivityResult(int requestCode, int resultCode, Intent data) {  
        super.onActivityResult(requestCode, resultCode, data);  
        uiHelper.onActivityResult(requestCode, resultCode, data);  
    }  
  
    @Override  
    public void onPause() {  
        super.onPause();  
        uiHelper.onPause();  
    }  
  
    @Override  
    public void onDestroy() {  
        super.onDestroy();  
        uiHelper.onDestroy();  
    }  
  
    @Override  
    public void onSaveInstanceState(Bundle outState) {  
        super.onSaveInstanceState(outState);  
        uiHelper.onSaveInstanceState(outState);  
    }  
    
    @Override
    public void AndroidLoginHandler(boolean flag,String result){
		if(!flag){
			Toast.makeText(getActivity(),"登入未完成\n"+result,100).show();	
		}else if(flag){
			cookie=result;
			historyImgBtn.setEnabled(true);
			Toast.makeText(getActivity(), "登入成功",100).show();
		}
		goImgBtn.setEnabled(true);
    }
    
    @Override
    public void LogoutHandler(boolean flag,String result){
    	if(!flag){
			Toast.makeText(getActivity(),"登出未完成\n"+result,100).show();	
		}else if(flag){
            cookie="null";
            mSession=null;
            Toast.makeText(getActivity(),"登出成功",100).show();
		}
		goImgBtn.setEnabled(true);
    }
    
    
    private boolean isSessionChanged(Session session) {

        // Check if session state changed
        if (mSession.getState() != session.getState())
            return true;

        // Check if accessToken changed
        if (mSession.getAccessToken() != null) {
            if (!mSession.getAccessToken().equals(session.getAccessToken()))
                return true;
        }
        else if (session.getAccessToken() != null) {
            return true;
        }

        // Nothing changed
        return false;
    }
    
    
   
}  


