package entwinebits.com.teachersassistant.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import entwinebits.com.teachersassistant.model.BatchDTO;
import entwinebits.com.teachersassistant.model.ScheduleDTO;
import entwinebits.com.teachersassistant.model.UserProfileDTO;
import entwinebits.com.teachersassistant.utils.HelperMethod;

/**
 * Created by Nargis Rahman on 12/1/2016.
 */
public class DatabaseRequestHelper {

    private String TAG = "DBRequestHandler";
    private BaseDatabaseController uniqInstance;

    public DatabaseRequestHelper(Context context) {
        uniqInstance = BaseDatabaseController.getBaseDBHandler(context);
    }

    public ArrayList<BatchDTO> getBatchList() {
        ArrayList<BatchDTO> batchDTOs ;
        SQLiteDatabase db = uniqInstance.openDatabse();
        batchDTOs = uniqInstance.getBatchList(db);
        uniqInstance.closeDataBase(db);
        HelperMethod.debugLog(TAG, " getBatchList +++++++++batchDTOs.size() ==== "+batchDTOs.size());
        return batchDTOs;
    }

    public long addBatch(BatchDTO batchDTO) {
        HelperMethod.debugLog(TAG, " addBatch called ++++++++");
        SQLiteDatabase db = uniqInstance.openDatabse();
        long id = uniqInstance.addBatch(batchDTO, db);
        uniqInstance.closeDataBase(db);
        return id;
    }

    public long addStudent(UserProfileDTO userProfileDTO) {
        HelperMethod.debugLog(TAG, " addStudent called ++++++++");
        SQLiteDatabase db = uniqInstance.openDatabse();
        long id = uniqInstance.addStudent(userProfileDTO, db);
        uniqInstance.closeDataBase(db);
        return id;
    }

    public long addSchedule(ScheduleDTO scheduleDTO) {
        HelperMethod.debugLog(TAG, " addSchedule called ++++++++");
        SQLiteDatabase db = uniqInstance.openDatabse();
        long id = uniqInstance.addSchedule(scheduleDTO, db);
        uniqInstance.closeDataBase(db);
        return id;
    }

    public ArrayList<ScheduleDTO> getScheduleListByBatch(int batchId) {
        HelperMethod.debugLog(TAG, " addSchedule called ++++++++");
        SQLiteDatabase db = uniqInstance.openDatabse();
        ArrayList<ScheduleDTO> scheduleList = uniqInstance.getScheduleListByBatch(db, batchId);
        uniqInstance.closeDataBase(db);
        return scheduleList;
    }


}
