package com.example.webguardian;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
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

	CheckBox prefCheckBox;
	TextView prefEditText;
	
	Button runButton;
	Button stopButton;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		prefCheckBox = (CheckBox) findViewById(R.id.prefCheckBox);
		/*prefEditText = (TextView) findViewById(R.id.prefEditText);*/
		
		runButton = (Button) findViewById(R.id.runButton);
		stopButton = (Button) findViewById(R.id.stopButton);

		runButton.setOnClickListener((OnClickListener) this);
		stopButton.setOnClickListener((OnClickListener) this); 
		loadPref();
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		loadPref();
	}

	private void loadPref() {
		SharedPreferences mySharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(this);

		boolean my_checkbox_preference = mySharedPreferences.getBoolean(
				"service_preference", false);
		prefCheckBox.setChecked(my_checkbox_preference);

		String my_edittext_preference = mySharedPreferences.getString(
				"site_URL_preference", "");
/*		prefEditText.setText(my_edittext_preference);
*/
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
