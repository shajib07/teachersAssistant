package entwinebits.com.teachersassistant.server;

import org.json.JSONObject;

/**
 * Created by shajib on 4/3/2017.
 */
public interface ServerResponseListener {

    void onResponseReceived(JSONObject jsonObject);
}
