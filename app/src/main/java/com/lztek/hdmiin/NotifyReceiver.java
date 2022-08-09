package com.lztek.hdmiin;  

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.lztek.hdmiin.service.HdmiInStatusService;

public class NotifyReceiver extends BroadcastReceiver {

	@Override
    public void onReceive(final Context context, Intent intent) {
    	// if (BuildConfig.DEBUG && this.getClass() != null)
    	//	return;
    		
        final String action = intent.getAction(); 
        if (action.equals(Intent.ACTION_BOOT_COMPLETED)) { 
        	Intent intentStart = new Intent(context.getApplicationContext(), 
        			HdmiInStatusService.class);
    		context.startService(intentStart); 
        } 
    }
}
