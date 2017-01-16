package entwinebits.com.teachersassistant;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.MonthDisplayHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import entwinebits.com.teachersassistant.adapter.DateChooserAdapter;
import entwinebits.com.teachersassistant.adapter.EditPaymentHistoryAdapter;
import entwinebits.com.teachersassistant.db.DatabaseRequestHelper;
import entwinebits.com.teachersassistant.dialogFragment.YearSelectionDialogFragment;
import entwinebits.com.teachersassistant.listener.DateSelectionListener;
import entwinebits.com.teachersassistant.listener.DialogCloseListener;
import entwinebits.com.teachersassistant.model.PaymentDTO;
import entwinebits.com.teachersassistant.model.PaymentHistoryDTO;
import entwinebits.com.teachersassistant.utils.Constants;
import entwinebits.com.teachersassistant.utils.HelperMethod;
import entwinebits.com.teachersassistant.utils.Months;

/**
 * Created by shajib on 1/2/2017.
 */
public class EditPaymentHistoryActivity extends AppCompatActivity implements View.OnClickListener, DialogCloseListener {

    private String TAG = "EditPaymentHistoryActivity";
    private FrameLayout edit_history_save_btn, edit_history_toolbar_back;
    private TextView edit_history_toolbar_title;
    private Spinner edit_history_spinner;
    private ArrayList<PaymentHistoryDTO> mPaymentHistoryList;
    private String mStudentName;
    private int mStudentId;
    private int mBatchId;
    private int mEditYear;
    private ProgressDialog mProgressDialog;
    private DatabaseRequestHelper mDatabaseRequestHelper;

    private LinearLayout[] inflatedLayout;
    private CheckBox[] full_paid_cb, others_paid_cb;
    private EditText[] paid_amount_et;
    private TextView[] paid_month_tv;

