package net.matmas.unknowncallerlookup;

import android.content.Context;

public class App extends android.app.Application {
	
	private static Context context;
	
	public void onCreate() {
		super.onCreate();
		App.context = getApplicationContext();
	}
	
	public static Context getContext() {
		return App.context;
	}
}