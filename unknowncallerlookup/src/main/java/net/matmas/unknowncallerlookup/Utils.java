package net.matmas.unknowncallerlookup;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.telephony.PhoneNumberUtils;

import net.matmas.unknowncallerlookup.fields.NumberFormat;
import net.matmas.unknowncallerlookup.fields.SearchSuffix;
import net.matmas.unknowncallerlookup.fields.UrlPrefix;

import java.lang.reflect.Method;
import java.net.URLEncoder;

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
