package entwinebits.com.teachersassistant.utils;

import android.util.Log;

/**
 * Created by shajib on 8/26/2016.
 */
public class HelperMethod {


    private static boolean isDebugOn = true;

    public static void debugLog(String logTag, String s) {
        if (isDebugOn) {
            Log.d(logTag, logTag + " ->" + s);
        }
    }

    public static void errorLog(String logTag, String s) {
        if (isDebugOn) {
            Log.e(logTag, logTag + " ->" + s);
        }
    }


}
