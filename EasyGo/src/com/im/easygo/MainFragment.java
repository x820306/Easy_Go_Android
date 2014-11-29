package com.im.easygo;  

  


import android.content.Intent;  
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
	private String token;
  
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
  
        LoginButton authButton = (LoginButton) view  
                .findViewById(R.id.authButton);  
        authButton.setFragment(this);  
        
        goImgBtn=(ImageButton)view.findViewById(R.id.imageButton1);
        historyImgBtn=(ImageButton)view.findViewById(R.id.imageButton3);
        
        goImgBtn.setOnClickListener(new Button.OnClickListener(){
        	@Override
        	public void onClick(View v) {
        		Intent intent = new Intent();
        		intent.setClass(getActivity(), MainActivity.class);
        		
        		Bundle bundle = new Bundle();
                bundle.putString("token", token);
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
                bundle.putString("token", token);
                intent.putExtras(bundle);
                
        		startActivity(intent); 
        	}
        });
        
        historyImgBtn.setEnabled(false);
        token="null";
       
        return view;  
    }  
  
    private void onSessionStateChange(Session session, SessionState state,  
            Exception exception) {  
        if (state.isOpened()) {  
            Log.i(TAG, "Logged in..."); 
            historyImgBtn.setEnabled(true);
            //Toast.makeText(getActivity(), session.getAccessToken(),100).show();
            token=session.getAccessToken();
        } else if (state.isClosed()) {  
            Log.i(TAG, "Logged out..."); 
            historyImgBtn.setEnabled(false);
            token="null";
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
}  


