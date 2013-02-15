package com.example.webguardian;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.TextView;

public class CheckWebSiteService extends Service {

	static final String TAG = "UpdateService";

	private Intent intent;
	private RemoteViews remoteViews;
	
	private StatisticDatasource datasource;

	private SharedPreferences pref;
	Timer timer = new Timer();
	MyTimerTask timerTask = new MyTimerTask();

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();	
		pref = PreferenceManager.getDefaultSharedPreferences(this);		
		datasource = new StatisticDatasource(this);
		datasource.open();
		Log.d(TAG, "onCreated");
	}

	@Override
	public int onStartCommand(Intent _intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);			
		Log.d(TAG, "onStart");
		intent = _intent;		
		int i = Integer.valueOf(pref.getString("refresh_time", "1"));
		remoteViews = new RemoteViews(this.getApplicationContext().getPackageName(), R.layout.activity_main);		 
		timer.schedule(timerTask, 1000, i * 1000);	
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		Log.d(TAG, "onDestroy");
		timer.cancel();
		datasource.close();
		super.onDestroy();
	}

	public int is_online() {
		String url = pref.getString("site_URL_preference", "");
		// check network
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
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
				remoteViews.setTextViewText(R.id.siteCode, "away");
				datasource.addRow("away");
				Log.d(TAG, "away");
				break;
			case 1:
				remoteViews.setTextViewText(R.id.siteCode, "busy");
				datasource.addRow("busy");
				Log.d(TAG, "busy");
				break;
			case 2:
				remoteViews.setTextViewText(R.id.siteCode, "online");
				datasource.addRow("online");
				Log.d(TAG, "online");
			}
		}

	}

}
