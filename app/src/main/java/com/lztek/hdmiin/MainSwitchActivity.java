package com.lztek.hdmiin;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.lztek.hdmiin.view.HdmiPlayerView;
import com.newbee.floatwindow.BuildConfig;
import com.newbee.floatwindow.R;

public class MainSwitchActivity extends Activity implements
	HdmiPlayerView.OnPlayEventListener {

	private ViewGroup mRootLayout;
	private HdmiPlayerView mHdmiPlayerView; 
	private TextView mTextView;
	
	private Handler mHandler;

	
	private android.content.BroadcastReceiver mExitReceiver = new android.content.BroadcastReceiver() { 
		@Override
		public void onReceive(android.content.Context context, Intent intent) {
			String action = intent.getAction();
			Log.d("#DEBUG#", "#MainSwitch#: " + action);
			if (action.equals(NotifySwitchReceiver.ACTION_PLAYER_SHUTDOWN)) {
				finish();
				return;
			} 
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
//		getWindow().getDecorView().setSystemUiVisibility(
//                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                | View.SYSTEM_UI_FLAG_FULLSCREEN
//                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
		
		android.content.IntentFilter filter = new android.content.IntentFilter(); 
		filter.addAction(NotifySwitchReceiver.ACTION_PLAYER_SHUTDOWN);
		this.registerReceiver(mExitReceiver, filter);

		
		mRootLayout = new FrameLayout(this);
		mRootLayout.setLayoutParams(new FrameLayout.LayoutParams(
							ViewGroup.LayoutParams.MATCH_PARENT,
							ViewGroup.LayoutParams.MATCH_PARENT));
		setContentView(mRootLayout);  
		
		mHandler = new Handler();
		initview(); 

    	String value = Helper.getSystemProperty(NotifySwitchReceiver.PROP_KEY);
    	if (null == value || !value.equalsIgnoreCase("true")) {
    		finish();
    	}
	} 
	
	@Override
	protected void onDestroy() { 
		super.onDestroy();
		
		this.unregisterReceiver(mExitReceiver);
		
    	Log.d("#HDMI#", "#MainSwitch#: onDestroy");
	}
	
	@Override
	protected void onStart() { 
		super.onStart();

    	Log.d("#HDMI#", "#MainSwitch#: onStart");
	}
	
	@Override
	protected void onStop() { 
		super.onStop(); 
    	Log.d("#HDMI#", "#MainSwitch#: onStop");

		mHdmiPlayerView.stopPlay();
		mHandler.removeCallbacksAndMessages(null); 
	}

	public void initview() {  

		RelativeLayout hdmiPlayerLayout = new RelativeLayout(this);
		hdmiPlayerLayout.setLayoutParams(new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT));
		mRootLayout.addView(hdmiPlayerLayout);  
		
		mHdmiPlayerView = new HdmiPlayerView(this); 
		RelativeLayout.LayoutParams hdmiPlayerLayoutParams = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
		hdmiPlayerLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		hdmiPlayerLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
		hdmiPlayerLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		mHdmiPlayerView.setLayoutParams(hdmiPlayerLayoutParams);
		hdmiPlayerLayout.addView(mHdmiPlayerView); 
		
		mHdmiPlayerView.setOnPlayEventListener(this);
		

		RelativeLayout textViewLayout = new RelativeLayout(this);
		textViewLayout.setLayoutParams(new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT));
		mRootLayout.addView(textViewLayout);
		
		mTextView = new TextView(this);
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
		layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		mTextView.setLayoutParams(layoutParams); 
		mTextView.setTextColor(Color.RED);
		mTextView.setTextSize(24); 
		mTextView.setGravity(Gravity.CENTER);
		mTextView.setBackgroundColor(Color.TRANSPARENT);
		mTextView.setSingleLine(false);
		textViewLayout.addView(mTextView);  
 
		mTextView.setText(getString(R.string.play_hdmi_loading));
	} 
	 
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		int keyCode = event.getKeyCode();
		if (BuildConfig.DEBUG  && keyCode == KeyEvent.KEYCODE_BACK) {
			return super.dispatchKeyEvent(event);
		} 
		
		if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_ESCAPE) {
			return true; 
		} 
		
		if (keyCode == KeyEvent.KEYCODE_ENTER) {
		}
		return super.dispatchKeyEvent(event);
	} 
	
	@Override
	public void onSurfacePrepared(HdmiPlayerView playerView) {
		mTextView.setText(getString(R.string.play_hdmi_loading));
		mHdmiPlayerView.startPlay();
	}

	@Override
	public void onHdmiStart(HdmiPlayerView playerView) { 
		mTextView.setText(""); 
	}
	
	@Override
	public void onHdmiError(HdmiPlayerView playerView) {
		String text = getString(R.string.play_hdmi_error);
		//android.widget.Toast.makeText(this, text, android.widget.Toast.LENGTH_SHORT).show();
		
		mTextView.setText(text); 
		 
		mHdmiPlayerView.stopPlay(); 
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() { 
				mTextView.setText(getString(R.string.play_hdmi_loading));
				mHdmiPlayerView.startPlay();
			}
		}, 3000);
	}
}
