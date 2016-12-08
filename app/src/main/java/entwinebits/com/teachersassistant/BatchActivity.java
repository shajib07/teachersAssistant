package entwinebits.com.teachersassistant;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import entwinebits.com.teachersassistant.adapter.BatchListAdapter;
import entwinebits.com.teachersassistant.db.DatabaseRequestHelper;
import entwinebits.com.teachersassistant.model.BatchDTO;
import entwinebits.com.teachersassistant.utils.HelperMethod;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batch);
        initToolbar();
        initLayout();
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
                    HelperMethod.debugLog(TAG, ""+dto.getBatchName());
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
        loadBatchList();
    }

    private void initLayout() {
        batchListAdapter = new BatchListAdapter(this, mBatchDTOList);
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
//                addNewBatchDialog();
                break;
        }
    }

    private void addNewBatchDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_batch_layout, null);
        builder.setView(dialogView);
        builder.setTitle(getString(R.string.add_new_batch));

        final AlertDialog dialog = builder.create();
        dialog.show();

        final EditText batch_name_et = (EditText) dialogView.findViewById(R.id.batch_name_et);
        TextView tv_ok = (TextView) dialogView.findViewById(R.id.tv_turn_on);
        TextView tv_cancel = (TextView) dialogView.findViewById(R.id.tv_cancel);

        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (batch_name_et.getText() != null) {
                    added_batch_name = batch_name_et.getText().toString();
                    notifyAdapter();
                    Toast.makeText(BatchActivity.this, added_batch_name, Toast.LENGTH_SHORT).show();
                }
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
    }
}
