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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

public class CheckWebSiteService extends Service {

	static final String TAG = "UpdateService";

	private Context context;
	private Intent intent;
	private RemoteViews remoteViews;

	Timer timer = new Timer();
	MyTimerTask timerTask = new MyTimerTask();

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(TAG, "onCreated");
	}

	@Override
	public int onStartCommand(Intent _intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		Log.d(TAG, "onStart");
		intent = _intent;
		remoteViews = new RemoteViews(this.getApplicationContext().getPackageName(), R.layout.activity_main);
		int refresh_period = 1;
		timer.schedule(timerTask, 1000, refresh_period * 1000);
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		Log.d(TAG, "onDestroy");
		timer.cancel();
		super.onDestroy();
	}

	public int is_online(String url) {
		// check network
		String site = "http://uroddd.ru";
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		// if no network is available networkInfo will be null
		// otherwise check if we are connected
		if (networkInfo == null || !networkInfo.isConnected()) {
			return 0;
		}

		// check if website is online
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(site);
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
			switch (is_online("")) {
			case 0:
				remoteViews.setTextViewText(R.id.siteCode, "away");
				Log.d(TAG, "away");
				break;
			case 1:
				remoteViews.setTextViewText(R.id.siteCode, "busy");
				Log.d(TAG, "busy");
				break;
			case 2:
				remoteViews.setTextViewText(R.id.siteCode, "online");
				Log.d(TAG, "online");
			}
		}

	}

}
