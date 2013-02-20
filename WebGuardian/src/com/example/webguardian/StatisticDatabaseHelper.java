package com.example.webguardian;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class StatisticDatabaseHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "statistictable.db";
	private static final int DATABASE_VERSION = 1;

	private static StatisticDatabaseHelper dbHelper = null;
	
	public static StatisticDatabaseHelper getInstance(Context context){
		if (dbHelper==null)
			dbHelper = new StatisticDatabaseHelper(context);
		return dbHelper;
	}
	private StatisticDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Method is called during creation of the database
	@Override
	public void onCreate(SQLiteDatabase database) {
		StatisticTable.onCreate(database);
	}

	// Method is called during an upgrade of the database,
	// e.g. if you increase the database version
	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {
		StatisticTable.onUpgrade(database, oldVersion, newVersion);
	}

}
