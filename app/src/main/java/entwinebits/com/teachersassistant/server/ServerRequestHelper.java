package entwinebits.com.teachersassistant.server;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import entwinebits.com.teachersassistant.model.PaymentDTO;
import entwinebits.com.teachersassistant.model.PaymentHistoryDTO;
import entwinebits.com.teachersassistant.utils.HelperMethod;
import entwinebits.com.teachersassistant.utils.ServerConstants;
import entwinebits.com.teachersassistant.utils.UserProfileHelper;

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

    public static JSONObject sendUserBatchListRequest(long userId) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(ServerConstants.ACTION, ServerConstants.ACTION_GET_USER_ALL_BATCHES);
            jsonObject.put(ServerConstants.ID, userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static JSONObject sendBatchStudentListRequest(long batchId) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(ServerConstants.ACTION, ServerConstants.ACTION_GET_BATCH_STUDENTS);
            jsonObject.put(ServerConstants.BATCH_ID, batchId);
        } catch (Exception e) {

            e.printStackTrace();
        }
        return jsonObject;
    }

    public static JSONObject sendAddPaymentListRequest(ArrayList<PaymentHistoryDTO> paymentList) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(ServerConstants.ACTION, 15);
            jsonObject.put("btchId", 71);
            JSONArray array = new JSONArray();

            for (PaymentHistoryDTO dto : paymentList) {
                JSONObject obj = new JSONObject();
                obj.put(ServerConstants.ID, dto.getStudentId());
                obj.put(ServerConstants.MONTH, dto.getMonth());
                obj.put(ServerConstants.YEAR, dto.getYear());
                obj.put(ServerConstants.AMOUNT, dto.getPaidAmount());
                obj.put(ServerConstants.STATUS, dto.isPaid());
                array.put(obj);
            }
            jsonObject.put("payList", array);
        } catch (Exception e) {
        }
        return jsonObject;
    }


    public static JSONObject sendAddPaymentHistoryRequest(int user_id, int batch_id, int month, int year, int amount, boolean status) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(ServerConstants.ACTION, 8);//add batch 4 5 for get all batch
            jsonObject.put(ServerConstants.ID, user_id);
            jsonObject.put(ServerConstants.BATCH_ID, batch_id);
            jsonObject.put(ServerConstants.MONTH, month);//add batch 4 5 for get all batch
            jsonObject.put(ServerConstants.YEAR, year);
            jsonObject.put(ServerConstants.AMOUNT, amount);//add batch 4 5 for get all batch
            jsonObject.put(ServerConstants.STATUS, status);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


}
