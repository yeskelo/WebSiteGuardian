package com.example.webguardian;

import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TabHost;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener, LoaderCallbacks<Cursor> {

	Button runButton;
	Button stopButton;
	Button refreshButton;
	ListView allStateslistView;
	ListView failureStateslistView;
	private StatisticDatasource datasource;
	private SimpleCursorAdapter dataAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getLoaderManager().initLoader(0, null, this);

		setContentView(R.layout.activity_main);

		runButton = (Button) findViewById(R.id.runButton);
		stopButton = (Button) findViewById(R.id.stopButton);
		refreshButton = (Button) findViewById(R.id.refreshButton);

		allStateslistView = (ListView) findViewById(R.id.allStatesList);
		failureStateslistView = (ListView) findViewById(R.id.failureStateList);

		runButton.setOnClickListener((OnClickListener) this);
		stopButton.setOnClickListener((OnClickListener) this);
		refreshButton.setOnClickListener((OnClickListener) this);

		TabHost tabs = (TabHost) findViewById(R.id.tabhost);

		tabs.setup();

		TabHost.TabSpec spec1 = tabs.newTabSpec("tag1");

		spec1.setContent(R.id.allStatesList);
		spec1.setIndicator("All States");

		tabs.addTab(spec1);

		TabHost.TabSpec spec2 = tabs.newTabSpec("tag2");
		spec2.setContent(R.id.failureStateList);
		spec2.setIndicator("Failure States");

		tabs.addTab(spec2);

		getActionBar().setDisplayShowTitleEnabled(false);

		datasource = new StatisticDatasource(this);
		datasource.open();
		fillAllStatesList();	
	}


	private void fillAllStatesList() {

		Cursor allStatesCursor = datasource.getAllStates();
		Cursor failureStatesCursor = datasource.getAllFailusresStates();

		String[] columns = new String[] { StatisticTable.COLUMN_SITE_STATE,
				StatisticTable.COLUMN_SITE_STATE, StatisticTable.COLUMN_DATE };

		int[] to = new int[] { R.id.icon, R.id.state, R.id.stateDate };

		dataAdapter = new SimpleCursorAdapter(this, R.layout.state_info,
				allStatesCursor, columns, to, 0);
		dataAdapter.setViewBinder(new CustomViewBinder(this));
		allStateslistView.setAdapter(dataAdapter);

		dataAdapter = new SimpleCursorAdapter(this, R.layout.state_info,
				failureStatesCursor, columns, to, 0);
		dataAdapter.setViewBinder(new CustomViewBinder(this));
		failureStateslistView.setAdapter(dataAdapter);
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
			break;
		case R.id.menu_settings:
			intent.setClass(MainActivity.this, SetPrefActivity.class);
			startActivityForResult(intent, 0);
			break;
		case R.id.availability:
			intent.setClass(MainActivity.this, PieChartActivity.class);
			startActivity(intent);
			break;
		}

		return true;
	}

	@Override
	public void onClick(View src) {
		datasource.open();
		switch (src.getId()) {
		case R.id.runButton:
			startService(new Intent(this, CheckWebSiteService.class));
			break;
		case R.id.stopButton:
			stopService(new Intent(this, CheckWebSiteService.class));
			break;
		case R.id.refreshButton:
			break;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		allStateslistView.setAdapter(null);
		failureStateslistView.setAdapter(null);
		fillAllStatesList();
	}


	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		return new CursorLoader(this, WebGuardianContentProvider.CONTENT_URI, null, null, null, null);
	}


	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		dataAdapter.swapCursor(cursor); 
		dataAdapter.notifyDataSetChanged();		
	}


	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		dataAdapter.swapCursor(null);
	}
}