    private LinearLayout history_edit_yr_ll;
    private TextView history_edit_yr_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_payment_history);
        mStudentId = getIntent().getIntExtra(Constants.STUDENT_ID, 0);
        mBatchId = getIntent().getIntExtra(Constants.BATCH_ID, 0);
        mStudentName = getIntent().getStringExtra(Constants.STUDENT_NAME);

        initData();
        initToolbar();
        initLayout();
    }


    private void bindHistoryData() {
        int i = 0;
        for (PaymentHistoryDTO dto : mPaymentHistoryList) {
            HelperMethod.debugLog(TAG, "ispaid : " + dto.isPaid() + " amount : " + dto.getPaidAmount());

            paid_amount_et[i].setText(dto.getPaidAmount() + "");
            paid_amount_et[i].setEnabled(false);
            full_paid_cb[i].setChecked(dto.isPaid());
            others_paid_cb[i].setChecked(!dto.isPaid());
            i++;
        }
    }

    private void bindDefaultData() {
        for (int i = 0; i < 12; i++) {
            paid_amount_et[i].setText("");
            paid_amount_et[i].setEnabled(false);
            full_paid_cb[i].setChecked(false);
            others_paid_cb[i].setChecked(true);
        }
    }

    private void loadHistoryData() {
        showProgressDialog();

        if (mDatabaseRequestHelper == null) {
            mDatabaseRequestHelper = new DatabaseRequestHelper(this);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                mPaymentHistoryList.clear();
                final ArrayList<PaymentHistoryDTO> list = mDatabaseRequestHelper.getPaymentHistoryByStudentYear(mStudentId, mEditYear);
                HelperMethod.debugLog(TAG, "size inside bind = " + list.size());
                mPaymentHistoryList.addAll(list);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (list.size() >= 1) {
                            bindHistoryData();
                        } else {
                            bindDefaultData();
                        }
                        hideProgressDialog();
                    }
                });
            }
        }).start();
    }

    private void initLayout() {
        setupInput();
    }



    private void setupInput() {
        history_edit_yr_ll = (LinearLayout) findViewById(R.id.history_edit_yr_ll);
        history_edit_yr_ll.setOnClickListener(this);

        history_edit_yr_tv = (TextView) findViewById(R.id.history_edit_yr_tv);

        inflatedLayout = new LinearLayout[12];
        full_paid_cb = new CheckBox[12];
        others_paid_cb = new CheckBox[12];
        paid_amount_et = new EditText[12];
        paid_month_tv = new TextView[12];

        inflatedLayout[0] = (LinearLayout) findViewById(R.id.edit_item_0);
        inflatedLayout[1] = (LinearLayout) findViewById(R.id.edit_item_1);
        inflatedLayout[2] = (LinearLayout) findViewById(R.id.edit_item_2);
        inflatedLayout[3] = (LinearLayout) findViewById(R.id.edit_item_3);
        inflatedLayout[4] = (LinearLayout) findViewById(R.id.edit_item_4);
        inflatedLayout[5] = (LinearLayout) findViewById(R.id.edit_item_5);
        inflatedLayout[6] = (LinearLayout) findViewById(R.id.edit_item_6);
        inflatedLayout[7] = (LinearLayout) findViewById(R.id.edit_item_7);
        inflatedLayout[8] = (LinearLayout) findViewById(R.id.edit_item_8);
        inflatedLayout[9] = (LinearLayout) findViewById(R.id.edit_item_9);
        inflatedLayout[10] = (LinearLayout) findViewById(R.id.edit_item_10);
        inflatedLayout[11] = (LinearLayout) findViewById(R.id.edit_item_11);

        for (int i = 0; i < 12; i++) {
            paid_month_tv[i] = (TextView) inflatedLayout[i].findViewById(R.id.paid_month_tv);
            paid_month_tv[i].setText(Months.get(i + 1) + "");

            paid_amount_et[i] = (EditText) inflatedLayout[i].findViewById(R.id.paid_amount_et);
//            paid_amount_et[i].setEnabled(false);

            full_paid_cb[i] = (CheckBox) inflatedLayout[i].findViewById(R.id.full_paid_cb);
            others_paid_cb[i] = (CheckBox) inflatedLayout[i].findViewById(R.id.others_paid_cb);
//            full_paid_cb[i].setChecked(false);
//            others_paid_cb[i].setChecked(true);

            final int finalI = i;
            full_paid_cb[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        others_paid_cb[finalI].setChecked(false);
                        paid_amount_et[finalI].setEnabled(true);
                    } else {
                        others_paid_cb[finalI].setChecked(true);
//                        paid_amount_et[finalI].setText();
                        paid_amount_et[finalI].setEnabled(false);
                    }
                }
            });

            others_paid_cb[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        full_paid_cb[finalI].setChecked(false);
                        paid_amount_et[finalI].setEnabled(false);
                    } else {
                        full_paid_cb[finalI].setChecked(true);
//                        paid_amount_et[finalI].setText("");
                        paid_amount_et[finalI].setEnabled(true);
                    }
                }
            });

        }
    }

    private void savePaymentHistory() {

        showProgressDialog();
        PaymentHistoryDTO dto;
        for (int i = 0; i < 12; i++) {
            dto = new PaymentHistoryDTO();
            dto.setPaidAmount(Integer.parseInt(paid_amount_et[i].getText().toString().length() == 0
                    ? "0" : paid_amount_et[i].getText().toString()));
            dto.setPaid(full_paid_cb[i].isChecked());
            dto.setStudentId(mStudentId);
            dto.setBatchId(mBatchId);
            dto.setMonth(i + 1);
            dto.setYear(mEditYear);
            dto.setStudentName(mStudentName);
            mPaymentHistoryList.add(dto);
        }

        if (mDatabaseRequestHelper == null) {
            mDatabaseRequestHelper = new DatabaseRequestHelper(this);
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (PaymentHistoryDTO dto : mPaymentHistoryList) {
                    mDatabaseRequestHelper.addPaymentHistory(dto);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideProgressDialog();
                        EditPaymentHistoryActivity.this.finish();
                        HelperMethod.debugLog(TAG, "FInish called");
                    }
                });
            }
        }).start();
    }

    private void showYearSelectionDialog() {
        FragmentManager fm = getSupportFragmentManager();
        YearSelectionDialogFragment yearSelectionDialogFragment = YearSelectionDialogFragment.newInstance("Select Year");
        yearSelectionDialogFragment.show(fm, "");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.history_edit_yr_ll:
                showYearSelectionDialog();
                break;
            case R.id.edit_history_save_btn:

                savePaymentHistory();

                break;

            case R.id.edit_history_toolbar_back:
                EditPaymentHistoryActivity.this.finish();
                break;
        }
    }

    private void showProgressDialog() {
        if (mProgressDialog == null || !mProgressDialog.isShowing()) {
            mProgressDialog = ProgressDialog.show(EditPaymentHistoryActivity.this, getString(R.string.loading_data),
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
        HelperMethod.debugLog(TAG, "onDestroy ");
    }

    private void initData() {
        mPaymentHistoryList = new ArrayList<>();
        mEditYear = 2017;
    }

    private void initToolbar() {
        edit_history_save_btn = (FrameLayout) findViewById(R.id.edit_history_save_btn);
        edit_history_save_btn.setOnClickListener(this);
        edit_history_toolbar_back = (FrameLayout) findViewById(R.id.edit_history_toolbar_back);
        edit_history_toolbar_back.setOnClickListener(this);
        edit_history_toolbar_title = (TextView) findViewById(R.id.edit_history_toolbar_title);
        edit_history_toolbar_title.setText("Edit "+mStudentName+"'s History");
    }

    @Override
    public void onDialogClosed(int dialogState, String year, String month) {
        switch (dialogState) {
            case Constants.DIALOG_STATE_POSITIVE:
                history_edit_yr_tv.setText(year);
                mEditYear = Integer.parseInt(year);
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
/*
    private void setUpSpinner() {
        edit_history_spinner = (Spinner) findViewById(R.id.edit_history_spinner);
        String[] years = getResources().getStringArray(R.array.Years);
        ArrayAdapter<CharSequence> yearAdapter = new ArrayAdapter<CharSequence>(this, R.layout.spinner_text, years);
        yearAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        edit_history_spinner.setAdapter(yearAdapter);

        edit_history_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(EditPaymentHistoryActivity.this, "select : " + parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
                mEditYear = Integer.parseInt(parent.getItemAtPosition(position).toString());
                loadHistoryData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
*/