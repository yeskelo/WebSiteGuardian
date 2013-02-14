package com.example.webguardian;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class CheckWebSiteService extends Service {

	static final String TAG="UpdateService";
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
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(TAG, "onStart");
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		Log.d(TAG, "onDestroy");
		super.onDestroy();
	}

}
