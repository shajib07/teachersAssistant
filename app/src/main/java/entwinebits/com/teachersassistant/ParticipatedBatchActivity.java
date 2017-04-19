package entwinebits.com.teachersassistant;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
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

import entwinebits.com.teachersassistant.adapter.BatchListAdapter;
import entwinebits.com.teachersassistant.model.BatchDTO;
import entwinebits.com.teachersassistant.server.ServerRequestHelper;
import entwinebits.com.teachersassistant.server.ServerResponseParser;
import entwinebits.com.teachersassistant.utils.ConstantFunctions;
import entwinebits.com.teachersassistant.utils.Constants;
import entwinebits.com.teachersassistant.utils.HelperMethod;
import entwinebits.com.teachersassistant.utils.ServerConstants;
import entwinebits.com.teachersassistant.utils.UserProfileHelper;

/**
 * Created by shajib on 4/19/2017.
 */
public class ParticipatedBatchActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "ParticipatedBatchActivity";
    private FrameLayout participated_batch_toolbar_back;

    private RecyclerView participated_batch_rv;
    private BatchListAdapter batchListAdapter;
    private ArrayList<BatchDTO> mParticipatedBatchList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.participated_batch_activity_layout);
        initToolbar();
        initLayout();
        makeServerRequest();
    }

    private void makeServerRequest() {
        if (!ConstantFunctions.isInternetConnected(this)) {
            Toast.makeText(this, R.string.check_internet, Toast.LENGTH_SHORT).show();
            return;
        }
        loadParticipatedBatchList();
    }

    private void initToolbar() {
        participated_batch_toolbar_back = (FrameLayout) findViewById(R.id.participated_batch_toolbar_back);
        participated_batch_toolbar_back.setOnClickListener(this);
    }

    private void initLayout() {
        batchListAdapter = new BatchListAdapter(this, mParticipatedBatchList, false);
        participated_batch_rv = (RecyclerView) findViewById(R.id.participated_batch_rv);
        participated_batch_rv.setLayoutManager(new LinearLayoutManager(this));
        participated_batch_rv.setAdapter(batchListAdapter);
    }


    private void loadParticipatedBatchList() {
        long id = UserProfileHelper.getInstance(this).getUserId();
        JSONObject jsonObject = ServerRequestHelper.sendUserBatchListRequest(id, ServerConstants.USER_TYPE_STUDENT);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {


            case R.id.participated_batch_toolbar_back:
                ParticipatedBatchActivity.this.finish();
                break;
        }
    }
}
