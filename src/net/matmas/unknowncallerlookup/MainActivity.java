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
			final EditText editText = (EditText) findViewById(id);
			
			editText.setText(load(key)); // load
			save(key, editText.getText().toString()); // save and trigger validation
			
			editText.addTextChangedListener(new TextWatcher() { // monitor changes
				public void onTextChanged(CharSequence text, int arg1, int arg2, int arg3) {
					save(key, text.toString());
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
		if (what.equals(URL_PREFIX)) {
			EditText urlPrefix = (EditText) findViewById(R.id.url_prefix);
			if (value.startsWith("http://") || value.startsWith("https://")) {
				urlPrefix.setError(null); // okay
			}
			else {
				urlPrefix.setError(getString(R.string.url_prefix_must_be_url));
				return;
			}
		}
		getPreferences().edit().putString(what, value).commit();
	}
	
	private static SharedPreferences getPreferences() {
		return App.getContext().getSharedPreferences("Search", 0);
	}
}
