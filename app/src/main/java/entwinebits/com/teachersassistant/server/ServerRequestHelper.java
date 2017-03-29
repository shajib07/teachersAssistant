package entwinebits.com.teachersassistant.server;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import entwinebits.com.teachersassistant.model.PaymentDTO;
import entwinebits.com.teachersassistant.model.PaymentHistoryDTO;
import entwinebits.com.teachersassistant.model.UserProfileDTO;
import entwinebits.com.teachersassistant.utils.HelperMethod;
import entwinebits.com.teachersassistant.utils.ServerConstants;
import entwinebits.com.teachersassistant.utils.UserProfileHelper;

/**
 * Created by shajib on 1/28/2017.
 */
public class ServerRequestHelper {

    public static String TAG = "ServerRequestHelper";

    public static JSONObject sendAddNewStudentRequest(int batchId, ArrayList<UserProfileDTO> addedStudentList, ArrayList<UserProfileDTO> existStudentList) {
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put(ServerConstants.ACTION, ServerConstants.ACTION_ADD_NEW_STUDENT);
            jsonObject.put(ServerConstants.BATCH_ID, batchId);

            JSONArray addedStudentJson = new JSONArray();
            for (UserProfileDTO dto : addedStudentList) {
                JSONObject jobj = new JSONObject();
                jobj.put(ServerConstants.FULL_NAME, dto.getUserFirstName());
                jobj.put(ServerConstants.EMAIL, dto.getUserEmail());
                jobj.put(ServerConstants.PASSWORD, dto.getUserPwd());
                jobj.put(ServerConstants.GENDER, 1);
                jobj.put(ServerConstants.USER_TYPE, 1);
                jobj.put(ServerConstants.INSTITUTE, dto.getUserMobilePhone());
                jobj.put(ServerConstants.PHONE_NUMBER, dto.getUserInstituteName());
                addedStudentJson.put(jobj);
            }
            jsonObject.put(ServerConstants.NEW_USER_LIST, addedStudentJson);

            JSONArray jsonExistArray = new JSONArray();
            jsonObject.put(ServerConstants.EXIST_USER_LIST, jsonExistArray);

            HelperMethod.debugLog(TAG, "sendAddNewStudentRequest : "+jsonObject.toString());
        } catch (Exception e) {
        }
        return jsonObject;
    }

    public static JSONObject sendStudentRemoveRequest(int batchId, int studentId) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(ServerConstants.ACTION, ServerConstants.ACTION_REMOVE_STUDENT_FROM_BATCH);
            jsonObject.put(ServerConstants.BATCH_ID, batchId);
            jsonObject.put(ServerConstants.ID, studentId);

            HelperMethod.debugLog(TAG, "sendStudentRemoveRequest : "+jsonObject.toString());
        } catch (Exception e) {
        }
        return jsonObject;
    }

    public static JSONObject sendUpdateUserInfoRequest(UserProfileDTO dto) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(ServerConstants.ACTION, ServerConstants.ACTION_UPDATE_USER_INFO);
            jsonObject.put(ServerConstants.ID, dto.getUserId());
            jsonObject.put(ServerConstants.FULL_NAME, dto.getUserFirstName());
