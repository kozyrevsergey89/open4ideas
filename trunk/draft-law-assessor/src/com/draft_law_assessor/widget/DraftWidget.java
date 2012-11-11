package com.draft_law_assessor.widget;

import com.draft_law_assessor.service.DraftService;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

public class DraftWidget extends AppWidgetProvider {
	
	
	
	@Override
	public void onUpdate(final Context context, final AppWidgetManager appWidgetManager,
			final int[] appWidgetIds) {
		context.startService(new Intent(context, DraftService.class));
		//super.onUpdate(context, appWidgetManager, appWidgetIds);
	}

	@Override
	public void onReceive(final Context context, final Intent intent) {
		super.onReceive(context, intent);
	}
	
}
