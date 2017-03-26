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

import entwinebits.com.teachersassistant.adapter.PaymentHistoryDetailsAdapter;
import entwinebits.com.teachersassistant.adapter.StudentPaymentHistoryAdapter;
import entwinebits.com.teachersassistant.model.PaymentHistoryDTO;
import entwinebits.com.teachersassistant.utils.Constants;

/**
 * Created by shajib on 3/25/2017.
 */
public class PaymentHistoryDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "PaymentHistoryDetailsActivity";
    private FrameLayout payment_details_toolbar_back;
    private TextView payment_details_toolbar_title;
    private NestedScrollView nestedScrollView;

    private PaymentHistoryDetailsAdapter mStudentHistoryAdapter;
    private RecyclerView payment_history_details_rv;

    private ArrayList<PaymentHistoryDTO> mPaymentHistoryList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pmnt_history_details_layout);
        initToolbar();

        initData();
        if (getIntent().hasExtra(Constants.PAYMENT_HISTORY_LIST)) {
            mPaymentHistoryList = getIntent().getParcelableArrayListExtra(Constants.PAYMENT_HISTORY_LIST);
        }
        initLayout();
    }

    private void initData() {
    }

    private void initLayout() {
        payment_history_details_rv = (RecyclerView)findViewById(R.id.payment_history_details_rv);
        payment_history_details_rv.setLayoutManager(new LinearLayoutManager(this));
        mStudentHistoryAdapter = new PaymentHistoryDetailsAdapter(this, mPaymentHistoryList);
        payment_history_details_rv.setAdapter(mStudentHistoryAdapter);
    }

    private void initToolbar() {
        payment_details_toolbar_title = (TextView) findViewById(R.id.payment_details_toolbar_title);

        payment_details_toolbar_back = (FrameLayout) findViewById(R.id.payment_details_toolbar_back);
        payment_details_toolbar_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.payment_details_toolbar_back:

                PaymentHistoryDetailsActivity.this.finish();
        }
    }
}
