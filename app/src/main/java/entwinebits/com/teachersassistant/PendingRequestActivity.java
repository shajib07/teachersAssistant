package entwinebits.com.teachersassistant;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import entwinebits.com.teachersassistant.model.BatchDTO;
import entwinebits.com.teachersassistant.model.PendingRequestDTO;
import entwinebits.com.teachersassistant.server.ServerRequestHelper;
import entwinebits.com.teachersassistant.server.ServerResponseParser;
import entwinebits.com.teachersassistant.utils.Constants;
import entwinebits.com.teachersassistant.utils.HelperMethod;
import entwinebits.com.teachersassistant.utils.ServerConstants;
import entwinebits.com.teachersassistant.utils.UserProfileHelper;

/**
 * Created by shajib on 4/7/2017.
 */
public class PendingRequestActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "PendingRequestActivity";
    private FrameLayout pending_request_toolbar_back;
    private TextView pending_request_toolbar_title;

    private Button accept_btn, reject_btn;
    private TextView batchTitle;

    private int mUserBatchId, mBatchId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_request_layout);
        initToolbar();
        initLayout();
        managePendingRequest();
    }

    private void managePendingRequest() {

        long userId = UserProfileHelper.getInstance(this).getUserId();
        JSONObject jsonObject = ServerRequestHelper.sendPendingRequest(userId, false);
        HelperMethod.debugLog(TAG, "managePendingRequest id == " + userId+ " json = "+jsonObject.toString());

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Constants.REQUEST_URL, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        HelperMethod.debugLog(TAG, response.toString());
                        if (!response.optBoolean(ServerConstants.ERROR)) {
                            Toast.makeText(PendingRequestActivity.this, "Pending Req Sucs", Toast.LENGTH_SHORT).show();
                            ArrayList<PendingRequestDTO> pendingRequestList = ServerResponseParser.parsePendingRequest(response);

                            updateUI(pendingRequestList);
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        HelperMethod.debugLog(TAG, "Error: " + error.getMessage());
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjReq);
    }

    private void updateUI(ArrayList<PendingRequestDTO> list) {
        if (list.size() < 1) {
            return;
        }
        PendingRequestDTO dto = list.get(0);
        mUserBatchId = dto.getUserBatchId();
        batchTitle.setText(dto.getBatchTitle());
        HelperMethod.debugLog(TAG, "title "+dto.getBatchTitle());

    }

    private void initLayout() {

        accept_btn = (Button)findViewById(R.id.accept_btn);
        reject_btn = (Button)findViewById(R.id.reject_btn);
        batchTitle = (TextView) findViewById(R.id.batchTitle);
        accept_btn.setOnClickListener(this);
        reject_btn.setOnClickListener(this);
    }

    private void initToolbar() {
        pending_request_toolbar_back = (FrameLayout)findViewById(R.id.pending_request_toolbar_back);
        pending_request_toolbar_back.setOnClickListener(this);
        pending_request_toolbar_title = (TextView) findViewById(R.id.pending_request_toolbar_title);
    }

    private void changeStudentStatus(int status) {
        long id = UserProfileHelper.getInstance(this).getUserId();
        JSONObject jsonObject = ServerRequestHelper.sendChangeStudentStatusRequest(mUserBatchId, status);
        HelperMethod.debugLog(TAG, "changeStudentStatus user id == "+id+ " "+jsonObject.toString());

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Constants.REQUEST_URL, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        HelperMethod.debugLog(TAG, response.toString());

                        if (!response.optBoolean(ServerConstants.ERROR)) {
                            Toast.makeText(PendingRequestActivity.this, "changeStudentStatus sucs ", Toast.LENGTH_SHORT).show();
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
            case R.id.accept_btn:
                changeStudentStatus(1);
                break;

            case R.id.reject_btn:
                changeStudentStatus(3);
                break;
            case R.id.pending_request_toolbar_back:
                PendingRequestActivity.this.finish();
                break;
        }
    }
}
