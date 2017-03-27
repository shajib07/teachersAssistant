package entwinebits.com.teachersassistant;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;

import entwinebits.com.teachersassistant.adapter.PaymentHistoryDetailsAdapter;
import entwinebits.com.teachersassistant.adapter.StudentPaymentHistoryAdapter;
import entwinebits.com.teachersassistant.model.PaymentHistoryDTO;
import entwinebits.com.teachersassistant.server.ServerRequestHelper;
import entwinebits.com.teachersassistant.utils.Constants;
import entwinebits.com.teachersassistant.utils.HelperMethod;
import entwinebits.com.teachersassistant.utils.ServerConstants;
import entwinebits.com.teachersassistant.utils.UserProfileHelper;

/**
 * Created by shajib on 3/25/2017.
 */
public class PaymentHistoryDetailsActivity extends AppCompatActivity implements View.OnClickListener, PaymentHistoryDetailsAdapter.PaymentEditSelectionListener {

    private String TAG = "PaymentHistoryDetailsActivity";
    private FrameLayout payment_details_toolbar_back;
    private TextView payment_details_toolbar_title;
    private NestedScrollView nestedScrollView;

    private PaymentHistoryDetailsAdapter mStudentHistoryAdapter;
    private RecyclerView payment_history_details_rv;

    private ArrayList<PaymentHistoryDTO> mPaymentHistoryList;
    private int mSelectedBatchId, mSelectedStudentId;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pmnt_history_details_layout);
        initToolbar();

        initData();
        if (getIntent().hasExtra(Constants.PAYMENT_HISTORY_LIST)) {
            mPaymentHistoryList = getIntent().getParcelableArrayListExtra(Constants.PAYMENT_HISTORY_LIST);
        }
        mSelectedBatchId = getIntent().getIntExtra(Constants.BATCH_ID, -1);
        mSelectedStudentId = getIntent().getIntExtra(Constants.STUDENT_ID, -1);
        initLayout();
    }

    private void initData() {
    }

    private void initLayout() {
        payment_history_details_rv = (RecyclerView)findViewById(R.id.payment_history_details_rv);
        payment_history_details_rv.setLayoutManager(new LinearLayoutManager(this));
        mStudentHistoryAdapter = new PaymentHistoryDetailsAdapter(this, mPaymentHistoryList);
        mStudentHistoryAdapter.setPaymentEditSelectionListener(this);
        payment_history_details_rv.setAdapter(mStudentHistoryAdapter);
    }

    private void initToolbar() {
        payment_details_toolbar_title = (TextView) findViewById(R.id.payment_details_toolbar_title);

        payment_details_toolbar_back = (FrameLayout) findViewById(R.id.payment_details_toolbar_back);
        payment_details_toolbar_back.setOnClickListener(this);
    }

    @Override
    public void onPaymentEdited(PaymentHistoryDTO dto) {
        showPaymentEditDialog(dto);
    }

    private void sendPaymentAddRequest(final int mSelectedMonth, final int mSelectedYear, final int mAmount) {
        JSONObject jsonObject = ServerRequestHelper.sendAddPaymentHistoryRequest(mSelectedStudentId, mSelectedBatchId, mSelectedMonth, mSelectedYear, mAmount, true);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Constants.REQUEST_URL, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        HelperMethod.debugLog(TAG, response.toString());
                        if (!response.optBoolean(ServerConstants.ERROR)) {
                            Toast.makeText(PaymentHistoryDetailsActivity.this, "Payment Added Successfully.", Toast.LENGTH_LONG).show();
                            mStudentHistoryAdapter.notifyDataAdapter(mSelectedYear, mSelectedMonth, mAmount);
                        } else {
                            if (response.optInt(ServerConstants.REASON_CODE) == 1) {
                                Toast.makeText(PaymentHistoryDetailsActivity.this, "Payment Already Exist.", Toast.LENGTH_LONG).show();
                                int preAmount = response.optInt(ServerConstants.AMOUNT);
//                                mPaymentId = response.optInt(ServerConstants.PAYMENT_ID);
//                                showPaymentExistDialog(preAmount, mAmount);
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        HelperMethod.debugLog(TAG, "Error: " + error.getMessage());
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjReq);
    }

    private void sendPaymentUpdateRequest(final PaymentHistoryDTO dto, final int curAmount) {
//        int id = (int) UserProfileHelper.getInstance(this).getUserId();
        JSONObject jsonObject = ServerRequestHelper.sendPaymentUpdateRequest(mSelectedStudentId, dto.getPaymentId(),
                dto.getMonth(), dto.getYear(), curAmount, 1);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Constants.REQUEST_URL, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        HelperMethod.debugLog(TAG, response.toString());
                        if (!response.optBoolean(ServerConstants.ERROR)) {
                            HelperMethod.debugLog(TAG, "sendUpdateRequest sucsss");
                            Toast.makeText(PaymentHistoryDetailsActivity.this, "Payment Updated Successfully", Toast.LENGTH_SHORT).show();
                            mStudentHistoryAdapter.notifyDataAdapter(dto.getYear(), dto.getMonth(), curAmount);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        HelperMethod.debugLog(TAG, "Error: " + error.getMessage());
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjReq);
    }
    public void showPaymentEditDialog(final PaymentHistoryDTO dto) {
        try {
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setContentView(R.layout.payment_edit_dialog_layout);
            dialog.setCancelable(true);

            final TextView dialog_title = (TextView) dialog.findViewById(R.id.dialog_title);
            dialog_title.setText("Please Choose");

            HelperMethod.debugLog(TAG, "dto "+dto.getPaidAmount()+ " "+dto.getMonth()+" "+dto.getYear()+ " payId "+dto.getPaymentId());
            final EditText edit_payment_paid_et = (EditText) dialog.findViewById(R.id.edit_payment_paid_et);
            edit_payment_paid_et.setHint(""+dto.getPaidAmount());

            EditText edit_payment_due_et = (EditText) dialog.findViewById(R.id.edit_payment_due_et);
            edit_payment_due_et.setHint("0");

            final Button dialog_ok_btn = (Button) dialog.findViewById(R.id.dialog_ok_btn);
            dialog_ok_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String paidAmountStr = edit_payment_paid_et.getText().toString();
                    if (paidAmountStr.length() < 1) {
                        return;
                    }
                    int paidAmount = Integer.parseInt(paidAmountStr);
                    int payId = dto.getPaymentId();
                    if (payId < 0) {
                        sendPaymentAddRequest(dto.getMonth(), dto.getYear(), paidAmount);
                    } else {
                        sendPaymentUpdateRequest(dto, paidAmount);
                    }
                    dialog.dismiss();
                }
            });

            final Button dialog_cancel_btn = (Button) dialog.findViewById(R.id.dialog_cancel_btn);
            dialog_cancel_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();

        } catch (Exception e) {
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.payment_details_toolbar_back:
                PaymentHistoryDetailsActivity.this.finish();
        }
    }
}
