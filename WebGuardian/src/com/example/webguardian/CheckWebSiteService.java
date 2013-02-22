package com.example.webguardian;

import java.io.IOException;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

public class CheckWebSiteService extends Service {

	static final String TAG = "UpdateService";	
	private boolean isRunning = false;
	private Intent intent;
	
	ConnectivityManager connectivityManager;

	private SharedPreferences pref;
	Timer timer = new Timer();
	MyTimerTask timerTask = new MyTimerTask();

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	private boolean isNetworkAvailable() {
	    connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isAvailable() && activeNetworkInfo.isConnected();
	}

	@Override
	public void onCreate() {
		super.onCreate();	
		pref = PreferenceManager.getDefaultSharedPreferences(this);		
		Log.d(TAG, "onCreated");
	}

	@Override
	public int onStartCommand(Intent _intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);			
		Log.d(TAG, "onStart");
		intent = _intent;		
		int i = Integer.valueOf(pref.getString("refresh_time", "1"));
		if (isNetworkAvailable() && !isRunning){
			timer.schedule(timerTask, 1000, i * 1000);	
			isRunning=true;}							
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		Log.d(TAG, "onDestroy");
		timer.cancel();
		isRunning=false;
		super.onDestroy();
	}

	public int is_online() {
		String url = pref.getString("site_URL_preference", "");
		// check network
		connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		// if no network is available networkInfo will be null
		// otherwise check if we are connected
		if (networkInfo == null || !networkInfo.isConnected()) {
			return 0;
		}

		// check if website is online
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(url);
		HttpResponse response;
		try {
			response = client.execute(request);
		} catch (ClientProtocolException e) {
			return 1;
		} catch (IOException e) {
			return 1;
		}
		int stat_code = response.getStatusLine().getStatusCode();
		if (stat_code != 200)
			return 1;
		else
			return 2;
	}

	private class MyTimerTask extends TimerTask {
		@Override
		public void run() {	
			Log.d(TAG, "Timer started");
			switch (is_online()) {
			case 0:				
				saveState("away");
				Log.d(TAG, "away");
				break;
			case 1:
				saveState("busy");
				Log.d(TAG, "busy");
				break;
			case 2:
				saveState("online");
				Log.d(TAG, "online");
			}
		}

	}
	
	private void saveState(String state) {
	    ContentValues values = new ContentValues();
	    values.put(StatisticTable.COLUMN_SITE_STATE, state);
	    values.put(StatisticTable.COLUMN_DATE, Calendar.getInstance().getTimeInMillis());
	    getContentResolver().insert(WebGuardianContentProvider.CONTENT_URI, values);	    
	  }
 

}
