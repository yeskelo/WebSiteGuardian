package com.example.webguardian;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class StatisticTable {

	// Database table
	public static final String STATISTIC_TABLE = "statistic";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_SITE_STATE = "site_state";
	public static final String COLUMN_DATE = "date";

	// Database creation SQL statement
	private static final String DATABASE_CREATE = "create table " + STATISTIC_TABLE
			+ "(" + COLUMN_ID + " integer primary key autoincrement, "
			+ COLUMN_SITE_STATE + " text not null, " + COLUMN_DATE
			+ " text not null" + ");";

	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	public static void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {
		Log.d(StatisticTable.class.getName(), "Upgrading database from version "
				+ oldVersion + " to " + newVersion
				+ ", which will destroy all old data");
		database.execSQL("DROP TABLE IF EXISTS " + STATISTIC_TABLE);
		onCreate(database);
	}
}
