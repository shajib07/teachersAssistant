package entwinebits.com.teachersassistant.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import entwinebits.com.teachersassistant.model.BatchDTO;
import entwinebits.com.teachersassistant.utils.HelperMethod;

/**
 * Created by Nargis Rahman on 12/1/2016.
 */
public class BaseDatabaseController extends SQLiteOpenHelper {

    public ArrayList<BatchDTO> getBatchList(SQLiteDatabase db) {
        ArrayList<BatchDTO> batchDTOs = new ArrayList<>();
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT  * FROM " + TABLE_BATCH + " ORDER BY " + KEY_BATCH_NAME + " DESC";
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    BatchDTO dto = new BatchDTO();
//                    dto.setBatchId(cursor.getLong(0));
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

    public void addBatch(BatchDTO batchDTO, SQLiteDatabase db) {
        HelperMethod.debugLog(TAG, "addBatch called ++++++ ");

        ContentValues values = new ContentValues();
//        values.put(KEY_BATCH_ID, batchDTO.getCourseId());
        values.put(KEY_BATCH_NAME, batchDTO.getBatchName());
//        values.put(KEY_COURSE_ID, batchDTO.getCourseId());
//        values.put(KEY_ROUTINE_ID, batchDTO.getRoutineId());

        try {
            long ret = db.insert(TABLE_BATCH, null, values);
            if (ret != -1) {
                HelperMethod.debugLog(TAG, " addBatch insert = " + ret);
            } else {
                HelperMethod.debugLog(TAG, "Failed to Inser: addBatch insert = " + ret);
            }
        } catch (Exception e) {
            HelperMethod.errorLog(TAG, "Exception : addBatch = " + e.toString());
        }
    }


    private static final String TABLE_USER_PROFILE = "user_profile";
    private static final String TABLE_COURSE = "table_course";
    private static final String TABLE_BATCH = "table_batch";
    private static final String TABLE_ROUTINE = "table_routine";
    private static final String TABLE_SCHEDULE = "table_schedule";


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

    //    field for batch table
    private static final String KEY_BATCH_ID = "bid";
    private static final String KEY_BATCH_NAME = "bat_name";

    //    field for course table
    private static final String KEY_COURSE_ID = "cid";
    private static final String KEY_COURSE_TITLE = "cours_title";
    private static final String KEY_COURSE_NAME = "cours_name";

    //    field for routine table
    private static final String KEY_ROUTINE_ID = "rid";
    private static final String KEY_START_DATE = "rtn_start";
    private static final String KEY_END_DATE = "rtn_end";

    //    field for schedule table
    private static final String KEY_SCHEDULE_ID = "sid";
    private static final String KEY_DAYS_OF_WEEK = "se_days";
    private static final String KEY_SCHEDULE_STATUS = "se_status";


    public static String CREATE_TABLE_USER_PROFILE = "CREATE TABLE " + TABLE_USER_PROFILE
            + " (" + KEY_ID + " LONG PRIMARY KEY ,"
            + KEY_USERNAME + " TEXT ,"
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
            + KEY_IS_TEACHER + " INTEGER )";

    public static String CREATE_TABLE_COURSE = "CREATE TABLE " + TABLE_COURSE
            + " (" + KEY_COURSE_ID + " LONG PRIMARY KEY ,"
            + KEY_COURSE_TITLE + " TEXT ,"
            + KEY_COURSE_NAME + " TEXT )";

    public static String CREATE_TABLE_BATCH = "CREATE TABLE " + TABLE_BATCH
            + " (" + KEY_BATCH_ID + " LONG PRIMARY KEY ,"
            + KEY_BATCH_NAME + " TEXT ,"
            + KEY_COURSE_ID + " LONG ,"
            + KEY_ROUTINE_ID + " LONG )";


    public static String CREATE_TABLE_ROUTINE = "CREATE TABLE " + TABLE_ROUTINE
            + " (" + KEY_ROUTINE_ID + " LONG PRIMARY KEY ,"
            + KEY_BATCH_ID + " LONG,"
            + KEY_START_DATE + " TEXT ,"
            + KEY_END_DATE + " TEXT )";

    public static String CREATE_TABLE_SCHEDULE = "CREATE TABLE " + TABLE_SCHEDULE
            + " (" + KEY_SCHEDULE_ID + " LONG PRIMARY KEY ,"
            + KEY_ROUTINE_ID + " LONG ,"
            + KEY_DAYS_OF_WEEK + " INTEGER ,"
            + KEY_START_DATE + " LONG ,"
            + KEY_END_DATE + " LONG,"
            + KEY_SCHEDULE_STATUS + " TEXT )";


    public BaseDatabaseController(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        db.execSQL(CREATE_TABLE_USER_PROFILE);
//        db.execSQL(CREATE_TABLE_COURSE);
        db.execSQL(CREATE_TABLE_BATCH);
//        db.execSQL(CREATE_TABLE_ROUTINE);
//        db.execSQL(CREATE_TABLE_SCHEDULE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_BATCH);
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
    private static final int DATABASE_VERSION = 1;
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
