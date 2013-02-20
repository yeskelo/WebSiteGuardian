package com.example.webguardian;

import org.achartengine.GraphicalView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

public class PieChartActivity extends Activity {

	GraphicalView chartView;
	private StatisticDatasource datasource;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		datasource = new StatisticDatasource(this);
		datasource.open();
		
		getActionBar().setDisplayShowTitleEnabled(false);
		
		LinearLayout ll = new LinearLayout(this);
		ll.setOrientation(LinearLayout.VERTICAL);
		
		int onLineCount = datasource.getStatusCount(getResources().getString(R.string.online_state));
		int busyCount = datasource.getStatusCount(getResources().getString(R.string.busy_state));
		int awayCount = datasource.getStatusCount(getResources().getString(R.string.away_state));		
		
		chartView = PieChartView.getNewInstance(this, onLineCount, busyCount, awayCount);
		ll.addView(chartView);
		setContentView(ll);
	}	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		getMenuInflater().inflate(R.menu.action_menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = new Intent();
		switch (item.getItemId()) {
		case R.id.history_of_check:
			intent.setClass(PieChartActivity.this, MainActivity.class);
			startActivity(intent);
			break;
		case R.id.menu_settings:
			intent.setClass(PieChartActivity.this, SetPrefActivity.class);
			startActivityForResult(intent, 0);
			break;
		case R.id.availability:
			intent.setClass(PieChartActivity.this, PieChartActivity.class);
			startActivity(intent);
			break;
		}
		return true;
	}
}
