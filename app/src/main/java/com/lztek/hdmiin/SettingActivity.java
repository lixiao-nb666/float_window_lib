package com.lztek.hdmiin;


import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import com.lztek.hdmiin.service.HdmiInStatusService;
import com.newbee.floatwindow.R;

public class SettingActivity extends Activity implements
	View.OnClickListener,
	CompoundButton.OnCheckedChangeListener,
        ServiceConnection {
	

	private Button mBtnComfirm;
	private Button mBtnCancel;

	private CheckBox mChbAutoPlay;
	
	private IHdmiIn mHdmiIn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_layout);

		
		mBtnComfirm = (Button) findViewById(R.id.btn_confirm);
		mBtnCancel = (Button)findViewById(R.id.btn_cancel);
		
		mChbAutoPlay = (CheckBox)findViewById(R.id.auto_play_checkBox);
		mChbAutoPlay.setEnabled(false);

		mBtnComfirm.setOnClickListener(this);
		mBtnCancel.setOnClickListener(this);
		
		mChbAutoPlay.setOnCheckedChangeListener(this);
		
		bindService(new Intent(this, HdmiInStatusService.class),  this, Context.BIND_AUTO_CREATE);
	} 
	
	@Override
	protected void onPause() { 
		super.onPause();  
	} 

	
	@Override
	protected void onStop() { 
		super.onStop(); 
		if (null != mHdmiIn) { 
			unbindService(this); 
		}
		System.exit(0);
	}

	@Override
	public void onClick(View v) {
		if (null == v) {   
		} else if (v == mBtnComfirm) {   
			if (null != mHdmiIn) { 
				try { 
					if (mChbAutoPlay.isChecked() != mHdmiIn.isAutoPlay()) { 
						mHdmiIn.setAutoPlay(mChbAutoPlay.isChecked());
					}
				} catch (Exception e) {
				} 
			}
			finish(); 
		} else if (v == mBtnCancel) {  
			finish(); 
		} else { 
		}  
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (buttonView == mChbAutoPlay) { 
		}
	}

	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		mHdmiIn = IHdmiIn.Stub.asInterface(service); 
		if (null != mHdmiIn) { 
			try { 
				mChbAutoPlay.setChecked(mHdmiIn.isAutoPlay());
			} catch (Exception e) {
			}
			mChbAutoPlay.setEnabled(true);
		}
	}

	@Override
	public void onServiceDisconnected(ComponentName name) {
		mHdmiIn = null;
		mChbAutoPlay.setEnabled(false);
	}  
 
}
