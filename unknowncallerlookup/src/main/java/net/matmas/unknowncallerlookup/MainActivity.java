package net.matmas.unknowncallerlookup;

import net.matmas.unknowncallerlookup.fields.Field;
import net.matmas.unknowncallerlookup.fields.Fields;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		for (final Field field : Fields.getAll()) {
			field.initActivity(this);
		}

//        Button testBrowser = (Button) findViewById(R.id.test);
//        testBrowser.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, WebActivity.class);
//                intent.putExtra("URL", "http://google.com/");
//                MainActivity.this.startActivity(intent);
//            }
//        });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return false;
	}
}
