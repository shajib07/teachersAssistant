package entwinebits.com.teachersassistant;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;

import entwinebits.com.teachersassistant.adapter.BatchPaymentHistoryAdapter;
import entwinebits.com.teachersassistant.adapter.PaymentHistoryDetailsAdapter;
import entwinebits.com.teachersassistant.model.PaymentHistoryDTO;
import entwinebits.com.teachersassistant.utils.Constants;

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


    }
}
