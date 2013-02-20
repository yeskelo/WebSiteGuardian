package com.example.webguardian;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Switch;
import android.widget.TabHost;

public class MainActivity extends Activity implements OnClickListener {

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
		/*View header = getLayoutInflater().inflate(R.layout.state_list_view_header, null);*/

		Cursor allStatesCursor = datasource.getAllStates();
		Cursor failureStatesCursor = datasource.getAllFailusresStates();

		// The desired columns to be bound
		String[] columns = new String[] { StatisticTable.COLUMN_SITE_STATE,
				StatisticTable.COLUMN_SITE_STATE, StatisticTable.COLUMN_DATE };

		// the XML defined views which the data will be bound to
		int[] to = new int[] { R.id.icon, R.id.state, R.id.stateDate };

		dataAdapter = new SimpleCursorAdapter(this, R.layout.state_info,
				allStatesCursor, columns, to, 0);
		dataAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {

			@Override
			public boolean setViewValue(View view, Cursor cursor,
					int columnIndex) {
				if (view.getId() == R.id.icon) {
					if (cursor.getString(1).equals(getResources().getString(R.string.busy_state))) 
						((ImageView) view).setImageResource(R.drawable.busy);
					else if ((cursor.getString(1).equals(getResources().getString(R.string.busy_state))))
						((ImageView) view).setImageResource(R.drawable.away);
					else if ((cursor.getString(1).equals(getResources().getString(R.string.online_state))))
						((ImageView) view).setImageResource(R.drawable.online);					
					return true; // true because the data was bound to the view
				}
				return false;
			}
		});

		/*allStateslistView.addHeaderView(header);*/
		allStateslistView.setAdapter(dataAdapter);

		dataAdapter = new SimpleCursorAdapter(this, R.layout.state_info,
				failureStatesCursor, columns, to, 0);
		dataAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {

			@Override
			public boolean setViewValue(View view, Cursor cursor,
					int columnIndex) {
				if (view.getId() == R.id.icon) {
					if (cursor.getString(1).equals(getResources().getString(R.string.busy_state))) {
						((ImageView) view).setImageResource(R.drawable.busy);
					}else if ((cursor.getString(1).equals(getResources().getString(R.string.busy_state))))
						((ImageView) view).setImageResource(R.drawable.away);
					return true; // true because the data was bound to the view
				}
				return false;
			}
		});
		/*failureStateslistView.addHeaderView(header);*/
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
}
