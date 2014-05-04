package net.matmas.unknowncallerlookup.receivers;

import net.matmas.unknowncallerlookup.RingingHandler;
import net.matmas.unknowncallerlookup.Utils;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

@SuppressLint("DefaultLocale")
public class CallReceiver extends BroadcastReceiver {

	private static String TAG = CallReceiver.class.toString();
	private static long last = 0;

    @Override
	public void onReceive(final Context context, Intent intent) {
		final String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);      
		final String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
        Log.d(TAG, "onReceive state " + state + " phone number " + incomingNumber);

		if (TelephonyManager.EXTRA_STATE_RINGING.equals(state) && Utils.isPhoneNumber(incomingNumber)) {
			onRinging(context, incomingNumber);
		}
		else {
			TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			telephonyManager.listen(new PhoneStateListener() {
	            public void onCallStateChanged(int state, String incomingNumber) {
	                super.onCallStateChanged(state, incomingNumber);
	                if (state == TelephonyManager.CALL_STATE_RINGING && Utils.isPhoneNumber(incomingNumber)) {
                        onRinging(context, incomingNumber);
	                }
	            }
	        }, PhoneStateListener.LISTEN_CALL_STATE);
		}
	}
	
	private void onRinging(Context context, String incomingNumber) {
        long INTERVAL = 1000;
        if (System.currentTimeMillis() - last > INTERVAL || System.currentTimeMillis() - last < 0) { // system time may be changed
			RingingHandler.onRinging(context, incomingNumber);
			last = System.currentTimeMillis();
		}
	}
}