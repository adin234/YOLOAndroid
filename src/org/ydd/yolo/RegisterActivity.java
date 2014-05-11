package org.ydd.yolo;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ydd.yolo.utiility.Constants;
import org.ydd.yolo.utiility.PostRequest;
import org.ydd.yolo.utiility.PostExecute;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;

public class RegisterActivity extends ActionBarActivity {
	ProgressDialog pDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		pDialog = new ProgressDialog(this);
		setContentView(R.layout.activity_register);
		
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	public void addCircleRow(View arg0) {
		View view = getLayoutInflater().inflate(R.layout.circle_row, null);
		((LinearLayout) findViewById(R.id.lovedOnes)).addView(view);
	}
	
	public void doRegister(View arg0) {
		String number = ((TextView) findViewById(R.id.number)).getText().toString();
		JSONArray lovedOnes = new JSONArray();
		LinearLayout circle = ((LinearLayout) findViewById(R.id.lovedOnes));
		pDialog.setTitle("Loading data");
		pDialog.setMessage("Please wait...");
		pDialog.show();
		for(int i=0; i<circle.getChildCount(); i++) {
			View v = circle.getChildAt(i);
			Boolean continues = true;
			JSONObject jObj = new JSONObject();
			try {
				String circleEmail = ((TextView) v.findViewById(R.id.email)).getText().toString();
				if(!circleEmail.equals("")) {
					jObj.put("email", circleEmail);
				} else {
					continues = false;
				}
			} catch(Exception e) { continues = false; }
			try {
				String circleNo = ((TextView) v.findViewById(R.id.number)).getText().toString();
				if(!circleNo.equals("")) {
					jObj.put("number", circleNo);
					continues = true;
				}
			} catch(Exception e) { continues = false; }
			
			if(continues) {
				lovedOnes.put(jObj);
			}
		}
		
		JSONObject sendData = new JSONObject();
		
		try {
			sendData.put("number", number);
			sendData.put("loved_ones", lovedOnes);
			
			doRegisterJSON(sendData.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void doRegisterJSON(String data) {
		PostRequest postRequest = new PostRequest();
        postRequest.postexecute = new PostExecute() {
			@Override
			public void doAfter(String string) throws JSONException {
	            try {
	            	JSONObject data = new JSONObject(string);
	            	if(!data.getString("username").equals("")) {
	            		pDialog.hide();
	            		startActivity(new Intent(getApplicationContext(), LoginActivity.class));
	            		finish();
	            	}
	            } catch(Exception e){}
			}
		};
		
		postRequest.jsonbody = data;
		Log.d("THIS IS THE DATA", data);
		postRequest.execute(Constants.server+"/register");
        
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
		return true;
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

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_register,
					container, false);
			View view = inflater.inflate(R.layout.circle_row, null);
			((LinearLayout) rootView.findViewById(R.id.lovedOnes)).addView(view);
			return rootView;
		}
	}

}
