package entwinebits.com.teachersassistant;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import entwinebits.com.teachersassistant.adapter.StudentListAdapter;
import entwinebits.com.teachersassistant.db.DatabaseRequestHelper;
import entwinebits.com.teachersassistant.model.BatchDTO;
import entwinebits.com.teachersassistant.model.ScheduleDTO;
import entwinebits.com.teachersassistant.model.UserProfileDTO;
import entwinebits.com.teachersassistant.utils.Constants;
import entwinebits.com.teachersassistant.utils.HelperMethod;

/**
 * Created by shajib on 12/23/2016.
 */
public class BatchDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "BatchDetailsActivity";
    private FrameLayout batch_details_toolbar_back, add_student_done_btn;
    private TextView batch_details_toolbar_title;
    private RecyclerView student_list_rv;
    private StudentListAdapter mStudentListAdapter;
    private ArrayList<UserProfileDTO> mStudentList;
    private long mBatchId ;
    private DatabaseRequestHelper dbRequestHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batch_details_layout);

        mBatchId = getIntent().getLongExtra(Constants.BATCH_ID, -1);
        if (mBatchId == -1) {
            Toast.makeText(this, "Error Occured : "+getClass().getName(), Toast.LENGTH_SHORT).show();
            finish();
        }
        initData();
        initToolbar();
        initLayout();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadStudentList();
    }

    private void loadStudentList() {
        if (dbRequestHelper == null) {
            dbRequestHelper = new DatabaseRequestHelper(this);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                final ArrayList<UserProfileDTO> studentList = dbRequestHelper.getStudentListByBatch((int) mBatchId);
                HelperMethod.debugLog(TAG, "loadBatchList size = "+studentList.size());
                mStudentList.clear();
                mStudentList.addAll(studentList);
                for (UserProfileDTO dto : studentList) {
                    HelperMethod.debugLog(TAG, "After db read : id : "+dto.getUserId()+ " name : "+dto.getUserName()+
                            " id "+dto.getMonthlyFee() + "mob = "+dto.getUserMobilePhone());
                }


                if (studentList != null && studentList.size() > 0) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mStudentListAdapter == null) {
                                mStudentListAdapter = new StudentListAdapter(BatchDetailsActivity.this, mStudentList);
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

    private void initLayout() {

        student_list_rv = (RecyclerView)findViewById(R.id.student_list_rv);
        student_list_rv.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initData() {
        mStudentList = new ArrayList<>();
    }

    private void initToolbar() {
        batch_details_toolbar_title = (TextView) findViewById(R.id.batch_details_toolbar_title);
        batch_details_toolbar_title.setText(getIntent().getStringExtra(Constants.BATCH_NAME));

        batch_details_toolbar_back = (FrameLayout) findViewById(R.id.batch_details_toolbar_back);
        batch_details_toolbar_back.setOnClickListener(this);
//        add_student_done_btn = (FrameLayout) findViewById(R.id.add_student_done_btn);
//        add_student_done_btn.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.batch_details_toolbar_back:
                finish();
                break;
        }
    }
}