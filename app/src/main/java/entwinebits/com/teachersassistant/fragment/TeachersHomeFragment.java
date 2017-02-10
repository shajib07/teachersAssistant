package entwinebits.com.teachersassistant.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;

import entwinebits.com.teachersassistant.BatchActivity;
import entwinebits.com.teachersassistant.R;
import entwinebits.com.teachersassistant.adapter.BatchListAdapter;
import entwinebits.com.teachersassistant.db.DatabaseRequestHelper;
import entwinebits.com.teachersassistant.model.BatchDTO;
import entwinebits.com.teachersassistant.model.ScheduleDTO;
import entwinebits.com.teachersassistant.server.ServerRequestHelper;
import entwinebits.com.teachersassistant.utils.Constants;
import entwinebits.com.teachersassistant.utils.HelperMethod;
import entwinebits.com.teachersassistant.utils.ServerConstants;
import entwinebits.com.teachersassistant.utils.UserProfileHelper;

/**
 * Created by Nargis Rahman on 12/2/2016.
 */
public class TeachersHomeFragment extends Fragment implements View.OnClickListener {

    private String TAG = "TeachersHomeFragment";
    private Activity mActivity;
    private RecyclerView home_batch_list_rv;
    private BatchListAdapter homeBatchListAdapter;

    private DatabaseRequestHelper dbRequestHelper;
    private FrameLayout home_batch_list_more, home_schedule_list_more;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mActivity == null) {
            mActivity = getActivity();
        }
        View view = inflater.inflate(R.layout.fragment_teachers_home_layout, container, false);
        initLayout(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
//        loadBatchList();
        loadUserBatchList();

    }

    private void loadUserBatchList() {
        String message = "";

        long id = UserProfileHelper.getInstance(mActivity).getUserId();
        //JSONObject jsonObject = ServerRequestHelper.sendUserBatchListRequest(id);
        JSONObject jsonObject = ServerRequestHelper.getBatchStudentList(69);
        HelperMethod.debugLog(TAG, "loadUserBatchList id "+id);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Constants.REQUEST_URL, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("JSON", response.toString());
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d("JSON", "Error: " + error.getMessage());
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(mActivity);
        requestQueue.add(jsonObjReq);
    }

    private void initLayout(View view) {
        home_batch_list_rv = (RecyclerView) view.findViewById(R.id.home_batch_list_rv);
        homeBatchListAdapter = new BatchListAdapter(mActivity, new ArrayList<BatchDTO>());
        home_batch_list_rv.setLayoutManager(new LinearLayoutManager(mActivity));

        home_batch_list_rv.setAdapter(homeBatchListAdapter);
        home_batch_list_rv.setNestedScrollingEnabled(false);
        home_batch_list_rv.setFocusable(false);
        home_batch_list_more = (FrameLayout) view.findViewById(R.id.home_batch_list_more);
        home_batch_list_more.setOnClickListener(this);
        home_schedule_list_more = (FrameLayout) view.findViewById(R.id.home_schedule_list_more);
        home_schedule_list_more.setOnClickListener(this);

    }

    private void loadBatchList() {
        if (dbRequestHelper == null) {
            dbRequestHelper = new DatabaseRequestHelper(mActivity);
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
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            homeBatchListAdapter.notifyAdapterData(batchList);
                        }
                    });
                }
            }
        }).start();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home_schedule_list_more:

                break;

            case R.id.home_batch_list_more:
                Intent batchIntent = new Intent(mActivity, BatchActivity.class);
                startActivity(batchIntent);
                break;
        }
    }
}
