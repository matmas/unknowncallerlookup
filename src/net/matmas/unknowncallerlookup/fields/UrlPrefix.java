package net.matmas.unknowncallerlookup.fields;

import net.matmas.unknowncallerlookup.App;
import net.matmas.unknowncallerlookup.R;

public class UrlPrefix extends Field {

	@Override
	public int getId() {
		return R.id.url_prefix;
	}

	@Override
	public String getPreferenceKey() {
		return "url_prefix";
	}

	@Override
	public String getDefaultValue() {
		return "https://www.google.com/search?q=";
	}

	@Override
	public boolean validate(String value) {
		if (value.startsWith("http://") || value.startsWith("https://")) {
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public String getValidationFailedMessage() {
		return App.getContext().getString(R.string.url_prefix_must_be_url);
	}

}
