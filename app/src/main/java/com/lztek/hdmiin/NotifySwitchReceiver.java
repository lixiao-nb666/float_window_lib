package com.lztek.hdmiin;  

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotifySwitchReceiver extends BroadcastReceiver {

	public static final String PROP_KEY = "persist.sys.hdmiin";

	public static final String ACTION_PLAYER_SHUTDOWN = "com.lztek.hdmiin.PLAYER_SHUTDOWN";
	
	@Override
    public void onReceive(final Context context, Intent intent) {
    	// if (BuildConfig.DEBUG && this.getClass() != null)
    	//	return;
    		
        final String action = intent.getAction(); 

		android.util.Log.d("#DEBUG#", "#HDMI NotifySwitchReceiver#: " + action);
		
		
        if (action.equals(Intent.ACTION_BOOT_COMPLETED)) { 
        	String value = Helper.getSystemProperty(PROP_KEY);
        	if (value.equalsIgnoreCase("true")) {  
	        	context.startActivity(new Intent(context, MainSwitchActivity.class).addFlags(
						Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("no-signal-go-on", true));
        	} 
        } else if (action.equals("android.intent.action.HDMIIN_KEY_SWITCH")) { 
        	String value = Helper.getSystemProperty(PROP_KEY);
        	if (value.equalsIgnoreCase("true")) { 
        		Helper.setSystemProperty(PROP_KEY, "false"); 
        		context.sendBroadcast(new Intent(ACTION_PLAYER_SHUTDOWN));
        	} else { 
        		Helper.setSystemProperty(PROP_KEY, "true");
	        	context.startActivity(new Intent(context, MainSwitchActivity.class).addFlags(
						Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("no-signal-go-on", true));
        	} 
        }
    }
}
