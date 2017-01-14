package entwinebits.com.teachersassistant;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import entwinebits.com.teachersassistant.db.DatabaseRequestHelper;
import entwinebits.com.teachersassistant.dialogFragment.SingleItemDialogFragment;
import entwinebits.com.teachersassistant.listener.DialogCloseListener;
import entwinebits.com.teachersassistant.model.BatchDTO;
import entwinebits.com.teachersassistant.model.PaymentDTO;
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

    private ArrayList<BatchDTO> mBatchList;
    private ArrayList<String> mBatchNameList;

    private DatabaseRequestHelper mDbRequestHelper;

    private TextView history_batch_tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_history_layout);

        initToolbar();
        initLayout();
        loadBatchList();

    }

    private void loadBatchList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (mDbRequestHelper == null) {
                    mDbRequestHelper = new DatabaseRequestHelper(PaymentHistoryActivity.this);
                }

                mBatchNameList = new ArrayList<String>();
                mBatchList = mDbRequestHelper.getBatchList();

                for (BatchDTO batchDTO : mBatchList) {
                    mBatchNameList.add(batchDTO.getBatchName());
//                    HelperMethod.debugLog(TAG, "load == "+batchDTO.getBatchId()+ " namm = "+batchDTO.getBatchName());
//
//                    int total_amount = 0;
//                    ArrayList<UserProfileDTO> studentList = mDbRequestHelper.getStudentListByBatch((int)batchDTO.getBatchId());
//                    for (UserProfileDTO dto : studentList) {
//                        total_amount += dto.getMonthlyFee();
//                    }
//
//                    PaymentDTO paymentDTO = new PaymentDTO();
//                    paymentDTO.setTotalAmount(total_amount);
//                    paymentDTO.setBatchName(batchDTO.getBatchName());
//                    mAllBatchHistoryList.add(paymentDTO);
//                    mSpinnerMap.put(batchDTO.getBatchId(), batchDTO.getBatchName());
                }

            }
        }).start();

    }

    private void initLayout() {

        history_batch_tv = (TextView)findViewById(R.id.history_batch_tv);
        history_batch_tv.setOnClickListener(this);
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

    @Override
    public void onDialogClosed(int dialogState, String year, String month) {
        switch (dialogState) {
            case Constants.DIALOG_STATE_POSITIVE:
                history_batch_tv.setText(year);
//                mEditYear = Integer.parseInt(year);
//                showProgressDialog();
//                loadHistoryData();
                break;
            case Constants.DIALOG_STATE_NEGATIVE:

                break;
            case Constants.DIALOG_STATE_NEUTRAL:
                break;
        }
    }
}
