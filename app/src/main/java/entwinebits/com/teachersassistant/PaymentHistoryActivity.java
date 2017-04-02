package entwinebits.com.teachersassistant;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import entwinebits.com.teachersassistant.adapter.BatchPaymentAdapter;
import entwinebits.com.teachersassistant.adapter.StudentPaymentHistoryAdapter;
import entwinebits.com.teachersassistant.db.DatabaseRequestHelper;
import entwinebits.com.teachersassistant.dialogFragment.DoubleItemDialogFragment;
import entwinebits.com.teachersassistant.dialogFragment.SingleItemDialogFragment;
import entwinebits.com.teachersassistant.dialogFragment.SingleListDialogFragment;
import entwinebits.com.teachersassistant.dialogFragment.YearSelectionDialogFragment;
import entwinebits.com.teachersassistant.listener.DialogCloseListener;
import entwinebits.com.teachersassistant.model.BatchDTO;
import entwinebits.com.teachersassistant.model.ItemDTO;
import entwinebits.com.teachersassistant.model.PaymentDTO;
import entwinebits.com.teachersassistant.model.PaymentHistoryDTO;
import entwinebits.com.teachersassistant.model.UserProfileDTO;
import entwinebits.com.teachersassistant.server.ServerRequestHelper;
import entwinebits.com.teachersassistant.server.ServerResponseParser;
import entwinebits.com.teachersassistant.utils.ConstantFunctions;
import entwinebits.com.teachersassistant.utils.Constants;
import entwinebits.com.teachersassistant.utils.HelperMethod;
import entwinebits.com.teachersassistant.utils.ServerConstants;
import entwinebits.com.teachersassistant.utils.UserProfileHelper;

/**
 * Created by shajib on 1/14/2017.
 */
public class PaymentHistoryActivity extends AppCompatActivity implements View.OnClickListener, DialogCloseListener {
    private String TAG = "PaymentHistoryActivity";
    private FrameLayout payment_history_toolbar_back;
    private TextView payment_history_toolbar_title;
    private ProgressDialog mProgressDialog;

    private CardView student_search_cardview, batch_search_cardview;
    private TextView search_student_history_tv, search_batch_history_tv;
    private LinearLayout from_ll, to_ll, batch_history_ll;
    private TextView to_month_tv, to_yr_tv, from_month_tv, from_yr_tv, batch_history_month_tv, batch_history_year_tv;
    private TextView history_batch_tv, history_student_tv, batch_history_batch_tv;
    private FrameLayout payment_history_search, batch_history_search;

    //    private RecyclerView student_payment_history_rv;
    private HashMap<String, ArrayList<PaymentHistoryDTO>> mBatchPaymentMap;

    private ArrayList<BatchDTO> mBatchList;
    private ArrayList<String> mBatchNameList;
    private ArrayList<ItemDTO> mBatchItemList;

    private DatabaseRequestHelper mDbRequestHelper;

    private int mMonthFrom, mMonthTo, mYearFrom, mYearTo;
    private Map<Integer, ArrayList<UserProfileDTO>> mBatchStudentMap;
    private Map<Integer, ArrayList<ItemDTO>> mBatchStudentItemMap;

    private int mSelectedBatchId = -1, mSelectedStudentId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_history_layout);

        initToolbar();
        initData();
        initLayout();
