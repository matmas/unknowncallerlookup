package net.matmas.unknowncallerlookup.receivers;

import java.net.URLEncoder;

import net.matmas.unknowncallerlookup.App;
import net.matmas.unknowncallerlookup.MainActivity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class CallReceiver extends BroadcastReceiver {

	private static String TAG = CallReceiver.class.toString();
	private static long last = 0;
	private static long interval = 1000;

	@Override
	public void onReceive(Context context, Intent intent) {
		final String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);      
		final String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
		if (TelephonyManager.EXTRA_STATE_RINGING.equals(state) && incomingNumber != null && !"".equals(incomingNumber)) {
			Log.d(TAG, "extra state ringing");
			onRingingOnce(incomingNumber);
		}
		else {
			TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			telephonyManager.listen(new PhoneStateListener() {
	            public void onCallStateChanged(int state, String incomingNumber) {
	                super.onCallStateChanged(state, incomingNumber);
	                Log.d(TAG, "on call state changed");
	                if (state == TelephonyManager.CALL_STATE_RINGING && incomingNumber != null && !"".equals(incomingNumber)) {
                		onRingingOnce(incomingNumber);
	                }
	            }
	        }, PhoneStateListener.LISTEN_CALL_STATE);
		}
	}
	
	private void onRingingOnce(String incomingNumber) {
		Log.d(TAG, "on ringing once");
		if (last + interval < System.currentTimeMillis() || last > System.currentTimeMillis()) { // system time may be changed 
			onRinging(incomingNumber);
			last = System.currentTimeMillis();
		}
	}
	
	private void onRinging(final String incomingNumber) {
		Log.d(TAG, "on ringing");
    	if ( !doesContactExist(incomingNumber)) {
			new Handler().postDelayed(new Runnable() {
				@SuppressWarnings("deprecation")
				public void run() {
					String URL = "https://www.google.com/search?q=" + URLEncoder.encode(incomingNumber);
					String suffix = MainActivity.loadSearchSuffix();
					if ( !suffix.equals("")) {
						URL += URLEncoder.encode(" " + suffix);
					}
					Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL));
					browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // because we are not calling it from an activity
					Log.d(TAG, "starting activity");
					App.getContext().startActivity(browserIntent);
				}
		 	}, 1000);
		}
	}
	
	private boolean doesContactExist(String number) {
	    Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
	    ContentResolver contentResolver = App.getContext().getContentResolver();
	    Cursor contactLookup = contentResolver.query(uri, new String[] { BaseColumns._ID }, null, null, null);
	    String contactId = null;
	    try {
	        if (contactLookup != null && contactLookup.getCount() > 0) {
	            contactLookup.moveToNext();
	            contactId = contactLookup.getString(contactLookup.getColumnIndex(BaseColumns._ID));
	        }
	    } finally {
	        if (contactLookup != null) {
	            contactLookup.close();
	        }
	    }
	    return contactId != null;
	}
	
}