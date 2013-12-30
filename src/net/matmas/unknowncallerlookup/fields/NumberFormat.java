package net.matmas.unknowncallerlookup.fields;

import net.matmas.unknowncallerlookup.R;
import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;

public class NumberFormat extends Field {

	public static String RAW = "raw";
	public static String FORMATTED = "formatted";
	public static String FORMATTED_REDUCED = "formatted_reduced";

	@Override
	public int getId() {
		return R.id.number_format;
	}

	@Override
	public String getPreferenceKey() {
		return "number_format";
	}

	@Override
	public String getDefaultValue() {
		return RAW;
	}

	private String positionToValue(int position) {
		if (position == 0) {
			return RAW;
		}
		if (position == 1) {
			return FORMATTED;
		}
		if (position == 2) {
			return FORMATTED_REDUCED;
		}
		return RAW;
	}

	private int valueToPosition(String value) {
		if (value.equals(RAW)) {
			return 0;
		}
		if (value.equals(FORMATTED)) {
			return 1;
		}
		if (value.equals(FORMATTED_REDUCED)) {
			return 2;
		}
		return 0;
	}

	@Override
	public void initActivity(final Activity activity) {
		final Spinner spinner = (Spinner) activity.findViewById(this.getId());

		spinner.setSelection(valueToPosition(this.load())); // load

		// save and trigger validation
		this.save(positionToValue(spinner.getSelectedItemPosition()), spinner);

		final Field this_ = this;
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				this_.save(positionToValue(pos), spinner);
			}

			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
	}

}
