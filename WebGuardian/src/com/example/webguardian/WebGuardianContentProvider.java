package com.example.webguardian;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class WebGuardianContentProvider extends ContentProvider {

	// database
	private StatisticDatasource database;

	// Used for the UriMacher
	private static final int STATISTICS = 1;
	private static final int STATISTIC_ID = 2;

	private static final String AUTHORITY = "com.example.webguardian.contentprovider";

	private static final String BASE_PATH = "STATISTICS";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
			+ "/" + BASE_PATH);

	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
			+ "/statistics";
	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
			+ "/statistic";

	private static final UriMatcher sURIMatcher = new UriMatcher(
			UriMatcher.NO_MATCH);
	static {
		sURIMatcher.addURI(AUTHORITY, BASE_PATH, STATISTICS);
		sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", STATISTIC_ID);
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		int uriType = sURIMatcher.match(uri);
		long id = 0;
		switch (uriType) {
		case STATISTICS:
			id = database.addRow((String) values
					.get(StatisticTable.COLUMN_SITE_STATE));
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return Uri.parse(BASE_PATH + "/" + id);
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		queryBuilder.setTables(StatisticTable.STATISTIC_TABLE);
		switch (sURIMatcher.match(uri)) {
		case STATISTICS:
			break;
		case STATISTIC_ID:
			queryBuilder.appendWhere(StatisticTable.COLUMN_ID + "="	+ uri.getLastPathSegment());
			break;
		default:
			throw new IllegalArgumentException("Unsuported URI " + uri);
		}
		String orderBy = TextUtils.isEmpty(sortOrder) ? " desc"	: sortOrder;
		Cursor cursor = queryBuilder.query(database.getDatabase(), projection, selection, selectionArgs, null, null, orderBy);
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		return cursor;
	}

	@Override
	public int delete(Uri arg0, String arg1, String[] arg2) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getType(Uri arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean onCreate() {
		database = new StatisticDatasource(getContext());
		database.open();
		return false;
	}

	@Override
	public int update(Uri arg0, ContentValues arg1, String arg2, String[] arg3) {
		throw new UnsupportedOperationException();
	}

}
