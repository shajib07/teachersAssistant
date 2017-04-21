package entwinebits.com.teachersassistant.utils;

import android.content.Context;
import android.content.SharedPreferences;

import entwinebits.com.teachersassistant.App;

/**
 * Created by shajib on 4/20/2017.
 */
public class AppPreferenceHelper {

    private static final String APP_USER_PREFS = "app_User_Prefs";

    private static AppPreferenceHelper instance;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private AppPreferenceHelper() {
        sharedPreferences = App.getContext().getSharedPreferences(APP_USER_PREFS, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    private static AppPreferenceHelper getInstance() {
        if (instance == null){
            instance = new AppPreferenceHelper();
        }
        return instance;
    }

    public static String getString(String key, String defValue) {
        return getInstance().sharedPreferences.getString(key, defValue);
    }

    public static boolean putString(String key, String value) {
        return getInstance().editor.putString(key, value).commit();
    }

    public static int getInt(String key, int defValue) {
        return getInstance().sharedPreferences.getInt(key, defValue);
    }

    public static boolean putInt(String key, int value) {
        return getInstance().editor.putInt(key, value).commit();
    }

    public static boolean getBoolean(String key, boolean defValue) {
        return getInstance().sharedPreferences.getBoolean(key, defValue);
    }

    public static boolean putBoolean(String key, boolean value) {
        return getInstance().editor.putBoolean(key, value).commit();
    }

    public static boolean putLong(String key, long value) {
        return getInstance().editor.putLong(key, value).commit();
    }

    public static long getLong(String key, long defValue) {
        return getInstance().sharedPreferences.getLong(key, defValue);
    }

    public static boolean hasPref(String key){
        return getInstance().sharedPreferences.contains(key);
    }
    public static boolean removePref(String key){
        return getInstance().editor.remove(key).commit();
    }

    public static boolean removeAllData() {
        return getInstance().editor.clear().commit();
    }

}
