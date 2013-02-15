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
		dbHelper = StatisticDatabaseHelper.getInstance(context);
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
        database.insert(StatisticTable.STATISTIC_TABLE, null, values);
	}
	
	public Statistic getLastState(){
		Statistic st = new Statistic();
		Cursor c = database.rawQuery("select " + StatisticTable.COLUMN_SITE_STATE + " , " +
									 StatisticTable.COLUMN_DATE + " FROM " + StatisticTable.STATISTIC_TABLE + 
									 " where " + StatisticTable.COLUMN_ID + " = (select MAX("+StatisticTable.COLUMN_ID +") from " + 
									 StatisticTable.STATISTIC_TABLE + ")", null);
		if (c != null ) {
			if (c.moveToFirst()) {
				st.setState(c.getString(0));
				st.setDate(c.getString(1));
			}
		}
		return st;
	}
}
