package entwinebits.com.teachersassistant;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.ArrayList;

import entwinebits.com.teachersassistant.adapter.AddedUserHorizontalAdapter;
import entwinebits.com.teachersassistant.model.UserProfileDTO;
import entwinebits.com.teachersassistant.utils.Constants;
import entwinebits.com.teachersassistant.utils.HelperMethod;

/**
 * Created by shajib on 12/18/2016.
 */
public class AddNewStudentActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "AddNewStudentActivity";
    private FrameLayout add_student_toolbar_back, add_student_done_btn;
    private EditText added_student_name, added_student_mobile_phn, added_student_payment_amount, added_student_institution, added_student_address;
    private Button add_student_btn;
    private ArrayList<UserProfileDTO> mAddedStudentList;
    private AddedUserHorizontalAdapter mAddedStudentAdapter;

    private RecyclerView added_student_rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_student);

        initData();
        initToolbar();
        initLayout();
    }

    private void initData() {
        mAddedStudentList = new ArrayList<>();
    }

    private void initLayout() {
        added_student_name = (EditText) findViewById(R.id.added_student_name);
        added_student_name.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        added_student_mobile_phn = (EditText) findViewById(R.id.added_student_mobile_phn);
        added_student_mobile_phn.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        added_student_payment_amount = (EditText) findViewById(R.id.added_student_payment_amount);
        added_student_payment_amount.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        added_student_institution = (EditText) findViewById(R.id.added_student_institution);
        added_student_institution.setImeOptions(EditorInfo.IME_ACTION_DONE);
        added_student_address = (EditText) findViewById(R.id.added_student_address);
        added_student_address.setImeOptions(EditorInfo.IME_ACTION_DONE);

        added_student_name.setSingleLine(true);
        added_student_mobile_phn.setSingleLine(true);
        added_student_payment_amount.setSingleLine(true);
        added_student_institution.setSingleLine(true);
        added_student_address.setSingleLine(true);

        add_student_btn = (Button) findViewById(R.id.add_student_btn);
        add_student_btn.setOnClickListener(this);

        mAddedStudentAdapter = new AddedUserHorizontalAdapter(mAddedStudentList);
        added_student_rv = (RecyclerView) findViewById(R.id.added_student_rv);
        added_student_rv.setNestedScrollingEnabled(false);
        added_student_rv.setLayoutManager(new LinearLayoutManager(this));
        added_student_rv.setAdapter(mAddedStudentAdapter);
    }

    private void initToolbar() {
        add_student_toolbar_back = (FrameLayout) findViewById(R.id.add_student_toolbar_back);
        add_student_toolbar_back.setOnClickListener(this);
        add_student_done_btn = (FrameLayout) findViewById(R.id.add_student_done_btn);
        add_student_done_btn.setOnClickListener(this);
    }

    private void inputStudentInfo() {
        UserProfileDTO userDto = new UserProfileDTO();
        userDto.setUserName(added_student_name.getText().toString());
        userDto.setUserAddress(added_student_address.getText().toString());
        String amount = added_student_payment_amount.getText().toString();
        int payment_amount = 0;
        if (amount.length() > 0) {
            payment_amount = Integer.parseInt(amount);
        }
        userDto.setMonthlyFee(payment_amount);
        userDto.setUserMobilePhone(added_student_mobile_phn.getText().toString());
        userDto.setUserInstituteName(added_student_institution.getText().toString());

        if (userDto.getUserName().length() > 0) {
            mAddedStudentList.add(userDto);
        } else {
            Toast.makeText(AddNewStudentActivity.this, "Please, Insert Student Name", Toast.LENGTH_SHORT).show();
        }
        mAddedStudentAdapter.notifyDataSetChanged();
        resetInputFields();
    }

    private void sendAddedStudentList() {
        Intent BackIntent = new Intent();
        HelperMethod.debugLog(AddNewBatchActivity.TAG, "mAddedStudentList : "+mAddedStudentList.size());
        BackIntent.putParcelableArrayListExtra(Constants.ADDED_STUDENT_LIST, mAddedStudentList);
        setResult(RESULT_OK, BackIntent);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_student_btn:
                inputStudentInfo();
                resetInputFields();
                break;

            case R.id.add_student_done_btn:
                sendAddedStudentList();
                break;

            case R.id.add_student_toolbar_back:
                AddNewStudentActivity.this.finish();
                break;
        }
    }

    private void resetInputFields() {
        added_student_name.setText("");
        added_student_mobile_phn.setText("");
        added_student_payment_amount.setText("");
        added_student_institution.setText("");
        added_student_address.setText("");
    }
}
