package entwinebits.com.teachersassistant.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import entwinebits.com.teachersassistant.model.BatchDTO;
import entwinebits.com.teachersassistant.model.PaymentDTO;
import entwinebits.com.teachersassistant.model.PaymentHistoryDTO;
import entwinebits.com.teachersassistant.model.ScheduleDTO;
import entwinebits.com.teachersassistant.model.UserProfileDTO;
import entwinebits.com.teachersassistant.utils.HelperMethod;

/**
 * Created by Nargis Rahman on 12/1/2016.
 */
public class BaseDatabaseController extends SQLiteOpenHelper {

    public ArrayList<UserProfileDTO> getStudentListByBatch(SQLiteDatabase db, int batchId) {
        ArrayList<UserProfileDTO> studentList = new ArrayList<>();
        Cursor cursor = null;
        try {
            HelperMethod.debugLog(TAG, "getStudentListByBatch : batchId == " + batchId);
            String selectQuery = "SELECT  * FROM " + TABLE_USER_PROFILE + " WHERE " + KEY_BATCH_ID + " = " + batchId;
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    UserProfileDTO dto = new UserProfileDTO();
                    dto.setUserId(cursor.getInt(0));
                    dto.setUserFirstName(cursor.getString(1));
                    dto.setMonthlyFee(cursor.getInt(2));
                    dto.setUserMobilePhone(cursor.getString(10));
                    dto.setUserInstituteName(cursor.getString(15));
                    dto.setUserAddress(cursor.getString(13));
                    studentList.add(dto);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            HelperMethod.errorLog(TAG, "Exception : getStudentListByBatch = " + e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return studentList;
    }

    public void deleteStudent(UserProfileDTO userProfileDTO, SQLiteDatabase db) {
        HelperMethod.debugLog(TAG, "deleteStudent called ++++++ ");
        int id = userProfileDTO.getUserId();
        HelperMethod.debugLog(TAG, "id = " + id + " name = " + userProfileDTO.getUserFirstName() + " fee = " + userProfileDTO.getMonthlyFee());
        try {
            int ret = db.delete(TABLE_USER_PROFILE, KEY_ID + " = ?", new String[]{String.valueOf(id)});
        } catch (Exception e) {
            HelperMethod.errorLog(TAG, "Exception : deleteStudent = " + e.toString());
        }
    }

    public void updateStudent(UserProfileDTO userProfileDTO, SQLiteDatabase db) {
        HelperMethod.debugLog(TAG, "updateStudent called ++++++ ");
        int id = (int) userProfileDTO.getUserId();
        HelperMethod.debugLog(TAG, "id = " + id + " name = " + userProfileDTO.getUserFirstName() + " fee = " + userProfileDTO.getMonthlyFee());

        ContentValues values = new ContentValues();
        values.put(KEY_USERNAME, userProfileDTO.getUserFirstName());
        values.put(KEY_MONTHLY_FEE, userProfileDTO.getMonthlyFee());
        values.put(KEY_MOBILE, userProfileDTO.getUserMobilePhone());
        values.put(KEY_INSTITUTE_NAME, userProfileDTO.getUserInstituteName());
        values.put(KEY_ADDRESS, userProfileDTO.getUserAddress());

        try {
            int ret = db.update(TABLE_USER_PROFILE, values, KEY_ID + " = ?", new String[]{String.valueOf(id)});
        } catch (Exception e) {
            HelperMethod.errorLog(TAG, "Exception : updateStudent = " + e.toString());
        }
    }

    public void updateStudentID(int id, SQLiteDatabase db) {
        HelperMethod.debugLog(TAG, "updateStudent called ++++++ ");

        ContentValues values = new ContentValues();
        values.put(KEY_ID, id);
        try {
            int ret = db.update(TABLE_USER_PROFILE, values, KEY_ID + " = ?", new String[]{String.valueOf(id)});
        } catch (Exception e) {
            HelperMethod.errorLog(TAG, "Exception : updateStudentID = " + e.toString());
        }
    }

    public long addStudent(UserProfileDTO userProfileDTO, SQLiteDatabase db) {
        HelperMethod.debugLog(TAG, "addStudent called ++++++ ");
        long id = -1;

        ContentValues values = new ContentValues();
        values.put(KEY_USERNAME, userProfileDTO.getUserFirstName());
        values.put(KEY_MONTHLY_FEE, userProfileDTO.getMonthlyFee());
        values.put(KEY_MOBILE, userProfileDTO.getUserMobilePhone());
        values.put(KEY_INSTITUTE_NAME, userProfileDTO.getUserInstituteName());
        values.put(KEY_ADDRESS, userProfileDTO.getUserAddress());
        values.put(KEY_BATCH_ID, userProfileDTO.getBatchId());
        values.put(KEY_IS_TEACHER, userProfileDTO.isTeacher());

        try {
            id = db.insert(TABLE_USER_PROFILE, null, values);
            if (id != -1) {
                HelperMethod.debugLog(TAG, " addStudent insert = " + id);
            } else {
                HelperMethod.debugLog(TAG, "Failed to Insert: addStudent insert = " + id);
            }
        } catch (Exception e) {
            HelperMethod.errorLog(TAG, "Exception : addStudent = " + e.toString());
        }
        return id;
    }


    public ArrayList<BatchDTO> getBatchList(SQLiteDatabase db) {
        ArrayList<BatchDTO> batchDTOs = new ArrayList<>();
        Cursor cursor = null;
        try {
//            String selectQuery = "SELECT  * FROM " + TABLE_BATCH + " ORDER BY " + KEY_BATCH_NAME + " DESC";
            String selectQuery = "SELECT  * FROM " + TABLE_BATCH;
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    BatchDTO dto = new BatchDTO();
                    dto.setBatchId(cursor.getLong(0));
//                    dto.setRoutineId(cursor.getLong(2));
                    dto.setBatchName(cursor.getString(1));
                    batchDTOs.add(dto);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            HelperMethod.errorLog(TAG, "Exception : getBatchList = " + e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return batchDTOs;
    }

    public ArrayList<ScheduleDTO> getScheduleListByBatch(SQLiteDatabase db, int batchId) {
        ArrayList<ScheduleDTO> scheduleDTOs = new ArrayList<>();
        Cursor cursor = null;
        try {
            HelperMethod.debugLog(TAG, "getScheduleListByBatch : batchId == " + batchId);
//            String selectQuery = "SELECT  * FROM " + TABLE_BATCH + " ORDER BY " + KEY_BATCH_NAME + " DESC";
            String selectQuery = "SELECT  * FROM " + TABLE_SCHEDULE + " WHERE " + KEY_BATCH_ID + " = " + batchId;
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    ScheduleDTO dto = new ScheduleDTO();
                    dto.setScheduleId(cursor.getLong(0));
                    dto.setBatchId(cursor.getInt(1));
                    dto.setDaysOfWeek(cursor.getInt(2));
                    dto.setStartTime(cursor.getLong(3));
                    dto.setEndTime(cursor.getLong(4));
                    scheduleDTOs.add(dto);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            HelperMethod.errorLog(TAG, "Exception : getScheduleListByBatch = " + e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return scheduleDTOs;
    }

    public void updateSchedule(ScheduleDTO scheduleDTO, SQLiteDatabase db) {
        HelperMethod.debugLog(TAG, "updateSchedule called ++++++ ");
        int id = (int) scheduleDTO.getScheduleId();
        HelperMethod.debugLog(TAG, "id = " + id + " bat id = " + scheduleDTO.getBatchId() + " st tm = " + scheduleDTO.getStartTime());

        ContentValues values = new ContentValues();
        values.put(KEY_DAYS_OF_WEEK, scheduleDTO.getDaysOfWeek());
        values.put(KEY_START_TIME, scheduleDTO.getStartTime());
        values.put(KEY_END_TIME, scheduleDTO.getEndTime());

        try {
            int ret = db.update(TABLE_SCHEDULE, values, KEY_SCHEDULE_ID + " = ?", new String[]{String.valueOf(id)});
        } catch (Exception e) {
            HelperMethod.errorLog(TAG, "Exception : updateSchedule = " + e.toString());
        }
    }

    public ArrayList<PaymentHistoryDTO> getPaymentHistoryByStudent(SQLiteDatabase db, int studentId) {
        ArrayList<PaymentHistoryDTO> paymentHistoryList = new ArrayList<>();
        Cursor cursor = null;
        try {
            HelperMethod.debugLog(TAG, "getPaymentHistoryByStudent : studentId == " + studentId);
            String selectQuery = "SELECT  * FROM " + TABLE_PAYMENT_HISTORY + " WHERE " + KEY_ID + " = " + studentId;
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    PaymentHistoryDTO dto = new PaymentHistoryDTO();
                    dto.setMonth(cursor.getInt(2));
                    dto.setYear(cursor.getInt(3));
                    dto.setPaidAmount(cursor.getInt(4));
                    dto.setPaid(cursor.getInt(5) == 1 ? true : false);
                    dto.setStudentName(cursor.getString(6));
                    paymentHistoryList.add(dto);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            HelperMethod.errorLog(TAG, "Exception : getPaymentHistoryByStudent = " + e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return paymentHistoryList;
    }

    public ArrayList<PaymentHistoryDTO> getPaymentHistoryByStudentYear(SQLiteDatabase db, int studentId, int year) {
        ArrayList<PaymentHistoryDTO> paymentHistoryList = new ArrayList<>();
        Cursor cursor = null;
        try {
            HelperMethod.debugLog(TAG, "getPaymentHistoryByStudentYear : studentId == " + studentId+ " year = "+year);
            String selectQuery = "SELECT  * FROM " + TABLE_PAYMENT_HISTORY + " WHERE " + KEY_ID + " = " + studentId
                    + " AND "+ KEY_PAYMENT_YEAR + " = " + year;
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    PaymentHistoryDTO dto = new PaymentHistoryDTO();
                    dto.setMonth(cursor.getInt(2));
                    dto.setYear(cursor.getInt(3));
                    dto.setPaidAmount(cursor.getInt(4));
                    dto.setPaid(cursor.getInt(5) == 1 ? true : false);
                    dto.setStudentName(cursor.getString(6));
                    paymentHistoryList.add(dto);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            HelperMethod.errorLog(TAG, "Exception : getPaymentHistoryByStudentYear = " + e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return paymentHistoryList;
    }

    public ArrayList<PaymentHistoryDTO> getPaymentHistoryByBatchMonth(SQLiteDatabase db, int batchId, int month) {
        ArrayList<PaymentHistoryDTO> paymentHistoryList = new ArrayList<>();
        Cursor cursor = null;
        try {
            HelperMethod.debugLog(TAG, "getPaymentHistoryByBatchMonth : batchId == " + batchId+ " month = "+month);
            String selectQuery = "SELECT  * FROM " + TABLE_PAYMENT_HISTORY + " WHERE " + KEY_BATCH_ID + " = " + batchId
                    + " AND "+ KEY_PAYMENT_MONTH + " = " + month;
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    PaymentHistoryDTO dto = new PaymentHistoryDTO();
                    dto.setMonth(cursor.getInt(2));
                    dto.setYear(cursor.getInt(3));
                    dto.setPaidAmount(cursor.getInt(4));
                    dto.setPaid(cursor.getInt(5) == 1 ? true : false);
                    dto.setStudentName(cursor.getString(6));
                    paymentHistoryList.add(dto);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            HelperMethod.errorLog(TAG, "Exception : getPaymentHistoryByBatchMonth = " + e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return paymentHistoryList;
    }


/*
    private boolean isAlreadyAdded(String filePath, SQLiteDatabase db) {
        boolean isexists;
        String selectQuery = "SELECT " + KEY_FILE_PATH + " FROM " + TABLE_FILE_HISTORY + " WHERE " + KEY_FILE_PATH + "='" + filePath + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        isexists = cursor.getCount() != 0;
        .debugLog(TAG, " isexists = " + isexists);
        cursor.close();
        return isexists;
    }
*/


    private boolean isPaymentHistoryExist(SQLiteDatabase db, int id, int month, int year) {

        HelperMethod.debugLog(TAG, "isPaymentHistoryExist called ++++++ "+id+" : mon "+month+" : yr "+year);
        boolean isExist = false;
        String selectQuery = "SELECT * FROM " + TABLE_PAYMENT_HISTORY
                + " WHERE " + KEY_ID + " = '" + String.valueOf(id) + "'"
                + " AND " + KEY_PAYMENT_MONTH + " = '" + String.valueOf(month) + "'"
                + " AND " + KEY_PAYMENT_YEAR + " = '" + String.valueOf(year) + "'";

        Cursor cursor = db.rawQuery(selectQuery, null);
        isExist = cursor.getCount() != 0;
        HelperMethod.debugLog(TAG, " isExist = " + isExist);
        cursor.close();

        return isExist;
    }

    public long addPaymentHistory(PaymentHistoryDTO paymentHistoryDTO, SQLiteDatabase db) {
        HelperMethod.debugLog(TAG, "addPaymentHistory called ++++++ ");
        long id = -1;

        ContentValues values = new ContentValues();
        values.put(KEY_ID, paymentHistoryDTO.getStudentId());
        values.put(KEY_PAYMENT_MONTH, paymentHistoryDTO.getMonth());
        values.put(KEY_PAYMENT_YEAR, paymentHistoryDTO.getYear());
        values.put(KEY_PAYMENT_AMOUNT, paymentHistoryDTO.getPaidAmount());
        values.put(KEY_FULL_PAYMENT_STATUS, paymentHistoryDTO.isPaid()==true ? 1 : 0);
        values.put(KEY_USERNAME, paymentHistoryDTO.getStudentName());
        values.put(KEY_BATCH_ID, paymentHistoryDTO.getBatchId());

        try {
            if (isPaymentHistoryExist(db, paymentHistoryDTO.getStudentId(), paymentHistoryDTO.getMonth(), paymentHistoryDTO.getYear()) ) {

                HelperMethod.debugLog(TAG, "addPaymentHistory Update");
                id = db.update(TABLE_PAYMENT_HISTORY, values, KEY_ID + " = ?"
                        + " AND " + KEY_PAYMENT_MONTH + " = ?"
                        + " AND " + KEY_PAYMENT_YEAR + " = ?",
                        new String[]{String.valueOf(paymentHistoryDTO.getStudentId()),
                                String.valueOf(paymentHistoryDTO.getMonth()), String.valueOf(paymentHistoryDTO.getYear())} );

            } else {
                HelperMethod.debugLog(TAG, "addPaymentHistory Insert");
                id = db.insert(TABLE_PAYMENT_HISTORY, null, values);
            }
            if (id != -1) {
                HelperMethod.debugLog(TAG, " addPaymentHistory insert = " + id);
            } else {
                HelperMethod.debugLog(TAG, "Failed to Insert: addPaymentHistory insert = " + id);
            }
        } catch (Exception e) {
            HelperMethod.errorLog(TAG, "Exception : addPaymentHistory = " + e.toString());
        }
        return id;
    }

    public long addSchedule(ScheduleDTO scheduleDTO, SQLiteDatabase db) {
        HelperMethod.debugLog(TAG, "addSchedule called ++++++ ");
        long id = -1;

        ContentValues values = new ContentValues();
        values.put(KEY_BATCH_ID, scheduleDTO.getBatchId());
        values.put(KEY_DAYS_OF_WEEK, scheduleDTO.getDaysOfWeek());
        values.put(KEY_START_TIME, scheduleDTO.getStartTime());
        values.put(KEY_END_TIME, scheduleDTO.getEndTime());

        try {
            id = db.insert(TABLE_SCHEDULE, null, values);
            if (id != -1) {
                HelperMethod.debugLog(TAG, " addSchedule insert = " + id);
            } else {
                HelperMethod.debugLog(TAG, "Failed to Insert: addSchedule insert = " + id);
            }
        } catch (Exception e) {
            HelperMethod.errorLog(TAG, "Exception : addSchedule = " + e.toString());
        }
        return id;
    }

    public void updateBatch(BatchDTO batchDTO, SQLiteDatabase db) {
        int id = (int) batchDTO.getBatchId();
        HelperMethod.debugLog(TAG, "updateBatch called ++++++ id = " + id + " name = " + batchDTO.getBatchName());

        ContentValues values = new ContentValues();
        values.put(KEY_BATCH_NAME, batchDTO.getBatchName());

        try {
            int ret = db.update(TABLE_BATCH, values, KEY_BATCH_ID + " = ?", new String[]{String.valueOf(id)});
        } catch (Exception e) {
            HelperMethod.errorLog(TAG, "Exception : updateBatch = " + e.toString());
        }
    }

    public long addBatch(BatchDTO batchDTO, SQLiteDatabase db) {
        HelperMethod.debugLog(TAG, "addBatch called ++++++ ");
        long id = -1;

        ContentValues values = new ContentValues();
//        values.put(KEY_BATCH_ID, batchDTO.getCourseId());
        values.put(KEY_BATCH_NAME, batchDTO.getBatchName());
//        values.put(KEY_COURSE_ID, batchDTO.getCourseId());
//        values.put(KEY_ROUTINE_ID, batchDTO.getRoutineId());
        try {
            id = db.insert(TABLE_BATCH, null, values);
            if (id != -1) {
                HelperMethod.debugLog(TAG, " addBatch insert = " + id);
            } else {
                HelperMethod.debugLog(TAG, "Failed to Inser: addBatch insert = " + id);
            }
        } catch (Exception e) {
            HelperMethod.errorLog(TAG, "Exception : addBatch = " + e.toString());
        }
        return id;
    }


    private static final String TABLE_USER_PROFILE = "user_profile";
    private static final String TABLE_COURSE = "table_course";
    private static final String TABLE_BATCH = "table_batch";
    private static final String TABLE_ROUTINE = "table_routine";
    private static final String TABLE_SCHEDULE = "table_schedule";
    private static final String TABLE_PAYMENT_HISTORY = "table_pmnt_hstry";


    //    field for user_profile
    private static final String KEY_ID = "id";
    private static final String KEY_USERNAME = "userNm";
    private static final String KEY_PASSWORD = "userPwd";
    private static final String KEY_USERIDENTITY = "uId";
    private static final String KEY_FIRST_NAME = "fn";
    private static final String KEY_LAST_NAME = "ln";
    private static final String KEY_GENDER = "gn";
    private static final String KEY_CITY = "city";
    private static final String KEY_COUNTRY = "cnty";
    private static final String KEY_MOBILE = "mbl";
    private static final String KEY_EMAIL = "eml";
    private static final String KEY_BIRTH_DAY = "bDay";
    private static final String KEY_ADDRESS = "adrs";
    private static final String KEY_DESIGNATION = "dsgn";
    private static final String KEY_INSTITUTE_NAME = "instNm";
    private static final String KEY_IS_TEACHER = "isTea";
    private static final String KEY_MONTHLY_FEE = "monthlyFee";


    //    private static final String KEY_BATCH_ID_N = "bidn";
    //    field for batch table
    private static final String KEY_BATCH_ID = "bid";
    private static final String KEY_BATCH_NAME = "bat_name";

    //    field for PAYMENT HISTORY table
    private static final String KEY_PAYMENT_HISTORY_ID = "pmnt_hstry_id";
    private static final String KEY_PAYMENT_MONTH = "pmnt_mnth";
    private static final String KEY_PAYMENT_YEAR = "pmnt_yr";
    private static final String KEY_PAYMENT_AMOUNT = "pmnt_amnt";
    private static final String KEY_FULL_PAYMENT_STATUS = "pmnt_sts";

    //    field for routine table
    private static final String KEY_ROUTINE_ID = "rid";
    private static final String KEY_START_DATE = "rtn_start";
    private static final String KEY_END_DATE = "rtn_end";

    //    field for schedule table
    private static final String KEY_SCHEDULE_ID = "sid";
    private static final String KEY_DAYS_OF_WEEK = "se_days";
    private static final String KEY_SCHEDULE_STATUS = "se_status";
    private static final String KEY_START_TIME = "se_st_tm";
    private static final String KEY_END_TIME = "se_en_tm";


    public static String CREATE_TABLE_USER_PROFILE = "CREATE TABLE " + TABLE_USER_PROFILE
            + " (" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_USERNAME + " TEXT ,"
            + KEY_MONTHLY_FEE + " INTEGER ,"
            + KEY_PASSWORD + " TEXT ,"
            + KEY_USERIDENTITY + " TEXT ,"
            + KEY_FIRST_NAME + " TEXT ,"
            + KEY_LAST_NAME + " TEXT ,"
            + KEY_GENDER + " TEXT ,"
            + KEY_CITY + " TEXT ,"
            + KEY_COUNTRY + " TEXT ,"
            + KEY_MOBILE + " TEXT ,"
            + KEY_EMAIL + " TEXT ,"
            + KEY_BIRTH_DAY + " TEXT ,"
            + KEY_ADDRESS + " TEXT ,"
            + KEY_DESIGNATION + " TEXT ,"
            + KEY_INSTITUTE_NAME + " TEXT ,"
            + KEY_IS_TEACHER + " INTEGER ,"
            + KEY_BATCH_ID + " TEXT )";

    public static String CREATE_TABLE_PAYMENT_HISTORY = "CREATE TABLE " + TABLE_PAYMENT_HISTORY
            + " (" + KEY_PAYMENT_HISTORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + KEY_ID + " INTEGER ,"
            + KEY_PAYMENT_MONTH + " INTEGER ,"
            + KEY_PAYMENT_YEAR + " INTEGER ,"
            + KEY_PAYMENT_AMOUNT + " INTEGER ,"
            + KEY_FULL_PAYMENT_STATUS + " INTEGER ,"
            + KEY_USERNAME + " TEXT ,"
            + KEY_BATCH_ID + " INTEGER )";

    public static String CREATE_TABLE_BATCH = "CREATE TABLE " + TABLE_BATCH
            + " (" + KEY_BATCH_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + KEY_BATCH_NAME + " TEXT ,"
            + KEY_ROUTINE_ID + " LONG )";


    public static String CREATE_TABLE_ROUTINE = "CREATE TABLE " + TABLE_ROUTINE
            + " (" + KEY_ROUTINE_ID + " LONG PRIMARY KEY ,"
            + KEY_BATCH_ID + " LONG,"
            + KEY_START_DATE + " TEXT ,"
            + KEY_END_DATE + " TEXT )";

    public static String CREATE_TABLE_SCHEDULE = "CREATE TABLE " + TABLE_SCHEDULE
            + " (" + KEY_SCHEDULE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + KEY_BATCH_ID + " LONG ,"
            + KEY_DAYS_OF_WEEK + " INTEGER ,"
            + KEY_START_TIME + " LONG ,"
            + KEY_END_TIME + " LONG,"
            + KEY_SCHEDULE_STATUS + " TEXT )";


    public BaseDatabaseController(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USER_PROFILE);
//        db.execSQL(CREATE_TABLE_COURSE);
        db.execSQL(CREATE_TABLE_BATCH);
//        db.execSQL(CREATE_TABLE_ROUTINE);
        db.execSQL(CREATE_TABLE_SCHEDULE);
        db.execSQL(CREATE_TABLE_PAYMENT_HISTORY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_BATCH);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_PROFILE);
        db.execSQL(CREATE_TABLE_SCHEDULE);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_BATCH);
        onCreate(db);
    }

    public synchronized SQLiteDatabase openDatabse() {
        SQLiteDatabase db = this.getWritableDatabase();
        mOpenCounter++;
        return db;
    }

    public synchronized void closeDataBase(SQLiteDatabase db) {
        mOpenCounter--;
        if (mOpenCounter <= 0) {
            if (db != null) {
                close();
            }
        }
    }

    private static final String TAG = "BaseDatabaseController";
    private static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "teacherAssistant";

    private static volatile BaseDatabaseController uniqInstance;
    private int mOpenCounter = 0;

    public static synchronized BaseDatabaseController getBaseDBHandler(Context context) {
        if (uniqInstance == null) {
            synchronized (BaseDatabaseController.class) {
                if (uniqInstance == null) {
                    uniqInstance = new BaseDatabaseController(context);
                }
            }
        }
        return uniqInstance;
    }
}
