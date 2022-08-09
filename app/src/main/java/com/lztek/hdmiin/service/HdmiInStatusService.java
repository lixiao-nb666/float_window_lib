package com.lztek.hdmiin.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Log;

import com.lztek.hdmiin.IHdmiIn;
import com.lztek.hdmiin.MainActivity;
import com.lztek.toolkit.Lztek;


public class HdmiInStatusService extends Service {
	private final String tag=getClass().getName()+">>>>>";



	public HdmiInStatusService() {
		Log.d(tag, "lixiaohdmiInstart----######[ onCreate ]#######1111");
	} 
	
	private Lztek mLztek = null;
	private HdmiIn mHdmiIn = new HdmiIn();
	private PendingIntent mPendingIntent = null;

	private int mPlayerCount = 0;
	
	private boolean mAutoPlay = true;
	
	@Override
	public void onCreate() { 
		super.onCreate();
  		try {
			mLztek = Lztek.create(this);
			SharedPreferences setting = getSharedPreferences("config", Context.MODE_MULTI_PROCESS);
			mAutoPlay = setting.getBoolean("auto_play", true);

			if (mAutoPlay) {
				mHandler.postDelayed(mHdmiInDetect, 2000);
			}

			AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
			Intent intent = new Intent(getApplicationContext(), HdmiInStatusService.class);
			mPendingIntent = PendingIntent.getService(getApplicationContext(), 0,
					intent, PendingIntent.FLAG_CANCEL_CURRENT);
			am.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 5*1000,
					5*1000, mPendingIntent);
		}catch (NoClassDefFoundError e){
			mLztek=null;
		}


	}
  
    @Override
    public void onDestroy() {    
        super.onDestroy();  
    	Log.d(tag, "######[ onDestroy ]#######");
        
        System.exit(0);
    }   
	
	@Override
	public IBinder onBind(Intent intent) {
    	mPlayerCount++; 
    	
    	Log.e(tag, "######[ onBind ]#######: " + mPlayerCount);
    	
		return mHdmiIn;
	} 
  
    @Override
    public boolean onUnbind(Intent intent) {
    	mPlayerCount--; 
    	
    	Log.e(tag, "######[ onUnbind ]####### " + mPlayerCount);

    	if (mAutoPlay) { 
    		mHandler.removeCallbacks(mHdmiInDetect); 
    		mHandler.postDelayed(mHdmiInDetect, 3000); 
    	}
		
        return super.onUnbind(intent);  
    }  
  
    public void onRebind(Intent intent) {
    	Log.d(tag, "######[ onRebind ]#######");
        super.onRebind(intent);  
    }  
   
	@Override
    public int onStartCommand(Intent intent, int flags, int startId) {
		try {
			android.app.Notification.Builder builder = new android.app.Notification.Builder(this);
			builder.setSmallIcon(android.R.drawable.ic_menu_help);
			builder.setTicker("");
			builder.setContentText("");
			builder.setContentTitle("");
			builder.setContentIntent(PendingIntent.getService(this, 0, intent, 0));
			startForeground(startId, builder.build());
			flags = START_STICKY;
			super.onStartCommand(intent, flags, startId);
		}catch (Exception e){

		}
    	return START_STICKY;
    }  
	

	private Handler mHandler = new Handler();
	private Runnable mHdmiInDetect = new Runnable() {
		@Override
		public void run() { 
			onHdmiInDetectRunnable(); 
		}
	}; 
	
	private void onHdmiInDetectRunnable () { 
		if (mAutoPlay) { 
			if (mPlayerCount <= 0 && hdmiValid()) { 
				this.startActivity(new Intent(this, MainActivity.class).addFlags(
						Intent.FLAG_ACTIVITY_NEW_TASK));
				mHandler.removeCallbacks(mHdmiInDetect); 
				mHandler.postDelayed(mHdmiInDetect, 5000); 
			} else { 
				mHandler.removeCallbacks(mHdmiInDetect); 
				mHandler.postDelayed(mHdmiInDetect, 2000); 
			}
		}
	}
	
	private boolean hdmiValid() {
		return mLztek.getHdmiinStatus() == 1; 
	}





	public class HdmiIn extends IHdmiIn.Stub {
		@Override
		public boolean isAutoPlay() throws RemoteException {
			return mAutoPlay;
		}

		@Override
		public void setAutoPlay(boolean value) throws RemoteException {
			if (value != mAutoPlay) {
				SharedPreferences setting = getSharedPreferences("config", Context.MODE_PRIVATE);
				setting.edit().putBoolean("auto_play", value).commit();
				mAutoPlay = setting.getBoolean("auto_play", true);

				if (mAutoPlay) {
					mHandler.postDelayed(mHdmiInDetect, 2000);
				} else {
					mHandler.removeCallbacks(mHdmiInDetect);
				}
			}
		}

		public HdmiInStatusService getService() {
			return HdmiInStatusService.this;
		}
	}
}
