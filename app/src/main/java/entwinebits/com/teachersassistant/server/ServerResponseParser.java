package entwinebits.com.teachersassistant.server;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import entwinebits.com.teachersassistant.model.UserProfileDTO;
import entwinebits.com.teachersassistant.utils.HelperMethod;
import entwinebits.com.teachersassistant.utils.ServerConstants;

/**
 * Created by shajib on 2/1/2017.
 */
public class ServerResponseParser {


    private static String TAG = "ServerResponseParser";
    /*
    * 	"users": [{
		"id": 9,
		"fName": "shajib",
		"gndr": null,
		"cty": null,
		"cntry": null,
		"email": "b3@shajibnn",
		"dc": null,
		"phnNum": null,
		"brthDate": 0,
		"insttut": "",
		"usrTyp": 1*/

    public static ArrayList<UserProfileDTO> parseUserSearchResponse(JSONObject response) {
        ArrayList<UserProfileDTO> searchUserList = new ArrayList<>();
        try {
            JSONArray jsonArray = response.getJSONArray(ServerConstants.USERS);
            HelperMethod.debugLog(TAG, "parseUserSearchResponse size "+jsonArray.length());
            UserProfileDTO userProfileDTO;
            for (int i=0; i<jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                userProfileDTO = new UserProfileDTO();
                userProfileDTO.setUserFirstName(jsonObject.optString(ServerConstants.FULL_NAME));
                userProfileDTO.setUserMobilePhone(jsonObject.optString(ServerConstants.PHONE_NUMBER));
                userProfileDTO.setUserInstituteName(jsonObject.optString(ServerConstants.INSTITUTE));
//                userProfileDTO.setUserGender(jsonObject.optBoolean(ServerConstants.GENDER) == true ? 1 : 0);
                searchUserList.add(userProfileDTO);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return searchUserList == null ? new ArrayList<UserProfileDTO>() : searchUserList;
    }
}
