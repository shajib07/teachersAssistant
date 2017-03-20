package entwinebits.com.teachersassistant.utils;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by shajib on 2/6/2017.
 */
public class ConstantFunctions {

    private static String TAG = "ConstantFunctions";
    public static String getDate(long milliSeconds, String dateFormat)
    {
        HelperMethod.debugLog(TAG, "getDAte : "+milliSeconds);
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }


    public static void getUserId() {

//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
//        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
//        editor.putLong(Constants.APP_USER_ID, mAppUserId);
//        editor.putBoolean(Constants.ALREADY_LOGGED_IN, true);
//        editor.commit();
//
    }
}
