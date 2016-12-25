package entwinebits.com.teachersassistant;

import android.app.TimePickerDialog;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import entwinebits.com.teachersassistant.adapter.AddedUserHorizontalAdapter;
import entwinebits.com.teachersassistant.db.DatabaseRequestHelper;
import entwinebits.com.teachersassistant.model.BatchDTO;
import entwinebits.com.teachersassistant.model.ScheduleDTO;
import entwinebits.com.teachersassistant.model.UserProfileDTO;
import entwinebits.com.teachersassistant.utils.Constants;
import entwinebits.com.teachersassistant.utils.DateTimeFormatHelper;
import entwinebits.com.teachersassistant.utils.HelperMethod;

/**
 * Created by Nargis Rahman on 12/2/2016.
 */
public class AddNewBatchActivity extends AppCompatActivity implements View.OnClickListener {

    public static String TAG = "AddNewBatchActivity";
    private Calendar mCalendar;
    private FrameLayout add_batch_toolbar_back, add_batch_save_btn;

    private RecyclerView addedUserRecycler;

    private List<TextView> editTextTimeFrom;
    private List<TextView> editTextTimeTo;
    private List<TextView> dayOfWeekList;
    private List<LinearLayout> dayViewLayout;
    private Button add_student_btn;

    private EditText batch_title_et;
    String[] dayName = {"Sat", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri"};
    TimePickerDialog.OnTimeSetListener onTimeSetListener;
    int day;
    boolean isStartTime = true;
    private AddedUserHorizontalAdapter addedUserAdapter;

    private ArrayList<UserProfileDTO> mAddedStudentList;
    private ArrayList<ScheduleDTO> mScheduleList;
    private BatchDTO mBatchDTO;
    private DatabaseRequestHelper dbRequestHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_batch);
        mAddedStudentList = new ArrayList<>();
        addedUserRecycler = (RecyclerView) findViewById(R.id.added_user_recycler_view);

        batch_title_et = (EditText) findViewById(R.id.batch_title_et);
        add_student_btn = (Button) findViewById(R.id.add_student_btn);
        add_student_btn.setOnClickListener(this);

        addedUserAdapter = new AddedUserHorizontalAdapter(mAddedStudentList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        addedUserRecycler.setLayoutManager(mLayoutManager);
        addedUserRecycler.setAdapter(addedUserAdapter);

        mBatchDTO = new BatchDTO();
        mScheduleList = new ArrayList<>();

        initTimeSetLayout();
        initView();

        mCalendar = Calendar.getInstance();
        // <editor-fold defaultstate="collapsed" desc="On Time Listner">
        onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int min) {
                ScheduleDTO scheduleDTO = null;
                boolean found = false;
                HelperMethod.debugLog(TAG, "day == "+day+" mScheduleList size = "+mScheduleList.size());
                for (ScheduleDTO dto : mScheduleList) {
                    if (dto.getDaysOfWeek() == day) {
                        HelperMethod.debugLog(TAG, "dto == "+dto.getDaysOfWeek()+ " day = "+day);
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
                    SimpleDateFormat format = new SimpleDateFormat("h:mm a");
                    scheduleDTO.setStartTime(format.format(c.getTime()));

//                    scheduleDTO.setStartTime(DateFormat.getTimeInstance().format(c.getTime()));

                } else {
                    editTextTimeTo.get(day).setText(DateTimeFormatHelper.convertTimeFormatTo12Hour(hour, min));
                    c.set(Calendar.HOUR_OF_DAY, hour);
                    c.set(Calendar.MINUTE, min);
                    SimpleDateFormat format = new SimpleDateFormat("h:mm a");
                    scheduleDTO.setEndTime(format.format(c.getTime()));

//                    scheduleDTO.setEndTime(DateFormat.getTimeInstance().format(c.getTime()));
                }
                if (!found) {
                    mScheduleList.add(scheduleDTO);
                }
            }
        };
        //</editor-fold>
        setTimeSetters();
        initDayOfWeek();

        dbRequestHelper = new DatabaseRequestHelper(this);
    }

    private void initTimeSetLayout() {

        dayViewLayout = new ArrayList<>();
        dayViewLayout.add((LinearLayout) findViewById(R.id.layout_sat));
        dayViewLayout.add((LinearLayout) findViewById(R.id.layout_sun));
        dayViewLayout.add((LinearLayout) findViewById(R.id.layout_mon));
        dayViewLayout.add((LinearLayout) findViewById(R.id.layout_tue));
        dayViewLayout.add((LinearLayout) findViewById(R.id.layout_wed));
        dayViewLayout.add((LinearLayout) findViewById(R.id.layout_thu));
        dayViewLayout.add((LinearLayout) findViewById(R.id.layout_fri));
    }

    private void initView() {
        add_batch_toolbar_back = (FrameLayout) findViewById(R.id.add_batch_toolbar_back);
        add_batch_toolbar_back.setOnClickListener(this);

        add_batch_save_btn = (FrameLayout) findViewById(R.id.add_batch_save_btn);
        add_batch_save_btn.setOnClickListener(this);
    }


    private void initDayOfWeek() {

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
        }
    }

    private void setTimeSetters() {

        editTextTimeFrom = new ArrayList<>();
        editTextTimeTo = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            editTextTimeFrom.add((TextView) dayViewLayout.get(i).findViewById(R.id.edit_txt_day_name_from));
            editTextTimeTo.add((TextView) dayViewLayout.get(i).findViewById(R.id.edit_txt_day_name_to));

            ((TextView) dayViewLayout.get(i).findViewById(R.id.add_day_name)).setText(dayName[i]);
            final int finalI = i;
            editTextTimeFrom.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    day = finalI;
                    isStartTime = true;
                    new TimePickerDialog(AddNewBatchActivity.this, onTimeSetListener,
                            mCalendar.get(Calendar.HOUR_OF_DAY), mCalendar.get(Calendar.MINUTE), false).show();
                }
            });
            editTextTimeTo.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    day = finalI;
                    isStartTime = false;
                    new TimePickerDialog(AddNewBatchActivity.this, onTimeSetListener,
                            mCalendar.get(Calendar.HOUR_OF_DAY), mCalendar.get(Calendar.MINUTE), false).show();
                }
            });

        }
