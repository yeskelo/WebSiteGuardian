package com.example.webguardian;

import android.app.Activity;
import android.os.Bundle;

public class SetPrefActivity extends Activity {
	 @Override
	 protected void onCreate(Bundle savedInstanceState) {
	  // TODO Auto-generated method stub
	  super.onCreate(savedInstanceState);
	  
	  getFragmentManager().beginTransaction().replace(android.R.id.content,
	                new UserPrefsFragment()).commit();
	 }
}
