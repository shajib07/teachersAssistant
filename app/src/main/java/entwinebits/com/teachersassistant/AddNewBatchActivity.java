package entwinebits.com.teachersassistant;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
    private TextView add_batch_toolbar_title;

    private RecyclerView addedUserRecycler;

    private List<TextView> editTextTimeFrom;
    private List<TextView> editTextTimeTo;
    private List<TextView> dayOfWeekList;
    private List<LinearLayout> dayViewLayout;
    private ArrayList<ImageView> mScheduleCancelIvList;
    private Button add_student_btn;

    private boolean mIsEditMode = false;

    private EditText batch_title_et;
    String[] dayName = {"Sat", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri"};
    TimePickerDialog.OnTimeSetListener onTimeSetListener;
    int day;
    boolean isStartTime = true;
    private AddedUserHorizontalAdapter addedUserAdapter;

    private ArrayList<UserProfileDTO> mAddedStudentList;
    private ArrayList<ScheduleDTO> mScheduleList;
    private ArrayList<ScheduleDTO> mEditScheduleList;
    private DatabaseRequestHelper dbRequestHelper;

    private String mEditBatchName;
    private long mEditBatchId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_batch);

        if (getIntent().hasExtra(Constants.BATCH_ID)) {
            mIsEditMode = true;
            mEditBatchId = getIntent().getLongExtra(Constants.BATCH_ID, -1);
            mEditBatchName = getIntent().getStringExtra(Constants.BATCH_NAME);
        }

        initToolbar();
        mAddedStudentList = new ArrayList<>();
        addedUserRecycler = (RecyclerView) findViewById(R.id.added_user_recycler_view);

        batch_title_et = (EditText) findViewById(R.id.batch_title_et);
        batch_title_et.requestFocus();
        add_student_btn = (Button) findViewById(R.id.add_student_btn);
        add_student_btn.setOnClickListener(this);

        addedUserAdapter = new AddedUserHorizontalAdapter(mAddedStudentList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        addedUserRecycler.setLayoutManager(mLayoutManager);
        addedUserRecycler.setAdapter(addedUserAdapter);

        mScheduleList = new ArrayList<>();

        initTimeSetLayout();

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

        if (mIsEditMode) {
            batch_title_et.setText(getIntent().getStringExtra(Constants.BATCH_NAME));

            mEditScheduleList = getIntent().getParcelableArrayListExtra(Constants.BATCH_SCHEDULE_LIST);
            mScheduleList.addAll(mEditScheduleList);
            setEditModeLayout();
        }

    }

    private void setEditModeLayout() {
        HelperMethod.debugLog(TAG, "setEditModeLayout : size = "+mScheduleList.size());
        for (ScheduleDTO dto : mScheduleList) {

            int day = dto.getDaysOfWeek();
            findViewById(R.id.scroll).setVisibility(View.VISIBLE);
            findViewById(dayViewLayout.get(day).getId()).setVisibility(View.VISIBLE);

            editTextTimeFrom.get(day).setText(dto.getStartTime());
            editTextTimeTo.get(day).setText(dto.getEndTime());

        }

    }

    private void initTimeSetLayout() {

        dayViewLayout = new ArrayList<>();
        mScheduleCancelIvList = new ArrayList<>();
        dayViewLayout.add((LinearLayout) findViewById(R.id.layout_sat));
        dayViewLayout.add((LinearLayout) findViewById(R.id.layout_sun));
        dayViewLayout.add((LinearLayout) findViewById(R.id.layout_mon));
        dayViewLayout.add((LinearLayout) findViewById(R.id.layout_tue));
        dayViewLayout.add((LinearLayout) findViewById(R.id.layout_wed));
        dayViewLayout.add((LinearLayout) findViewById(R.id.layout_thu));
        dayViewLayout.add((LinearLayout) findViewById(R.id.layout_fri));

        for (int i = 0; i < 7; i++) {
            mScheduleCancelIvList.add((ImageView) dayViewLayout.get(i).findViewById(R.id.schedule_cancel_iv));
            final int finalI = i;
            mScheduleCancelIvList.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dayViewLayout.get(finalI).setVisibility(View.GONE);
                    editTextTimeTo.get(finalI).setText("TO");
                    editTextTimeFrom.get(finalI).setText("FROM");
                }
            });
        }
    }


    private void initToolbar() {
        add_batch_toolbar_title = (TextView) findViewById(R.id.add_batch_toolbar_title);
        add_batch_toolbar_title.setText("Add New Batch");
        add_batch_toolbar_back = (FrameLayout) findViewById(R.id.add_batch_toolbar_back);
        add_batch_toolbar_back.setOnClickListener(this);
        add_batch_save_btn = (FrameLayout) findViewById(R.id.add_batch_save_btn);
        add_batch_save_btn.setOnClickListener(this);
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

            GradientDrawable bgShape = (GradientDrawable)dayOfWeekList.get(i).getBackground();
            bgShape.setColor(dayColor[i]);

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

    private void saveEditBatch() {
        BatchDTO editBatchDTO = new BatchDTO();
        editBatchDTO.setBatchId(mEditBatchId);

        if (dbRequestHelper == null) {
            dbRequestHelper = new DatabaseRequestHelper(AddNewBatchActivity.this);
        }
        String batchName = batch_title_et.getText().toString();
        if (batchName.length() < 1) {
            Toast.makeText(AddNewBatchActivity.this, "Please, Insert Batch Name", Toast.LENGTH_SHORT).show();
            return;
        }
        editBatchDTO.setBatchName(batchName);
        dbRequestHelper.updateBatch(editBatchDTO);

//        if (mScheduleList != null) {
//            HelperMethod.debugLog(TAG, "EDit : mScheduleList : "+mScheduleList.size() );
//            editBatchDTO.setScheduleDTOList(mScheduleList);
//            for (ScheduleDTO dto : mScheduleList) {
//                dto.setBatchId(mEditBatchId);
//                dbRequestHelper.updateSchedule(dto);
//                HelperMethod.debugLog(TAG, "EDit : after schedule update : schedule id == " );
//            }
//        }

        if (mScheduleList != null) {

            boolean found = false;
            ArrayList<ScheduleDTO> removeList = new ArrayList<>();
            HelperMethod.debugLog(TAG, " TAR : mScheduleList "+mScheduleList.size()+" "+mEditScheduleList.size());

            for (ScheduleDTO dto : mScheduleList) {
                for (ScheduleDTO editScheduleDTO : mEditScheduleList) {
                    if (dto.getDaysOfWeek() == editScheduleDTO.getDaysOfWeek()) {

                        HelperMethod.debugLog(TAG, " TAR : inside update ");
                        dbRequestHelper.updateSchedule(dto);
                        removeList.add(editScheduleDTO);
                        found = true;
                        break;
                    }
                }
                if (found) {
                    mEditScheduleList.removeAll(removeList);
                    found = false;
                } else {
                    HelperMethod.debugLog(TAG, " TAR : inside addSchedule ");
                    dto.setBatchId(mEditBatchId);
                    dbRequestHelper.addSchedule(dto);

                }
            }
        }

        if (mAddedStudentList != null) {
            for (UserProfileDTO dto : mAddedStudentList) {
                dto.setBatchId(mEditBatchId);
                dto.setTeacher(false);
                long stuId = dbRequestHelper.addStudent(dto);
                HelperMethod.debugLog(TAG, "Edit : after student insert : student id == " + stuId);
                dbRequestHelper.updateStudentID((int) stuId);
            }
        }

        Intent resultIntent = new Intent();
        resultIntent.putExtra(Constants.BATCH_ID, mEditBatchId);
        resultIntent.putExtra(Constants.BATCH_NAME, editBatchDTO.getBatchName());
        resultIntent.putParcelableArrayListExtra(Constants.BATCH_SCHEDULE_LIST, editBatchDTO.getScheduleDTOList());
        setResult(RESULT_OK, resultIntent);
        AddNewBatchActivity.this.finish();

    }

    private void saveNewBatch() {
        BatchDTO saveBatchDTO = new BatchDTO();
        if (dbRequestHelper == null) {
            dbRequestHelper = new DatabaseRequestHelper(AddNewBatchActivity.this);
        }

        String batchName = batch_title_et.getText().toString();
        if (batchName.length() < 1) {
            Toast.makeText(AddNewBatchActivity.this, "Please, Insert Batch Name", Toast.LENGTH_SHORT).show();
            return;
        }

        saveBatchDTO.setBatchName(batchName);

        final long batchId = dbRequestHelper.addBatch(saveBatchDTO);
        HelperMethod.debugLog(TAG, "after insert id == " + batchId);

        if (mAddedStudentList != null) {
            saveBatchDTO.setStudentDtoList(mAddedStudentList);
            for (UserProfileDTO dto : mAddedStudentList) {
                dto.setBatchId(batchId);
                dto.setTeacher(false);
                long stuId = dbRequestHelper.addStudent(dto);
                HelperMethod.debugLog(TAG, "after student insert : student id == " + stuId);
                dbRequestHelper.updateStudentID((int) stuId);
            }
        }

        if (mScheduleList != null) {
            saveBatchDTO.setScheduleDTOList(mScheduleList);
            for (ScheduleDTO dto : mScheduleList) {
                dto.setBatchId(batchId);
                long schId = dbRequestHelper.addSchedule(dto);
                HelperMethod.debugLog(TAG, "after schedule insert : schedule id == " + schId);
            }
        }

        AddNewBatchActivity.this.finish();

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                ArrayList<ScheduleDTO> list = dbRequestHelper.getScheduleListByBatch((int) batchId);
//                int i = 1;
//                for (ScheduleDTO dto : list) {
//                    HelperMethod.debugLog(TAG, "" + i + " batch Id = " + dto.getBatchId() + " st = " + dto.getStartTime() + " "+dto.getEndTime());
//                    i++;
//                }
//            }
//        }).start();

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

    private void makeVisibleAnimation(final View view) {
        view.setVisibility(View.VISIBLE);
        view.animate()
                .translationY(0)
                .alpha(1.0f)
                .setDuration(10000)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        view.setVisibility(View.GONE);
                    }
                });
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
                        break;
                    }
                }
                findViewById(R.id.scroll).setVisibility(View.VISIBLE);
                dayViewLayout.get(day).setVisibility(View.VISIBLE);
//                findViewById(dayViewLayout.get(day).getId()).setVisibility(View.VISIBLE);
//                makeVisibleAnimation(findViewById(dayViewLayout.get(day).getId()));

                break;

            case R.id.add_batch_save_btn:

                if (mIsEditMode) {
                    saveEditBatch();
                } else {
                    saveNewBatch();
                }
                break;

            case R.id.add_batch_toolbar_back:
                AddNewBatchActivity.this.finish();
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