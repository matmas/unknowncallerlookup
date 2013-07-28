package net.matmas.unknowncallerlookup;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.widget.EditText;

public class MainActivity extends Activity {

	public static String SEARCH_SUFFIX = "search_suffix";
	public static String URL_PREFIX = "url_prefix";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Map<String, Integer> fields = new HashMap<String, Integer>();
		fields.put(SEARCH_SUFFIX, R.id.search_suffix);
		fields.put(URL_PREFIX, R.id.url_prefix);
		
		for (final String key : fields.keySet()) {
			int id = fields.get(key);
			final EditText searchSuffix = (EditText) findViewById(id);
			
			searchSuffix.setText(load(key));
			searchSuffix.addTextChangedListener(new TextWatcher() {
				public void onTextChanged(CharSequence searchPattern, int arg1, int arg2, int arg3) {
					save(key, searchPattern.toString());
				}
				public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				}
				public void afterTextChanged(Editable arg0) {
				}
			});
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return false;
	}

	public static String load(String what) {
		String defaultValue = "";
		if (what.equals(URL_PREFIX)) {
			defaultValue = "https://www.google.com/search?q=";
		}
		return getPreferences().getString(what, defaultValue);
	}
	
	private void save(String what, String value) {
		getPreferences().edit().putString(what, value).commit();
	}
	
	private static SharedPreferences getPreferences() {
		return App.getContext().getSharedPreferences("Search", 0);
	}
}
