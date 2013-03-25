package net.matmas.unknowncallerlookup;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.widget.EditText;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		final EditText searchPattern = (EditText) findViewById(R.id.searchSuffix);
		searchPattern.setText(loadSearchSuffix());
		
		searchPattern.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence searchPattern, int arg1, int arg2, int arg3) {
				saveSearchSuffix(searchPattern.toString());
			}
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
			}
			public void afterTextChanged(Editable arg0) {
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return false;
	}

	public static String loadSearchSuffix() {
		return getPreferences().getString("search_suffix", "");
	}
	
	private void saveSearchSuffix(String searchPattern) {
		getPreferences().edit().putString("search_suffix", searchPattern).commit();
	}
	
	private static SharedPreferences getPreferences() {
		return App.getContext().getSharedPreferences("Search", 0);
	}
}
