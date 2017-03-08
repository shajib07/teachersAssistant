package entwinebits.com.teachersassistant;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
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

import entwinebits.com.teachersassistant.adapter.StudentListAdapter;
import entwinebits.com.teachersassistant.adapter.StudentPaymentHistoryAdapter;
import entwinebits.com.teachersassistant.db.DatabaseRequestHelper;
import entwinebits.com.teachersassistant.dialogFragment.YearSelectionDialogFragment;
import entwinebits.com.teachersassistant.listener.DialogCloseListener;
import entwinebits.com.teachersassistant.listener.PaymentUpdateListener;
import entwinebits.com.teachersassistant.model.PaymentDTO;
import entwinebits.com.teachersassistant.model.PaymentHistoryDTO;
import entwinebits.com.teachersassistant.model.UserProfileDTO;
import entwinebits.com.teachersassistant.server.ServerRequestHelper;
import entwinebits.com.teachersassistant.server.ServerRequestManager;
import entwinebits.com.teachersassistant.server.ServerResponseParser;
import entwinebits.com.teachersassistant.utils.Constants;
import entwinebits.com.teachersassistant.utils.HelperMethod;
import entwinebits.com.teachersassistant.utils.ServerConstants;

/**
 * Created by shajib on 12/23/2016.
 */
public class StudentDetailsActivity extends AppCompatActivity implements View.OnClickListener,
        DialogCloseListener, PaymentUpdateListener {

    private String TAG = "StudentDetailsActivity";
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
    private ImageView student_details_edit_iv;
    private DatabaseRequestHelper mDbRequestHelper;

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

                HelperMethod.debugLog(TAG, "StudentDetailsActivity : Inside run: " + mStudentDTO.getUserId() + " -- " + mShowHistoryYear);

                ArrayList<PaymentHistoryDTO> list = mDatabaseRequestHelper.getPaymentHistoryByStudentYear(mStudentDTO.getUserId(), mShowHistoryYear);
                mPaymentHistoryList.clear();
                mPaymentHistoryList.addAll(list);
                HelperMethod.debugLog(TAG, "StudentDetailsActivity : before ui thread loop " + mPaymentHistoryList.size() + " -- " + list.size());

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
        student_details_toolbar_title.setText(mStudentDTO.getUserFirstName());

        student_details_toolbar_back = (FrameLayout) findViewById(R.id.student_details_toolbar_back);
        student_details_toolbar_back.setOnClickListener(this);

    }

    private void initLayout() {

        student_details_edit_iv = (ImageView) findViewById(R.id.student_details_edit_iv);
        student_details_edit_iv.setOnClickListener(this);
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

        student_name_tv.setText(mStudentDTO.getUserFirstName());
        student_mobile_phn_tv.setText((mStudentDTO.getUserMobilePhone() == null ||
                mStudentDTO.getUserMobilePhone().equals("")) ? "Not Set" : mStudentDTO.getUserMobilePhone());
//        student_monthly_fee_tv.setText(mStudentDTO.getMonthlyFee() == 0 ? "Not Set" : "" + mStudentDTO.getMonthlyFee());
        student_institute_tv.setText((mStudentDTO.getUserInstituteName() == null ||
                mStudentDTO.getUserInstituteName().equals("")) ? "Not Set" : mStudentDTO.getUserInstituteName());
        student_address_tv.setText((mStudentDTO.getUserAddress() == null ||
                mStudentDTO.getUserAddress().equals("")) ? "Not Set" : mStudentDTO.getUserAddress());

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
        YearSelectionDialogFragment yearSelectionDialogFragment = YearSelectionDialogFragment.newInstance(0);
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

                HelperMethod.debugLog(TAG, "id == " + mStudentDTO.getUserId() + " bat == " + mStudentDTO.getBatchId());
                intent.putExtra(Constants.STUDENT_NAME, mStudentDTO.getUserFirstName());
                intent.putExtra(Constants.BATCH_ID, mBatchId);
                startActivity(intent);
                break;

            case R.id.student_details_toolbar_back:
                StudentDetailsActivity.this.finish();
                break;
            case R.id.student_details_edit_iv:
                showMenuDialog();
                break;
        }
    }

    private void showMenuDialog() {
        try {
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setContentView(R.layout.student_menu_dialog_layout);
            dialog.setCancelable(true);

            final TextView dialog_title = (TextView) dialog.findViewById(R.id.dialog_title);
            dialog_title.setText("Please Choose");

            final Button dialog_upper_btn = (Button) dialog.findViewById(R.id.dialog_upper_btn);
            dialog_upper_btn.setText("Edit");
            dialog_upper_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    Intent intent = new Intent(StudentDetailsActivity.this, AddNewStudentActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent.putExtra(Constants.EDIT_STUDENT_DTO, mStudentDTO);
                    startActivityForResult(intent, 120);
                }
            });

            final Button dialog_lower_btn = (Button) dialog.findViewById(R.id.dialog_lower_btn);
            dialog_lower_btn.setText("Delete");
            dialog_lower_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog.dismiss();