//            jsonObject.put(ServerConstants.MONTH, monthFrom);
//            jsonObject.put(ServerConstants.YEAR, yearFrom);
//            jsonObject.put(ServerConstants.MONTHEND, monthTo);
//            jsonObject.put(ServerConstants.YEAREND, yearTo);

            HelperMethod.debugLog(TAG, "sendUpdateUserInfoRequest : "+jsonObject.toString());
        } catch (Exception e) {
        }
        return jsonObject;
    }

    public static JSONObject sendGetUserInfoRequest(int userId) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(ServerConstants.ACTION, ServerConstants.ACTION_GET_USER);
            jsonObject.put(ServerConstants.ID, userId);
            HelperMethod.debugLog(TAG, "sendGetUserInfoRequest : "+jsonObject.toString());
        } catch (Exception e) {
        }
        return jsonObject;
    }

    public static JSONObject sendUserSearchRequest(String query) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(ServerConstants.ACTION, ServerConstants.ACTION_SEARCH_USER_BY_TYPE);
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
            HelperMethod.debugLog(TAG, "sendUserBatchListRequest : "+jsonObject.toString());
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

    public static JSONObject sendBatchPaymentListRequest(int batchId, int month, int year, int userType) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(ServerConstants.ACTION, ServerConstants.ACTION_GET_PAYMENT_LIST);
            jsonObject.put(ServerConstants.BATCH_ID, batchId);
            jsonObject.put(ServerConstants.MONTH, month);
            jsonObject.put(ServerConstants.YEAR, year);
            jsonObject.put(ServerConstants.USER_TYPE, userType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static JSONObject sendPaymentUpdateRequest(int id, int payId, int month, int year, int amount, int status) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(ServerConstants.ACTION, ServerConstants.ACTION_UPDATE_STUDENT_PAYMENT_HISTORY);
            jsonObject.put(ServerConstants.ID, id);
            jsonObject.put(ServerConstants.PAYMENT_ID, payId);
            jsonObject.put(ServerConstants.MONTH, month);
            jsonObject.put(ServerConstants.YEAR, year);
            jsonObject.put(ServerConstants.AMOUNT, amount);
            jsonObject.put(ServerConstants.STATUS, status);
            HelperMethod.debugLog(TAG, "sendPaymentUpdateRequest "+jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static JSONObject sendGetStudentPaymentListRequest(int batch_id, int student_id, int monthFrom, int yearFrom,
                                                              int monthTo, int yearTo) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(ServerConstants.ACTION, ServerConstants.ACTION_GET_STUDENT_PAYMENT_LIST);
            jsonObject.put(ServerConstants.BATCH_ID, batch_id);
            jsonObject.put(ServerConstants.ID, student_id);
            jsonObject.put(ServerConstants.MONTH, monthFrom);
            jsonObject.put(ServerConstants.YEAR, yearFrom);
            jsonObject.put(ServerConstants.MONTHEND, monthTo);
            jsonObject.put(ServerConstants.YEAREND, yearTo);

            HelperMethod.debugLog(TAG, "sendGetStudentPaymentListRequest : "+jsonObject.toString());
        } catch (Exception e) {
        }
        return jsonObject;
    }

    public static JSONObject sendAddPaymentListRequest(int batch_id, ArrayList<PaymentHistoryDTO> paymentList) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(ServerConstants.ACTION, ServerConstants.ACTION_ADD_STUDENT_PAYMENT_LIST);
            jsonObject.put(ServerConstants.BATCH_ID, batch_id);
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
            HelperMethod.debugLog(TAG, "sendAddPaymentListRequest : "+jsonObject.toString());
        } catch (Exception e) {
        }
        return jsonObject;
    }


    public static JSONObject sendAddPaymentHistoryRequest(int user_id, int batch_id, int month, int year, int amount, boolean status) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(ServerConstants.ACTION, ServerConstants.ACTION_ADD_PAYMENT_HISTORY);//add batch 4 5 for get all batch
            jsonObject.put(ServerConstants.ID, user_id);
            jsonObject.put(ServerConstants.BATCH_ID, batch_id);
            jsonObject.put(ServerConstants.MONTH, month);//add batch 4 5 for get all batch
            jsonObject.put(ServerConstants.YEAR, year);
            jsonObject.put(ServerConstants.AMOUNT, amount);//add batch 4 5 for get all batch
            jsonObject.put(ServerConstants.STATUS, status);
            HelperMethod.debugLog(TAG, "sendAddPaymentHistoryRequest = "+jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

//    public static JSONObject sendAddPaymentHistoryRequest(int user_id, int batch_id, int month, int year, int amount, boolean status) {
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put(ServerConstants.ACTION, 8);//add batch 4 5 for get all batch
//            jsonObject.put(ServerConstants.ID, user_id);
//            jsonObject.put(ServerConstants.BATCH_ID, batch_id);
//            jsonObject.put(ServerConstants.MONTH, month);//add batch 4 5 for get all batch
//            jsonObject.put(ServerConstants.YEAR, year);
//            jsonObject.put(ServerConstants.AMOUNT, amount);//add batch 4 5 for get all batch
//            jsonObject.put(ServerConstants.STATUS, status);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return jsonObject;
//    }


}
