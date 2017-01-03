package entwinebits.com.teachersassistant;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;

import entwinebits.com.teachersassistant.adapter.StudentPaymentHistoryAdapter;
import entwinebits.com.teachersassistant.listener.PaymentUpdateListener;
import entwinebits.com.teachersassistant.model.PaymentDTO;
import entwinebits.com.teachersassistant.model.UserProfileDTO;
import entwinebits.com.teachersassistant.utils.Constants;
import entwinebits.com.teachersassistant.utils.HelperMethod;

/**
 * Created by shajib on 12/23/2016.
 */
public class StudentDetailsActivity extends AppCompatActivity implements View.OnClickListener, PaymentUpdateListener {

    private String TAG = "StudentDetailsActivity";
    private RecyclerView student_payment_history_rv;
    private StudentPaymentHistoryAdapter historyAdapter;
    private FrameLayout student_details_toolbar_back;
    private TextView student_details_toolbar_title;

    private Spinner student_details_history_spinner;
    private UserProfileDTO mStudentDTO;

    private TextView student_name_tv, student_mobile_phn_tv, student_monthly_fee_tv, student_institute_tv, student_address_tv;
    private LinearLayout edit_history_ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_details_layout);

        if (getIntent().hasExtra(Constants.EDIT_STUDENT_DTO)) {
            mStudentDTO = getIntent().getParcelableExtra(Constants.EDIT_STUDENT_DTO);
        }

        initToolbar();
        initLayout();

    }

    private void initToolbar() {
        student_details_toolbar_title = (TextView) findViewById(R.id.student_details_toolbar_title);
        student_details_toolbar_title.setText(mStudentDTO.getUserName());

        student_details_toolbar_back = (FrameLayout) findViewById(R.id.student_details_toolbar_back);
        student_details_toolbar_back.setOnClickListener(this);

    }

    private void initLayout() {
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

        student_details_history_spinner = (Spinner) findViewById(R.id.student_details_history_spinner);
        String[] years = getResources().getStringArray(R.array.Years);
        ArrayAdapter<CharSequence> yearAdapter = new ArrayAdapter<CharSequence>(this, R.layout.spinner_text, years );
        yearAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        student_details_history_spinner.setAdapter(yearAdapter);

        historyAdapter = new StudentPaymentHistoryAdapter(this, this);
        student_payment_history_rv = (RecyclerView)findViewById(R.id.student_payment_history_rv);
        student_payment_history_rv.setNestedScrollingEnabled(false);
        student_payment_history_rv.setLayoutManager(new LinearLayoutManager(this));
        student_payment_history_rv.setHasFixedSize(true);
        student_payment_history_rv.setAdapter(historyAdapter);

    }

    @Override
    public void onPaymentUpdate(PaymentDTO dto) {
        HelperMethod.debugLog(TAG, "onPaymentUpdate +++ "+dto.getPaymentMonth()+" y : "+dto.getPaymentYear());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_history_ll:

                Intent intent = new Intent(StudentDetailsActivity.this, EditPaymentHistoryActivity.class);
                startActivity(intent);
                break;

            case R.id.student_details_toolbar_back:
                StudentDetailsActivity.this.finish();
                break;
        }
    }
}
