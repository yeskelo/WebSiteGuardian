package com.example.webguardian;

import android.app.Activity;
import android.database.Cursor;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.SimpleCursorAdapter.ViewBinder;

public class CustomViewBinder implements ViewBinder {

	Activity activity;
	
	@Override
	public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
		if (view.getId() == R.id.icon) {
			if (cursor.getString(1).equals(activity.getResources().getString(R.string.busy_state))) 
				((ImageView) view).setImageResource(R.drawable.busy);
			else if ((cursor.getString(1).equals(activity.getResources().getString(R.string.busy_state))))
				((ImageView) view).setImageResource(R.drawable.away);
			else if ((cursor.getString(1).equals(activity.getResources().getString(R.string.online_state))))
				((ImageView) view).setImageResource(R.drawable.online);					
			return true; // true because the data was bound to the view
		} else if (view.getId() == R.id.stateDate){
			((TextView) view).setText((String) DateUtils.getRelativeDateTimeString( activity, Long.valueOf(cursor.getString(2)), DateUtils.MINUTE_IN_MILLIS, DateUtils.WEEK_IN_MILLIS, 0));
			return true;
		}
		return false;
	}

	public CustomViewBinder(Activity activity) {
		this.activity = activity;
	}
}
