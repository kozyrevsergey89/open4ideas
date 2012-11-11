package com.draft_law_assessor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import com.draft_law_assessor.app.DraftApp;
import com.draft_law_assessor.service.DraftService;
import com.draft_law_assessor.widget.DraftWidget;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;

public class LoginActivity extends Activity {

	private SharedPreferences prefs;
	private Facebook facebook;
	private int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setResult(RESULT_CANCELED);
        setContentView(R.layout.activity_main);

        if (getIntent().getExtras() != null) {
        	appWidgetId = getIntent().getExtras()
        			.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
        					AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        
        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) { finish(); }
        
        prefs = getSharedPreferences(DraftService.DRAFT_PREFS, MODE_PRIVATE);
        facebook = new Facebook(DraftApp.APP_ID);
        manageSession();
    }
    
    
    private void manageSession() {
    	String accessToken = prefs.getString(DraftApp.ACCESS_TOKEN, null);
    	long expires = prefs.getLong(DraftApp.EXPIRES_TIME, 0);
    	if(accessToken != null && expires != 0) {
    		facebook.setAccessToken(accessToken);
    		facebook.setAccessExpires(expires);
    	}
    	
    	if (!facebook.isSessionValid()) {
    		facebook.authorize(this, new DialogListener() {

				@Override
				public void onComplete(final Bundle values) {
					SharedPreferences.Editor editor = prefs.edit();
					editor.putString(DraftApp.ACCESS_TOKEN, facebook.getAccessToken());
					editor.putLong(DraftApp.EXPIRES_TIME, facebook.getAccessExpires());
					editor.commit();
					//setResult();
				}

				@Override
				public void onFacebookError(final FacebookError e) {
					if (DraftApp.DEBUG) { Log.e("ERROR", e.getMessage() + " - facebook error"); }
					finish();
				}

				@Override
				public void onError(final DialogError e) {
					if (DraftApp.DEBUG) { Log.e("ERROR", e.getMessage() + ""); }
					finish();
				}

				@Override
				public void onCancel() { finish(); }
    		});
    	}
    }
    
    private void setResult() {
    	AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
    	ComponentName thisAppWidget = new ComponentName(this.getPackageName(), LoginActivity.class.getName());
    	int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget);
		Intent resultValue = new Intent(this, DraftWidget.class);
		resultValue.setAction("android.appwidget.action.APPWIDGET_UPDATE");
		resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds);
		sendBroadcast(resultValue);
		
		Intent intent = new Intent();
		intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
		setResult(RESULT_OK, intent);
		finish();
    }
    

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	facebook.authorizeCallback(requestCode, resultCode, data);
    	AsyncFacebookRunner runner = new AsyncFacebookRunner(facebook);
		runner.request("me", new MeCallback());
    	setResult();
    	startService(new Intent(this, DraftService.class));
    }
    
	private class MeCallback implements RequestListener {

		@Override
		public void onComplete(final String response, final Object state) {
			Log.i("123123", response);
		}

		@Override
		public void onIOException(final IOException e, final Object state) {
			onError(e.getMessage());
		}

		@Override
		public void onFileNotFoundException(final FileNotFoundException e,
				final Object state) {
			onError(e.getMessage());
		}

		@Override
		public void onMalformedURLException(final MalformedURLException e,
				final Object state) {
			onError(e.getMessage());
		}

		@Override
		public void onFacebookError(final FacebookError e, final Object state) {
			onError(e.getMessage());
		}
		
		private void onError(final String message) {
			if (DraftApp.DEBUG && !TextUtils.isEmpty(message)) { Log.e("ERROR", message); }
		}
		
	}
	
	
    
    
}
