package com.lztek.hdmiin;


import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lztek.hdmiin.service.HdmiInStatusService;
import com.lztek.hdmiin.view.HdmiPlayerView;
import com.newbee.floatwindow.BuildConfig;
import com.newbee.floatwindow.R;

public class MainActivity extends Activity implements
	View.OnClickListener,
        ServiceConnection,
	HdmiPlayerView.OnPlayEventListener {

	private ViewGroup mRootLayout;
	private HdmiPlayerView mHdmiPlayerView; 
	private TextView mTextView;
	private ImageButton mBtnSetting;
	
	private IHdmiIn mHdmiIn;
	private Handler mHandler;

	private int mPlayerErrorCount = -1; 
	private boolean mProcessExit = false; 
	
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
		
		mRootLayout = new FrameLayout(this);
		mRootLayout.setLayoutParams(new FrameLayout.LayoutParams(
							ViewGroup.LayoutParams.MATCH_PARENT,
							ViewGroup.LayoutParams.MATCH_PARENT));
		setContentView(mRootLayout);  
		
		mHandler = new Handler();
		initview();

		boolean findService = false;
		try { 
			ActivityManager am = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
			java.util.List<ActivityManager.RunningServiceInfo> runningServices =
					am.getRunningServices(128);  
			ComponentName componentName = new ComponentName(getPackageName(),
					HdmiInStatusService.class.getName());
			for (ActivityManager.RunningServiceInfo info  : runningServices) {
				if ( info.service.equals(componentName)) { 
					findService = true;
					break;
				} 
			}
		} catch (Exception e) {
		}
		
		if (!findService) { 
			startService(new Intent(getApplicationContext(), HdmiInStatusService.class));
		}
	} 
	@Override
	protected void onResume() { 
		super.onResume();
		
    	Log.d("#HDMI#", "######: onResume");
	}
	
	@Override
	protected void onPause() { 
		super.onPause(); 

    	Log.d("#HDMI#", "######: onPause");
	}
	
	@Override
	protected void onStart() { 
		super.onStart();

    	Log.d("#HDMI#", "######: onStart");
		bindService(new Intent(getApplicationContext(), HdmiInStatusService.class),
				this, Context.BIND_AUTO_CREATE);
	}
	
	@Override
	protected void onStop() { 
		super.onStop();

    	Log.d("#HDMI#", "######: onStop");

		mHdmiPlayerView.stopPlay();
		mHandler.removeCallbacksAndMessages(null);
		if (null != mHdmiIn) { 
			unbindService(this); 
		} else if (mProcessExit) {
			System.exit(0);
		}
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

		RelativeLayout buttonLayout = new RelativeLayout(this);
		RelativeLayout.LayoutParams wrapLayoutParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		wrapLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		wrapLayoutParams.bottomMargin = 10; 
		buttonLayout.setLayoutParams(wrapLayoutParams); 
		buttonLayout.setGravity(Gravity.CENTER);
		textViewLayout.addView(buttonLayout); 
		
		mBtnSetting = new ImageButton(this);
		mBtnSetting.setAlpha(0.8f); 
		mBtnSetting.setBackgroundResource(R.drawable.ic_allapps); 
		buttonLayout.addView(mBtnSetting, new RelativeLayout.LayoutParams(60, 60));
		
		mTextView.setText(getString(R.string.play_hdmi_loading));
		mBtnSetting.setOnClickListener(this);  
	} 
	
	private Runnable mHideBtnSettingRunnable = new Runnable() {
		@Override
		public void run() { 
			mBtnSetting.setVisibility(View.GONE);
			mHandler.removeCallbacks(mHideBtnSettingRunnable);
		}
	};
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		int keyCode = event.getKeyCode();
		if (BuildConfig.DEBUG  && keyCode == KeyEvent.KEYCODE_BACK) {
			return super.dispatchKeyEvent(event);
		}

		if (mPlayerErrorCount == 0 && event.getAction() == KeyEvent.ACTION_DOWN) {
			if (mBtnSetting.getVisibility() != View.VISIBLE) {
				mBtnSetting.setVisibility(View.VISIBLE);
			} else {
				mHandler.removeCallbacks(mHideBtnSettingRunnable);
			} 
			mHandler.postDelayed(mHideBtnSettingRunnable, 5000);
		}
		
		if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_ESCAPE) {
			finish();
			mProcessExit = true; 
			return true; 
		} 
		
		if (keyCode == KeyEvent.KEYCODE_ENTER) {
		}
		return super.dispatchKeyEvent(event);
	} 
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		if (mPlayerErrorCount == 0 && event.getAction() == MotionEvent.ACTION_DOWN) {
			if (mBtnSetting.getVisibility() != View.VISIBLE) {
				mBtnSetting.setVisibility(View.VISIBLE);
			} else {
				mHandler.removeCallbacks(mHideBtnSettingRunnable);
			} 
			mHandler.postDelayed(mHideBtnSettingRunnable, 5000);
		}
		return super.dispatchTouchEvent(event);
	}

	@Override
	public void onClick(View v) {
		if (null == v) {   
		} else if (v == mBtnSetting) {  
			this.startActivity(new Intent(this, SettingActivity.class));
			finish();
		} else { 
		}  
	}   
	 

	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		mHdmiIn = IHdmiIn.Stub.asInterface(service); 
		if (mSurfacePrepared && null != mHdmiIn) { 
			mTextView.setText(getString(R.string.play_hdmi_loading));
			mHdmiPlayerView.startPlay();
		}  
    	Log.d("#HDMI#", "######: onServiceConnected " + (null != mHdmiIn? "Ok" : "Failed"));
	}

	@Override
	public void onServiceDisconnected(ComponentName name) {
    	Log.d("#HDMI#", "######: onServiceDisconnected");
		mHdmiIn = null;
		
		if (mProcessExit) {
			System.exit(0);
		}
	}
	
	private boolean mSurfacePrepared = false;
	@Override
	public void onSurfacePrepared(HdmiPlayerView playerView) { 
		mSurfacePrepared = true;
		if (mSurfacePrepared && null != mHdmiIn) { 
			mTextView.setText(getString(R.string.play_hdmi_loading));
			mHdmiPlayerView.startPlay();
		}
	}

	@Override
	public void onHdmiStart(HdmiPlayerView playerView) {
		mPlayerErrorCount = 0;
		
		mTextView.setText("");
		//mBtnSetting.setVisibility(View.GONE);
		mHandler.postDelayed(mHideBtnSettingRunnable, 3000);
	}
	
	@Override
	public void onHdmiError(HdmiPlayerView playerView) {
		mPlayerErrorCount = mPlayerErrorCount <= 0? 1 : mPlayerErrorCount + 1;
		String text = getString(R.string.play_hdmi_error);
		//android.widget.Toast.makeText(this, text, android.widget.Toast.LENGTH_SHORT).show();
		
		mTextView.setText(text);
		mBtnSetting.setVisibility(View.VISIBLE);
		mHandler.removeCallbacks(mHideBtnSettingRunnable);
		 
		mHdmiPlayerView.stopPlay();
		if (mPlayerErrorCount < 1) { // 3
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() { 
					mTextView.setText(getString(R.string.play_hdmi_loading));
					mHdmiPlayerView.startPlay();
				}
			}, 3000);
		} else {
			mProcessExit = true;
			finish(); 
		}
	}
}
