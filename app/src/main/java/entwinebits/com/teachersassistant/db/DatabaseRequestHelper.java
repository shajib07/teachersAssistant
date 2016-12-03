package entwinebits.com.teachersassistant.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import entwinebits.com.teachersassistant.model.BatchDTO;
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

    public void addBatch(BatchDTO batchDTO) {
        HelperMethod.debugLog(TAG, " addBatch called ++++++++");
        SQLiteDatabase db = uniqInstance.openDatabse();
        uniqInstance.addBatch(batchDTO, db);
        uniqInstance.closeDataBase(db);
    }

}
