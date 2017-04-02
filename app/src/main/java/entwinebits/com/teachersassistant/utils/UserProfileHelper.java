package entwinebits.com.teachersassistant.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by shajib on 2/6/2017.
 */
public class UserProfileHelper {

    private static UserProfileHelper userProfileHelper;
    private Context mContext;

    private UserProfileHelper(Context context) {
        this.mContext = context;
    }

    public static UserProfileHelper getInstance(Context context){
        if(userProfileHelper == null){
            userProfileHelper = new UserProfileHelper(context);
        }
        return userProfileHelper;
    }

    public long getUserId() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        long id = preferences.getLong(Constants.APP_USER_ID, 0);
        return id;
    }



}
