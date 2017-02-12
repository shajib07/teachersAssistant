package entwinebits.com.teachersassistant.server;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import entwinebits.com.teachersassistant.model.BatchDTO;
import entwinebits.com.teachersassistant.model.ScheduleDTO;
import entwinebits.com.teachersassistant.model.UserProfileDTO;
import entwinebits.com.teachersassistant.utils.HelperMethod;
import entwinebits.com.teachersassistant.utils.ServerConstants;

/**
 * Created by shajib on 2/1/2017.
 */
public class ServerResponseParser {
    private static String TAG = "ServerResponseParser";

    /**
     *  {
     "error": false,
     "batches": [{
     "id": 72,
     "ttl": "qer",
     "rtnList": [{
     "dayOfWk": "2",
     "id": "29",
     "strtTm": "10",
     "endTm": "11.3"
     }]
     }]
     }
     * @param response
     * @return
     */
    public static ArrayList<BatchDTO> parseUserBatchListResponse(JSONObject response) {

        ArrayList<BatchDTO> userBatchList = new ArrayList<>();
        try {
            BatchDTO batchDTO = new BatchDTO();

            JSONArray jsonArray = response.getJSONArray(ServerConstants.BATCHES);
            HelperMethod.debugLog(TAG, "parseUserBatchListResponse size "+jsonArray.length());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                batchDTO.setBatchId(jsonObject.optInt(ServerConstants.ID));
                batchDTO.setBatchName(jsonObject.optString(ServerConstants.TITLE));

                ArrayList<ScheduleDTO> routineList = new ArrayList<>();
                JSONArray routineArray = jsonObject.optJSONArray(ServerConstants.ROUTINE_INFLO_LIST);
                for (int j=0; j<routineArray.length(); j++) {
                    ScheduleDTO scheduleDTO = new ScheduleDTO();
                    JSONObject routineObj = (JSONObject) routineArray.get(i);
                    scheduleDTO.setDaysOfWeek(routineObj.optInt(ServerConstants.DAY_OF_WEEK) );
                    scheduleDTO.setScheduleId(routineObj.optInt(ServerConstants.ID));
                    scheduleDTO.setStartTime(routineObj.optString(ServerConstants.START_TIME));
                    scheduleDTO.setEndTime(routineObj.optString(ServerConstants.END_TIME));
                    routineList.add(scheduleDTO);
                }
                batchDTO.setScheduleDTOList(routineList);
                userBatchList.add(batchDTO);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        return userBatchList;

    }

    public static ArrayList<UserProfileDTO> parseUserSearchResponse(JSONObject response) {
        ArrayList<UserProfileDTO> searchUserList = new ArrayList<>();
        try {
            JSONArray jsonArray = response.getJSONArray(ServerConstants.USERS);
            HelperMethod.debugLog(TAG, "parseUserSearchResponse size " + jsonArray.length());
            UserProfileDTO userProfileDTO;
            for (int i = 0; i < jsonArray.length(); i++) {
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
