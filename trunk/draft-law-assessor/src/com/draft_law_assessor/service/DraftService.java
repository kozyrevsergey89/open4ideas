package com.draft_law_assessor.service;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.gson.Gson;

import android.app.IntentService;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

public class DraftService extends IntentService {

	public static final String DRAFT_SERVICE = "com.draft_low_assessor";
	public static final String DRAFT_PREFS = "draft_prefs";
	
	public DraftService() {
		super(DRAFT_SERVICE);
	}
	
	@Override
	protected void onHandleIntent(final Intent intent) {
		new KozyrevServerRequest().execute("http://192.168.2.104:8080/");
	}
	
	private class KozyrevServerRequest extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(final String... params) {
			HttpClient httpClient = new DefaultHttpClient();
			HttpResponse response;
			String resString = null;
			try {
				response = httpClient.execute(new HttpGet(params[0]));
				StatusLine statusLine = response.getStatusLine();
				if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					response.getEntity().writeTo(out);
					resString = out.toString();
				} else {
					response.getEntity().getContent().close();
					throw new IOException(statusLine.getReasonPhrase());
				}
			}catch (final ClientProtocolException ex) {
				Log.e("123123", ex.getMessage() + "");
			} catch (IOException e) {
				Log.e("123123", e.getMessage() + "");
			}
			return resString;
		}
		
		@Override
		protected void onPostExecute(final String result) {
			super.onPostExecute(result);
			InputStream source = new ByteArrayInputStream(result.getBytes());
			Gson gson = new Gson();
			Reader reader = new InputStreamReader(source);
			DraftList list = gson.fromJson(reader, DraftList.class);
			List<DraftModel> release = list.getDraftList();
		}
		
	}

}
