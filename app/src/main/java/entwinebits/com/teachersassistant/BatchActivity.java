package entwinebits.com.teachersassistant;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import entwinebits.com.teachersassistant.adapter.BatchListAdapter;
import entwinebits.com.teachersassistant.db.DatabaseRequestHelper;
import entwinebits.com.teachersassistant.model.BatchDTO;
import entwinebits.com.teachersassistant.model.ScheduleDTO;
import entwinebits.com.teachersassistant.server.ServerRequestHelper;
import entwinebits.com.teachersassistant.server.ServerResponseParser;
import entwinebits.com.teachersassistant.utils.Constants;
import entwinebits.com.teachersassistant.utils.HelperMethod;
import entwinebits.com.teachersassistant.utils.ServerConstants;
import entwinebits.com.teachersassistant.utils.UserProfileHelper;

/**
 * Created by Nargis Rahman on 12/1/2016.
 */
public class BatchActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "BatchActivity";
    private Toolbar toolbar_batch_activity;
    private FrameLayout batch_toolbar_back, batch_toolbar_add;

    private DatabaseRequestHelper dbRequestHelper;
    private RecyclerView batch_list_rv;
    private BatchListAdapter batchListAdapter;
    private ArrayList<BatchDTO> mBatchDTOList;
    private String added_batch_name = "";

    private HashMap<Integer, ScheduleDTO> mScheduleListMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batch);
        initToolbar();
        initLayout();
//        loadBatchList();
        loadUserBatchList();
    }

    private void loadUserBatchList() {
        long id = UserProfileHelper.getInstance(this).getUserId();
        JSONObject jsonObject = ServerRequestHelper.sendUserBatchListRequest(id, ServerConstants.USER_TYPE_TEACHER);
        HelperMethod.debugLog(TAG, "loadUserBatchList user id == "+id);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Constants.REQUEST_URL, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());

                        if (!response.optBoolean(ServerConstants.ERROR)) {
                            final ArrayList<BatchDTO> batchList = ServerResponseParser.parseUserBatchListResponse(response);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    HelperMethod.debugLog(TAG, "loadBatchList size = "+batchList.size());
                                    if (batchList != null && batchList.size() > 0) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                batchListAdapter.notifyAdapterData(batchList);
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


    private void loadBatchList() {
        if (dbRequestHelper == null) {
            dbRequestHelper = new DatabaseRequestHelper(this);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                final ArrayList<BatchDTO> batchList = dbRequestHelper.getBatchList();
                HelperMethod.debugLog(TAG, "loadBatchList size = "+batchList.size());
                for (BatchDTO dto : batchList) {

                    ArrayList<ScheduleDTO> scheduleDTOs = dbRequestHelper.getScheduleListByBatch((int)dto.getBatchId());
                    dto.setScheduleDTOList(scheduleDTOs);
                    HelperMethod.debugLog(TAG, "After db read : batch name : "+dto.getBatchName()+" id "+dto.getBatchId()
                        + "schedule size = "+dto.getScheduleDTOList().size());
                }


                if (batchList != null && batchList.size() > 0) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            batchListAdapter.notifyAdapterData(batchList);
                        }
                    });
                }
            }
        }).start();
    }

    private void initToolbar() {
        toolbar_batch_activity = (Toolbar) findViewById(R.id.toolbar_batch_activity);
        setSupportActionBar(toolbar_batch_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        batch_toolbar_back = (FrameLayout) findViewById(R.id.batch_toolbar_back);
        batch_toolbar_back.setOnClickListener(this);

        batch_toolbar_add = (FrameLayout) findViewById(R.id.batch_toolbar_add);
        batch_toolbar_add.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        loadBatchList();
    }

    private void initLayout() {
        batchListAdapter = new BatchListAdapter(this, mBatchDTOList, true);
        batch_list_rv = (RecyclerView) findViewById(R.id.batch_list_rv);
        batch_list_rv.setLayoutManager(new LinearLayoutManager(this));
        batch_list_rv.setAdapter(batchListAdapter);
    }

    private void notifyAdapter() {
        BatchDTO dto = new BatchDTO();
        dto.setBatchName(added_batch_name);
        batchListAdapter.notifyAdapterData(new ArrayList<>(Arrays.asList(dto)));
        dbRequestHelper.addBatch(dto);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.batch_toolbar_back:
                BatchActivity.this.finish();
                break;

            case R.id.batch_toolbar_add:
                Intent intent = new Intent(BatchActivity.this, AddNewBatchActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                break;
        }
    }
}