//        loadBatchList();
        getUserBatchList();
    }

    private void loadStudentList() {
        JSONObject jsonObject = ServerRequestHelper.sendBatchStudentListRequest(mSelectedBatchId);
        HelperMethod.debugLog(TAG, "loadUserBatchList batch id == "+mSelectedBatchId);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Constants.REQUEST_URL, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        HelperMethod.debugLog(TAG, response.toString());

                        if (!response.optBoolean(ServerConstants.ERROR)) {
                            final ArrayList<UserProfileDTO> studentList = ServerResponseParser.parseBatchStudentListResponse(response);

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    ItemDTO itemDTO;
                                    final ArrayList<ItemDTO> studentItemList = new ArrayList<>();
                                    for (UserProfileDTO dto : studentList) {
                                        itemDTO = new ItemDTO();
                                        itemDTO.setItemName(dto.getUserFirstName());
                                        itemDTO.setItemId(dto.getUserId());
                                        studentItemList.add(itemDTO);
                                    }
                                    mBatchStudentMap.put(mSelectedBatchId, studentList);
                                    mBatchStudentItemMap.put(mSelectedBatchId, studentItemList);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            showSingleListDialog(3, studentItemList);
                                        }
                                    });
                                }
                            }).start();
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjReq);
    }

    private void getBatchPaymentList() {
        JSONObject jsonObject = ServerRequestHelper.sendBatchPaymentListRequest((int) mBatchList.get(0).getBatchId(),
                1, 2016, 0);
        HelperMethod.debugLog(TAG, "getBatchPaymentList req == " + jsonObject.toString());
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Constants.REQUEST_URL, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());

                        if (!response.optBoolean(ServerConstants.ERROR)) {
                            HelperMethod.debugLog(TAG, "getBatchPaymentList = " + response.toString());
//                            mBatchList = ServerResponseParser.parseUserBatchListResponse(response);
//                            new Thread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    HelperMethod.debugLog(TAG, "loadBatchList size = " + mBatchList.size());
//                                }
//                            }).start();
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjReq);
    }

    private void getUserBatchList() {
        long id = UserProfileHelper.getInstance(this).getUserId();
        JSONObject jsonObject = ServerRequestHelper.sendUserBatchListRequest(id);
        HelperMethod.debugLog(TAG, "loadUserBatchList user id == " + id);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Constants.REQUEST_URL, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());

                        if (!response.optBoolean(ServerConstants.ERROR)) {
                            mBatchList = ServerResponseParser.parseUserBatchListResponse(response);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    HelperMethod.debugLog(TAG, "loadBatchList size = " + mBatchList.size());
                                    mBatchNameList = new ArrayList<>();
                                    mBatchItemList = new ArrayList<>();
                                    for (BatchDTO batchDTO : mBatchList) {
                                        ItemDTO dto = new ItemDTO();
                                        dto.setItemId((int)batchDTO.getBatchId());
                                        dto.setItemName(batchDTO.getBatchName());
                                        mBatchItemList.add(dto);
                                        mBatchNameList.add(batchDTO.getBatchName());
                                        HelperMethod.debugLog(TAG, "load == " + batchDTO.getBatchId() + " name = " + batchDTO.getBatchName());
                                    }

                                }
                            }).start();
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjReq);
    }

    private ArrayList<PaymentHistoryDTO> generatePaymentDetailsList(ArrayList<PaymentHistoryDTO> list, int stMonth, int stYear, int endMonth, int endYear) {
        ArrayList<PaymentHistoryDTO> paymentList = new ArrayList<>();

        if (stYear == endYear) {
            for (int monInx = stMonth; monInx <= endMonth; monInx++) {
                addPaymentDetails(paymentList, list, stYear, monInx);
            }
            return paymentList;
        }

        for (int yrInx = stYear; yrInx <= endYear; yrInx++) {
            if (yrInx == stYear) {
                for (int monInx = stMonth; monInx <= 12; monInx++) {
                    addPaymentDetails(paymentList, list, yrInx, monInx);
                }
            } else if (yrInx == endYear){
                for (int monInx = 1; monInx <= endMonth; monInx++) {
                    addPaymentDetails(paymentList, list, yrInx, monInx);                }
            } else {
                for (int monInx = 1; monInx <= 12; monInx++) {
                    addPaymentDetails(paymentList, list, yrInx, monInx);
                }
            }
        }
        return paymentList;
    }


    private void addPaymentDetails(ArrayList<PaymentHistoryDTO> paymentList, ArrayList<PaymentHistoryDTO> list, int yrInx, int monInx) {
        boolean isFound = false;
        PaymentHistoryDTO tempDto = new PaymentHistoryDTO();

        for (PaymentHistoryDTO dto : list) {

            if (dto.getYear() == yrInx && dto.getMonth() == monInx) {
                HelperMethod.debugLog(TAG, "generatePaymentDetailsList "+dto.getYear());
                tempDto = dto;
                isFound = true;
                break;
            }
        }
        if (!isFound) {
            tempDto = new PaymentHistoryDTO(yrInx, monInx);
        }
        paymentList.add(tempDto);
    }

    private void sendStudentPaymentListRequest() {

        showProgressDialog();
        JSONObject jsonObject = ServerRequestHelper.sendGetStudentPaymentListRequest(mSelectedBatchId, mSelectedStudentId, mMonthFrom, mYearFrom, mMonthTo, mYearTo);
        HelperMethod.debugLog(TAG, "sendStudentPaymentListRequest batch id == " + mSelectedBatchId + " stud id == " + mSelectedStudentId);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Constants.REQUEST_URL, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(final JSONObject response) {
                        hideProgressDialog();
                        HelperMethod.debugLog(TAG, response.toString());
                        if (!response.optBoolean(ServerConstants.ERROR)) {

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    ArrayList<PaymentHistoryDTO> paymentHistoryDTOs = ServerResponseParser.parseGetStudentPaymentListRequest(response);

                                    final ArrayList<PaymentHistoryDTO> processedList = generatePaymentDetailsList(paymentHistoryDTOs, mMonthFrom, mYearFrom, mMonthTo, mYearTo);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            HelperMethod.debugLog(TAG, "processedList == "+processedList.size());
                                            Intent paymentDetailsIntent = new Intent(PaymentHistoryActivity.this, PaymentHistoryDetailsActivity.class);
                                            paymentDetailsIntent.putExtra(Constants.BATCH_ID, mSelectedBatchId);
                                            paymentDetailsIntent.putExtra(Constants.STUDENT_ID, mSelectedStudentId);
                                            paymentDetailsIntent.putParcelableArrayListExtra(Constants.PAYMENT_HISTORY_LIST, processedList);
                                            startActivity(paymentDetailsIntent);
                                        }
                                    });
                                }
                            }).start();
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideProgressDialog();
                        HelperMethod.debugLog(TAG, "Error: " + error.getMessage());
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjReq);
    }

    private void sendBatchPaymentListRequest() {

        showProgressDialog();
        JSONObject jsonObject = ServerRequestHelper.sendBatchPaymentListRequest(mSelectedBatchId, mMonthFrom, mYearFrom, 1);
        HelperMethod.debugLog(TAG, "sendStudentPaymentListRequest batch id == " + mSelectedBatchId + " stud id == " + mSelectedStudentId);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Constants.REQUEST_URL, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(final JSONObject response) {
                        hideProgressDialog();
                        HelperMethod.debugLog(TAG, response.toString());
                        if (!response.optBoolean(ServerConstants.ERROR)) {

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    final ArrayList<PaymentHistoryDTO> paymentHistoryDTOs = ServerResponseParser.parseGetBatchPaymentListRequest(response);

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            HelperMethod.debugLog(TAG, "paymentHistoryDTOs == "+paymentHistoryDTOs.size());
                                            Intent paymentDetailsIntent = new Intent(PaymentHistoryActivity.this, BatchPaymentHistoryActivity.class);
                                            paymentDetailsIntent.putExtra(Constants.BATCH_ID, mSelectedBatchId);
                                            paymentDetailsIntent.putParcelableArrayListExtra(Constants.BATCH_PAYMENT_HISTORY_LIST, paymentHistoryDTOs);
                                            startActivity(paymentDetailsIntent);
                                        }
                                    });
                                }
                            }).start();
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideProgressDialog();
                        HelperMethod.debugLog(TAG, "Error: " + error.getMessage());
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjReq);
    }


    private void loadBatchList() {
        showProgressDialog();
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (mDbRequestHelper == null) {
                    mDbRequestHelper = new DatabaseRequestHelper(PaymentHistoryActivity.this);
                }

                mBatchNameList = new ArrayList<>();
                mBatchList = mDbRequestHelper.getBatchList();

                for (BatchDTO batchDTO : mBatchList) {
                    mBatchNameList.add(batchDTO.getBatchName());
                    HelperMethod.debugLog(TAG, "load == " + batchDTO.getBatchId() + " name = " + batchDTO.getBatchName());

                    int total_amount = 0;
                    ArrayList<UserProfileDTO> studentList = mDbRequestHelper.getStudentListByBatch((int) batchDTO.getBatchId());
                    HelperMethod.debugLog(TAG, "stu size == " + studentList.size());
                    batchDTO.setStudentDtoList(studentList);

                    for (UserProfileDTO dto : studentList) {
                        ArrayList<PaymentHistoryDTO> tempPaymentList = mDbRequestHelper.getPaymentHistoryByStudent(dto.getUserId());
                        mBatchPaymentMap.put(batchDTO.getBatchName(), tempPaymentList);
                        HelperMethod.debugLog(TAG, "stu  == " + dto.getUserFirstName());
                    }
//
//                    PaymentDTO paymentDTO = new PaymentDTO();
//                    paymentDTO.setTotalAmount(total_amount);
//                    paymentDTO.setBatchName(batchDTO.getBatchName());
//                    mAllBatchHistoryList.add(paymentDTO);
//                    mSpinnerMap.put(batchDTO.getBatchId(), batchDTO.getBatchName());
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideProgressDialog();
                    }
                });

            }
        }).start();

    }

    private void initLayout() {
        student_search_cardview = (CardView)findViewById(R.id.student_search_cardview);
        batch_search_cardview = (CardView)findViewById(R.id.batch_search_cardview);
        search_student_history_tv = (TextView) findViewById(R.id.search_student_history_tv);
        search_batch_history_tv = (TextView) findViewById(R.id.search_batch_history_tv);

        search_student_history_tv.setOnClickListener(this);
        search_batch_history_tv.setOnClickListener(this);

        to_ll = (LinearLayout) findViewById(R.id.to_ll);
        to_ll.setOnClickListener(this);
        from_ll = (LinearLayout) findViewById(R.id.from_ll);
        from_ll.setOnClickListener(this);
        batch_history_ll = (LinearLayout) findViewById(R.id.batch_history_ll);
        batch_history_ll.setOnClickListener(this);

        from_yr_tv = (TextView) findViewById(R.id.from_yr_tv);
        from_month_tv = (TextView) findViewById(R.id.from_month_tv);
        to_yr_tv = (TextView) findViewById(R.id.to_yr_tv);
        to_month_tv = (TextView) findViewById(R.id.to_month_tv);

        batch_history_month_tv = (TextView) findViewById(R.id.batch_history_month_tv);
        batch_history_year_tv = (TextView) findViewById(R.id.batch_history_year_tv);
        batch_history_batch_tv = (TextView) findViewById(R.id.batch_history_batch_tv);
        batch_history_batch_tv.setOnClickListener(this);

        history_batch_tv = (TextView) findViewById(R.id.history_batch_tv);
        history_batch_tv.setOnClickListener(this);
        history_student_tv = (TextView) findViewById(R.id.history_student_tv);
        history_student_tv.setOnClickListener(this);
        payment_history_search = (FrameLayout) findViewById(R.id.payment_history_search);
        payment_history_search.setOnClickListener(this);
        batch_history_search = (FrameLayout) findViewById(R.id.batch_history_search);
        batch_history_search.setOnClickListener(this);
    }


    private void initToolbar() {
        payment_history_toolbar_title = (TextView) findViewById(R.id.payment_history_toolbar_title);
//        payment_history_toolbar_title.setText(mStudentDTO.getUserName());

        payment_history_toolbar_back = (FrameLayout) findViewById(R.id.payment_history_toolbar_back);
        payment_history_toolbar_back.setOnClickListener(this);
    }

    private ArrayList<String> getMonthList() {
        ArrayList<String> list = new ArrayList<>();
        String[] arr = getResources().getStringArray(R.array.months);
        for (int i = 0; i < arr.length; i++) {
            list.add(arr[i]);
        }
        return list;
    }

    private ArrayList<String> getYearList() {
        ArrayList<String> list = new ArrayList<>();
        String[] arr = getResources().getStringArray(R.array.Years);
        for (int i = 0; i < arr.length; i++) {
            list.add(arr[i]);
        }
        return list;
    }

    private void showSingleItemDialog(int dialogId) {
        SingleItemDialogFragment singleItemDialogFragment = SingleItemDialogFragment.newInstance(dialogId, mBatchNameList);
        singleItemDialogFragment.show(getSupportFragmentManager(), "");
    }

    private void showSingleListDialog(int dialogId, ArrayList<ItemDTO> itemList) {
        if (itemList == null) {
            HelperMethod.debugLog(TAG, "showSingleItemDialog mBatchItemList NUll");
            itemList = new ArrayList<>();
        }
        SingleListDialogFragment singleListDialogFragment = SingleListDialogFragment.newInstance(dialogId, itemList);
        singleListDialogFragment.show(getSupportFragmentManager(), "");
    }

    private void showDateSelectionDialog(int dialogId) {
        FragmentManager fm = getSupportFragmentManager();
        DoubleItemDialogFragment doubleItemDialogFragment = DoubleItemDialogFragment.newInstance(dialogId, getYearList(), getMonthList());
        doubleItemDialogFragment.show(fm, "");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.search_student_history_tv:
                batch_search_cardview.setVisibility(View.GONE);
                student_search_cardview.setVisibility(View.VISIBLE);
                break;

            case R.id.search_batch_history_tv:
                batch_search_cardview.setVisibility(View.VISIBLE);
                student_search_cardview.setVisibility(View.GONE);
                break;

            case R.id.batch_history_ll:
                showDateSelectionDialog(4);
                break;
            case R.id.from_ll:
                showDateSelectionDialog(1);
                break;

            case R.id.to_ll:
                showDateSelectionDialog(2);
                break;

            case R.id.history_student_tv:
                if (mSelectedBatchId < 0) {
                    return;
                }
                if (!mBatchStudentMap.containsKey(mSelectedBatchId)) {
                    loadStudentList();
                    return;
                }
                showSingleListDialog(3, mBatchStudentItemMap.get(mSelectedBatchId));
                break;

            case R.id.history_batch_tv:
                showSingleListDialog(0, mBatchItemList);
                break;

            case R.id.batch_history_batch_tv:
                showSingleListDialog(5, mBatchItemList);
                break;

            case R.id.payment_history_search:
                sendStudentPaymentListRequest();
                break;

            case R.id.batch_history_search:
                sendBatchPaymentListRequest();
                break;

            case R.id.payment_history_toolbar_back:
                PaymentHistoryActivity.this.finish();
                break;
        }
    }


    private void initData() {
        mBatchStudentItemMap = new HashMap<>();
        mBatchStudentMap = new HashMap<>();
        mBatchPaymentMap = new HashMap<>();
    }

    private void loadPaymentHistory(final String batchName, final ArrayList<UserProfileDTO> studentList) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (mDbRequestHelper == null) {
                    mDbRequestHelper = new DatabaseRequestHelper(PaymentHistoryActivity.this);
                }
                for (UserProfileDTO dto : studentList) {
                    ArrayList<PaymentHistoryDTO> tempPaymentList = mDbRequestHelper.getPaymentHistoryByStudent(dto.getUserId());
                    mBatchPaymentMap.put(batchName, tempPaymentList);

                }
            }
        }).start();

    }

    @Override
    public void onDialogClosed(int dialogId, int dialogState, String year, String month, int id) {
        switch (dialogState) {
            case Constants.DIALOG_STATE_POSITIVE:

                switch (dialogId) {
                    case 0:
                        history_batch_tv.setText(year);
                        HelperMethod.debugLog(TAG, "year "+year+" month = "+month+" id "+id);
                        mSelectedBatchId = id;
                        break;

                    case 1:
                        from_month_tv.setText(month);
                        from_yr_tv.setText(year);
                        mMonthFrom = ConstantFunctions.getMonthIntType(month);
                        mYearFrom = Integer.parseInt(year);
                        HelperMethod.debugLog(TAG, "mMonthFrom "+mMonthFrom+" mYearFrom "+mYearFrom);
                        break;

                    case 2:
                        to_month_tv.setText(month);
                        to_yr_tv.setText(year);
                        mMonthTo = ConstantFunctions.getMonthIntType(month);
                        mYearTo = Integer.parseInt(year);
                        HelperMethod.debugLog(TAG, "mMonthTo "+mMonthTo+" mYearTo "+mYearTo);
                        break;

                    case 3:
                        history_student_tv.setText(year);
                        mSelectedStudentId = id;
                        HelperMethod.debugLog(TAG, "year "+year+" month = "+month+" id "+id);
                        break;

                    case 4:
                        batch_history_month_tv.setText(month);
                        batch_history_year_tv.setText(year);
                        mMonthFrom = ConstantFunctions.getMonthIntType(month);
                        mYearFrom = Integer.parseInt(year);
                        HelperMethod.debugLog(TAG, "mMonthFrom "+mMonthFrom+" mYearFrom "+mYearFrom);
                        break;

                    case 5:
                        batch_history_batch_tv.setText(year);
                        HelperMethod.debugLog(TAG, "year "+year+" month = "+month+" id "+id);
                        mSelectedBatchId = id;
                        break;


                }
                break;
            case Constants.DIALOG_STATE_NEGATIVE:

                break;
            case Constants.DIALOG_STATE_NEUTRAL:
                break;
        }
    }

    private void showProgressDialog() {
        if (mProgressDialog == null || !mProgressDialog.isShowing()) {
            mProgressDialog = ProgressDialog.show(PaymentHistoryActivity.this, getString(R.string.loading_data),
                    getString(R.string.please_Wait), true, false);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setIndeterminateDrawable(getResources().getDrawable(R.drawable.custom_progress));
        }
    }

    private void hideProgressDialog() {
        try {
            if (mProgressDialog != null || mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
                mProgressDialog = null;
            }
        } catch (Exception e) {
        }
    }

}
