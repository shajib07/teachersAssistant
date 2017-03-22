package entwinebits.com.teachersassistant;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.util.Util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import entwinebits.com.teachersassistant.adapter.StudentPaymentHistoryAdapter;
import entwinebits.com.teachersassistant.dialogFragment.DoubleItemDialogFragment;
import entwinebits.com.teachersassistant.dialogFragment.YearSelectionDialogFragment;
import entwinebits.com.teachersassistant.listener.DialogCloseListener;
import entwinebits.com.teachersassistant.model.PaymentHistoryDTO;
import entwinebits.com.teachersassistant.server.ServerRequestHelper;
import entwinebits.com.teachersassistant.server.ServerResponseParser;
import entwinebits.com.teachersassistant.utils.ConstantFunctions;
import entwinebits.com.teachersassistant.utils.Constants;
import entwinebits.com.teachersassistant.utils.HelperMethod;
import entwinebits.com.teachersassistant.utils.Months;
import entwinebits.com.teachersassistant.utils.ServerConstants;

/**
 * Created by shajib on 3/11/2017.
 */
public class AddPaymentHistoryActivity extends AppCompatActivity implements View.OnClickListener, DialogCloseListener {

    private String TAG = "AddPaymentHistoryActivity";
    private FrameLayout add_payment_toolbar_back;
    private TextView add_payment_toolbar_title;
    private TextView selected_year_tv, selected_month_tv;
    private LinearLayout selected_year_ll;
    private EditText payment_amount;
    private Button add_payment_btn;

    private int mStudentId, mBatchId;
    private int mSelectedYear, mSelectedMonth, mAmount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_payment_layout);

        mStudentId = getIntent().getIntExtra(Constants.STUDENT_ID, 0);
        mBatchId = getIntent().getIntExtra(Constants.BATCH_ID, 0);

        initToolbar();
        initLayout();
    }

    private void initToolbar() {
        add_payment_toolbar_title = (TextView) findViewById(R.id.add_payment_toolbar_title);
        add_payment_toolbar_title.setText("Add Payment");

        add_payment_toolbar_back = (FrameLayout) findViewById(R.id.add_payment_toolbar_back);
        add_payment_toolbar_back.setOnClickListener(this);
    }

    private void initLayout() {
        selected_year_ll = (LinearLayout) findViewById(R.id.selected_year_ll);
        selected_year_ll.setOnClickListener(this);

        payment_amount = (EditText) findViewById(R.id.payment_amount);
        selected_year_tv = (TextView) findViewById(R.id.selected_year_tv);
        selected_month_tv = (TextView) findViewById(R.id.selected_month_tv);
        add_payment_btn = (Button) findViewById(R.id.add_payment_btn);
        add_payment_btn.setOnClickListener(this);
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

    private void showDateSelectionDialog(int dialogId) {
        FragmentManager fm = getSupportFragmentManager();
        DoubleItemDialogFragment doubleItemDialogFragment = DoubleItemDialogFragment.newInstance(dialogId, getYearList(), getMonthList());
        doubleItemDialogFragment.show(fm, "");
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.add_payment_btn:
                mAmount = Integer.parseInt(payment_amount.getText().toString());
                sendAddPaymentReq();
                break;
            case R.id.selected_year_ll:
                showDateSelectionDialog(0);
                break;
            case R.id.add_payment_toolbar_back:
                AddPaymentHistoryActivity.this.finish();
                break;
            default:
                break;
        }
    }


    private void sendAddPaymentReq() {
        JSONObject jsonObject = ServerRequestHelper.sendAddPaymentHistoryRequest(mStudentId,
                mBatchId, mSelectedMonth, mSelectedYear, mAmount, true);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Constants.REQUEST_URL, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        HelperMethod.debugLog(TAG, response.toString());
                        if (!response.optBoolean(ServerConstants.ERROR)) {
                            Toast.makeText(AddPaymentHistoryActivity.this, "Payment Added Successfully.", Toast.LENGTH_LONG).show();
                            AddPaymentHistoryActivity.this.finish();
                        } else {
                            if (response.optInt(ServerConstants.REASON_CODE) == 1) {
                                Toast.makeText(AddPaymentHistoryActivity.this, "Payment Already Exist.", Toast.LENGTH_LONG).show();
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


    @Override
    public void onDialogClosed(int dialogId, int dialogState, String year, String month) {
        switch (dialogState) {
            case Constants.DIALOG_STATE_POSITIVE:
                if (dialogId == 0) {
                    selected_year_tv.setText(year);
                    selected_month_tv.setText(month);
                    mSelectedYear = Integer.parseInt(year);
                    mSelectedMonth = ConstantFunctions.getMonthIntType(month);

                    HelperMethod.debugLog(TAG, "mSelectedMonth == "+mSelectedMonth);
                }
                break;
            case Constants.DIALOG_STATE_NEGATIVE:

                break;
            case Constants.DIALOG_STATE_NEUTRAL:
                break;
        }
    }
}
