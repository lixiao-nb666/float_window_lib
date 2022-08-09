package com.newbee.floatwindow;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class PhoneStartReceiver extends BroadcastReceiver {

	static final String ACTION = "android.intent.action.BOOT_COMPLETED";

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
			Intent intent2 = new Intent(context, DemoActivity.class);
			intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent2);
		}
	}

}