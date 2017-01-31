package entwinebits.com.teachersassistant;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import entwinebits.com.teachersassistant.adapter.StudentListAdapter;
import entwinebits.com.teachersassistant.db.DatabaseRequestHelper;
import entwinebits.com.teachersassistant.model.BatchDTO;
import entwinebits.com.teachersassistant.model.ScheduleDTO;
import entwinebits.com.teachersassistant.model.UserProfileDTO;
import entwinebits.com.teachersassistant.utils.Constants;
import entwinebits.com.teachersassistant.utils.Days;
import entwinebits.com.teachersassistant.utils.DialogProvider;
import entwinebits.com.teachersassistant.utils.HelperMethod;
import entwinebits.com.teachersassistant.utils.Months;

/**
 * Created by shajib on 12/23/2016.
 */
public class BatchDetailsActivity extends AppCompatActivity implements View.OnClickListener {


    private String TAG = "StudentDetailsActivity";
    private FrameLayout batch_details_toolbar_back, add_student_done_btn;
    private TextView batch_details_toolbar_title;
    private ImageView batch_details_edit_iv;

    private LinearLayout batch_details_schedule_ll;

    private RecyclerView student_list_rv;
    private StudentListAdapter mStudentListAdapter;
    private ArrayList<UserProfileDTO> mStudentList;
    private long mBatchId;
    private DatabaseRequestHelper dbRequestHelper;
    private ArrayList<ScheduleDTO> mScheduleList;

    private TextView total_student_tv;
    private ImageView add_student_iv;
    private int mTotalStudent;

    private String mBatchName;
//    private String mWeekDays;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        HelperMethod.debugLog(TAG, "BatchDetailsActivity : onCreate ");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batch_details_layout);

        mBatchId = getIntent().getLongExtra(Constants.BATCH_ID, -1);
        mBatchName = getIntent().getStringExtra(Constants.BATCH_NAME);
//        mWeekDays = getIntent().getStringExtra(Constants.BATCH_WEEK_DAYS);
        mScheduleList = getIntent().getParcelableArrayListExtra(Constants.BATCH_SCHEDULE_LIST);
        for (ScheduleDTO dto : mScheduleList) {
            HelperMethod.debugLog(TAG, "here "+dto.getDaysOfWeek()+" time "+dto.getStartTime());
        }
        if (mBatchId == -1) {
            Toast.makeText(this, "Error Occured : " + getClass().getName(), Toast.LENGTH_SHORT).show();
            finish();
        }
        initData();
        initToolbar();
        initLayout();
        loadStudentList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        HelperMethod.debugLog(TAG, "BatchDetailsActivity : onResume ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        HelperMethod.debugLog(TAG, "BatchDetailsActivity : onPause ");

    }

