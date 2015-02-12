package net.matmas.unknowncallerlookup;

import android.app.ActivityManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.telephony.PhoneNumberUtils;

import net.matmas.unknowncallerlookup.fields.NumberFormat;
import net.matmas.unknowncallerlookup.fields.SearchSuffix;
import net.matmas.unknowncallerlookup.fields.UrlPrefix;

import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.List;

/**
 * Created by matmas on 5/4/14.
 */
public class Utils {

    public static boolean isPhoneNumber(String number) {
        if (number == null) {
            return false;
        }
        if (number.equals("")) {
            return false;
        }
        if (number.trim().toLowerCase().equals("unknown")) {
            return false;
        }
        if (number.matches(".*\\d.*")) {
            return true;
        }
        return false;
    }

    public static String formatNumberUserPreference(String number) {
        String numberFormat = new NumberFormat().loadValidOrDefault();
        if (numberFormat.equals(NumberFormat.RAW)) {
            return number;
        }
        else if (numberFormat.equals(NumberFormat.FORMATTED)) {
            return PhoneNumberUtils.formatNumber(number);
        }
        else if (numberFormat.equals(NumberFormat.FORMATTED_REDUCED)) {
            number = PhoneNumberUtils.formatNumber(number);
            if (!number.startsWith("+") && !number.startsWith("00")) {
                int firstDashIndex = number.indexOf("-");
                number = PhoneNumberUtils.stripSeparators(number);
                if (firstDashIndex != -1) {
                    number = number.substring(0, firstDashIndex) + "-" + number.substring(firstDashIndex, number.length());
                }
            }
            return number;
        }
        return number;
    }

    public static boolean doesContactExist(String number) {
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

    // incomingNumber must be a phone number (isPhoneNumber must return true)
    public static String constructPhoneNumberSearchUrl(String incomingNumber) {
        String prefix = new UrlPrefix().loadValidOrDefault(); // in case people managed to save invalid data using previous version of this app
        String suffix = new SearchSuffix().loadValidOrDefault();

        String URL = prefix + URLEncoder.encode(Utils.formatNumberUserPreference(incomingNumber));
        if ( !suffix.equals("")) {
            URL += URLEncoder.encode(" " + suffix);
        }
        return URL;
    }

    public interface TopActivityHandler {
        public boolean onTopActivityChanged(String topActivityClassName);
        public void onTimeout();
    }

    public static void monitorTopActivity(final TopActivityHandler topActivityHandler, final long timeoutMillis) {
        final int MSG_ID_CHECK_TOP_ACTIVITY = 1;
        final long DELAY_INTERVAL = 100;

        new Handler() {
            String currentTopActivityName = "";
            long started = 0;

            @Override
            public void handleMessage(Message msg) {
                if (msg.what == MSG_ID_CHECK_TOP_ACTIVITY) {

                    if (started == 0) {
                        started = System.currentTimeMillis();
                    }

                    String topActivityName = getTopActivityClassName();

                    // currentTopActivityName might be null
                    if ( !topActivityName.equals(currentTopActivityName)) {
                        currentTopActivityName = topActivityName;

                        if (topActivityHandler.onTopActivityChanged(currentTopActivityName)) {
                            return;
                        }
                    }

                    if (System.currentTimeMillis() - started > timeoutMillis) {
                        // Timeout occurred
                        topActivityHandler.onTimeout();
                        return;
                    }

                    this.sendEmptyMessageDelayed(MSG_ID_CHECK_TOP_ACTIVITY, DELAY_INTERVAL);
                }
            }
        }.sendEmptyMessage(MSG_ID_CHECK_TOP_ACTIVITY);
    }

    private static String getTopActivityClassName() {
        ActivityManager activityManager = (ActivityManager) App.getContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(1);
        return tasks.get(0).topActivity.getClassName();
    }

//    public static void expandNotificationPanel() {
//        try {
//            Object service = App.getContext().getSystemService("statusbar");
//            Class<?> statusBarManager = Class.forName("android.app.StatusBarManager");
//            Method expand = null;
//            try {
//                expand = statusBarManager.getMethod("expand");
//            } catch (NoSuchMethodException ex) {
//                expand = statusBarManager.getMethod("expandNotificationsPanel");
//            }
//            expand.setAccessible(true);
//            expand.invoke(service);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }
}
