package entwinebits.com.teachersassistant.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by shajib on 2/6/2017.
 */
public class ConstantFunctions {

    private static String TAG = "ConstantFunctions";

    public static String getDate(long milliSeconds, String dateFormat) {
        HelperMethod.debugLog(TAG, "getDate : "+milliSeconds);
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    public static int getMonthIntType(String month) {
        int val = -1;
        try {
            Date date = new SimpleDateFormat("MMM").parse(month);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            val = cal.get(Calendar.MONTH) + 1;
            HelperMethod.debugLog(TAG, " val = " +val);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return val;
    }


    public static boolean isInternetConnected(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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
