package entwinebits.com.teachersassistant;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;

import entwinebits.com.teachersassistant.adapter.StudentListAdapter;
import entwinebits.com.teachersassistant.db.DatabaseRequestHelper;
import entwinebits.com.teachersassistant.model.BatchDTO;
import entwinebits.com.teachersassistant.model.ScheduleDTO;
import entwinebits.com.teachersassistant.model.UserProfileDTO;
import entwinebits.com.teachersassistant.server.ServerRequestHelper;
import entwinebits.com.teachersassistant.server.ServerResponseParser;
import entwinebits.com.teachersassistant.system.DeviceConfig;
import entwinebits.com.teachersassistant.utils.ConstantFunctions;
import entwinebits.com.teachersassistant.utils.Constants;
import entwinebits.com.teachersassistant.utils.Days;
import entwinebits.com.teachersassistant.utils.DialogProvider;
import entwinebits.com.teachersassistant.utils.HelperMethod;
import entwinebits.com.teachersassistant.utils.Months;
import entwinebits.com.teachersassistant.utils.ServerConstants;

/**
 * Created by shajib on 12/23/2016.
 */
public class BatchDetailsActivity extends AppCompatActivity implements View.OnClickListener, StudentListAdapter.StudentSelectionListener {

    private String TAG = "BatchDetailsActivity";
    private FrameLayout batch_details_toolbar_back, add_student_fl;
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
    private int mTotalStudent;
    private String mBatchName;
    private boolean mIsMyBatch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        HelperMethod.debugLog(TAG, "BatchDetailsActivity : onCreate ");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batch_details_layout);

        mBatchId = getIntent().getLongExtra(Constants.BATCH_ID, -1);
        mBatchName = getIntent().getStringExtra(Constants.BATCH_NAME);
