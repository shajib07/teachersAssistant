package entwinebits.com.teachersassistant;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import entwinebits.com.teachersassistant.adapter.BatchPaymentAdapter;
import entwinebits.com.teachersassistant.db.DatabaseRequestHelper;
import entwinebits.com.teachersassistant.dialogFragment.SingleItemDialogFragment;
import entwinebits.com.teachersassistant.listener.DialogCloseListener;
import entwinebits.com.teachersassistant.model.BatchDTO;
import entwinebits.com.teachersassistant.model.PaymentDTO;
import entwinebits.com.teachersassistant.model.PaymentHistoryDTO;
import entwinebits.com.teachersassistant.model.UserProfileDTO;
import entwinebits.com.teachersassistant.utils.Constants;
import entwinebits.com.teachersassistant.utils.HelperMethod;

/**
 * Created by shajib on 1/14/2017.
 */
public class PaymentHistoryActivity extends AppCompatActivity implements View.OnClickListener, DialogCloseListener {
    private String TAG = "PaymentHistoryActivity";
    private FrameLayout payment_history_toolbar_back;
    private TextView payment_history_toolbar_title;
    private ProgressDialog mProgressDialog;

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

    private void showSingleItemDialog() {
        SingleItemDialogFragment singleItemDialogFragment = SingleItemDialogFragment.newInstance("SingleItemDialogFragment", mBatchNameList);
        singleItemDialogFragment.show(getSupportFragmentManager(), "");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.history_batch_tv:

                showSingleItemDialog();
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
    public void onDialogClosed(int dialogState, String year, String month) {
        switch (dialogState) {
            case Constants.DIALOG_STATE_POSITIVE:
                history_batch_tv.setText(year);
                for (BatchDTO dto : mBatchList) {
                    if (dto.getBatchName().equals(year)) {
                        mPaymentHistoryList.clear();
                        HelperMethod.debugLog(TAG, "selected : "+year+" size : "+mBatchPaymentMap.get(year).size());
                        mPaymentHistoryList.addAll(mBatchPaymentMap.get(year));
                        mBatchPaymentAdapter.notifyDataSetChanged();
                        break;
                    }
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
