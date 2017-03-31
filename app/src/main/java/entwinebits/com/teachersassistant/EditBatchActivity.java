package entwinebits.com.teachersassistant;

import android.app.TimePickerDialog;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import entwinebits.com.teachersassistant.adapter.StudentListAdapter;
import entwinebits.com.teachersassistant.model.ScheduleDTO;
import entwinebits.com.teachersassistant.model.UserProfileDTO;
import entwinebits.com.teachersassistant.server.ServerRequestHelper;
import entwinebits.com.teachersassistant.server.ServerResponseParser;
import entwinebits.com.teachersassistant.utils.ConstantFunctions;
import entwinebits.com.teachersassistant.utils.Constants;
import entwinebits.com.teachersassistant.utils.DateTimeFormatHelper;
import entwinebits.com.teachersassistant.utils.HelperMethod;
import entwinebits.com.teachersassistant.utils.ServerConstants;

/**
 * Created by shajib on 3/30/2017.
 */
public class EditBatchActivity extends AppCompatActivity implements View.OnClickListener {

    public static String TAG = "EditBatchActivity";
    private Calendar mCalendar;
    private FrameLayout edit_batch_toolbar_back, edit_batch_save_btn;
    private TextView edit_batch_toolbar_title;

    private TextView batch_title_tv;
    private EditText batch_title_et;
    private FrameLayout batch_title_save_btn, batch_title_edit_btn, batch_title_cancel_btn;
    private FrameLayout schedule_edit_btn, schedule_cancel_btn;

    private long mBatchId;
    private String mBatchName;
    private ArrayList<ScheduleDTO> mScheduleList, mAddedScheduleList;
    private ScheduleDTO mCurrentSchedule;

    private List<TextView> editTextTimeFrom;
    private List<TextView> editTextTimeTo;
    private List<TextView> dayOfWeekList;
    private List<LinearLayout> dayViewLayout;
    private ArrayList<FrameLayout> mScheduleCancelIvList, mScheduleSubmitIvList, mScheduleDeleteList, mScheduleEditList;
    String[] dayName = {"Sat", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri"};
    TimePickerDialog.OnTimeSetListener onTimeSetListener;
    int day;
    boolean isStartTime = true;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_batch_layout);
        initToolbar();

