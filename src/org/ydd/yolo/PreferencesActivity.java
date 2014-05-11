package org.ydd.yolo;

import org.ydd.yolo.services.MyService;
import org.ydd.yolo.utiility.AppPreferences;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.Toast;
import android.os.Build;

public class PreferencesActivity extends ActionBarActivity {
	AppPreferences pref;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_preferences);
		pref = new AppPreferences(getApplicationContext());
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, PlaceholderFragment.newInstance(Integer.parseInt(pref.get("interval_index", "3")))).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.preferences, menu);
		return true;
	}
	
	public void updatePreferences(View arg0) {
		Spinner n = ((Spinner) findViewById(R.id.spinner1));
		String value = getResources().getStringArray(R.array.interval_values)[n.getSelectedItemPosition()];
//		pref.set("interval", value);
//		pref.set("interval_index", ""+n.getSelectedItemPosition());
		
		Log.d("NUMBER", "asdf "+pref.get("number"));
		Log.d("INTERVAL", "asdf "+pref.get("interval"));
		Intent intent=new Intent(this, MyService.class);
        stopService(intent);
        startService(intent);
		Toast.makeText(getApplicationContext(), "Successfully updated preference", Toast.LENGTH_LONG).show();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		int section;
		
		public PlaceholderFragment() {
		}
		
		public static PlaceholderFragment newInstance(int someInt) {
			PlaceholderFragment myFragment = new PlaceholderFragment();
			myFragment.section = someInt;
		    return myFragment;
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_preferences,
					container, false);

			((Spinner) rootView.findViewById(R.id.spinner1)).setSelection(section);
			
			return rootView;
		}
	}

}
