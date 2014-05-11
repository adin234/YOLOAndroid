package org.ydd.yolo.utiility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;

import android.os.AsyncTask;
import android.util.Log;

public class GetRequest extends AsyncTask<String, String, String> {
	public PostExecute postexecute;
    @Override
    protected String doInBackground(String... urls) {
            // TODO Auto-generated method stub
            StringBuilder builder = new StringBuilder();
            HttpClient client = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(urls[0]);
            
            try {
                    HttpResponse response = client.execute(httpGet);
                    StatusLine statusLine = response.getStatusLine();
                    int statusCode = statusLine.getStatusCode();
                    if (statusCode == 200) {
                            HttpEntity entity = response.getEntity();
                            InputStream content = entity.getContent();
                            BufferedReader reader = new BufferedReader(
                                            new InputStreamReader(content));
                            String line;
                            while ((line = reader.readLine()) != null) {
                                    builder.append(line);
                            }
                            Log.v("Getter", "Your data: " + builder.toString()); //response data
                    } else {
                            Log.e("Getter", "Failed to download file");
                    }
            } catch (ClientProtocolException e) {
                    e.printStackTrace();
            } catch (IOException e) {
                    e.printStackTrace();
            }
            
            return builder.toString();
    }
    
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        try {
			postexecute.doAfter(result);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}