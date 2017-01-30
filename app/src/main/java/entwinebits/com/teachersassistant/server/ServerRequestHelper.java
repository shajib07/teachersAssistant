package entwinebits.com.teachersassistant.server;

import org.json.JSONObject;

import entwinebits.com.teachersassistant.utils.HelperMethod;
import entwinebits.com.teachersassistant.utils.ServerConstants;

/**
 * Created by shajib on 1/28/2017.
 */
public class ServerRequestHelper {

    public static String TAG = "ServerRequestHelper";

    public static JSONObject sendUserSearchRequest(String query) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(ServerConstants.ACTION, 10);
            jsonObject.put(ServerConstants.USER_TYPE, 1);
            jsonObject.put(ServerConstants.SEARCH_PARAM, query);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

}
