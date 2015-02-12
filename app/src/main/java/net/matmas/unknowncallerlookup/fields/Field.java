package net.matmas.unknowncallerlookup.fields;

import net.matmas.unknowncallerlookup.App;
import android.app.Activity;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

public abstract class Field {
	public abstract int getId();
	public abstract String getPreferenceKey();
	public abstract String getDefaultValue();
	
	public boolean validate(String value) {
		return true; // anything will pass unless overridden
	}

	public String getValidationFailedMessage() {
		return "";
	}
	
	public String load() {
		String defaultValue = getDefaultValue();
		String key = getPreferenceKey();
		return getPreferences().getString(key, defaultValue);
	}
	
	public String loadValidOrDefault() {
		String value = load();
		if (validate(value)) {
			return value;
		}
		else {
			return getDefaultValue();
		}
	}
	
	public void save(String value, View view) {
		if (view instanceof EditText) {
			EditText editText = (EditText) view;
			if (validate(value)) {
				editText.setError(null); // okay
			}
			else {
				editText.setError(getValidationFailedMessage());
				return;
			}
		}
		getPreferences().edit().putString(getPreferenceKey(), value).commit();
	}
	
	private SharedPreferences getPreferences() {
		return App.getContext().getSharedPreferences("Search", 0);
	}
	
	public void initActivity(Activity activity) {
		final EditText editText = (EditText) activity.findViewById(this.getId());
		
		editText.setText(this.load()); // load
		this.save(editText.getText().toString(), editText); // save and trigger validation
		
		editText.addTextChangedListener(new TextWatcher() { // monitor changes
			public void onTextChanged(CharSequence text, int arg1, int arg2, int arg3) {
				Field.this.save(text.toString(), editText);
			}
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
			}
			public void afterTextChanged(Editable arg0) {
			}
		});
	}
}
