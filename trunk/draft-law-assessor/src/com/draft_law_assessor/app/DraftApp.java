package com.draft_law_assessor.app;

import com.facebook.android.Facebook;

import android.app.Application;

public class DraftApp extends Application {

	public static final String APP_ID = "458083000905284";
	public static final String ACCESS_TOKEN = "access_token";
	public static final String EXPIRES_TIME = "access_expires";
	public static final boolean DEBUG = true;
	
	private Facebook facebook;
	
	@Override
	public void onCreate() {
		super.onCreate();
		facebook = new Facebook(APP_ID);
	}
	
	public Facebook getFacebookInst() { return facebook; }
	
}