//
        // setTimeSetterListener(editTextTimeFrom);
        // setTimeSetterListener(editTextTimeTo);
    }

    private void setTimeSetterListener(List<EditText> editTextList) {
        for (int i = 0; i < 7; i++) {
            editTextList.get(i).setOnClickListener(this);
        }
    }

    private void saveNewBatch() {
        if (dbRequestHelper == null) {
            dbRequestHelper = new DatabaseRequestHelper(AddNewBatchActivity.this);
        }
        String batchName = batch_title_et.getText().toString();
        if (batchName.length() < 1) {
            Toast.makeText(AddNewBatchActivity.this, "Please, Insert Batch Name", Toast.LENGTH_SHORT).show();
            return;
        }

        mBatchDTO.setBatchName(batchName);
        for (ScheduleDTO dto : mScheduleList) {
            HelperMethod.debugLog(TAG, "days = "+dto.getDaysOfWeek()+" start = "+dto.getStartTime()+ " end = "+dto.getEndTime());
        }


        final long batchId = dbRequestHelper.addBatch(mBatchDTO);
        HelperMethod.debugLog(TAG, "after insert id == " + batchId);

        if (mAddedStudentList != null) {
            mBatchDTO.setStudentDtoList(mAddedStudentList);
            for (UserProfileDTO dto : mAddedStudentList) {
                dto.setBatchId(batchId);
                dto.setTeacher(false);
                long stuId = dbRequestHelper.addStudent(dto);
                HelperMethod.debugLog(TAG, "after student insert : student id == " + stuId);
                dbRequestHelper.updateStudentID((int) stuId);
            }
        }

        if (mScheduleList != null) {
            mBatchDTO.setScheduleDTOList(mScheduleList);
            for (ScheduleDTO dto : mScheduleList) {
                dto.setBatchId(batchId);
                long schId = dbRequestHelper.addSchedule(dto);
                HelperMethod.debugLog(TAG, "after schedule insert : schedule id == " + schId);
            }
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<ScheduleDTO> list = dbRequestHelper.getScheduleListByBatch((int) batchId);
                int i = 1;
                for (ScheduleDTO dto : list) {
                    HelperMethod.debugLog(TAG, "" + i + " batch Id = " + dto.getBatchId() + " st = " + dto.getStartTime() + " "+dto.getEndTime());
                    i++;
                }

//                ArrayList<BatchDTO> list = dbRequestHelper.getBatchList();
//                int i = 1;
//                for (BatchDTO dto : list) {
//                    HelperMethod.debugLog(TAG, "" + i + " name = " + dto.getBatchName() + " id = " + dto.getBatchId());
//                    i++;
//                }
            }
        }).start();

        AddNewBatchActivity.this.finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 121) {
            if (resultCode == RESULT_OK) {
                ArrayList<UserProfileDTO> addedStuList = data.getParcelableArrayListExtra(Constants.ADDED_STUDENT_LIST);
                mAddedStudentList.clear();
                mAddedStudentList.addAll(addedStuList);
                HelperMethod.debugLog(TAG, "onActivityResult : addedStuList size = "+mAddedStudentList.size());
                addedUserAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.day_sat:
            case R.id.day_sun:
            case R.id.day_mon:
            case R.id.day_tue:
            case R.id.day_wed:
            case R.id.day_thu:
            case R.id.day_fri:
                for (int i = 0; i < dayOfWeekList.size(); i++) {
                    if (view.getId() == dayOfWeekList.get(i).getId()) {
                        day = i;
                    }
                }
                findViewById(R.id.scroll).setVisibility(View.VISIBLE);
                findViewById(dayViewLayout.get(day).getId()).setVisibility(View.VISIBLE);
                break;

            case R.id.add_batch_save_btn:

                saveNewBatch();

                break;

            case R.id.add_batch_toolbar_back:
                finish();
                break;
            case R.id.add_student_btn:
                Intent addIntent = new Intent(AddNewBatchActivity.this, AddNewStudentActivity.class);
                addIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivityForResult(addIntent, 121);

                break;
            default:
                break;
        }
    }

}