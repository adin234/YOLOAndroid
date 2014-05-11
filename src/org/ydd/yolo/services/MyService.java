package org.ydd.yolo.services;

import org.json.JSONException;
import org.json.JSONObject;
import org.ydd.yolo.utiility.AppPreferences;
import org.ydd.yolo.utiility.Constants;
import org.ydd.yolo.utiility.PostExecute;
import org.ydd.yolo.utiility.PutRequest;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class MyService extends Service implements LocationListener {
	private static final String TAG = "BOOMBOOMTESTGPS";
	private LocationManager mLocationManager = null;
	private static final int LOCATION_INTERVAL = 1000;
	private static final float LOCATION_DISTANCE = 10f;
	private LocationManager locationManager;
	private AppPreferences pref;
	private LocationManager locationManagerNetwork;
	
	@Override
	public IBinder onBind(Intent arg0) {
	    return null;
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
	    Log.e(TAG, "onStartCommand");
	    super.onStartCommand(intent, flags, startId);  
	    initialize();
	    return START_STICKY;
	}
	
	@Override
	public void onCreate() {
		pref = new AppPreferences(getApplicationContext());
		Toast.makeText(this, "START SERVE", Toast.LENGTH_LONG).show();
		initialize();
	}
	
	public void initialize() {
		Log.d("interval is", ""+Integer.parseInt(pref.get("interval", "60"))*1000);
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER,
                Integer.parseInt(pref.get("interval", "60"))*1000,   // 3 sec
                1, this);
		
		locationManagerNetwork = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationManagerNetwork.requestLocationUpdates( LocationManager.NETWORK_PROVIDER,
				Integer.parseInt(pref.get("interval", "60"))*1000,   // 3 sec
                1, this);
	}
	
	@Override
	public void onDestroy()
	{
	    Log.e(TAG, "onDestroy");
	    super.onDestroy();
	}
	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
		PutRequest put = new PutRequest();
		double longi = arg0.getLongitude();
		double lati = arg0.getLatitude();
		
		JSONObject data = new JSONObject();
		try {
			data.put("long", longi);
			data.put("lat", lati);
			data.put("number", pref.get("number"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		pref.set("lastPull", ""+System.currentTimeMillis());
		Log.d("last pull in", pref.get("lastPull"));
		
		put.jsonbody = data.toString();
		put.postexecute = new PostExecute() {
			
			@Override
			public void doAfter(String string) throws JSONException {
				Log.d("DATA SENT", string);
			}
		};
		Log.d("THIS IS THE DATA", data.toString());
		put.execute(Constants.server+"/update");
		
	}
	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		Log.d("CHANGED", arg0+"|"+arg1);
	}
}