    private void loadStudentList() {
        if (dbRequestHelper == null) {
            dbRequestHelper = new DatabaseRequestHelper(this);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                final ArrayList<UserProfileDTO> studentList = dbRequestHelper.getStudentListByBatch((int) mBatchId);
                mTotalStudent = studentList.size();
                HelperMethod.debugLog(TAG, "loadBatchList size = " + studentList.size());
                mStudentList.clear();
                mStudentList.addAll(studentList);
                for (UserProfileDTO dto : studentList) {
//                    HelperMethod.debugLog(TAG, "After db read : id : " + dto.getUserId() + " name : " + dto.getUserName() +
//                            " id " + dto.getMonthlyFee() + "mob = " + dto.getUserMobilePhone());
                }


                if (studentList != null && studentList.size() > 0) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            total_student_tv.setText("" + mTotalStudent);

                            if (mStudentListAdapter == null) {
                                mStudentListAdapter = new StudentListAdapter(BatchDetailsActivity.this, mStudentList, mBatchId);
                                student_list_rv.setAdapter(mStudentListAdapter);
                            } else {
                                mStudentListAdapter.notifyDataSetChanged();
                            }
                        }
                    });
                }
            }
        }).start();

    }


    private void initScheduleLayout() {

        for (ScheduleDTO dto : mScheduleList) {
            View inflatedView = LayoutInflater.from(this).inflate(R.layout.batch_schedule_layout, batch_details_schedule_ll, false);
            TextView week_day_tv;
            TextView batch_schedule_from_tv, batch_schedule_to_tv;

            week_day_tv = (TextView) inflatedView.findViewById(R.id.week_day_tv);
            batch_schedule_from_tv = (TextView) inflatedView.findViewById(R.id.batch_schedule_from_tv);
            batch_schedule_to_tv = (TextView) inflatedView.findViewById(R.id.batch_schedule_to_tv);

            week_day_tv.setText(Days.get(dto.getDaysOfWeek() + 1) + "");
            batch_schedule_from_tv.setText(dto.getStartTime());
            batch_schedule_to_tv.setText(dto.getEndTime());

            batch_details_schedule_ll.addView(inflatedView);
        }
    }

    private void initLayout() {

        batch_details_edit_iv = (ImageView) findViewById(R.id.batch_details_edit_iv);
        batch_details_edit_iv.setOnClickListener(this);

        batch_details_schedule_ll = (LinearLayout)findViewById(R.id.batch_details_schedule_ll);
        initScheduleLayout();


//        String[] weekDays = mWeekDays.split(",");
//        StringBuilder builder = new StringBuilder();
//        for (String s: weekDays) {
//            builder.append(s);
//            builder.append("  ");
//        }

        student_list_rv = (RecyclerView) findViewById(R.id.student_list_rv);
        student_list_rv.setLayoutManager(new LinearLayoutManager(this));


//        batch_schedule_tv.setText(builder.toString().trim());
        total_student_tv = (TextView) findViewById(R.id.total_student_tv);
        add_student_iv = (ImageView) findViewById(R.id.add_student_iv);
        add_student_iv.setOnClickListener(this);
    }

    private void initData() {
        mStudentList = new ArrayList<>();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 120) {
            if (resultCode == RESULT_OK) {
                if (data.hasExtra(Constants.EDIT_STUDENT_DTO)) {

                    UserProfileDTO editedDto = data.getParcelableExtra(Constants.EDIT_STUDENT_DTO);
                    HelperMethod.debugLog(TAG, "EDIT_STUDENT_DTO : "+editedDto.getUserName());
                    if (dbRequestHelper == null) {
                        dbRequestHelper = new DatabaseRequestHelper(this);
                    }
                    dbRequestHelper.updateStudent(editedDto);
                    int idx = 0 ;
                    for (int i =0; i< mStudentList.size();i++) {
                        if (mStudentList.get(i).getUserId() == editedDto.getUserId()) {
                            idx = i;
                            break;
                        }
                    }
                    mStudentList.remove(idx);
                    mStudentList.add(idx, editedDto);
                    mStudentListAdapter.notifyDataSetChanged();
                    return;
                }

                ArrayList<UserProfileDTO> addedStuList = data.getParcelableArrayListExtra(Constants.ADDED_STUDENT_LIST);
                if (dbRequestHelper == null) {
                    dbRequestHelper = new DatabaseRequestHelper(this);
                }
                for (UserProfileDTO dto : addedStuList) {
                    dto.setBatchId(mBatchId);
                    dto.setTeacher(false);
                    long id = dbRequestHelper.addStudent(dto);
                    dto.setUserId((int) id);
                }

                mStudentList.addAll(addedStuList);
                HelperMethod.debugLog(TAG, "onActivityResult : after added, size = " + mStudentList.size());

                if (mStudentListAdapter == null) {
                    mStudentListAdapter = new StudentListAdapter(BatchDetailsActivity.this, mStudentList, mBatchId);
                    student_list_rv.setAdapter(mStudentListAdapter);
                } else {
                    mStudentListAdapter.notifyDataSetChanged();
                }

                mTotalStudent += addedStuList.size();
                total_student_tv.setText("" + mTotalStudent);

//                if (dbRequestHelper == null) {
//                    dbRequestHelper = new DatabaseRequestHelper(this);
//                }
//                for (UserProfileDTO dto : addedStuList) {
//                    dto.setBatchId(mBatchId);
//                    dto.setTeacher(false);
//                    dbRequestHelper.addStudent(dto);
//                }

            }
        } else if (requestCode == 130) {
            if (resultCode == RESULT_OK) {

                batch_details_toolbar_title.setText(data.getStringExtra(Constants.BATCH_NAME));

            }
        }
    }

    private void editBatchInfo() {
        String upperText = "Edit", lowerText = "Delete";
        View.OnClickListener upperListener, lowerListener;
        upperListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BatchDetailsActivity.this, AddNewBatchActivity.class);
                intent.putExtra(Constants.BATCH_ID, mBatchId);
                intent.putExtra(Constants.BATCH_NAME, mBatchName);
                intent.putExtra(Constants.BATCH_SCHEDULE_LIST, mScheduleList);
                startActivityForResult(intent, 130);
            }
        };
        lowerListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {}
        };
        DialogProvider.showDoubleOptionDialog(this, upperText, lowerText, upperListener, lowerListener);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.batch_details_edit_iv:

                editBatchInfo();
                break;

            case R.id.add_student_iv:
                Intent addIntent = new Intent(BatchDetailsActivity.this, AddNewStudentActivity.class);
                addIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivityForResult(addIntent, 120);

                break;
            case R.id.batch_details_toolbar_back:
                BatchDetailsActivity.this.finish();
                break;
        }
    }

    private void initToolbar() {
        batch_details_toolbar_title = (TextView) findViewById(R.id.batch_details_toolbar_title);
        batch_details_toolbar_title.setText(mBatchName);

        batch_details_toolbar_back = (FrameLayout) findViewById(R.id.batch_details_toolbar_back);
        batch_details_toolbar_back.setOnClickListener(this);
//        add_student_done_btn = (FrameLayout) findViewById(R.id.add_student_done_btn);
//        add_student_done_btn.setOnClickListener(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        HelperMethod.debugLog(TAG, "BatchDetailsActivity : onDestroy ");
    }
}
