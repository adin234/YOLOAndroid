package org.ydd.yolo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(final Context context, final Intent bootintent) {
		Log.d("START?", "YES");
		Intent mServiceIntent = new Intent();
		mServiceIntent.setAction("org.ydd.yolo.services.MyService");
		context.startService(mServiceIntent);
	}
}