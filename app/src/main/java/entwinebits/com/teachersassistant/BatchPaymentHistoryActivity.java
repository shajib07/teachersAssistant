package entwinebits.com.teachersassistant;

import android.app.Dialog;
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

import entwinebits.com.teachersassistant.adapter.BatchPaymentHistoryAdapter;
import entwinebits.com.teachersassistant.adapter.PaymentHistoryDetailsAdapter;
import entwinebits.com.teachersassistant.model.PaymentHistoryDTO;
import entwinebits.com.teachersassistant.server.ServerRequestHelper;
import entwinebits.com.teachersassistant.utils.Constants;
import entwinebits.com.teachersassistant.utils.HelperMethod;
import entwinebits.com.teachersassistant.utils.ServerConstants;

/**
 * Created by shajib on 3/29/2017.
 */
public class BatchPaymentHistoryActivity extends AppCompatActivity implements View.OnClickListener, PaymentHistoryDetailsAdapter.PaymentEditSelectionListener {

    private String TAG = "BatchPaymentHistoryActivity";
    private FrameLayout batch_history_toolbar_back;
    private TextView batch_history_toolbar_title;

    private BatchPaymentHistoryAdapter mBatchPaymentHistoryAdapter;
    private RecyclerView batch_payment_history_rv;

    private ArrayList<PaymentHistoryDTO> mBatchPaymentList;
    private int mSelectedBatchId, mSelectedStudentId;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batch_pmnt_history_layout);
        initToolbar();
        if (getIntent().hasExtra(Constants.BATCH_PAYMENT_HISTORY_LIST)) {
            mBatchPaymentList = getIntent().getParcelableArrayListExtra(Constants.BATCH_PAYMENT_HISTORY_LIST);
        }
        mSelectedBatchId = getIntent().getIntExtra(Constants.BATCH_ID, -1);

        initLayout();
    }

    private void initLayout() {
        batch_payment_history_rv = (RecyclerView)findViewById(R.id.batch_payment_history_rv);
        batch_payment_history_rv.setLayoutManager(new LinearLayoutManager(this));
        mBatchPaymentHistoryAdapter = new BatchPaymentHistoryAdapter(this, mBatchPaymentList);
        mBatchPaymentHistoryAdapter.setPaymentEditSelectionListener(this);
        batch_payment_history_rv.setAdapter(mBatchPaymentHistoryAdapter);
    }


    private void initToolbar() {
        batch_history_toolbar_title = (TextView) findViewById(R.id.batch_history_toolbar_title);
        batch_history_toolbar_title.setText("Batch Payment History");

        batch_history_toolbar_back = (FrameLayout) findViewById(R.id.batch_history_toolbar_back);
        batch_history_toolbar_back.setOnClickListener(this);
    }

    private void sendPaymentUpdateRequest(final PaymentHistoryDTO dto, final int curAmount) {
        JSONObject jsonObject = ServerRequestHelper.sendPaymentUpdateRequest(dto.getUserId(), dto.getPaymentId(),
                dto.getMonth(), dto.getYear(), curAmount, 1);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Constants.REQUEST_URL, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        HelperMethod.debugLog(TAG, response.toString());
                        if (!response.optBoolean(ServerConstants.ERROR)) {
                            HelperMethod.debugLog(TAG, "sendUpdateRequest sucsss");
                            Toast.makeText(BatchPaymentHistoryActivity.this, "Payment Updated Successfully", Toast.LENGTH_SHORT).show();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    for (PaymentHistoryDTO paymentHistoryDTO : mBatchPaymentList) {
                                        if (paymentHistoryDTO.getUserId() == dto.getUserId()) {
                                            paymentHistoryDTO.setPaidAmount(curAmount);
                                            break;
                                        }
                                    }
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            mBatchPaymentHistoryAdapter.notifyDataSetChanged();
                                        }
                                    });
                                }
                            }).start();
//                            mStudentHistoryAdapter.notifyDataAdapter(dto.getYear(), dto.getMonth(), curAmount);
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

                    sendPaymentUpdateRequest(dto, paidAmount);
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

            case R.id.batch_history_toolbar_back:
                BatchPaymentHistoryActivity.this.finish();
                break;
        }
    }

    @Override
    public void onPaymentEdited(PaymentHistoryDTO dto) {
        showPaymentEditDialog(dto);
    }
}
