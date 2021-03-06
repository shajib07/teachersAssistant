package entwinebits.com.teachersassistant.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

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

    public void updateBatch(BatchDTO batchDTO) {
        HelperMethod.debugLog(TAG, " updateBatch called ++++++++");
        SQLiteDatabase db = uniqInstance.openDatabse();
        uniqInstance.updateBatch(batchDTO, db);
        uniqInstance.closeDataBase(db);
    }

    public void updateStudentID(int id) {
        HelperMethod.debugLog(TAG, " updateStudentID called ++++++++");
        SQLiteDatabase db = uniqInstance.openDatabse();
        uniqInstance.updateStudentID(id, db);
        uniqInstance.closeDataBase(db);
    }

    public void deleteStudent(UserProfileDTO userProfileDTO) {
        HelperMethod.debugLog(TAG, " updateStudent called ++++++++");
        SQLiteDatabase db = uniqInstance.openDatabse();
        uniqInstance.deleteStudent(userProfileDTO, db);
        uniqInstance.closeDataBase(db);
    }

    public void updateStudent(UserProfileDTO userProfileDTO) {
        HelperMethod.debugLog(TAG, " updateStudent called ++++++++");
        SQLiteDatabase db = uniqInstance.openDatabse();
        uniqInstance.updateStudent(userProfileDTO, db);
        uniqInstance.closeDataBase(db);
    }

    public long addStudent(UserProfileDTO userProfileDTO) {
        HelperMethod.debugLog(TAG, " addStudent called ++++++++");
        SQLiteDatabase db = uniqInstance.openDatabse();
        long id = uniqInstance.addStudent(userProfileDTO, db);
        uniqInstance.closeDataBase(db);
        return id;
    }

    public void updateSchedule(ScheduleDTO scheduleDTO) {
        HelperMethod.debugLog(TAG, " updateSchedule called ++++++++");
        SQLiteDatabase db = uniqInstance.openDatabse();
        uniqInstance.updateSchedule(scheduleDTO, db);
        uniqInstance.closeDataBase(db);
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

    public ArrayList<UserProfileDTO> getStudentListByBatch(int batchId) {
        HelperMethod.debugLog(TAG, " getStudentListByBatch called ++++++++");
        SQLiteDatabase db = uniqInstance.openDatabse();
        ArrayList<UserProfileDTO> studentList = uniqInstance.getStudentListByBatch(db, batchId);
        uniqInstance.closeDataBase(db);
        return studentList;
    }

    public ArrayList<PaymentHistoryDTO> getPaymentHistoryByStudentYear(int studentId, int year) {
        HelperMethod.debugLog(TAG, " getPaymentHistoryByStudentYear called ++++++++");
        SQLiteDatabase db = uniqInstance.openDatabse();
        ArrayList<PaymentHistoryDTO> historyList = uniqInstance.getPaymentHistoryByStudentYear(db, studentId, year);
        uniqInstance.closeDataBase(db);
        return historyList;
    }
    public ArrayList<PaymentHistoryDTO> getPaymentHistoryByBatchMonth(int batchId, int month) {
        HelperMethod.debugLog(TAG, " getPaymentHistoryByBatchMonth called ++++++++");
        SQLiteDatabase db = uniqInstance.openDatabse();
        ArrayList<PaymentHistoryDTO> historyList = uniqInstance.getPaymentHistoryByStudentYear(db, batchId, month);
        uniqInstance.closeDataBase(db);
        return historyList;
    }

    public ArrayList<PaymentHistoryDTO> getPaymentHistoryByStudent(int studentId) {
        HelperMethod.debugLog(TAG, " addPaymentHistory called ++++++++");
        SQLiteDatabase db = uniqInstance.openDatabse();
        ArrayList<PaymentHistoryDTO> historyList = uniqInstance.getPaymentHistoryByStudent(db, studentId);
        uniqInstance.closeDataBase(db);
        return historyList;
    }

    public long addPaymentHistory(PaymentHistoryDTO paymentHistoryDTO) {
        HelperMethod.debugLog(TAG, " addPaymentHistory called ++++++++");
        SQLiteDatabase db = uniqInstance.openDatabse();
        long id = uniqInstance.addPaymentHistory(paymentHistoryDTO, db);
        uniqInstance.closeDataBase(db);
        return id;
    }



}
