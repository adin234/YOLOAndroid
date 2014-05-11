package org.ydd.yolo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ydd.yolo.utiility.AppPreferences;
import org.ydd.yolo.utiility.Constants;
import org.ydd.yolo.utiility.GetRequest;
import org.ydd.yolo.utiility.PostExecute;
import org.ydd.yolo.utiility.PostRequest;
import org.ydd.yolo.utiility.PutRequest;

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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.os.Build;

public class UpdateCircleActivity extends ActionBarActivity {
	AppPreferences pref;
	ProgressDialog pDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_circle);
		pref = new AppPreferences(getApplicationContext());
		pDialog = new ProgressDialog(this);
		
		pDialog.setTitle("Please Wait");
		pDialog.setMessage("Loading data...");
		pDialog.show();
		
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
		
		getCircle();
	}
	
	public void addCircleRow(View arg0) {
		View view = getLayoutInflater().inflate(R.layout.circle_row, null);
		((LinearLayout) findViewById(R.id.lovedOnes)).addView(view);
	}
	
	public void addCircleRow(String email, String number) {
		View view = getLayoutInflater().inflate(R.layout.circle_row, null);
		((EditText) view.findViewById(R.id.email)).setText(email);
		((EditText) view.findViewById(R.id.number)).setText(number);
		((LinearLayout) findViewById(R.id.lovedOnes)).addView(view);
	}

	private void getCircle() {
		// TODO Auto-generated method stub
		GetRequest req = new GetRequest();
		req.postexecute = new PostExecute() {
			
			@Override
			public void doAfter(String string) throws JSONException {
				// TODO Auto-generated method stub
				JSONObject obj = new JSONObject(string);
				JSONArray arr = obj.getJSONArray("loved_ones");
				
				pDialog.hide();
				
				for(int i=0; i<arr.length(); i++) {
					String email = "";
					String number = "";
					
					try {
						number = arr.getJSONObject(i).getString("number");
					} catch(Exception e) {}
					try {
						email = arr.getJSONObject(i).getString("email");
					} catch(Exception e) {}
					
					addCircleRow(email, number);
				}
			}
		};
		
		req.execute(Constants.server+"/user?number="+pref.get("number"));
	}
	
	public void updateCircle(View arg0) {
		JSONArray lovedOnes = new JSONArray();
		LinearLayout circle = ((LinearLayout) findViewById(R.id.lovedOnes));
		
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
		
		JSONObject data = new JSONObject();
		try {
			data.put("number", pref.get("number"));
			data.put("loved_ones", lovedOnes);			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		PutRequest postRequest = new PutRequest();
        postRequest.postexecute = new PostExecute() {
			@Override
			public void doAfter(String string) throws JSONException {
	            try {
	            	JSONObject data = new JSONObject(string);
	            	if(!data.getString("username").equals("")) {
	            		startActivity(new Intent(getApplicationContext(), LoginActivity.class));
	            		finish();
	            	}
	            } catch(Exception e){}
			}
		};
		
		postRequest.jsonbody = data.toString();
		Log.d("THIS IS THE DATA", data.toString());
		postRequest.execute(Constants.server+"/update");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.update_circle, menu);
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
			View rootView = inflater.inflate(R.layout.fragment_update_circle,
					container, false);
			return rootView;
		}
	}

}
