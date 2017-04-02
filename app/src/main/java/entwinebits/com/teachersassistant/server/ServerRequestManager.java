package entwinebits.com.teachersassistant.server;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import entwinebits.com.teachersassistant.model.PaymentDTO;
import entwinebits.com.teachersassistant.model.PaymentHistoryDTO;
import entwinebits.com.teachersassistant.utils.Constants;
import entwinebits.com.teachersassistant.utils.HelperMethod;
import entwinebits.com.teachersassistant.utils.ServerConstants;

/**
 * Created by shajib on 3/8/2017.
 */
public class ServerRequestManager {

    public static String TAG = "ServerRequestManager";
    private Context mContext;
    private ServerResponseListener serverResponseListener;

    public ServerRequestManager(Context context) {
        this.mContext = context;
    }

    public ServerResponseListener getServerResponseListener() {
        return serverResponseListener;
    }

    public void setServerResponseListener(ServerResponseListener serverResponseListener) {
        this.serverResponseListener = serverResponseListener;
    }

    public JSONObject getUserInfo(JSONObject jsonObject) {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Constants.REQUEST_URL, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            HelperMethod.debugLog(TAG, response.toString());
                            if (!response.optBoolean(ServerConstants.ERROR)) {

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        HelperMethod.debugLog(TAG, "Error: " + error.getMessage());
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(jsonObjReq);



        return jsonObject;
    }

    public static ArrayList<PaymentHistoryDTO> processGetStudentPaymentListRequest(Context context, JSONObject jsonObject) {
        final ArrayList<PaymentHistoryDTO> paymentHistoryList = new ArrayList<>();
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Constants.REQUEST_URL, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            HelperMethod.debugLog(TAG, response.toString());
                            if (!response.optBoolean(ServerConstants.ERROR)) {

                                JSONArray jsonArray = (JSONArray) response.getJSONArray(ServerConstants.PAYMENT_LIST);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    PaymentHistoryDTO dto = new PaymentHistoryDTO();
                                    JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
//                                    dto.setPaidAmount(jsonObject1.optInt(ServerConstants.PAYMENT_ID));
                                    dto.setMonth(jsonObject1.optInt(ServerConstants.MONTH));
                                    dto.setYear(jsonObject1.optInt(ServerConstants.YEAR));
                                    dto.setPaidAmount(jsonObject1.optInt(ServerConstants.AMOUNT));
                                    dto.setPaid(jsonObject1.optInt(ServerConstants.STATUS) == 1 ? true : false);
                                    paymentHistoryList.add(dto);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        HelperMethod.debugLog(TAG, "Error: " + error.getMessage());
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonObjReq);

        return paymentHistoryList;
    }
}