//        mWeekDays = getIntent().getStringExtra(Constants.BATCH_WEEK_DAYS);
        mScheduleList = getIntent().getParcelableArrayListExtra(Constants.BATCH_SCHEDULE_LIST);
        if (getIntent().hasExtra(Constants.IS_MY_BATCH)) {
            mIsMyBatch = getIntent().getBooleanExtra(Constants.IS_MY_BATCH, false);
        }
        HelperMethod.debugLog(TAG, "mIsMyBatch == "+mIsMyBatch);

        for (ScheduleDTO dto : mScheduleList) {
            HelperMethod.debugLog(TAG, "here "+dto.getDaysOfWeek()+" time "+dto.getStartTime()+ " id "+dto.getRoutineId()+ " sc id "+dto.getScheduleId());
        }
        if (mBatchId == -1) {
            Toast.makeText(this, "Error Occured : " + getClass().getName(), Toast.LENGTH_SHORT).show();
            finish();
        }
        initData();
        initToolbar();
        initLayout();
        loadStudentList();

        DeviceConfig deviceConfig = new DeviceConfig(this);
        deviceConfig.getDeviceDensity();
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
        JSONObject jsonObject = ServerRequestHelper.sendBatchStudentListRequest(mBatchId);
        HelperMethod.debugLog(TAG, "loadUserBatchList batch id == " + mBatchId);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Constants.REQUEST_URL, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        HelperMethod.debugLog(TAG, response.toString());

                        if (!response.optBoolean(ServerConstants.ERROR)) {
                            final ArrayList<UserProfileDTO> studentList = ServerResponseParser.parseBatchStudentListResponse(response);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    mTotalStudent = studentList.size();
                                    HelperMethod.debugLog(TAG, "studentList size = " + studentList.size());
                                    mStudentList.clear();
                                    mStudentList.addAll(studentList);

                                    if (studentList != null && studentList.size() > 0) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                total_student_tv.setText("" + mTotalStudent);

                                                if (mStudentListAdapter == null) {
                                                    mStudentListAdapter = new StudentListAdapter(BatchDetailsActivity.this, mIsMyBatch, mStudentList, mBatchId, BatchDetailsActivity.this);
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
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjReq);

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
            String startTime = ConstantFunctions.getDate(dto.getStartTime(), Constants.TIME_12_HOUR_FORMAT);
            String endTime = ConstantFunctions.getDate(dto.getEndTime(), Constants.TIME_12_HOUR_FORMAT);

            batch_schedule_from_tv.setText(startTime);
            batch_schedule_to_tv.setText(endTime);

            batch_details_schedule_ll.addView(inflatedView);
        }
    }

    private void initLayout() {
        if (mIsMyBatch) {
            add_student_fl = (FrameLayout) findViewById(R.id.add_student_fl);
            add_student_fl.setOnClickListener(this);
            add_student_fl.setVisibility(View.VISIBLE);
            batch_details_edit_iv = (ImageView) findViewById(R.id.batch_details_edit_iv);
            batch_details_edit_iv.setOnClickListener(this);
            batch_details_edit_iv.setVisibility(View.VISIBLE);
        }

        batch_details_schedule_ll = (LinearLayout)findViewById(R.id.batch_details_schedule_ll);
        initScheduleLayout();

        student_list_rv = (RecyclerView) findViewById(R.id.student_list_rv);
        student_list_rv.setLayoutManager(new LinearLayoutManager(this));

        total_student_tv = (TextView) findViewById(R.id.total_student_tv);
    }

    private void initData() {
        mStudentList = new ArrayList<>();
    }

    private void sendAddNewStudentRequest(ArrayList<UserProfileDTO> addedStudentList) {

        JSONObject jsonObject = ServerRequestHelper.sendAddNewStudentRequest((int)mBatchId, addedStudentList, new ArrayList<UserProfileDTO>());
        HelperMethod.debugLog(TAG, "sendAddNewStudentRequest  "+ jsonObject.toString());
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Constants.REQUEST_URL, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        HelperMethod.debugLog(TAG, response.toString());
                        if (!response.optBoolean(ServerConstants.ERROR) ) {
                            Toast.makeText(BatchDetailsActivity.this, "New Students Added.", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        HelperMethod.debugLog(TAG, "sendAddNewStudentRequest : " + error.getMessage());
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjReq);
        HelperMethod.debugLog(TAG, "sendAddNewStudentRequest Full : req == "+jsonObject.toString());

    }

    private void sendStudentRemoveRequest(int studentId) {

        JSONObject jsonObject = ServerRequestHelper.sendStudentRemoveRequest((int)mBatchId, studentId);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Constants.REQUEST_URL, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        HelperMethod.debugLog(TAG, response.toString());
                        if (!response.optBoolean(ServerConstants.ERROR) ) {
                            Toast.makeText(BatchDetailsActivity.this, "student Removed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        HelperMethod.debugLog(TAG, "sendStudentRemoveRequest : " + error.getMessage());
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjReq);
        HelperMethod.debugLog(TAG, "sendStudentRemoveRequest Full : req == "+jsonObject.toString());
    }

    private void deleteBatchRequest() {

        JSONObject jsonObject = ServerRequestHelper.sendDeleteBatchRequest((int)mBatchId);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Constants.REQUEST_URL, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        HelperMethod.debugLog(TAG, response.toString());
                        if (!response.optBoolean(ServerConstants.ERROR) ) {
                            Toast.makeText(BatchDetailsActivity.this, "Batch Deleted.", Toast.LENGTH_SHORT).show();
                            BatchDetailsActivity.this.finish();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        HelperMethod.debugLog(TAG, "deleteBatchRequest : " + error.getMessage());
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjReq);
        HelperMethod.debugLog(TAG, "sendStudentRemoveRequest Full : req == "+jsonObject.toString());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 120) {
            if (resultCode == RESULT_OK) {
                if (data.hasExtra(Constants.EDIT_STUDENT_DTO)) {

                    UserProfileDTO editedDto = data.getParcelableExtra(Constants.EDIT_STUDENT_DTO);
                    HelperMethod.debugLog(TAG, "EDIT_STUDENT_DTO : "+editedDto.getUserFirstName());
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

                sendAddNewStudentRequest(addedStuList);

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
                    mStudentListAdapter = new StudentListAdapter(BatchDetailsActivity.this, mIsMyBatch, mStudentList, mBatchId, this);
                    student_list_rv.setAdapter(mStudentListAdapter);
                } else {
                    mStudentListAdapter.notifyDataSetChanged();
                }

                mTotalStudent += addedStuList.size();
                total_student_tv.setText("" + mTotalStudent);

            }
        } else if (requestCode == 130) {
            if (resultCode == RESULT_OK) {

                batch_details_toolbar_title.setText(data.getStringExtra(Constants.BATCH_NAME));

            }
        }
    }

    private void showDeleteBatchConfirmation() {

        String upperText = "OK", lowerText = "Cancel";
        View.OnClickListener upperListener, lowerListener;
        upperListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteBatchRequest();
            }
        };
        lowerListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        };
        DialogProvider.showDoubleOptionDialog(this, upperText, lowerText, upperListener, lowerListener);


    }

    private void editBatchInfo() {
        String upperText = "Edit", lowerText = "Delete";
        View.OnClickListener upperListener, lowerListener;
        upperListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BatchDetailsActivity.this, EditBatchActivity.class);
                intent.putExtra(Constants.BATCH_ID, mBatchId);
                intent.putExtra(Constants.BATCH_NAME, mBatchName);
                intent.putExtra(Constants.BATCH_SCHEDULE_LIST, mScheduleList);
                startActivityForResult(intent, 130);
            }
        };
        lowerListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteBatchConfirmation();
            }
        };
        DialogProvider.showDoubleOptionDialog(this, upperText, lowerText, upperListener, lowerListener);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.batch_details_edit_iv:

                editBatchInfo();
                break;

            case R.id.add_student_fl:
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        HelperMethod.debugLog(TAG, "BatchDetailsActivity : onDestroy ");
    }

    @Override
    public void onStudentSelected(UserProfileDTO dto) {
        sendStudentRemoveRequest(dto.getUserId());
    }


/*
        new Thread(new Runnable() {
            @Override
            public void run() {
                final ArrayList<UserProfileDTO> studentList = dbRequestHelper.getStudentListByBatch((int) mBatchId);
//                final ArrayList<UserProfileDTO> studentList = dbRequestHelper.getStudentListByBatch((int) mBatchId);
                mTotalStudent = studentList.size();
                HelperMethod.debugLog(TAG, "loadBatchList size = " + studentList.size());
                mStudentList.clear();
                mStudentList.addAll(studentList);

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

*/

/*
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
*/
}
