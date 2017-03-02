package entwinebits.com.teachersassistant;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import entwinebits.com.teachersassistant.adapter.AddedUserHorizontalAdapter;
import entwinebits.com.teachersassistant.db.DatabaseRequestHelper;
import entwinebits.com.teachersassistant.model.UserProfileDTO;
import entwinebits.com.teachersassistant.utils.Constants;
import entwinebits.com.teachersassistant.utils.HelperMethod;

/**
 * Created by shajib on 12/18/2016.
 */
public class AddNewStudentActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "AddNewStudentActivity";
    private FrameLayout add_student_toolbar_back, add_student_toolbar_btn;
    private EditText added_student_name, added_student_mobile_phn, added_student_payment_amount, added_student_institution, added_student_address;
    private TextView toolbar_save_btn;
    private ArrayList<UserProfileDTO> mAddedStudentList;
    private AddedUserHorizontalAdapter mAddedStudentAdapter;


    private Button search_user_btn, add_more_user_btn;
    private SearchView searchView;

    private RecyclerView added_student_rv;
    private UserProfileDTO mEditStudentDTO;
    private boolean isEditable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_student);

        if (getIntent().hasExtra(Constants.EDIT_STUDENT_DTO)) {
            mEditStudentDTO = getIntent().getParcelableExtra(Constants.EDIT_STUDENT_DTO);
            isEditable = true;
        }
        initData();
        initToolbar();
        initLayout();

    }

    private void initSearchView() {
        searchView.setFocusableInTouchMode(true);

        final EditText searchET = ((EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text));
        if (searchET != null) {
            searchET.setTextColor(getResources().getColor(android.R.color.black));
            searchET.setHintTextColor(getResources().getColor(android.R.color.darker_gray));
        }

        ImageView searchIcon = (ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_button);
        if (searchIcon != null) {
            searchIcon.setImageResource(R.drawable.ic_action_add);
            searchIcon.setAlpha(0.6f);
        }

        searchView.onActionViewExpanded();

        ImageView crossButton = (ImageView) searchET.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
        if (crossButton != null) {
            crossButton.setImageResource(R.drawable.ic_action_add);
            crossButton.setAlpha(0.5f);
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                HelperMethod.debugLog(TAG, "onQueryTextSubmit : " + query);
//                sendSearchJsonRequest(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }


    private void initData() {
        mAddedStudentList = new ArrayList<>();
    }

    private void initLayout() {

//        searchView = (SearchView) findViewById(R.id.searchView);
//        searchView.setQueryHint("Name/Mobile Phone");
//        initSearchView();

        search_user_btn = (Button) findViewById(R.id.search_user_btn);
        search_user_btn.setOnClickListener(this);
        add_more_user_btn = (Button) findViewById(R.id.add_more_user_btn);
        add_more_user_btn.setOnClickListener(this);

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

        toolbar_save_btn = (TextView) findViewById(R.id.toolbar_save_btn);

        mAddedStudentAdapter = new AddedUserHorizontalAdapter(mAddedStudentList);
        added_student_rv = (RecyclerView) findViewById(R.id.added_student_rv);
        added_student_rv.setNestedScrollingEnabled(false);
        added_student_rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        added_student_rv.setAdapter(mAddedStudentAdapter);


        if (isEditable) {
            setEditInfo();
            add_student_toolbar_btn.setVisibility(View.GONE);
            added_student_rv.setVisibility(View.GONE);
            toolbar_save_btn.setVisibility(View.VISIBLE);
        }

    }

    private void setEditInfo() {
        added_student_name.setText(mEditStudentDTO.getUserFirstName() == null ? "" : mEditStudentDTO.getUserFirstName());
        added_student_mobile_phn.setText(mEditStudentDTO.getUserMobilePhone() == null ? "" : mEditStudentDTO.getUserMobilePhone());
        added_student_payment_amount.setText(mEditStudentDTO.getMonthlyFee() + "");
        added_student_institution.setText(mEditStudentDTO.getUserInstituteName() == null ? "" : mEditStudentDTO.getUserInstituteName());
        added_student_address.setText(mEditStudentDTO.getUserAddress() == null ? "" : mEditStudentDTO.getUserAddress());

    }

    private void initToolbar() {
        add_student_toolbar_back = (FrameLayout) findViewById(R.id.add_student_toolbar_back);
        add_student_toolbar_back.setOnClickListener(this);
        add_student_toolbar_btn = (FrameLayout) findViewById(R.id.add_student_toolbar_btn);
        add_student_toolbar_btn.setOnClickListener(this);
    }

    private void inputStudentInfo() {
        UserProfileDTO userDto = new UserProfileDTO();
        userDto.setUserFirstName(added_student_name.getText().toString());
        userDto.setUserAddress(added_student_address.getText().toString());
        String amount = added_student_payment_amount.getText().toString();
        int payment_amount = 0;
        if (amount.length() > 0) {
            payment_amount = Integer.parseInt(amount);
        }
        userDto.setMonthlyFee(payment_amount);
        userDto.setUserMobilePhone(added_student_mobile_phn.getText().toString());
        userDto.setUserInstituteName(added_student_institution.getText().toString());

        if (userDto.getUserFirstName().length() > 0) {
            mAddedStudentList.add(userDto);
        } else {
            Toast.makeText(AddNewStudentActivity.this, "Please, Insert Student Name", Toast.LENGTH_SHORT).show();
        }
        mAddedStudentAdapter.notifyDataSetChanged();
        resetInputFields();
    }

    private void sendAddedStudentList() {
        if (mAddedStudentList.size() < 1) {
            UserProfileDTO userDto = new UserProfileDTO();
            userDto.setUserFirstName(added_student_name.getText().toString());
            userDto.setUserAddress(added_student_address.getText().toString());
            String amount = added_student_payment_amount.getText().toString();
            int payment_amount = 0;
            if (amount.length() > 0) {
                payment_amount = Integer.parseInt(amount);
            }
            userDto.setMonthlyFee(payment_amount);
            userDto.setUserMobilePhone(added_student_mobile_phn.getText().toString());
            userDto.setUserInstituteName(added_student_institution.getText().toString());

            if (userDto.getUserFirstName().length() > 0) {
                mAddedStudentList.add(userDto);
            } else {
                Toast.makeText(AddNewStudentActivity.this, "Please, Insert Student Name", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        Intent BackIntent = new Intent();
        HelperMethod.debugLog(AddNewBatchActivity.TAG, "mAddedStudentList : " + mAddedStudentList.size());
        BackIntent.putParcelableArrayListExtra(Constants.ADDED_STUDENT_LIST, mAddedStudentList);
        setResult(RESULT_OK, BackIntent);
        finish();
    }

    private void saveEditInfo() {

        mEditStudentDTO.setUserFirstName(added_student_name.getText().toString());
        mEditStudentDTO.setMonthlyFee(Integer.parseInt(added_student_payment_amount.getText().toString()));
        mEditStudentDTO.setUserMobilePhone(added_student_mobile_phn.getText().toString());
        mEditStudentDTO.setUserInstituteName(added_student_institution.getText().toString());
        mEditStudentDTO.setUserAddress(added_student_address.getText().toString());
//        DatabaseRequestHelper dbRequestHelper = new DatabaseRequestHelper(this);
//        dbRequestHelper.updateStudent(mEditStudentDTO);

        Intent BackIntent = new Intent();
        HelperMethod.debugLog(AddNewBatchActivity.TAG, "mAddedStudentList : " + mEditStudentDTO.getUserFirstName());
        BackIntent.putExtra(Constants.EDIT_STUDENT_DTO, mEditStudentDTO);
        setResult(RESULT_OK, BackIntent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_CODE_ADD_STUDENT) {
            if (resultCode == RESULT_OK) {
                added_student_rv.setVisibility(View.VISIBLE);
                UserProfileDTO userProfileDTO = data.getParcelableExtra(Constants.ADDED_STUDENT);
                if (userProfileDTO != null) {
                    mAddedStudentList.add(userProfileDTO);
                    mAddedStudentAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.search_user_btn:

                Intent searchUserIntent = new Intent(AddNewStudentActivity.this, SearchActivity.class);
                searchUserIntent.putExtra(Constants.ADD_STUDENT_FROM_SEARCH, true);
                startActivityForResult(searchUserIntent, Constants.REQUEST_CODE_ADD_STUDENT);
                break;

            case R.id.add_more_user_btn:
                if (isEditable) {
                    saveEditInfo();
                    return;
                }
                added_student_rv.setVisibility(View.VISIBLE);
                inputStudentInfo();
                break;

            case R.id.add_student_toolbar_btn:
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
