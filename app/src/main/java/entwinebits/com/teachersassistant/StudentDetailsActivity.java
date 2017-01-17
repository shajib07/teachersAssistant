package entwinebits.com.teachersassistant;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import entwinebits.com.teachersassistant.adapter.StudentPaymentHistoryAdapter;
import entwinebits.com.teachersassistant.db.DatabaseRequestHelper;
import entwinebits.com.teachersassistant.dialogFragment.YearSelectionDialogFragment;
import entwinebits.com.teachersassistant.listener.DialogCloseListener;
import entwinebits.com.teachersassistant.listener.PaymentUpdateListener;
import entwinebits.com.teachersassistant.model.PaymentDTO;
import entwinebits.com.teachersassistant.model.PaymentHistoryDTO;
import entwinebits.com.teachersassistant.model.UserProfileDTO;
import entwinebits.com.teachersassistant.utils.Constants;
import entwinebits.com.teachersassistant.utils.HelperMethod;

/**
 * Created by shajib on 12/23/2016.
 */
public class StudentDetailsActivity extends AppCompatActivity implements View.OnClickListener,
        DialogCloseListener, PaymentUpdateListener {

    private String TAG = "EditPaymentHistoryActivity";
    private RecyclerView student_payment_history_rv;
    private StudentPaymentHistoryAdapter historyAdapter;
    private FrameLayout student_details_toolbar_back;
    private TextView student_details_toolbar_title;
    private ProgressDialog mProgressDialog;

    private Spinner student_details_history_spinner;
    private UserProfileDTO mStudentDTO;

    private TextView student_name_tv, student_mobile_phn_tv, student_monthly_fee_tv, student_institute_tv, student_address_tv;
    private LinearLayout edit_history_ll;
    private DatabaseRequestHelper mDatabaseRequestHelper;
    private ArrayList<PaymentHistoryDTO> mPaymentHistoryList;
    private int mShowHistoryYear = 2017;
    private int mBatchId;
    private LinearLayout history_showing_yr_ll;
    private TextView history_showing_yr_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        HelperMethod.debugLog(TAG, "StudentDetailsActivity : onCreate ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_details_layout);

        if (getIntent().hasExtra(Constants.EDIT_STUDENT_DTO)) {
            mStudentDTO = getIntent().getParcelableExtra(Constants.EDIT_STUDENT_DTO);
            mBatchId = getIntent().getIntExtra(Constants.BATCH_ID, 0);
        }
        initData();

        initToolbar();
        initLayout();
        HelperMethod.debugLog(TAG, "oncrete finished");
    }

    private void initData() {
        mPaymentHistoryList = new ArrayList<>();
    }

    @Override
    protected void onResume() {
        super.onResume();
        HelperMethod.debugLog(TAG, "StudentDetailsActivity : onResume ");
        loadHistoryData();
    }

    private void loadHistoryData() {
        if (mDatabaseRequestHelper == null) {
            mDatabaseRequestHelper = new DatabaseRequestHelper(this);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {

                HelperMethod.debugLog(TAG, "StudentDetailsActivity : Inside run: "+mStudentDTO.getUserId()+ " -- "+mShowHistoryYear);

                ArrayList<PaymentHistoryDTO> list = mDatabaseRequestHelper.getPaymentHistoryByStudentYear(mStudentDTO.getUserId(), mShowHistoryYear);
                mPaymentHistoryList.clear();
                mPaymentHistoryList.addAll(list);
                HelperMethod.debugLog(TAG, "StudentDetailsActivity : before ui thread loop "+mPaymentHistoryList.size()+ " -- "+list.size());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        HelperMethod.debugLog(TAG, "StudentDetailsActivity : before noti ");

                        if (historyAdapter == null) {
                            historyAdapter = new StudentPaymentHistoryAdapter(StudentDetailsActivity.this, mPaymentHistoryList);
                            student_payment_history_rv.setAdapter(historyAdapter);
                            HelperMethod.debugLog(TAG, "StudentDetailsActivity : before noti NULL ++ ");

                        } else {
                            HelperMethod.debugLog(TAG, "StudentDetailsActivity : before noti ELSEE ++ ");
                            historyAdapter.notifyDataSetChanged();
                        }

                        hideProgressDialog();
                        HelperMethod.debugLog(TAG, "StudentDetailsActivity : after noti ");
                    }
                });
            }
        }).start();
    }

    private void initToolbar() {
        student_details_toolbar_title = (TextView) findViewById(R.id.student_details_toolbar_title);
        student_details_toolbar_title.setText(mStudentDTO.getUserName());

        student_details_toolbar_back = (FrameLayout) findViewById(R.id.student_details_toolbar_back);
        student_details_toolbar_back.setOnClickListener(this);

    }

    private void initLayout() {
        history_showing_yr_ll = (LinearLayout) findViewById(R.id.history_showing_yr_ll);
        history_showing_yr_ll.setOnClickListener(this);
        history_showing_yr_tv = (TextView) findViewById(R.id.history_showing_yr_tv);

        student_name_tv = (TextView) findViewById(R.id.student_name_tv);
        student_mobile_phn_tv = (TextView) findViewById(R.id.student_mobile_phn_tv);
        student_monthly_fee_tv = (TextView) findViewById(R.id.student_monthly_fee_tv);
        student_institute_tv = (TextView) findViewById(R.id.student_institute_tv);
        student_address_tv = (TextView) findViewById(R.id.student_address_tv);
        edit_history_ll = (LinearLayout) findViewById(R.id.edit_history_ll);
        edit_history_ll.setOnClickListener(this);

        student_name_tv.setText(mStudentDTO.getUserName());
        student_mobile_phn_tv.setText(mStudentDTO.getUserMobilePhone().equals("") ? "Not Set" : mStudentDTO.getUserMobilePhone());
        student_monthly_fee_tv.setText(mStudentDTO.getMonthlyFee() == 0 ? "Not Set" : "" + mStudentDTO.getMonthlyFee());
        student_institute_tv.setText(mStudentDTO.getUserInstituteName().equals("") ? "Not Set" : mStudentDTO.getUserInstituteName());
        student_address_tv.setText(mStudentDTO.getUserAddress().equals("") ? "Not Set" : mStudentDTO.getUserAddress());

//        setUpSpinner();

//        historyAdapter = new StudentPaymentHistoryAdapter(this, mPaymentHistoryList, this);
        student_payment_history_rv = (RecyclerView) findViewById(R.id.student_payment_history_rv);
        student_payment_history_rv.setNestedScrollingEnabled(false);
        student_payment_history_rv.setLayoutManager(new LinearLayoutManager(this));
        student_payment_history_rv.setHasFixedSize(true);
//        student_payment_history_rv.setAdapter(historyAdapter);

        HelperMethod.debugLog(TAG, "initLayout finish");
    }


    private void showYearSelectionDialog() {
        FragmentManager fm = getSupportFragmentManager();
        YearSelectionDialogFragment yearSelectionDialogFragment = YearSelectionDialogFragment.newInstance("Select Year");
        yearSelectionDialogFragment.show(fm, "");
    }

    @Override
    public void onPaymentUpdate(PaymentDTO dto) {
        HelperMethod.debugLog(TAG, "onPaymentUpdate +++ " + dto.getPaymentMonth() + " y : " + dto.getPaymentYear());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.history_showing_yr_ll:
                showYearSelectionDialog();
                break;
            case R.id.edit_history_ll:

                Intent intent = new Intent(StudentDetailsActivity.this, EditPaymentHistoryActivity.class);
                intent.putExtra(Constants.STUDENT_ID, mStudentDTO.getUserId());
                intent.putExtra(Constants.STUDENT_NAME, mStudentDTO.getUserName());
                intent.putExtra(Constants.BATCH_ID, mBatchId);
                startActivity(intent);
                break;

            case R.id.student_details_toolbar_back:
                StudentDetailsActivity.this.finish();
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        HelperMethod.debugLog(TAG, "StudentDetailsActivity : onPause ");
    }

    private void showProgressDialog() {
        if (mProgressDialog == null || !mProgressDialog.isShowing()) {
            mProgressDialog = ProgressDialog.show(StudentDetailsActivity.this, getString(R.string.loading_data),
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        HelperMethod.debugLog(TAG, "StudentDetailsActivity : onDestroy ");
    }

    @Override
    public void onDialogClosed(int dialogState, String year, String month) {

        switch (dialogState) {
            case Constants.DIALOG_STATE_POSITIVE:
                history_showing_yr_tv.setText(year);
                mShowHistoryYear = Integer.parseInt(year);
                showProgressDialog();
                loadHistoryData();
                break;
            case Constants.DIALOG_STATE_NEGATIVE:

                break;
            case Constants.DIALOG_STATE_NEUTRAL:
                break;
        }

    }
}