//                    Intent intent = new Intent(StudentDetailsActivity.this, StudentDetailsActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                    intent.putExtra(Constants.EDIT_STUDENT_DTO, dto);
//                    startActivity(intent);

                    showDeleteConfirmationDialog();
                }
            });
            dialog.show();

        } catch (Exception e) {
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 120) {
            if (resultCode == RESULT_OK) {
                if (data.hasExtra(Constants.EDIT_STUDENT_DTO)) {

                    mStudentDTO = data.getParcelableExtra(Constants.EDIT_STUDENT_DTO);
                    HelperMethod.debugLog(TAG, "EDIT_STUDENT_DTO : " + mStudentDTO.getUserFirstName());

                    student_name_tv.setText(mStudentDTO.getUserFirstName());
                    student_mobile_phn_tv.setText(mStudentDTO.getUserMobilePhone());
                    student_monthly_fee_tv.setText("" + mStudentDTO.getMonthlyFee());
                    student_institute_tv.setText(mStudentDTO.getUserInstituteName());
                    student_address_tv.setText(mStudentDTO.getUserAddress());

                    if (mDbRequestHelper == null) {
                        mDbRequestHelper = new DatabaseRequestHelper(this);
                    }
                    mDbRequestHelper.updateStudent(mStudentDTO);
//                    int idx = 0 ;
//                    for (int i =0; i< mStudentList.size();i++) {
//                        if (mStudentList.get(i).getUserId() == editedDto.getUserId()) {
//                            idx = i;
//                            break;
//                        }
//                    }
//                    mStudentList.remove(idx);
//                    mStudentList.add(idx, editedDto);
//                    mStudentListAdapter.notifyDataSetChanged();

                    sendActivityResult();
                }

            }
        }

    }

    private void sendActivityResult() {
        UserProfileDTO updatedStudentDTO = new UserProfileDTO();
        updatedStudentDTO.setUserFirstName(student_name_tv.getText().toString());
        updatedStudentDTO.setUserMobilePhone(student_mobile_phn_tv.getText().toString());
        updatedStudentDTO.setMonthlyFee(Integer.parseInt(student_monthly_fee_tv.getText().toString()));
        updatedStudentDTO.setUserInstituteName(student_institute_tv.getText().toString());
        updatedStudentDTO.setUserAddress(student_address_tv.getText().toString());
        Intent BackIntent = new Intent();
        HelperMethod.debugLog(AddNewBatchActivity.TAG, "updatedStudentDTO : " + updatedStudentDTO.getUserFirstName());
        BackIntent.putExtra(Constants.EDIT_STUDENT_DTO, updatedStudentDTO);
        setResult(RESULT_OK, BackIntent);
    }

    private void showDeleteConfirmationDialog() {
        try {
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setContentView(R.layout.double_btn_dialog_layout);
            dialog.setCancelable(true);

            final Button dialog_left_btn = (Button) dialog.findViewById(R.id.dialog_left_btn);
            dialog_left_btn.setText("Cancel");
            dialog_left_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            final Button dialog_right_btn = (Button) dialog.findViewById(R.id.dialog_right_btn);
            dialog_right_btn.setText("OK");
            dialog_right_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog.dismiss();
//                    mStudentList.remove(dto);
//                    notifyDataSetChanged();
                    if (mDbRequestHelper == null) {
                        mDbRequestHelper = new DatabaseRequestHelper(StudentDetailsActivity.this);
                    }
                    mDbRequestHelper.deleteStudent(mStudentDTO);
                }
            });
            dialog.show();

        } catch (Exception e) {
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

    private void sendPaymentListRequest() {

        JSONObject jsonObject = ServerRequestHelper.sendGetStudentPaymentListRequest(mBatchId, mStudentDTO.getUserId(), 1, mShowHistoryYear, 12, mShowHistoryYear);
        HelperMethod.debugLog(TAG, "sendPaymentListRequest batch id == " + mBatchId + " stud id == " + mStudentDTO.getUserId());

//        HelperMethod.debugLog(TAG, "paymentHistoryList == "+paymentHistoryList.size());
//        for (PaymentHistoryDTO dto : paymentHistoryList) {
//            HelperMethod.debugLog(TAG, ""+dto.getPaidAmount()+" "+dto.getMonth());
//        }
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Constants.REQUEST_URL, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        HelperMethod.debugLog(TAG, response.toString());
                        if (!response.optBoolean(ServerConstants.ERROR)) {

                            ArrayList<PaymentHistoryDTO> paymentHistoryDTOs = ServerResponseParser.parseGetStudentPaymentListRequest(response);
                            HelperMethod.debugLog(TAG, "paymentHistoryDTOs siz == "+paymentHistoryDTOs.size());
                            Toast.makeText(StudentDetailsActivity.this, "get Payment list : "+response.optBoolean(ServerConstants.ERROR), Toast.LENGTH_SHORT).show();
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

    @Override
    public void onDialogClosed(int dialogId, int dialogState, String year, String month) {

        switch (dialogState) {
            case Constants.DIALOG_STATE_POSITIVE:
                if (dialogId == 0) {
                    history_showing_yr_tv.setText(year);
                    mShowHistoryYear = Integer.parseInt(year);
                    showProgressDialog();
                    loadHistoryData();
                    sendPaymentListRequest();

                }
                break;
            case Constants.DIALOG_STATE_NEGATIVE:

                break;
            case Constants.DIALOG_STATE_NEUTRAL:
                break;
        }

    }
}
