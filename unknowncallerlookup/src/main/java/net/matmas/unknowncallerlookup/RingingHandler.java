package net.matmas.unknowncallerlookup;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.List;

/**
 * Created by matmas on 5/4/14.
 */
public class RingingHandler {

    public static void onRinging(final Context context, final String incomingNumber) {
        if ( !Utils.doesContactExist(incomingNumber)) {
            Utils.monitorTopActivity(new Utils.TopActivityHandler() {
                @Override
                public boolean onTopActivityChanged(String topActivityClassName) {
                    Log.i("top activity changed", topActivityClassName);
                    if (topActivityClassName.equals("com.android.incallui.InCallActivity")) {
                        openBrowserInSec(context, incomingNumber);
                        return true;
                    }
                    return false;
                }

                @Override
                public void onTimeout() {
                    Log.i("top activity", "timeout");
                    openBrowserInSec(context, incomingNumber);
                }
            }, 5000);
		}
    }

    private static void openBrowserInSec(final Context context, final String incomingNumber) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                openBrowser(context, incomingNumber);
            }
        }, 1000);
    }

    private static void openBrowser(Context context, String incomingNumber) {
        Intent intent = new Intent(context, WebActivity.class);

        String URL = Utils.constructPhoneNumberSearchUrl(incomingNumber);
        intent.putExtra("URL", URL);

        // This is a mandatory flag when we don't have a parent activity
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(intent);
    }

}
