package entwinebits.com.teachersassistant.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import entwinebits.com.teachersassistant.model.UserProfileDTO;

/**
 * Created by shajib on 2/6/2017.
 */
public class UserProfileHelper {

    private static String TAG = "UserProfileHelper";
    private static UserProfileHelper userProfileHelper;
    private Context mContext;
    private UserProfileDTO userProfileDTO;

    private UserProfileHelper(Context context) {
        this.mContext = context;
        this.userProfileDTO = new UserProfileDTO();
    }

    public static UserProfileHelper getInstance(Context context){
        if(userProfileHelper == null){
            userProfileHelper = new UserProfileHelper(context);
        }
        return userProfileHelper;
    }


    public UserProfileDTO getUserProfile(){
        userProfileDTO.setUserFirstName(AppPreferenceHelper.getString(ServerConstants.FULL_NAME, ""));
        userProfileDTO.setUserId(AppPreferenceHelper.getInt(ServerConstants.ID, 0));
        userProfileDTO.setUserMobilePhone(AppPreferenceHelper.getString(ServerConstants.PHONE_NUMBER, ""));
        userProfileDTO.setUserEmail(AppPreferenceHelper.getString(ServerConstants.EMAIL, ""));
        userProfileDTO.setUserGender(AppPreferenceHelper.getInt(ServerConstants.GENDER, 0));
        userProfileDTO.setUserInstituteName(AppPreferenceHelper.getString(ServerConstants.INSTITUTE, ""));
        userProfileDTO.setUserDesignation(AppPreferenceHelper.getString(ServerConstants.DESIGNATION, ""));
        userProfileDTO.setUserAddress(AppPreferenceHelper.getString(ServerConstants.CITY, ""));
        userProfileDTO.setUserCountry(AppPreferenceHelper.getString(ServerConstants.COUNTRY, ""));

        return userProfileDTO;
    }

    public void saveUserProfile(UserProfileDTO userProfileDTO) {

        HelperMethod.debugLog(TAG, userProfileDTO.toString());
        AppPreferenceHelper.putString(ServerConstants.FULL_NAME, userProfileDTO.getUserFirstName());
        AppPreferenceHelper.putInt(ServerConstants.ID, userProfileDTO.getUserId());
        AppPreferenceHelper.putString(ServerConstants.PHONE_NUMBER, userProfileDTO.getUserMobilePhone());
        AppPreferenceHelper.putString(ServerConstants.EMAIL, userProfileDTO.getUserEmail());
        AppPreferenceHelper.putInt(ServerConstants.GENDER, userProfileDTO.getUserGender());
        AppPreferenceHelper.putString(ServerConstants.INSTITUTE, userProfileDTO.getUserInstituteName());
        AppPreferenceHelper.putString(ServerConstants.DESIGNATION, userProfileDTO.getUserDesignation());
        AppPreferenceHelper.putString(ServerConstants.CITY, userProfileDTO.getUserCity());
        AppPreferenceHelper.putString(ServerConstants.COUNTRY, userProfileDTO.getUserCountry());
    }

    public String getUserName() {
        return AppPreferenceHelper.getString(ServerConstants.FULL_NAME, "");
    }
    public long getUserId() {
        return AppPreferenceHelper.getInt(ServerConstants.ID, 0);
    }



}
