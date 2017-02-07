package entwinebits.com.teachersassistant;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
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

import java.util.ArrayList;
import java.util.HashMap;

import entwinebits.com.teachersassistant.adapter.BatchPaymentAdapter;
import entwinebits.com.teachersassistant.db.DatabaseRequestHelper;
import entwinebits.com.teachersassistant.dialogFragment.DoubleItemDialogFragment;
import entwinebits.com.teachersassistant.dialogFragment.SingleItemDialogFragment;
import entwinebits.com.teachersassistant.dialogFragment.YearSelectionDialogFragment;
import entwinebits.com.teachersassistant.listener.DialogCloseListener;
import entwinebits.com.teachersassistant.model.BatchDTO;
import entwinebits.com.teachersassistant.model.PaymentDTO;
import entwinebits.com.teachersassistant.model.PaymentHistoryDTO;
import entwinebits.com.teachersassistant.model.UserProfileDTO;
import entwinebits.com.teachersassistant.utils.Constants;
import entwinebits.com.teachersassistant.utils.HelperMethod;
import entwinebits.com.teachersassistant.utils.ServerConstants;

/**
 * Created by shajib on 1/14/2017.
 */
public class PaymentHistoryActivity extends AppCompatActivity implements View.OnClickListener, DialogCloseListener {
    private String TAG = "PaymentHistoryActivity";
    private FrameLayout payment_history_toolbar_back;
    private TextView payment_history_toolbar_title;
    private ProgressDialog mProgressDialog;

    private LinearLayout from_ll, to_ll;
    private TextView to_month_tv, to_yr_tv, from_month_tv, from_yr_tv;

    private RecyclerView student_payment_history_rv;
    private BatchPaymentAdapter mBatchPaymentAdapter;
    private ArrayList<PaymentHistoryDTO> mPaymentHistoryList;
    private HashMap<String, ArrayList<PaymentHistoryDTO>> mBatchPaymentMap;

    private ArrayList<BatchDTO> mBatchList;
    private ArrayList<String> mBatchNameList;

    private DatabaseRequestHelper mDbRequestHelper;

    private TextView history_batch_tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_history_layout);

        initToolbar();
        initData();
        initLayout();
        loadBatchList();

    }

    private void addPaymentHistoryRequest()
    {
        String message = "";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(ServerConstants.ACTION, 8);//add batch 4 5 for get all batch
            jsonObject.put(ServerConstants.ID, 9);
            jsonObject.put(ServerConstants.BATCH_ID, 1);
            jsonObject.put(ServerConstants.MONTH, 1);//add batch 4 5 for get all batch
            jsonObject.put(ServerConstants.YEAR, 2017);
            jsonObject.put(ServerConstants.AMOUNT, 300);//add batch 4 5 for get all batch
            jsonObject.put(ServerConstants.STATUS, 0);
        }catch (Exception e){}

        final String message1 = jsonObject.toString();
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Constants.REQUEST_URL, jsonObject,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("JSON", response.toString());

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                VolleyLog.d("JSON", "Error: " + error.getMessage());
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
                    HelperMethod.debugLog(TAG, "load == "+batchDTO.getBatchId()+ " name = "+batchDTO.getBatchName());

                    int total_amount = 0;
                    ArrayList<UserProfileDTO> studentList = mDbRequestHelper.getStudentListByBatch((int)batchDTO.getBatchId());
                    HelperMethod.debugLog(TAG, "stu size == "+studentList.size());
                    batchDTO.setStudentDtoList(studentList);

                    for (UserProfileDTO dto : studentList) {
                        ArrayList<PaymentHistoryDTO> tempPaymentList = mDbRequestHelper.getPaymentHistoryByStudent(dto.getUserId());
                        mBatchPaymentMap.put(batchDTO.getBatchName(), tempPaymentList);
                        HelperMethod.debugLog(TAG, "stu  == "+dto.getUserName());
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
        to_ll = (LinearLayout) findViewById(R.id.to_ll);
        to_ll.setOnClickListener(this);
        from_ll = (LinearLayout) findViewById(R.id.from_ll);
        from_ll.setOnClickListener(this);
        from_yr_tv = (TextView) findViewById(R.id.from_yr_tv);
        from_month_tv = (TextView) findViewById(R.id.from_month_tv);
        to_yr_tv = (TextView) findViewById(R.id.to_yr_tv);
        to_month_tv = (TextView) findViewById(R.id.to_month_tv);

        history_batch_tv = (TextView)findViewById(R.id.history_batch_tv);
        history_batch_tv.setOnClickListener(this);

        mBatchPaymentAdapter = new BatchPaymentAdapter(this, mPaymentHistoryList);
        student_payment_history_rv = (RecyclerView) findViewById(R.id.student_payment_history_rv);
        student_payment_history_rv.setLayoutManager(new LinearLayoutManager(this));
        student_payment_history_rv.setAdapter(mBatchPaymentAdapter);
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
        for (int i=0; i < arr.length; i++) {
            list.add(arr[i]);
        }
        return list;
    }

    private ArrayList<String> getYearList() {
        ArrayList<String> list = new ArrayList<>();
        String[] arr = getResources().getStringArray(R.array.Years);
        for (int i=0; i < arr.length; i++) {
            list.add(arr[i]);
        }
        return list;
    }

    private void showSingleItemDialog(int dialogId) {
        SingleItemDialogFragment singleItemDialogFragment = SingleItemDialogFragment.newInstance(dialogId, mBatchNameList);
        singleItemDialogFragment.show(getSupportFragmentManager(), "");
    }

    private void showDateSelectionDialog(int dialogId) {
        FragmentManager fm = getSupportFragmentManager();
        DoubleItemDialogFragment doubleItemDialogFragment = DoubleItemDialogFragment.newInstance(dialogId, getYearList(), getMonthList());
        doubleItemDialogFragment.show(fm, "");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.from_ll:
                showDateSelectionDialog(1);
                break;

            case R.id.to_ll:
                showDateSelectionDialog(2);
                break;

            case R.id.history_batch_tv:

                showSingleItemDialog(0);
                break;

            case R.id.payment_history_toolbar_back:
                PaymentHistoryActivity.this.finish();
                break;
        }
    }


    private void initData() {
        mPaymentHistoryList = new ArrayList<>();
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
    public void onDialogClosed(int dialogId, int dialogState, String year, String month) {
        switch (dialogState) {
            case Constants.DIALOG_STATE_POSITIVE:

                switch (dialogId) {
                    case 0:
                        history_batch_tv.setText(year);
                        for (BatchDTO dto : mBatchList) {
                            if (dto.getBatchName().equals(year)) {
                                mPaymentHistoryList.clear();
                                HelperMethod.debugLog(TAG, "selected : " + year + " size : " + mBatchPaymentMap.get(year).size());
                                mPaymentHistoryList.addAll(mBatchPaymentMap.get(year));
                                mBatchPaymentAdapter.notifyDataSetChanged();
                                break;
                            }
                        }
                        break;

                    case 1:
                        from_month_tv.setText(month);
                        from_yr_tv.setText(year);
                        break;

                    case 2:
                        to_month_tv.setText(month);
                        to_yr_tv.setText(year);
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
