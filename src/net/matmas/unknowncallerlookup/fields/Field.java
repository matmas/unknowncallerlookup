package net.matmas.unknowncallerlookup.fields;

import net.matmas.unknowncallerlookup.App;
import android.content.SharedPreferences;
import android.widget.EditText;

public abstract class Field {
	public abstract int getId();
	public abstract String getPreferenceKey();
	public abstract String getDefaultValue();
	public abstract boolean validate(String value);
	public abstract String getValidationFailedMessage();
	
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
	
	public void save(String value, EditText editText) {
		if (validate(value)) {
			editText.setError(null); // okay
		}
		else {
			editText.setError(getValidationFailedMessage());
			return;
		}
		
		getPreferences().edit().putString(getPreferenceKey(), value).commit();
	}
	
	private SharedPreferences getPreferences() {
		return App.getContext().getSharedPreferences("Search", 0);
	}
}
