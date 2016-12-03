package entwinebits.com.teachersassistant.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import entwinebits.com.teachersassistant.BatchActivity;
import entwinebits.com.teachersassistant.R;
import entwinebits.com.teachersassistant.adapter.BatchListAdapter;
import entwinebits.com.teachersassistant.db.DatabaseRequestHelper;
import entwinebits.com.teachersassistant.model.BatchDTO;
import entwinebits.com.teachersassistant.utils.HelperMethod;

/**
 * Created by Nargis Rahman on 12/2/2016.
 */
public class TeachersHomeFragment extends Fragment implements View.OnClickListener {

    private String TAG = "TeachersHomeFragment";
    private Activity mActivity;
    private RecyclerView home_batch_list_rv;
    private BatchListAdapter homeBatchListAdapter;

    private DatabaseRequestHelper dbRequestHelper;
    private TextView home_batch_list_more;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mActivity == null) {
            mActivity = getActivity();
        }
        View view = inflater.inflate(R.layout.fragment_teachers_home_layout, container, false);
        initLayout(view);
        return view;
    }

    private void initLayout(View view) {
        home_batch_list_rv = (RecyclerView) view.findViewById(R.id.home_batch_list_rv);
        homeBatchListAdapter = new BatchListAdapter(mActivity, new ArrayList<BatchDTO>());
        home_batch_list_rv.setLayoutManager(new LinearLayoutManager(mActivity));

        home_batch_list_rv.setAdapter(homeBatchListAdapter);
        home_batch_list_rv.setNestedScrollingEnabled(false);
        home_batch_list_more = (TextView)view.findViewById(R.id.home_batch_list_more);
        home_batch_list_more.setOnClickListener(this);
        loadBatchList();
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
                if (batchList != null && batchList.size() > 0) {
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            homeBatchListAdapter.notifyAdapter(batchList);
                        }
                    });
                }
            }
        }).start();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home_batch_list_more:
                Intent batchIntent = new Intent(mActivity, BatchActivity.class);
                startActivity(batchIntent);
                break;
        }
    }
}
