package com.example.webguardian;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class StatisticDatasource {
	// Database fields
	private SQLiteDatabase database;
	private StatisticDatabaseHelper dbHelper;
	private String[] allColumns = { StatisticTable.COLUMN_ID,
			StatisticTable.COLUMN_SITE_STATE, StatisticTable.COLUMN_DATE };

	public StatisticDatasource(Context context) {
		dbHelper = new StatisticDatabaseHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public void addRow(String siteStatus){

		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		ContentValues values = new ContentValues();
		values.put(StatisticTable.COLUMN_SITE_STATE, siteStatus);
		values.put(StatisticTable.COLUMN_DATE, dateFormat.format(cal.getTime()));
        long insertId = database.insert(StatisticTable.STATISTIC_TABLE, null, values);
	    Cursor cursor = database.query(StatisticTable.STATISTIC_TABLE,
			        allColumns, StatisticTable.COLUMN_ID + " = " + insertId, null,
			        null, null, null);
	    cursor.moveToFirst();
	    Statistic newRow = cursorToStatistic(cursor);
	    cursor.close();		
	}
	
	  private Statistic cursorToStatistic(Cursor cursor) {
		    Statistic statistic = new Statistic();
		    statistic.setId(cursor.getLong(0));
		    statistic.setState(cursor.getString(1));
		    statistic.setDate(cursor.getString(2));
		    return statistic;
		  }
}
