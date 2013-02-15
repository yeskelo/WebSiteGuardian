package com.example.webguardian;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener{

	Button runButton;
	Button stopButton;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		runButton = (Button) findViewById(R.id.runButton);
		stopButton = (Button) findViewById(R.id.stopButton);

		runButton.setOnClickListener((OnClickListener) this);
		stopButton.setOnClickListener((OnClickListener) this);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		Intent intent = new Intent();
		intent.setClass(MainActivity.this, SetPrefActivity.class);
		startActivityForResult(intent, 0);

		return true;
	}

	@Override
	public void onClick(View src){
		switch (src.getId()) {
		case R.id.runButton:	
			startService(new Intent(this, CheckWebSiteService.class));
			break;
		case R.id.stopButton:
			stopService(new Intent(this, CheckWebSiteService.class));
			break;
		}
	}

}
