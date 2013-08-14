package net.matmas.unknowncallerlookup.fields;

import net.matmas.unknowncallerlookup.R;

public class SearchSuffix extends Field {

	@Override
	public int getId() {
		return R.id.search_suffix;
	}

	@Override
	public String getPreferenceKey() {
		return "search_suffix";
	}

	@Override
	public String getDefaultValue() {
		return "";
	}

	@Override
	public boolean validate(String value) {
		return true; // any string will do
	}

	@Override
	public String getValidationFailedMessage() {
		return ""; // we don't have any
	}

}
