package com.im.easygo;  

  


import java.io.IOException;
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
  
public class MainFragment extends Fragment {  
    private static final String TAG = "MainFragment";  
    private UiLifecycleHelper uiHelper;  
	private ImageButton goImgBtn;
	private ImageButton historyImgBtn;
	private String cookie;
  
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
       
        return view;  
    }  
  
    private void onSessionStateChange(Session session, SessionState state,  
            Exception exception) {  
        if (state.isOpened()) {  
            Log.i(TAG, "Logged in..."); 
            //Toast.makeText(getActivity(), session.getAccessToken(),100).show();
            goImgBtn.setEnabled(false);
            String token=session.getAccessToken();
            new postToken().execute(token);
        } else if (state.isClosed()) {  
            Log.i(TAG, "Logged out..."); 
            historyImgBtn.setEnabled(false);
            cookie="null";
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
    
    private class postToken extends AsyncTask<String,Void, String>
    {
		@Override
		protected String doInBackground(String... arg0) {
			
			String postUrl="http://192.168.1.105:1337/android_login";
			String content = "null";
    		List<NameValuePair> params=new ArrayList<NameValuePair>();  
            params.add(new BasicNameValuePair("access_token", arg0[0]));  
    		 
       
            HttpPost post=new HttpPost(postUrl);  
			 try {
				post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
				
				HttpClient client=new DefaultHttpClient();  
                HttpResponse response=client.execute(post);  
                if(response.getStatusLine().getStatusCode()==200){  
                   //content=EntityUtils.toString(response.getEntity());    
                   content=response.getFirstHeader("Set-Cookie").getValue();

                   
                } else{
                	content="null";
                	
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
			//new getAu().execute(result);
			cookie=result;
			goImgBtn.setEnabled(true);
			if(!cookie.equals("null")){
				historyImgBtn.setEnabled(true);
			}
        }
    }
    
   
}  


