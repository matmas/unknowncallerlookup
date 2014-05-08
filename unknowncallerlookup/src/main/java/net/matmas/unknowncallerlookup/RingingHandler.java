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

    private static final int MSG_ID_CHECK_TOP_ACTIVITY = 1;
    private static final long DELAY_INTERVAL = 100;
    private static final long TIMEOUT = 5000;

    public static void onRinging(final Context context, final String incomingNumber) {
        if ( !Utils.doesContactExist(incomingNumber)) {

            new Handler() {
                String currentTopActivityName = "";
                long started = 0;

                @Override
                public void handleMessage(Message msg) {
                    if (msg.what == MSG_ID_CHECK_TOP_ACTIVITY) {

                        if (started == 0) {
                            started = System.currentTimeMillis();
                        }

                        ActivityManager activityManager = (ActivityManager) App.getContext().getSystemService(Context.ACTIVITY_SERVICE);

                        List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(1);
                        String topActivityName = tasks.get(0).topActivity.getClassName();

                        // currentTopActivityName might be null
                        if ( !topActivityName.equals(currentTopActivityName)) {
                            currentTopActivityName = topActivityName;

                            if (currentTopActivityName.equals("com.android.incallui.InCallActivity")) {
                                openBrowserInSec(context, incomingNumber);
                                return;
                            }
                        }

                        if (System.currentTimeMillis() - started > TIMEOUT) {
                            // Timeout occurred
                            openBrowserInSec(context, incomingNumber);
                            return;
                        }

                        this.sendEmptyMessageDelayed(MSG_ID_CHECK_TOP_ACTIVITY, DELAY_INTERVAL);
                    }
                }
            }.sendEmptyMessage(MSG_ID_CHECK_TOP_ACTIVITY);
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
