package net.matmas.unknowncallerlookup;

import net.matmas.unknowncallerlookup.fields.Field;
import net.matmas.unknowncallerlookup.fields.Fields;
import android.app.Activity;
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
		
		for (final Field field : Fields.getAll()) {
			final EditText editText = (EditText) findViewById(field.getId());
			
			editText.setText(field.load()); // load
			field.save(editText.getText().toString(), editText); // save and trigger validation
			
			editText.addTextChangedListener(new TextWatcher() { // monitor changes
				public void onTextChanged(CharSequence text, int arg1, int arg2, int arg3) {
					field.save(text.toString(), editText);
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
}