        mBatchId = getIntent().getLongExtra(Constants.BATCH_ID, -1);
        mBatchName = getIntent().getStringExtra(Constants.BATCH_NAME);
        mScheduleList = getIntent().getParcelableArrayListExtra(Constants.BATCH_SCHEDULE_LIST);
        mAddedScheduleList = new ArrayList<>(mScheduleList);
        HelperMethod.debugLog(TAG, "mAddedScheduleList size "+mAddedScheduleList.size());
        mCurrentSchedule = new ScheduleDTO();
        mCalendar = Calendar.getInstance();
        initLayout();
        initTimeSetLayout();
        setTimeSetters();
        initDayOfWeek();
        setEditModeLayout();
    }

    private void initLayout() {
        batch_title_tv = (TextView) findViewById(R.id.batch_title_tv);
        batch_title_et = (EditText) findViewById(R.id.batch_title_et);
        batch_title_tv.setText(mBatchName);

        batch_title_save_btn = (FrameLayout)findViewById(R.id.batch_title_save_btn);
        batch_title_save_btn.setOnClickListener(this);
        batch_title_edit_btn = (FrameLayout)findViewById(R.id.batch_title_edit_btn);
        batch_title_edit_btn.setOnClickListener(this);
        batch_title_cancel_btn = (FrameLayout)findViewById(R.id.batch_title_cancel_btn);
        batch_title_cancel_btn.setOnClickListener(this);

        schedule_edit_btn = (FrameLayout)findViewById(R.id.schedule_edit_btn);
        schedule_edit_btn.setOnClickListener(this);
        schedule_cancel_btn = (FrameLayout)findViewById(R.id.schedule_cancel_btn);
        schedule_cancel_btn.setOnClickListener(this);
    }

    private void initToolbar() {
        edit_batch_toolbar_title = (TextView) findViewById(R.id.edit_batch_toolbar_title);
        edit_batch_toolbar_title.setText("Edit Batch");
        edit_batch_toolbar_back = (FrameLayout) findViewById(R.id.edit_batch_toolbar_back);
        edit_batch_toolbar_back.setOnClickListener(this);
        edit_batch_save_btn = (FrameLayout) findViewById(R.id.edit_batch_save_btn);
        edit_batch_save_btn.setOnClickListener(this);
    }

    private void updateBatchTitle(final String title) {
        JSONObject jsonObject = ServerRequestHelper.sendBatchTitleUpdateRequest(title, mBatchId);
        HelperMethod.debugLog(TAG, "updateBatchTitle batch id == "+mBatchId);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Constants.REQUEST_URL, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        HelperMethod.debugLog(TAG, response.toString());

                        if (!response.optBoolean(ServerConstants.ERROR)) {
                            batch_title_tv.setText(title);
                            batch_title_et.setText("");
                            batch_title_et.setVisibility(View.GONE);
                            batch_title_tv.setVisibility(View.VISIBLE);
                            batch_title_edit_btn.setVisibility(View.VISIBLE);
                            batch_title_cancel_btn.setVisibility(View.GONE);
                            batch_title_save_btn.setVisibility(View.GONE);
                            Toast.makeText(EditBatchActivity.this, "Batch Name Updated", Toast.LENGTH_SHORT).show();
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

    private void setEditModeLayout() {
        HelperMethod.debugLog(TAG, "setEditModeLayout : size = " + mScheduleList.size());
        for (ScheduleDTO dto : mScheduleList) {
            int day = dto.getDaysOfWeek();
            findViewById(R.id.scroll).setVisibility(View.VISIBLE);
            findViewById(dayViewLayout.get(day).getId()).setVisibility(View.VISIBLE);

            String startTime = ConstantFunctions.getDate(dto.getStartTime(), Constants.TIME_12_HOUR_FORMAT);
            String endTime = ConstantFunctions.getDate(dto.getEndTime(), Constants.TIME_12_HOUR_FORMAT);
            editTextTimeFrom.get(day).setText(startTime);
            editTextTimeTo.get(day).setText(endTime);
        }
    }


    private void updateScheduleRequest(final ScheduleDTO dto) {
        HelperMethod.debugLog(TAG, "updateScheduleRequest == "+dto.getDaysOfWeek());
        JSONObject jsonObject = ServerRequestHelper.sendBatchScheduleUpdateRequest(mBatchId, dto);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Constants.REQUEST_URL, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        HelperMethod.debugLog(TAG, response.toString());
                        if (!response.optBoolean(ServerConstants.ERROR) ) {
                            Toast.makeText(EditBatchActivity.this, "Schedule Updated.", Toast.LENGTH_SHORT).show();
                            int day = dto.getDaysOfWeek();
                            editTextTimeFrom.get(day).setEnabled(false);
                            editTextTimeTo.get(day).setEnabled(false);
                            mScheduleCancelIvList.get(day).setVisibility(View.GONE);
                            mScheduleSubmitIvList.get(day).setVisibility(View.GONE);
                            mScheduleEditList.get(day).setVisibility(View.VISIBLE);
                            mScheduleDeleteList.get(day).setVisibility(View.VISIBLE);

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        HelperMethod.debugLog(TAG, "updateScheduleRequest : " + error.getMessage());
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjReq);
    }

    private void addScheduleRequest(final ScheduleDTO dto) {
        HelperMethod.debugLog(TAG, "addScheduleRequest = "+dto.getDaysOfWeek());
        JSONObject jsonObject = ServerRequestHelper.sendBatchScheduleAddRequest(mBatchId, dto);

        HelperMethod.debugLog(TAG, "addScheduleRequest = "+jsonObject.toString());
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Constants.REQUEST_URL, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        HelperMethod.debugLog(TAG, response.toString());
                        if (!response.optBoolean(ServerConstants.ERROR) ) {
                            Toast.makeText(EditBatchActivity.this, "New Schedule Added.", Toast.LENGTH_SHORT).show();
                            mAddedScheduleList.add(dto);
                            int day = dto.getDaysOfWeek();
                            editTextTimeFrom.get(day).setEnabled(false);
                            editTextTimeTo.get(day).setEnabled(false);
                            mScheduleCancelIvList.get(day).setVisibility(View.GONE);
                            mScheduleSubmitIvList.get(day).setVisibility(View.GONE);
                            mScheduleEditList.get(day).setVisibility(View.VISIBLE);
                            mScheduleDeleteList.get(day).setVisibility(View.VISIBLE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        HelperMethod.debugLog(TAG, "addScheduleList : " + error.getMessage());
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjReq);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.day_sat:
            case R.id.day_sun:
            case R.id.day_mon:
            case R.id.day_tue:
            case R.id.day_wed:
            case R.id.day_thu:
            case R.id.day_fri:
                for (int i = 0; i < dayOfWeekList.size(); i++) {
                    if (v.getId() == dayOfWeekList.get(i).getId()) {
                        day = i;
                        break;
                    }
                }
                findViewById(R.id.scroll).setVisibility(View.VISIBLE);
                dayViewLayout.get(day).setVisibility(View.VISIBLE);
//                findViewById(dayViewLayout.get(day).getId()).setVisibility(View.VISIBLE);
//                makeVisibleAnimation(findViewById(dayViewLayout.get(day).getId()));

                break;


            case R.id.schedule_edit_btn:
                schedule_cancel_btn.setVisibility(View.VISIBLE);
                schedule_edit_btn.setVisibility(View.GONE);
                for (int i = 0; i < dayOfWeekList.size(); i++) {
                    dayOfWeekList.get(i).setEnabled(true);
                }

                break;

            case R.id.edit_batch_save_btn:

                break;

            case R.id.batch_title_cancel_btn:
                batch_title_et.setText("");
                batch_title_edit_btn.setVisibility(View.VISIBLE);
                batch_title_cancel_btn.setVisibility(View.GONE);
                batch_title_save_btn.setVisibility(View.GONE);
                batch_title_tv.setVisibility(View.VISIBLE);
                batch_title_et.setVisibility(View.GONE);
                break;

            case R.id.batch_title_edit_btn:
                batch_title_et.setVisibility(View.VISIBLE);
                batch_title_tv.setVisibility(View.GONE);
                batch_title_edit_btn.setVisibility(View.GONE);
                batch_title_save_btn.setVisibility(View.VISIBLE);
                batch_title_cancel_btn.setVisibility(View.VISIBLE);
                break;

            case R.id.batch_title_save_btn:
                String update_title = batch_title_et.getText().toString();
                HelperMethod.debugLog(TAG, update_title+" "+mBatchName+" "+!update_title.equalsIgnoreCase(mBatchName));
                if ( update_title.length() > 0 && !update_title.equalsIgnoreCase(mBatchName)) {

                    updateBatchTitle(update_title);
                } else {
                    Toast.makeText(this, "Please Input Valid Title", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.edit_batch_toolbar_back:
                EditBatchActivity.this.finish();
                break;
        }
    }

    private void setTimeSetters() {
        onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int min) {
                ScheduleDTO scheduleDTO = null;
                boolean found = false;
                HelperMethod.debugLog(TAG, "day == " + day + " mScheduleList size = " + mScheduleList.size());
                for (ScheduleDTO dto : mScheduleList) {
                    if (dto.getDaysOfWeek() == day) {
                        HelperMethod.debugLog(TAG, "dto == " + dto.getDaysOfWeek() + " day = " + day);
                        scheduleDTO = dto;
                        found = true;
                    }
                }
                if (!found) {
                    scheduleDTO = new ScheduleDTO();
                }
                scheduleDTO.setDaysOfWeek(day);
                Calendar c = Calendar.getInstance();

                if (isStartTime) {
                    editTextTimeFrom.get(day).setText(DateTimeFormatHelper.convertTimeFormatTo12Hour(hour, min));
                    c.set(Calendar.HOUR_OF_DAY, hour);
                    c.set(Calendar.MINUTE, min);

                    long startLong = c.getTime().getTime();
                    HelperMethod.debugLog(TAG, "start long = "+startLong);
                    scheduleDTO.setStartTime(startLong);

                } else {
                    editTextTimeTo.get(day).setText(DateTimeFormatHelper.convertTimeFormatTo12Hour(hour, min));
                    c.set(Calendar.HOUR_OF_DAY, hour);
                    c.set(Calendar.MINUTE, min);

                    long endLong = c.getTime().getTime();
                    HelperMethod.debugLog(TAG, " end long = "+endLong);
                    scheduleDTO.setEndTime(endLong);
                }
                mCurrentSchedule = scheduleDTO;
                if (!found) {
                    mScheduleList.add(scheduleDTO);
                }
            }
        };

        editTextTimeFrom = new ArrayList<>();
        editTextTimeTo = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            editTextTimeFrom.add((TextView) dayViewLayout.get(i).findViewById(R.id.edit_txt_day_name_from));
            editTextTimeTo.add((TextView) dayViewLayout.get(i).findViewById(R.id.edit_txt_day_name_to));
            editTextTimeFrom.get(i).setEnabled(false);
            editTextTimeTo.get(i).setEnabled(false);

            ((TextView) dayViewLayout.get(i).findViewById(R.id.add_day_name)).setText(dayName[i]);

            final int finalI = i;
            editTextTimeFrom.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    day = finalI;
                    isStartTime = true;
                    new TimePickerDialog(EditBatchActivity.this, onTimeSetListener,
                            mCalendar.get(Calendar.HOUR_OF_DAY), mCalendar.get(Calendar.MINUTE), false).show();
                }
            });
            editTextTimeTo.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    day = finalI;
                    isStartTime = false;
                    new TimePickerDialog(EditBatchActivity.this, onTimeSetListener,
                            mCalendar.get(Calendar.HOUR_OF_DAY), mCalendar.get(Calendar.MINUTE), false).show();
                }
            });
        }
    }

    private void editSchedule(int i) {
        HelperMethod.debugLog(TAG, "editSchedule == " + i );
        mCurrentSchedule = new ScheduleDTO();
        mScheduleSubmitIvList.get(i).setVisibility(View.VISIBLE);
        mScheduleCancelIvList.get(i).setVisibility(View.VISIBLE);
        mScheduleDeleteList.get(i).setVisibility(View.GONE);
        mScheduleEditList.get(i).setVisibility(View.GONE);

        editTextTimeFrom.get(i).setEnabled(true);
        editTextTimeTo.get(i).setEnabled(true);
    }

    private void initTimeSetLayout() {
        dayViewLayout = new ArrayList<>();
        mScheduleCancelIvList = new ArrayList<>();
        mScheduleSubmitIvList = new ArrayList<>();
        mScheduleDeleteList = new ArrayList<>();
        mScheduleEditList = new ArrayList<>();

        dayViewLayout.add((LinearLayout) findViewById(R.id.layout_sat));
        dayViewLayout.add((LinearLayout) findViewById(R.id.layout_sun));
        dayViewLayout.add((LinearLayout) findViewById(R.id.layout_mon));
        dayViewLayout.add((LinearLayout) findViewById(R.id.layout_tue));
        dayViewLayout.add((LinearLayout) findViewById(R.id.layout_wed));
        dayViewLayout.add((LinearLayout) findViewById(R.id.layout_thu));
        dayViewLayout.add((LinearLayout) findViewById(R.id.layout_fri));

        for (int i = 0; i < 7; i++) {

            mScheduleCancelIvList.add((FrameLayout) dayViewLayout.get(i).findViewById(R.id.schedule_cancel_fl));
            mScheduleSubmitIvList.add((FrameLayout) dayViewLayout.get(i).findViewById(R.id.schedule_submit_fl));
            mScheduleDeleteList.add((FrameLayout) dayViewLayout.get(i).findViewById(R.id.schedule_delete_fl));
            mScheduleEditList.add((FrameLayout) dayViewLayout.get(i).findViewById(R.id.schedule_edit_fl));

            final int finalI = i;
            mScheduleDeleteList.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HelperMethod.debugLog(TAG, "deleteSchedule == "+finalI+" mScheduleList size "+mScheduleList.size());
                    if (mScheduleList.size() < 2) {
                        Toast.makeText(EditBatchActivity.this, "You cannot delete last routine", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    showDeleteConfirmationDialog(finalI);
                    deleteSchedule(finalI);
                }
            });
            mScheduleEditList.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editSchedule(finalI);
                }
            });
            mScheduleCancelIvList.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mCurrentSchedule = null;
                    editTextTimeTo.get(finalI).setText("TO");
                    editTextTimeFrom.get(finalI).setText("FROM");
                    editTextTimeTo.get(finalI).setEnabled(false);
                    editTextTimeFrom.get(finalI).setEnabled(false);
                    mScheduleEditList.get(finalI).setVisibility(View.VISIBLE);
                    mScheduleDeleteList.get(finalI).setVisibility(View.VISIBLE);
                    mScheduleCancelIvList.get(finalI).setVisibility(View.GONE);
                    mScheduleSubmitIvList.get(finalI).setVisibility(View.GONE);
                }
            });
            mScheduleSubmitIvList.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addSchedule(finalI);
                }
            });
        }
    }

    private void showDeleteConfirmationDialog(int i) {
        boolean found = false;
        ScheduleDTO tempDto = new ScheduleDTO();
        for (ScheduleDTO dto : mScheduleList) {
            HelperMethod.debugLog(TAG, "dto == "+dto.getDaysOfWeek()+ " i = "+i);
            if (dto.getDaysOfWeek() == i) {
                found = true;
                tempDto = dto;
                break;
            }
        }
        if (found) {
            deleteScheduleRequest(tempDto);
        } else {
            Toast.makeText(this, "No Routine found", Toast.LENGTH_SHORT).show();
        }


    }

    private void deleteSchedule(int i) {

        boolean found = false;
        ScheduleDTO tempDto = new ScheduleDTO();
        for (ScheduleDTO dto : mScheduleList) {
            HelperMethod.debugLog(TAG, "dto == "+dto.getDaysOfWeek()+ " i = "+i);
            if (dto.getDaysOfWeek() == i) {
                found = true;
                tempDto = dto;
                break;
            }
        }
        if (found) {
            deleteScheduleRequest(tempDto);
        } else {
            Toast.makeText(this, "No Routine found", Toast.LENGTH_SHORT).show();
        }
    }

    private void addSchedule(int i) {
        HelperMethod.debugLog(TAG, "addSchedule == ");
        boolean found = false;
        for (ScheduleDTO dto : mAddedScheduleList) {
            if (dto.getDaysOfWeek() == mCurrentSchedule.getDaysOfWeek()) {
                found = true;
                break;
            }
        }
        if (found) {
            updateScheduleRequest(mCurrentSchedule);
        } else {
            addScheduleRequest(mCurrentSchedule);
        }
    }

    private void initDayOfWeek() {
        int[] dayColor = getResources().getIntArray(R.array.dayColor);

        dayOfWeekList = new ArrayList<>();
        dayOfWeekList.add((TextView) findViewById(R.id.day_sat));
        dayOfWeekList.add((TextView) findViewById(R.id.day_sun));
        dayOfWeekList.add((TextView) findViewById(R.id.day_mon));
        dayOfWeekList.add((TextView) findViewById(R.id.day_tue));
        dayOfWeekList.add((TextView) findViewById(R.id.day_wed));
        dayOfWeekList.add((TextView) findViewById(R.id.day_thu));
        dayOfWeekList.add((TextView) findViewById(R.id.day_fri));

        for (int i = 0; i < dayOfWeekList.size(); i++) {
            dayOfWeekList.get(i).setOnClickListener(this);
            dayOfWeekList.get(i).setEnabled(false);
            GradientDrawable bgShape = (GradientDrawable) dayOfWeekList.get(i).getBackground();
            bgShape.setColor(dayColor[i]);
        }
    }

    private void deleteScheduleRequest(final ScheduleDTO dto) {
        JSONObject jsonObject = ServerRequestHelper.sendBatchScheduleDeleteRequest(dto);

        HelperMethod.debugLog(TAG, "addScheduleRequest = "+jsonObject.toString());
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Constants.REQUEST_URL, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        HelperMethod.debugLog(TAG, response.toString());
                        if (!response.optBoolean(ServerConstants.ERROR) ) {
                            Toast.makeText(EditBatchActivity.this, "Schedule Deleted.", Toast.LENGTH_SHORT).show();
                            dayViewLayout.get(dto.getDaysOfWeek()).setVisibility(View.GONE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        HelperMethod.debugLog(TAG, "deleteScheduleRequest : " + error.getMessage());
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjReq);
    }

}
