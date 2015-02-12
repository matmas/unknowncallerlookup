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
}
